from flask import Flask, session, request
import json
import auth
from user import Level

app = Flask(__name__)
app.secret_key = "40e2d93e1f4e61d32e7c4c73de53648b811a6c0d0aa3e73ab16158aef69e43d4"

@app.route("/login")
def login():
    pass

@app.route("/logout")
def logout():
    pass

@app.route("/parts-get")
def parts_get():
    pass

@app.route("/parts-modify")
def parts_mod():
    pass

@app.route("/users-new")
def users_new():
    pass

@app.route("/users-modify")
def users_modify():
    pass

if __name__ == "__main__":
    app.run()

