from bson import ObjectId
from datetime import datetime, timezone

def create_file_cluster(*, owner_id: ObjectId, name: str) -> dict:

    now = datetime.now(timezone.utc)

    return {

        "_id": ObjectId(),
        "ownerId": owner_id,
        "name": name,
        "createdAt": now,
        "isDeleted": False,

    }
    