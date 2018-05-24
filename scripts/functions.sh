#!/usr/bin/env bash

gitcmd="git -c commit.gpgsign=false"

color() {
    if [ $2 ]; then
            echo -e "\e[$1;$2m"
    else
            echo -e "\e[$1m"
    fi
}
colorend() {
    echo -e "\e[m"
}

paperstash() {
    STASHED=$($gitcmd stash  2>/dev/null|| return 0) # errors are ok
}

paperunstash() {
    if [[ "$STASHED" != "No local changes to save" ]] ; then
        $gitcmd stash pop 2>/dev/null|| return 0 # errors are ok
    fi
}
