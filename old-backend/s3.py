import boto3
import os
from dotenv import load_dotenv

load_dotenv()
bucket = os.getenv("AWS_BUCKET")

s3 = boto3.client("s3", aws_access_key_id=os.getenv("AWS_ACCESS_KEY_ID"), aws_secret_access_key=os.getenv("AWS_SECRET_ACCESS_KEY"), region_name="us-east-2")

def generate_presigned_get_url(key, expires=3600):
    return s3.generate_presigned_url("get_object", Params={"Bucket": bucket, "Key": key}, ExpiresIn=expires)

def generate_presigned_put_url(key, mime, expires=3600):
    return s3.generate_presigned_url("put_object", Params={"Bucket": bucket, "Key": key, "ContentType": mime}, ExpiresIn=expires)

def get_mime_from_s3(key):
    head = s3.head_object(Bucket=bucket, Key=key)
    return head["ContentType"]

