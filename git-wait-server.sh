#!/bin/bash
while ! curl --output /dev/null --silent --head --fail http://localhost:8080; 
do sleep 1 && echo -n .;
done;