from fastapi import FastAPI, UploadFile
from fastapi.middleware.cors import CORSMiddleware
from s3 import upload_file_to_s3, generate_presigned_url, get_mime_from_s3
import os
from dotenv import load_dotenv

load_dotenv()
domain = os.getenv("DOMAIN")

app = FastAPI()
app.add_middleware(

    CORSMiddleware,
    allow_origins=["*"], # @todo fix this
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]

)


@app.post("/upload")
async def upload_file(file: UploadFile):

    key = file.filename
    mime = file.content_type
    upload_file_to_s3(file.file, key, mime)
    url = generate_presigned_url(key)
    embedUrl = f"http://{domain}/view/{key}"

    return {

        "file": key,
        "url": url,
        "embedUrl": embedUrl
        
    }

@app.get("/api/file/{key}")
def get_file(key: str):

    url = generate_presigned_url(key)
    mime = get_mime_from_s3(key)
    return {

        "key": key,
        "signedUrl": url,
        "mime": mime

    }