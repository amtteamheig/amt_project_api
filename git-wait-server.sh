#!/bin/bash
echo "Waiting spring to launch on 8080..."
timeout 300 bash -c 'while [[ "$(curl -s -o /dev/null -w ''%{http_code}'' localhost:8080)" != "200" ]]; do sleep 5; done' || false