from fastapi import APIRouter, Depends, HTTPException
from bson import ObjectId
from cuid2 import Cuid
import os
from mongo import db
from routes.auth import get_current_user
from models.file import create_file_document
from models.file_cluster import create_file_cluster
from s3 import generate_presigned_get_url, generate_presigned_put_url, get_mime_from_s3
import os
from cuid2 import Cuid
from util.mime import get_extension_from_mime

router = APIRouter()
cuid = Cuid()

bucket = os.getenv("AWS_BUCKET")
domain = os.getenv("DOMAIN")

@router.post("/clusters")
def create_cluster(payload: dict, user=Depends(get_current_user)):

    name = payload.get("name")
    if not name:
        raise HTTPException(status_code=401, detail="[filespark/backend/routes/files.py **exchange_code_for_session**]: Google token does not exist or invalid.")
    
    cluster = create_file_cluster(owner_id=user["_id"], name=name)
    db.file_clusters.insert_one(cluster)

    return {

        "id": str(cluster["_id"]),
        "name": cluster["name"],

    }

@router.get("/presign-upload")
def presign_upload(filename: str, mime: str, user = Depends(get_current_user), cluster_id: str | None = None):

    file_id = cuid.generate()
    file_extension = get_extension_from_mime(mime)

    cluster_object_id = ObjectId(cluster_id) if cluster_id else None
    file_document = create_file_document(owner_id=user["_id"], cluster_id=cluster_object_id, original_name=filename, mime = mime, extension=file_extension, bucket=bucket, key=file_id, size_bytes=0)
    db.files.insert_one(file_document)

    upload_url = generate_presigned_put_url(file_id, mime)

    print(file_extension)
    
    return {

        "fileId": file_id,
        "key": file_id,
        "mime": mime,
        "extension": file_extension,
        "uploadUrl": upload_url,
        "viewUrl": f"http://{domain}/view/{str(user['_id'])}/{file_id}",
        "originalFilename": filename

    }
    
@router.get("/api/file/{key}")
def get_file(key: str):

    file = db.files.find_one({ "key": key, "isDeleted": False, })
    if not file:
        raise HTTPException (status_code=404, detail="[filespark/backend/routes/files.py **get_file**]: File not found.")

    url = generate_presigned_get_url(key)
    return {

        "key": key,
        "signedUrl": url,
        "mime": file["mime"]

    }
