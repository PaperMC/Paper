Files in this directory are applied directly to the tree before any other
patches.

These patches are always applied directly from the root of the repository.
"$basedir"

This allows us to fix malformed patch files and other changes from upstream.
This is not intended to replace any other system and should not be used in
place of the existing specific patch directories.

Documentation is intentionally sparse to avoid being misused.

`diff -ruN originalfile changedfile`  

See the man pages on diff and patch.

