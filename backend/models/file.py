from bson import ObjectId
from datetime import datetime, timezone
from typing import Optional

def create_file_document(*, owner_id: ObjectId, cluster_id: Optional[ObjectId], original_name: str, mime: str, extension: str, bucket: str, key: str, size_bytes: int, region: Optional[str] = None, visibility: str = "private",
    checksum: Optional[str] = None,) -> dict:

    return {

        "_id": ObjectId(),

        "ownerId": owner_id,
        "clusterId": cluster_id,

        "originalName": original_name,
        "mime": mime,
        "extension": extension,

        "bucket": bucket,
        "key": key,
        "region": region,
        "sizeBytes": size_bytes,
        "checksum": checksum,

        "visibility": visibility,
        "status": "uploading",

        "viewCount": 0,
        "downloadCount": 0,

        "isDeleted": False,
        "deletedAt": None,

        "createdAt": datetime.now(timezone.utc),
        
    }
