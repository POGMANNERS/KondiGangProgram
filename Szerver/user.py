from database import db
from enum import Enum

class Level(Enum):
    """ Level enum, jogosultságok feldolgozására """
    admin = 0
    professional = 1
    manager = 2
    operator = 3

class User:
    username: str
    password: bytes
    salt: bytes
    level: int
    name: str

    def __new__(cls, username: str):
        """ Ha nem létezik a felhasználó, megszakítja a konstruktort """
        doc = db.getUserByUsername(username)
        if doc is not None:
            return super().__new__(cls)

    def __init__(self, username: str):
        """ User konstruktor, username -> User """
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