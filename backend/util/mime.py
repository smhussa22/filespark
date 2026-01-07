from fastapi import HTTPException

mime_dictionary = {

    "image/png": "png",
    "image/jpeg": "jpg",  
    "image/webp": "webp",
    "image/heic": "heic",

    "video/mp4": "mp4",
    "video/quicktime": "mov", 
    "video/x-matroska": "mkv", 

    "application/pdf": "pdf",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document": "docx", 
    "application/zip": "zip",
    "application/x-rar-compressed": "rar",
    "application/x-7z-compressed": "7z",

    "text/plain": "txt",

}

def get_extension_from_mime(mime: str) -> str:
    
    if not mime:
        raise HTTPException(400, "[filespark/backend/util/mime.py]: MIME type not found.")

    extension = mime_dictionary.get(mime.lower())
    if not extension:
        raise HTTPException(400, f"[filespark/backend/util/mime.py]: {mime} currently unsupported.")
    
    return extension
