#!/bin/bash
echo "Waiting spring to launch on 8080..."

while ! timeout 1m bash -c "echo > /dev/tcp/localhost/8080"; do   
  sleep 1
  echo "..."
done

echo "Spring launched"