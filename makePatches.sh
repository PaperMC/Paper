#!/bin/bash

if [ -z "$1" ]
then
	echo "Please run this script again with the clean decompile sources as an argument. In most cases this will be ../work/decompile-XXXX"
	exit
fi

rm -f nms-patches/*

for file in $(ls src/main/java/net/minecraft/server)
do
	echo "Diffing $file"
	dos2unix -q $1/net/minecraft/server/$file $1/net/minecraft/server/$file
	diff -u $1/net/minecraft/server/$file src/main/java/net/minecraft/server/$file > nms-patches/"$(echo $file | cut -d. -f1)".patch
done
