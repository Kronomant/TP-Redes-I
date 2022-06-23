#!/bin/bash

echo "Killing server and/or client"
pkill -f "java Server"
pkill -f "java Client"
