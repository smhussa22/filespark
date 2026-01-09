import requests
from jose import jwt
from jose.utils import base64url_decode
from datetime import datetime, timedelta, timezone
from mongo import db
from bson import ObjectId
from models.user import create_user_document
from pymongo.errors import DuplicateKeyError
from fastapi import HTTPException, Header
import os 

session_secret = os.getenv("SESSION_SECRET")

def get_or_create_user(*, email, name, picture, google_id):
    user = db.users.find_one({"google_id": google_id})
    if user:
        return user

    doc = create_user_document(
        email=email,
        name=name,
        picture=picture,
        google_id=google_id,
    )

    try:
        db.users.insert_one(doc)
        return doc
    except DuplicateKeyError:
        return db.users.find_one({"google_id": google_id})


def verify_google_id_token(id_token: str, *, client_id: str):
    jwks = requests.get("https://www.googleapis.com/oauth2/v3/certs").json()
    headers = jwt.get_unverified_header(id_token)

    key = next(
        k for k in jwks["keys"]
        if k["kid"] == headers["kid"]
    )

    return jwt.decode(
        id_token,
        key,
        algorithms=["RS256"],
        audience=client_id,
        issuer=["https://accounts.google.com", "accounts.google.com"],
    )


def issue_session_token(user_id, *, secret: str):
    now = datetime.now(timezone.utc)

    return jwt.encode(
        {
            "sub": str(user_id),
            "iat": now,
            "exp": now + timedelta(days=7),
            "typ": "session",
        },
        secret,
        algorithm="HS256",
    )

def get_current_user(authorization: str = Header(..., alias="Authorization")):
    
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401, detail = "[filespark/backend/routes/auth.py **get_current_user**]: Authorization NULL or does not start with 'Bearer'.")
    
    token = authorization.split(" ")[1]
    if not session_secret:
        raise HTTPException(status_code=500, detail = "[filespark/backend/routes/auth.py **get_current_user**]: Session secret NULL.")
    
    payload = jwt.decode(token, session_secret, algorithms=["HS256"])
    user_id = payload.get("sub")
    if not user_id:
        raise HTTPException(status_code=401, detail = f"[filespark/backend/routes/auth.py **get_current_user**]: Payload does not contain user_id. \\ {payload}")
    
    user = db.users.find_one({"_id": ObjectId(user_id)})
    if not user:
        raise HTTPException(status_code=401, detail = "[filespark/backend/routes/auth.py **get_current_user**]: User could not be found.")
    
    return user