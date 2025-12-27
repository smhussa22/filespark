from bson import ObjectId
from datetime import datetime, timezone
from typing import Optional

def create_file_document(*, owner_id: ObjectId, original_name: str, mime: str, bucket: str, key: str, size_bytes: int, region: Optional[str] = None, visibility: str = "public",  checksum: Optional[str] = None) -> dict:

    return { # placeholder file objectn ot in use

        "ownerId": owner_id,

        "_id": ObjectId(), 
        "fileId": ObjectId(), 
        "originalName": original_name,

        "mime": mime,
        "extension": "placeholder will get real mime from",

        "bucket": bucket,
        "key": key,
        "region": region,

        "sizeBytes": size_bytes,

        "checksum": checksum,

        "visibility": visibility,
        "viewCount": 0,
        "downloadCount": 0,

        "createdAt": datetime.now(timezone.utc)

    }
