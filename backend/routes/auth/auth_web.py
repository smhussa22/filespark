import os
import requests
from fastapi import APIRouter, HTTPException, Depends, Header, HTTPException
from routes.auth.auth_common import (
    verify_google_id_token,
    get_or_create_user,
    issue_session_token,
    get_current_user,
)

router = APIRouter(prefix="/auth/google/web")

CLIENT_ID = os.getenv("GOOGLE_WEBSITE_CLIENT_ID")
CLIENT_SECRET = os.getenv("GOOGLE_WEBSITE_CLIENT_SECRET")
REDIRECT_URI = os.getenv("GOOGLE_WEBSITE_REDIRECT_URI")
SESSION_SECRET = os.getenv("SESSION_SECRET")

@router.post("/callback")
def google_web_callback(payload: dict):
    code = payload.get("code")
    if not code:
        raise HTTPException(400, "Missing code")

    token_res = requests.post(
        "https://oauth2.googleapis.com/token",
        data={
            "code": code,
            "client_id": CLIENT_ID,
            "client_secret": CLIENT_SECRET,
            "redirect_uri": REDIRECT_URI,
            "grant_type": "authorization_code",
        },
    ).json()

    id_token = token_res.get("id_token")
    if not id_token:
        raise HTTPException(401, "Google id_token missing")

    google_user = verify_google_id_token(
        id_token,
        client_id=CLIENT_ID,
    )

    user = get_or_create_user(
        google_id=google_user["sub"],
        email=google_user["email"],
        name=google_user.get("name"),
        picture=google_user.get("picture"),
    )

    session_token = issue_session_token(
        user["_id"],
        secret=SESSION_SECRET,
    )

    return {
        "token": session_token,
        "user": {
            "id": str(user["_id"]),
            "email": user["email"],
            "name": user["name"],
            "picture": user["picture"],
        },
    }

@router.get("/me")
def me(user=Depends(get_current_user)):
    return {
        "id": str(user["_id"]),
        "email": user["email"],
        "name": user["name"],
        "picture": user["picture"],
    }

