from flask import (
    Blueprint,
    current_app as app,
    render_template,
    request,
    g
)

import requests
from concurrent.futures import ThreadPoolExecutor 
from io import BytesIO
from PIL import Image
import os


@app.route('/', methods=('GET',))
def index():
    return render_template("index.html")


@app.route('/cat', methods=('GET',))
def server_cat():
    return app.send_static_file('cat.jpg')


api = Blueprint('api', __name__, url_prefix='/api')

@api.route('/', methods=('POST',))
def gimme():
    name = request.form['name']
    surname = request.form['surname']
    width = request.form['width']
    height = request.form['height']

    diversity = call_diversity(name + ' ' + surname)
    jokes = call_jokes(name, surname)
    wise_words = call_wise_words()
    kitten = call_kittens(width, height)

    g.fullname = diversity['fullname']
    g.gender = diversity['gender']
    g.gender_prob = diversity['gender probability'] * 100
    g.ethnicity = diversity['ethnicity']
    g.ethnicity_prob = diversity['ethnicity probability'] * 100

    g.jokes = jokes

    g.wise_words = wise_words['en']
    g.wise_author = wise_words['author']

    return render_template("result.html")


def call_diversity(fullname):
    param = fullname.replace(' ', '%20')
    endpoint = 'https://api.diversitydata.io/?fullname={}'.format(fullname)

    r = requests.get(endpoint)
    return r.json()



def call_jokes(name, surname):
    endpoints=["http://api.icndb.com/jokes/random?firstName={}&lastName={}"\
        .format(name, surname)] * 3

    results = [] 
    with ThreadPoolExecutor() as executor:
        results = executor.map(lambda x: requests.get(x), endpoints)


    return list(map(lambda res: res.json()['value']['joke'], results))


def call_wise_words():
    endpoint="https://programming-quotes-api.herokuapp.com/quotes/random"

    r = requests.get(endpoint)
    return r.json()
    
    
def call_kittens(width, height):
    endpoint = "http://placekitten.com/{}/{}".format(width, height)
    r = requests.get(endpoint)
    i = Image.open(BytesIO(r.content))

    os.remove('core/static/cat.jpg')
    i.save('core/static/cat.jpg', "JPEG")

