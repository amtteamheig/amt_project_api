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

# Getting badges locations

bronzeBadgeLocation=$(curl --location --request GET "$address/badges" --header "x-api-key: $apikey" | cut -c125-128)
re='^[0-9]+$'

while ! [[ $bronzeBadgeLocation =~ $re ]] 
do
   bronzeBadgeLocation=${bronzeBadgeLocation::-1}
done

silverBadgeLocation=$(expr $bronzeBadgeLocation + 1)
goldBadgeLocation=$(expr $bronzeBadgeLocation + 2)

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

# Getting pointScales locations

activityPointScaleLocation=$(curl --location --request GET "$address/pointScales" --header "x-api-key: $apikey" | cut -c132-135)

while ! [[ $activityPointScaleLocation =~ $re ]] 
do
   activityPointScaleLocation=${activityPointScaleLocation::-1}
done

popularityPointScaleLocation=$(expr $activityPointScaleLocation + 1)

# Creating rules

firstPost="firstPost"
questionPosted="questionPosted"
commentPosted="commentPosted"
rateCommentOrQuestion="rateCommentOrQuestion"
popularCommentOrQuestion="popularCommentOrQuestion"


curl --location --request POST "$address/rules" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw "{
  \"if\": {
    \"type\": \"$firstPost\"
  },
  \"then\": {
    \"awardBadge\": \"/badges/$silverBadgeLocation\",
    \"awardPoints\": {
      \"pointScale\": \"/pointScales/$activityPointScaleLocation\",
      \"amount\": 3
    }
  }
}"

curl --location --request POST "$address/rules" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw "{
  \"if\": {
    \"type\": \"$questionPosted\"
  },
  \"then\": {
    \"awardBadge\": \"/badges/$bronzeBadgeLocation\",
    \"awardPoints\": {
      \"pointScale\": \"/pointScales/$activityPointScaleLocation\",
      \"amount\": 3
    }
  }
}"

curl --location --request POST "$address/rules" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw "{
  \"if\": {
    \"type\": \"$commentPosted\"
  },
  \"then\": {
    \"awardBadge\": \"/badges/$bronzeBadgeLocation\",
    \"awardPoints\": {
      \"pointScale\": \"/pointScales/$activityPointScaleLocation\",
      \"amount\": 2
    }
  }
}"

curl --location --request POST "$address/rules" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw "{
  \"if\": {
    \"type\": \"$rateCommentOrQuestion\"
  },
  \"then\": {
    \"awardBadge\": \"/badges/$bronzeBadgeLocation\",
    \"awardPoints\": {
      \"pointScale\": \"/pointScales/$activityPointScaleLocation\",
      \"amount\": 1
    }
  }
}"

curl --location --request POST "$address/rules" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw "{
  \"if\": {
    \"type\": \"$popularCommentOrQuestion\"
  },
  \"then\": {
    \"awardBadge\": \"/badges/$goldBadgeLocation\",
    \"awardPoints\": {
      \"pointScale\": \"/pointScales/$popularityPointScaleLocation\",
      \"amount\": 1
    }
  }
}"

# Events (users)

user1="1"
user2="2"
user3="3"    

#user1

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user1\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$firstPost\"
}"

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user1\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$questionPosted\"
}"

#user2

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user2\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$firstPost\"
}"

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user2\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$commentPosted\"
}"

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user2\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$popularCommentOrQuestion\"
}"


#user3

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user3\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$firstPost\"
}"

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user3\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$rateCommentOrQuestion\"
}"

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user3\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$rateCommentOrQuestion\"
}"

curl --location --request POST "$address/events" \
--header "x-api-key: $apikey" \
--header 'Content-Type: application/json' \
--data-raw "{
  \"userId\": \"$user3\",
  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
  \"type\": \"$rateCommentOrQuestion\"
}"

# Printing api key

printf '\nall done! your api key is '$apikey'\n'