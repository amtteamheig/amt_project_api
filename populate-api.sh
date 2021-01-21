#!/bin/bash

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
  "name": "Curiosity",
  "description": "Gain points by browsing the website"
}'

# Getting pointScales locations

activityPointScaleLocation=$(curl --location --request GET "$address/pointScales" --header "x-api-key: $apikey" | cut -c132-135)

while ! [[ $activityPointScaleLocation =~ $re ]] 
do
   activityPointScaleLocation=${activityPointScaleLocation::-1}
done

curiosityPointScaleLocation=$(expr $activityPointScaleLocation + 1)

# Creating rules

firstPost="firstPost"
questionPosted="questionPosted"
commentPosted="commentPosted"
openAQuestion="openAQuestion"


curl --location --request POST "$address/rules" \
--header 'Content-Type: application/json' \
--header "x-api-key: $apikey" \
--data-raw "{
  \"if\": {
    \"type\": \"$firstPost\"
  },
  \"then\": {
    \"awardBadge\": \"/badges/$goldBadgeLocation\",
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
    \"type\": \"$commentPosted\"
  },
  \"then\": {
    \"awardBadge\": \"/badges/$silverBadgeLocation\",
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
    \"type\": \"$openAQuestion\"
  },
  \"then\": {
    \"awardBadge\": \"/badges/$bronzeBadgeLocation\",
    \"awardPoints\": {
      \"pointScale\": \"/pointScales/$curiosityPointScaleLocation\",
      \"amount\": 1
    }
  }
}"

# check command line arguements

if [ $# -eq 1 ] && [ $1 == "-u" ]
then 

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
	
	curl --location --request POST "$address/events" \
	--header "x-api-key: $apikey" \
	--header 'Content-Type: application/json' \
	--data-raw "{
	  \"userId\": \"$user1\",
	  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
	  \"type\": \"$openAQuestion\"
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
	  \"type\": \"$openAQuestion\"
	}"
	
	curl --location --request POST "$address/events" \
	--header "x-api-key: $apikey" \
	--header 'Content-Type: application/json' \
	--data-raw "{
	  \"userId\": \"$user2\",
	  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
	  \"type\": \"$openAQuestion\"
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
	  \"type\": \"$openAQuestion\"
	}"

	curl --location --request POST "$address/events" \
	--header "x-api-key: $apikey" \
	--header 'Content-Type: application/json' \
	--data-raw "{
	  \"userId\": \"$user3\",
	  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
	  \"type\": \"$openAQuestion\"
	}"

	curl --location --request POST "$address/events" \
	--header "x-api-key: $apikey" \
	--header 'Content-Type: application/json' \
	--data-raw "{
	  \"userId\": \"$user3\",
	  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
	  \"type\": \"$openAQuestion\"
	}"

	curl --location --request POST "$address/events" \
	--header "x-api-key: $apikey" \
	--header 'Content-Type: application/json' \
	--data-raw "{
	  \"userId\": \"$user3\",
	  \"timestamp\": \"2020-12-18T09:39:23.676Z\",
	  \"type\": \"$openAQuestion\"
	}"


fi

# Printing api key

printf '\nall done! your api key is '$apikey'\n'