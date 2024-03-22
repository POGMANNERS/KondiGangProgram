import auth
from database import db

if __name__ == '__main__':
    db.storage.drop()
    db.part.drop()
    db.user.drop()
    db.addStorage()
    db.addPart("Napelem", "300", 3)
    db.addPart("Vezeték", "200", 10)
    db.addPart("Vezérlő", "600", 5)
    auth.registerUser(username="admin", password="admin", level=0, name="admin")
    auth.registerUser(username="landi", password="landi", level=1, name="Landi")