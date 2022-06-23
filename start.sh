#!/bin/bash

services="$1"

javac *.java

if [ $services == "c" ]; then
    echo "Starting client"
    java Client &
elif [ $services == "s" ]; then
    echo "Starting server"
    java Server &
else
    echo "Starting client and server"
    java Client &
    java Server &
fi
