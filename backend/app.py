from fastapi import FastAPI, Depends
from fastapi.middleware.cors import CORSMiddleware
from s3 import generate_presigned_get_url, generate_presigned_put_url, get_mime_from_s3
import os
from dotenv import load_dotenv
from cuid2 import Cuid
from routes.auth import router as auth_router
from routes.auth import get_current_user

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
def presign_upload(filename: str, mime: str, user = Depends(get_current_user)):
    file_id = cuid.generate()
    file_extension = get_extension_from_mime(mime)
    upload_url = generate_presigned_put_url(file_id, mime)

    print("User dict:", user)
    print("User _id:", user["_id"])
    print("User id (str):", str(user["_id"]))
    
    return {

        "fileId": file_id,
        "key": file_id,
        "mime": mime,
        "extension": file_extension,
        "uploadUrl": upload_url,
        "viewUrl": f"http://{domain}/view/{str(user['_id'])}/{file_id}",
        "originalFilename": filename

    }
    
@app.get("/api/file/{key}")
def get_file(key: str):

    url = generate_presigned_get_url(key)
    mime = get_mime_from_s3(key)
    return {

        "key": key,
        "signedUrl": url,
        "mime": mime

    }
