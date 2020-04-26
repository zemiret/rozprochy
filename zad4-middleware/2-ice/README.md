# Compiling

You need to have `slice` compiler installed

## Server

Generating types:
```
rm -r server/src/types && slice2java -I. types.ice && mv types server/src
```

## Client

First of all `cd client`

Setup (virtualenv)
```
python3 -m venv .venv
source .venv/bin/activate
pip install zeroc-ice
```

Generating types:
```
slice2py ../types.ice
```

