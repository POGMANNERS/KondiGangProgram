import pymongo

client = pymongo.MongoClient("mongodb://localhost:27017/")

# Adatbázis kiválasztása (ha nem létezik, létre fog jönni)
mydb = client["rendszerfejlesztes"]

# Kollekció kiválasztása (ha nem létezik, létre fog jönni)
felhasznalok = mydb["felhasznalok"]
alkatresz = mydb["alkatresz"]
projekt= mydb["projekt"]

# ha kódot futtatsz teszteléshez, egy ilyen blokkba kell rakni
if __name__ == "__main__":
    print(*projekt.find({"nev": "Napelem-Páka"}))

