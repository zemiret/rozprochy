from flask import Flask

def create_app(test_config=None):
    # create and configure the app
    app = Flask(__name__)

    with app.app_context():
        from . import views

    app.register_blueprint(views.api)

    return app

