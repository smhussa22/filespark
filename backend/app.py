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
def presign_upload(filename: str, mime: str, user = Depends(get_current_user)):
    file_id = cuid.generate()
    file_extension = get_extension_from_mime(mime)
    user_id = str(user["_id"])
    upload_url = generate_presigned_put_url(file_id, mime)
    

    return {

        "fileId": file_id,
        "key": file_id,
        "mime": mime,
        "extension": file_extension,
        "uploadUrl": upload_url,
        "user": user_id,
        "viewUrl": f"http://{domain}/view/{user_id}/{file_id}",
        "originalFilename": filename

    }
    
@app.get("/api/public/file/{user_id}/{key}")
def get_file_public(user_id: str, key: str):

    url = generate_presigned_get_url(key)
    file = db.files.find_one({ "fileId": key })
    if not file:
        raise HTTPException(404)

    mime = file["mime"]
    print(mime)
    url = generate_presigned_get_url(key)

    return {

        "key": key,
        "signedUrl": url,
        "mime": mime

    }
