from flask import Flask, session, request
import auth
from user import Level
from database import db
from bson import json_util

app = Flask(__name__)
app.secret_key = "40e2d93e1f4e61d32e7c4c73de53648b811a6c0d0aa3e73ab16158aef69e43d4"

# URL: 127.0.0.1:5000/

@app.route("/login", methods=['GET', 'POST'])
def login():
    data = request.json
    if level := auth.login(data['username'], data['password']):
        return str(level)
    else:
        return "Denied"

@app.route("/logout", methods=['GET', 'POST'])
@auth.login_required
def logout():
    auth.logout()
    return "Success"

@app.route("/parts-new", methods=['GET', 'POST'])
@auth.permission_required(Level.manager)
def parts_new():
    data = request.json
    if db.addPart(**data):
        return "Success"
    else:
        return "Failed"

@app.route("/parts-get", methods=['GET', 'POST'])
@auth.permission_required(Level.manager)
def parts_get():
    return json_util.dumps(db.listParts())

@app.route("/parts-modify", methods=['GET', 'POST'])
@auth.permission_required(Level.manager)
def parts_mod():
    data = request.json
    if db.modifyPart(**data):
        return "Success"
    else:
        return "Failed"

@app.route("/users-new", methods=['GET', 'POST'])
@auth.permission_required(Level.admin)
def users_new():
    data = request.json
    if auth.registerUser(**data):
        return "Success"
    else:
        return "Failed"



if __name__ == "__main__":
    app.run()

