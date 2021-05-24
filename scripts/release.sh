#!/usr/bin/env bash

set -e

cd "$(dirname "${BASH_SOURCE[0]}")"/..

while true; do
    read -p "Do you really want to PUBLISH a new RELEASE?" yn
    case $yn in
        [Yy]*) git tag "$(date +%s)" && git push origin --tags && exit 0 ;;
        [Nn]*) echo "Aborted!" ; exit 1 ;;
        * ) echo "answer yes or no";;
    esac
done
