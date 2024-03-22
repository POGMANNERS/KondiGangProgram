from flask import session
from functools import wraps
from user import User, Level
from hashing import hashing
from database import db

def login_required(func):
    """ Login ellenőrző decorator """
    @wraps(func)
    def inner(*args, **kwargs):
        if 'user' in session and session['user']:
            return func(*args, **kwargs)
        else:
            return "Denied", 401
    return inner

def permission_required(level):
    """ Authorizáció decorator """
    def decorator(func):
        @wraps(func)
        def inner(*args, **kwargs):
            if "user" not in session and not session.get("user"):
                return "Denied", 401
            user = User(session["user"])
            if Level(user.level) == level:
                return func(*args, **kwargs)
            else:
                return "Denied", 403
        return inner
    return decorator

def login(username, password):
    """ Login függvény """
    user = User(username)
    if user is None:
        return None
    else:
        if user.password == hashing.hashPassword(password, user.salt):
            session["user"] = user.username
            return Level(user.level).name
        else:
            return None

def logout():
    """ Logout függvény """
    session.pop("user", None)

def registerUser(**kwargs):
    """ Új felhasználó felvétele """
    newuser = kwargs
    newuser['salt'] = hashing.newSalt()
    newuser['password'] = hashing.hashPassword(newuser['password'], newuser['salt'])
    return db.addUser(**newuser)

if __name__ == '__main__':
    registerUser(username="landi", password="landi", level=1, name="Landi")