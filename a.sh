#!/bin/bash

cd patches/server


for file in [0-9][0-9][0-9][0-9]-*.patch; do
    suffix=$(echo $file | cut -d'-' -f2-)
    mv "$file" "$suffix"
done
