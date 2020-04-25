# Compiling

You need to have `slice` compiler installed

## Server

Generating types:
```
rm -r server/src/types && slice2java -I. types.ice && mv types server/src
```

