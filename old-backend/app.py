from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from dotenv import load_dotenv
from routes.auth.auth_client import router as auth_client_router
from routes.auth.auth_web import router as auth_web_router
from routes.files import router as files_router

# @todo fix allow origins

load_dotenv()
app = FastAPI()
app.add_middleware( CORSMiddleware, allow_origins=["*"], allow_credentials=True, allow_methods=["*"], allow_headers=["*"])
app.include_router(auth_client_router)
app.include_router(files_router)
app.include_router(auth_web_router)