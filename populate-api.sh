address='http://localhost:8080'
apikey=$(curl --location --request POST "$address/registration" | cut -c11-46)

curl --location --request POST "$address/badges" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw '{
  "name": "myBadgeName",
  "obtainedDate": "2020-12-18",
  "imageUrl": "www.bastien.com"
}'

printf '\nall done! your api key is '$apikey'\n'