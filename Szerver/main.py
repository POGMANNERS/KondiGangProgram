from flask import Flask, session, request
import json
import auth
from user import Level

app = Flask(__name__)
app.secret_key = "40e2d93e1f4e61d32e7c4c73de53648b811a6c0d0aa3e73ab16158aef69e43d4"



if __name__ == "__main__":
    app.run()

# routes:
# login
# logout
# parts-modify
# parts-get
# users-new
# users-modify
