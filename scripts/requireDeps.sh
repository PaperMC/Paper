#!/bin/sh
set -ue

# Check if an application is on the PATH.
# If it is not, return with non-zero.
_is_dep_available() {
	command -v "$1" >/dev/null || (echo "$1 was not found and is a required dependency"; false)
}

_is_dep_available git
_is_dep_available patch
_is_dep_available mvn
