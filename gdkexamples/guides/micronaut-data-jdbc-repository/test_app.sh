#!/bin/bash
. ./common.sh
if [ -f resources.sh ]
then
    . ./resources.sh
fi

echo 'Waiting for resources to be created...'

check_app_running_or_exit 200

echo 'Test started...'

EXIT_VALUE=0

echo "Testing POST request..."

#tag::add-genre[]
TEST_RES=$(curl -X "POST" "http://localhost:8080/genres" \
               -H 'Content-Type: application/json; charset=utf-8' \
               -d $'{ "name": "music" }')

echo "Result: $TEST_RES"
#end::add-genre[]

ITEM_ID=$(jq -r '.id' <<< "$TEST_RES")
ITEM_NAME=$(jq -r '.name' <<< "$TEST_RES")

if [[ $ITEM_NAME != "music" ]]; then
  echo -e "${RED}Failed${RESET}"
  EXIT_VALUE=1
else
  echo -e "${GREEN}Ok${RESET}"
fi

echo "Test GET request..."

#tag::get-genres[]
TEST_RES=$(curl -s localhost:8080/genres/list | jq -r ".[] | select(.id == $ITEM_ID) | .name")

echo "Result: $TEST_RES"
#end::get-genres[]

if [[ $TEST_RES != "music" ]]; then
  echo -e "${RED}Failed${RESET}"
  EXIT_VALUE=1
else
  echo -e "${GREEN}Ok${RESET}"
fi


echo "Test DELETE request..."

curl -s -X "DELETE" "http://localhost:8080/genres/$ITEM_ID"

TEST_RES=$(curl -s localhost:8080/genres/list | jq -r ".[] | select(.id == $ITEM_ID) | .name")

echo "Result: $TEST_RES"

if [[ $TEST_RES == "music" ]]; then
  echo -e "${RED}Failed${RESET}"
  EXIT_VALUE=1
else
  echo -e "${GREEN}Ok${RESET}"
fi

reportResultAndExit $EXIT_VALUE