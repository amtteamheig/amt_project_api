#!/bin/bash
printf "%s" "waiting for ServerXY ..."
while ! timeout 0.2 ping -c 1 -n "http://localhost:8080/" &> /dev/null
do
    printf "%c" "."
done
printf "\n%s\n"  "Server is back online"