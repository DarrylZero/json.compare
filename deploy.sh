#!/bin/bash
echo "Publishing jar files ..."

gradle :resource_plugin:publish -Prepo_password=$1


