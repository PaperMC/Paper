#!/usr/bin/env bash

# Set root project directory
CWD=$(pwd)

rbp() {
    NEW_CWD=$(pwd)
    cd "$CWD"
    scripts/rebuildPatches.sh "$CWD"
    cd "$NEW_CWD"
}

rebuildPatches() {
    rbp
}

lunch() {
    NEW_CWD=$(pwd)
    cd "$CWD"

    if [[ "$1" = "jar" ]] || [[ "$1" = "--jar" ]] ; then
        ARG="--jar"
    fi

    scripts/build.sh "$CWD" "$ARG"
    ARG=""

    cd "$NEW_CWD"
}

root() {
    cd "$CWD"
}

LAST_EDIT=""

edit() {
    if [[ "$1" = "server" ]] ; then
        cd "$CWD/Paper-Server"
        LAST_EDIT=$(pwd)

        stash
        git rebase -i upstream/upstream
        unstash
    elif [[ "$1" = "api" ]] ; then
        cd "$CWD/Paper-API"
        LAST_EDIT=$(pwd)

        stash
        git rebase -i upstream/upstream
        unstash
    elif [[ "$1" = "continue" ]] ; then
        cd "$LAST_EDIT"
        git add .
        git commit --amend
        git rebase --continue
    else
        echo "You must edit either the api or server."
    fi
}

stash() {
    STASHED=$(git stash)
}

unstash() {
    if [[ "$STASHED" != "No local changes to save" ]] ; then
        git stash pop
    fi
}