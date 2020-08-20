#!/usr/bin/env sh

gitcmd="git -c commit.gpgsign=false"

color() {
    if [ -n "${2:-}" ]; then
        printf "\e[%s;%sm" "$1" "$2"
    else
        printf "\e[%sm" "$1"
    fi
}

colorend() {
    printf "\e[m"
}

# Check if a string contains a substring.
#
# https://stackoverflow.com/a/8811800/5830060
str_contains() {
    string="$1"
    substring="$(printf '%q' "$2")"
    if [ "${string#*$substring}" != "$string" ]; then
        return 0 # $substring is in $string
    else
        return 1 # $substring is NOT in $string
    fi
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
