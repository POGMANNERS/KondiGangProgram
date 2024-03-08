import hashlib
import os

class hashing:
    _saltLen = 16
    _iterations = 40

    @classmethod
    def newSalt(cls) -> bytes:
        """ Új salt generálása """
        return os.urandom(cls._saltLen)

    @classmethod
    def hashPassword(cls, password, salt) -> bytes:
        """ Jelszó hashelése """
        return hashlib.pbkdf2_hmac("sha256", password.encode("UTF-8"), salt, cls._iterations)