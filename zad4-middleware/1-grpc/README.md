# Compiling

Compiling types definition:

## GoLang (server)

You need to have protoc go plugin installed, then:
```
protoc -I=. types.proto --go_out=plugins=grpc:server/gen
```
will generate files in `gen` directory

## Java (client)

Using java plugin from this folder - linux executable.
If you're using windows, you need to use a different plugin.
```
protoc -I=. types.proto --grpc-java_out=client/app/gen --plugin=protoc-gen-grpc-java=$(pwd)/protoc-gen-grpc-java-1.27.0-linux-x86_64.exe --java_out=client/app/gen
```

will generate files in `gen` directory