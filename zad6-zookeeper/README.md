# Running

To set up the servers:
```
docker-compose up
```

To start clients use zookeper cli:
```
zkCli.sh -server localhost:2181
zkCli.sh -server localhost:2182
```

To run this watcher, the easiest way is to use IntellIJ, and
pass `localhost:2181 ./prog.sh` as arguments

# Runtime

* In 1 client: `create /z`
* In 2 client: `ls /` (make sure /z is synced)
* See app logs, to check if it logs events
* tail -f logfile.hello to see that prog is running
* In 1 client: `create /z/sub`, `create /z/sub/sub1`
* In app press any key to see the tree for /z
* In client: `deleteall /z` to stop program

