from bson import ObjectId
from datetime import datetime, timezone
from typing import Optional

def create_user_document(*, email: str, name: str, google_id: str, picture: Optional[str]) -> dict:

    now = datetime.now(timezone.utc)

    return {

        "_id": ObjectId(),
        "google_id": google_id,
        "email": email,
        "name": name,
        "picture": picture,
        "created_at": now,
        "updated_at": now,

    }

