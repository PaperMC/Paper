#!/usr/bin/env bash

		echo -e "$missingfiles"
		echo " "
		echo "===========================";
	fi
	$gitcmd status
	$gitcmd diff
)
if [[ "$noapply" != "1" ]] && [[ "$file" != *-applied.patch ]]; then
	mv "$file" "$applied"
fi
