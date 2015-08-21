#!/bin/bash

rm -rf .idea/copyright
rm -rf .idea/libraries
rm -rf .idea/modules
rm -rf .idea/scopes
rm -rf .idea/.name
rm -rf .idea/*.xml

rm -rf project/project
rm -rf project/target

find . -name "skylark-*" -type d -exec rm -rf {}/target \;
find . -name "skylark-*" -type d -exec rm -rf {}/project \;

rm -rf target

RESULT=$?

exit ${RESULT}