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
    def getUser(cls, _id):
        user = cls.user.find_one({"_id": ObjectId(_id)})
        return user

    @classmethod
    def getUserByUsername(cls, username):
        user = cls.user.find_one({"username": username})
        return user

    @classmethod
    def addPart(cls, name, price, maxnum):
        part = cls.part.insert_one({"name": name, "price": price, "maxnum": maxnum})
        return part

    @classmethod
    def modifyPart(cls, _id, name, price, maxnum):
        part = cls.part.update_one({"_id":ObjectId(_id["$oid"])}, {"$set":{"name": name, "price":price, "maxnum": maxnum}})
        return part

    @classmethod
    def listParts(cls):
        parts = cls.part.find()
        return parts

    @classmethod
    def addUser(cls, username, password, salt, level, name):
        if cls.user.find_one({"username":username}):
            return None
        else:
            user = cls.user.insert_one({"username": username, "password": password, "salt": salt, "level": level, "name": name})
            return user

    @classmethod
    def addStorage(cls):
        cls.storage.drop()
        storage=[10*[4*[6*[{"part_id":0, "number":0}]]]]
        #print(storage)

        cls.storage.insert_one({"storage":storage})






# ha kódot futtatsz teszteléshez, egy ilyen blokkba kell rakni
if __name__ == "__main__":
    pass
