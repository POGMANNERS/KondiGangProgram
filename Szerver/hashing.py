import hashlib
import os

class hashing:
    saltLen = 16
    iterations = 40

    @classmethod
    def newSalt(cls):
        return os.urandom(cls.saltLen)

    @classmethod
    def hashPassword(cls, password, salt):
        return hashlib.pbkdf2_hmac("sha256", password.encode("UTF-8"), salt, cls.iterations)