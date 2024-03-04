import pymongo
from bson.objectid import ObjectId
class db:
    client = pymongo.MongoClient("mongodb://localhost:27017/")

    # Adatbázis kiválasztása (ha nem létezik, létre fog jönni)
    mydb = client["rendszerfejlesztes"]

    # Kollekció kiválasztása (ha nem létezik, létre fog jönni)
    felhasznalok = mydb["felhasznalok"]
    alkatresz = mydb["alkatresz"]
    projekt= mydb["projekt"]

    @classmethod
    def getUser(cls, id):
        user=cls.felhasznalok.find_one({"_id": ObjectId(id)})
        return user
    @classmethod
    def addPart(cls, name, price, maxnum):
        part=cls.alkatresz.insert_one({ "alkatresz_nev": name, "alkatresz_ar":price, "maxdb": maxnum})
        return part
    @classmethod
    def modifyPart(cls, id, name, price, maxnum):
        part=cls.alkatresz.update_one({"_id":ObjectId(id)},{"$set":{ "alkatresz_nev": name, "alkatresz_ar":price, "maxdb": maxnum}})
        return part

    @classmethod
    def listParts(cls):
        parts=cls.alkatresz.find()
        return parts

    @classmethod
    def addUser(cls,username,password,salt,level,name):
        user=cls.felhasznalok.insert_one({"felhasznalonev": username,"jelszo": password,"salt": salt,"jogosultsag": level,"nev": name})
        return user

# ha kódot futtatsz teszteléshez, egy ilyen blokkba kell rakni
if __name__ == "__main__":
    #print(db.addPart("Napelem", "300", 3))
    print(db.modifyPart("65e5aa117a834ed19b9f330b",name="napelem",price="200",maxnum=4))
    print(*db.listParts())
    print(db.addUser("Potter","aa","bb",1,"Jandovics Ákos Attila"))
