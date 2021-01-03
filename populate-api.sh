address='http://localhost:8080'
apikey=$(curl --location --request POST "$address/registration" | cut -c11-46)

# Creating badges

curl --location --request POST "$address/badges" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw '{
  "name": "Bronze",
  "imageUrl": "https://emoji.gg/assets/emoji/8300_Bronze.png"
}'

curl --location --request POST "$address/badges" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw '{
  "name": "Silver",
  "imageUrl": "https://emoji.gg/assets/emoji/5633_Silver.png"
}'

curl --location --request POST "$address/badges" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw '{
  "name": "Gold",
  "imageUrl": "https://emoji.gg/assets/emoji/2005_Gold.png"
}'

# Creating pointScales

curl --location --request POST "$address/pointScales" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw '{
  "name": "Activity",
  "description": "Gain points by being active on the website"
}'

curl --location --request POST "$address/pointScales" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw '{
  "name": "Popularity",
  "description": "Gain points by owning popular comments or questions"
}'

# Printing api key

printf '\nall done! your api key is '$apikey'\n'