import pymongo
from bson.objectid import ObjectId

class db:
    client = pymongo.MongoClient("mongodb://localhost:27017/")

    # Adatbázis kiválasztása (ha nem létezik, létre fog jönni)
    mydb = client["rendszerfejlesztes"]

    # Kollekció kiválasztása (ha nem létezik, létre fog jönni)
    user = mydb["user"]
    part = mydb["part"]
    projekt = mydb["projekt"]
    storage = mydb["storage"]

    @classmethod
    def getUser(cls, id):
        user = cls.user.find_one({"_id": ObjectId(id)})
        return user

    @classmethod
    def getUserByUsername(cls, username):
        user = cls.user.find_one({"username": username})
        return user

    @classmethod
    def addPart(cls, name, price, maxnum):
        part = cls.part.insert_one({"name": name, "price":price, "maxnum": maxnum})
        return part

    @classmethod
    def modifyPart(cls, id, name, price, maxnum):
        part = cls.part.update_one({"_id":ObjectId(id)}, {"$set":{"name": name, "price":price, "maxnum": maxnum}})
        return part

    @classmethod
    def listParts(cls):
        parts = cls.part.find()
        return parts

    @classmethod
    def addUser(cls,username,password,salt,level,name):
        if cls.user.find_one({"username":username}):
            return None
        else:
            user = cls.user.insert_one({"username": username, "password": password, "salt": salt, "level": level, "name": name})
            return user

    @classmethod
    def addStorage(cls):
        cls.storage.drop()
        storage = cls.storage.insert_one({
            "rows": [
                {"columns":  [
                    {"compartments":  [
                        {
                            "part_id": 1,
                            "number" : 2
                        }
                    ]}
                ]}
            ]
        })



# ha kódot futtatsz teszteléshez, egy ilyen blokkba kell rakni
if __name__ == "__main__":
    print(db.addPart("Napelem", "300", 3))
    print(db.modifyPart("65e5fe2b091ee18e83023afe",name="napelem",price="200",maxnum=4))
    print(*db.listParts())
    print(db.addUser("Potterrr","aa","bb",1,"Jandovics Ákos Attila"))
    print(db.addStorage())
