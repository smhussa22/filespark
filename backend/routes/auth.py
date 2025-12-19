from mongo import db
from models.user import create_user_document
from pymongo.errors import DuplicateKeyError
import os
import requests
from fastapi import APIRouter, HTTPException, Header, Depends
from fastapi.responses import PlainTextResponse, RedirectResponse
from jose import jwt
from datetime import datetime, timedelta, timezone
from bson import ObjectId
import urllib


# @todo sep the web login
router = APIRouter(prefix="/auth")

db.users.create_index("email", unique=True)
db.users.create_index("google_id", unique=True)

google_website_client_id = os.getenv("GOOGLE_WEBSITE_CLIENT_ID")
google_website_client_secret = os.getenv("GOOGLE_WEBSITE_CLIENT_SECRET")
google_website_redirect_uri = os.getenv("GOOGLE_WEBSITE_REDIRECT_URI")

google_desktop_client_id = os.getenv("GOOGLE_DESKTOP_CLIENT_ID")
google_desktop_client_secret = os.getenv("GOOGLE_DESKTOP_CLIENT_SECRET")

session_secret = os.getenv("SESSION_SECRET")

def exchange_code_for_session(code: str, redirect_uri: str) -> dict:
    
    token_response = requests.post(
        "https://oauth2.googleapis.com/token", 
        data = {

            "code": code,
            "client_id": google_desktop_client_id,
            "client_secret": google_desktop_client_secret,
            "grant_type": "authorization_code",
            "redirect_uri": redirect_uri,

        }
    )

    token_data = token_response.json()

    id_token = token_data.get("id_token")
    if not id_token:
        raise HTTPException(status_code=401, detail="google token none or invalid")
    
    google_payload = jwt.get_unverified_claims(id_token)

    google_id=google_payload.get("sub")
    email=google_payload.get("email")
    name=google_payload.get("name")
    picture=google_payload.get("picture")

    if not google_id or not email:
        raise HTTPException(status_code=401, detail="google id or email none or invalid")
    
    user = get_or_create_user(google_id=google_id, email=email, name=name, picture=picture)
    now = datetime.now(timezone.utc)

    app_token = jwt.encode(

        {

            "sub": str(user["_id"]),
            "iat": now,
            "exp": now + timedelta(days=7),

        },
        session_secret,
        algorithm="HS256"

    )

    return { 
    
        "token": app_token,
        "user": 
        {

            "id": str(user["_id"]),
            "email": user["email"],
            "name": user["name"],
            "picture": user["picture"],

        }

    }

def get_or_create_user(*, email: str, name: str, picture: str | None, google_id: str):
    
    user = db.users.find_one({ "google_id": google_id })
    
    if user:
        return user
    
    user_document = create_user_document(email=email, name=name, picture=picture, google_id=google_id)

    try:
        db.users.insert_one(user_document)
        return user_document
    except DuplicateKeyError:
        return db.users.find_one({ "google_id": google_id })
    
@router.post("/google/callback")
def google_callback(payload: dict):

    code = payload.get("code")
    if not code:
        raise HTTPException(status_code=400, detail="missing code")
    
    return exchange_code_for_session(code)

def get_current_user(authorization: str = Header()):
    
    if not authorization or not authorization.startswith("Bearer "):
        raise HTTPException(status_code=401)
    
    token = authorization.split(" ")[1]
    if not session_secret:
        raise HTTPException(status_code=500)
    
    payload = jwt.decode(token, session_secret, algorithms=["HS256"])
    user_id = payload.get("sub")
    if not user_id:
        raise HTTPException(status_code=401)
    
    user = db.users.find_one({"_id": ObjectId(user_id)})
    if not user:
        raise HTTPException(status_code=401)
    
    return user

@router.get("/me")
def me(user=Depends(get_current_user)):

    return {

        "id": str(user["_id"]),
        "email": user["email"],
        "name": user["name"],
        "picture": user["picture"],

    }

@router.post("/google/session")
def google_session(payload: dict):
    code = payload.get("code")
    redirect_uri = payload.get("redirect_uri")

    if not code or not redirect_uri:
        raise HTTPException(status_code=400)
    
    return exchange_code_for_session(code, redirect_uri)

@router.get("/google/login")
def google_login(port: int):

    redirect_uri = f"http://localhost:{port}/callback"

    params = {

        "client_id": google_desktop_client_id,
        "redirect_uri": redirect_uri,
        "response_type": "code",
        "scope": "openid email profile",
        "access_type": "offline",
        "prompt": "select_account"

    }

    url = ("https://accounts.google.com/o/oauth2/v2/auth?"+ urllib.parse.urlencode(params))
    return RedirectResponse(url)







# test route
@router.get("/auth/test")
def test():
    return PlainTextResponse("backend")
    

