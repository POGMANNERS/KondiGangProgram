import hashlib
import os

class hashing:
    saltLen = 16
    iterations = 40

    @classmethod
    def newSalt(cls) -> bytes:
        """ Új salt generálása """
        return os.urandom(cls.saltLen)

    @classmethod
    def hashPassword(cls, password, salt) -> bytes:
        """ Jelszó hashelése """
        return hashlib.pbkdf2_hmac("sha256", password.encode("UTF-8"), salt, cls.iterations)