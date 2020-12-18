apikey=$(curl --location --request POST 'http://localhost:8080/registration' | cut -c11-46)

curl --location --request POST 'http://localhost:8080/badges' \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw '{
  "name": "myBadgeName",
  "obtainedDate": "2020-12-18",
  "imageUrl": "www.bastien.com"
}'