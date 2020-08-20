#!/bin/sh

gitcmd="git -c commit.gpgsign=false"

color() {
    if [ ! -z "${2:-}" ]; then
        echo -e "\e[$1;$2m"
    else
        echo -e "\e[$1m"
    fi
}

colorend() {
    echo -e "\e[m"
}

paperstash() {
    # We want the exit code here to avoid "magic strings".
    $gitcmd stash push 2>/dev/null && STASHED=0 || STASHED="$?"
}

paperunstash() {
    if [ "${STASHED:-0}" ]; then
        $gitcmd stash pop 2>/dev/null || true # errors are ok
    fi
}

# vim: set ff=unix autoindent ts=4 sw=4 tw=0 et :
