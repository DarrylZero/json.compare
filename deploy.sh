#!/bin/bash
echo "Publishing jar files ..."

gradle :resource_plugin:publish -Prepo_user=$1 -Prepo_password=$2


