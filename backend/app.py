from fastapi import FastAPI, UploadFile
from s3 import uploadFileToS3, generatePresignedURL

app = FastAPI()

@app.post("/upload")

async def uploadFile(file: UploadFile):
    key = file.filename
    uploadFileToS3(file.file, key)
    url = generatePresignedURL(key)
    return { "file": key, "url": url }
