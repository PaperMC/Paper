#!/bin/sh
set -ue

# Check if an application is on the PATH.
# If it is not, return with non-zero.
_is_dep_available() {
	command -v "$1" >/dev/null || (echo "$1 was not found and is a required dependency"; return 1)
}

if [ -z "${1:-}" ]; then
    _is_dep_available git
    _is_dep_available patch
else
    for dep in $@; do
        _is_dep_available "$dep"
    done
fi

# vim: set ff=unix autoindent ts=4 sw=4 tw=0 et :
