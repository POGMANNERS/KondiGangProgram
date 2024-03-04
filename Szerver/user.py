from database import db
from enum import Enum

class Level(Enum):
    admin = 0
    professional = 1
    manager = 2
    operator = 3

class User:
    def __new__(cls, username):
        doc = db.getUserByUsername(username)
        if doc is not None:
            return super().__new__(cls)

    def __init__(self, username):
        doc = db.getUserByUsername(username)
        for k,v in doc.items():
            self.__setattr__(k,v)

    def __str__(self):
        return str(self.__dict__)

    @property
    def id(self):
        return self._id


if __name__ == "__main__":
    print(isinstance(Level(0).name, str))