Regenerate protobuf models:
```
$ protoc --java_out=app/src/main/java protobuf/*
```

Generate thrift models:
```
$ cd ..
$ thrift --gen java:android crimes.thrift
```
