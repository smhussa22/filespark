from fastapi import FastAPI, Depends, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from s3 import generate_presigned_get_url, generate_presigned_put_url, get_mime_from_s3
import os
from dotenv import load_dotenv
from cuid2 import Cuid
from routes.auth import router as auth_router
from routes.auth import get_current_user
from mongo import db

load_dotenv()
domain = os.getenv("DOMAIN")

app = FastAPI()
app.include_router(auth_router)
app.add_middleware(

    CORSMiddleware,
    allow_origins=["*"], # @todo fix this
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]

)

cuid = Cuid()


def get_extension_from_mime(mime: str) -> str:

    if not mime:
        return ""
    
    mime_dictionary = {

        "image/png": "png",
        "image/jpeg": "jpg",  
        "image/webp": "webp",
        "image/heic": "heic",

        "video/mp4": "mp4",
        "video/quicktime": "mov", 
        "video/x-matroska": "mkv", 

        "application/pdf": "pdf",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document": "docx", 
        "application/zip": "zip",
        "application/x-rar-compressed": "rar",
        "application/x-7z-compressed": "7z",

        "text/plain": "txt",
    }

    return mime_dictionary.get(mime.lower(), "")

@app.get("/presign-upload")
def presign_upload(filename: str, mime: str, user=Depends(get_current_user)):
    file_id = cuid.generate()  # File ID created using Cuid
    file_extension = get_extension_from_mime(mime)  # Get file extension from mime
    user_id = str(user["_id"])  # Get user ID from the logged-in user
    s3_key = f"{user_id}/{file_id}"  # Unique S3 key with user ID for file organization
    
    # Generate presigned URL for uploading the file
    upload_url = generate_presigned_put_url(s3_key, mime)
    
    return {
        "fileId": file_id,
        "key": s3_key,  # Using user-specific file key
        "mime": mime,
        "extension": file_extension,
        "uploadUrl": upload_url,  # Presigned URL to upload file to S3
        "user": user_id,  # Returning user ID
        "viewUrl": f"http://{domain}/view/{user_id}/{file_id}",  # View URL to access file
        "originalFilename": filename
    }
    
@app.get("/api/public/file/{user_id}/{key}")
def get_file_public(user_id: str, key: str):
    print(f"Fetching file with user_id: {user_id}, key: {key}")  # Debug output
    try:
        url = generate_presigned_get_url(key)  # Generate the URL to access the file
        mime = get_mime_from_s3(key)  # Get MIME type from S3
        print(f"Generated URL: {url}, MIME: {mime}")  # Debug output
    except Exception as e:
        print(f"Error fetching file: {e}")  # Handle any errors
        raise HTTPException(status_code=500, detail= e.__cause__ + e.__context__)

    return {
        "key": key,
        "signedUrl": url,  # Presigned URL for the file
        "mime": mime  # MIME type of the file
    }
