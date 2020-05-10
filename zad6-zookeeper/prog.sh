#!/bin/bash

while [ 1 ]; do
	echo "Hello: $$" >> logfile.hello
	sleep 2
done
