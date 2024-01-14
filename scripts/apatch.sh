#!/usr/bin/env bash
# Bash script for applying and wiggling patches using Git

echo "Executing the patching script..."

git_command="git -c commit.gpgsign=false"

no_apply=1
is_reject=0

# Check if the "--noapplied" flag is provided
if [[ $1 == "--noapplied" ]]; then
    no_apply=1
    shift
fi

# Check if a file is specified
if [ ! -z "$1" ]; then
    patch_file="$1"
# If no file is specified and there's a patch file in rebase-apply, use it
elif [ -z "$1" ] && [ -f .git/rebase-apply/patch ]; then
    patch_file=".git/rebase-apply/patch"
    no_apply=1
    is_reject=1
else
    echo "Please specify a file"
    exit 1
fi

applied_patch=$(echo $patch_file | sed 's/.patch$/-applied\.patch/g')

# Check if the "--reset" flag is provided
if [ "$1" == "--reset" ]; then
    $git_command am --abort
    $git_command reset --hard
    $git_command clean -f
    exit 0
fi

# Attempt to apply the patch using "git am"
(test "$is_reject" != "1" && $git_command am -3 $patch_file) || (
    echo "Failures - Wiggling"
    $git_command reset --hard
    $git_command clean -f
    errors=$($git_command apply --rej $patch_file 2>&1)
    echo "$errors" >> ~/patch.log
    export missing_files=""
    export summary_fail=""
    export summary_good=""
    for i in $(find . -name \*.rej); do
        base=$(echo "$i" | sed 's/.rej//g')
        if [ -f "$i" ]; then
            sed -e 's/^diff a\/\(.*\) b\/\(.*\)[[:space:]].*rejected.*$/--- \1\n+++ \2/' -i $i && wiggle -v -l --replace "$base" "$i"
            rm "$base.porig" "$i"
        else
            echo "No such file: $base"
            missing_files="$missing_files\n$base"
        fi
    done
    for i in $($git_command status --porcelain | awk '{print $2}'); do
        file_data=$(cat "$i")
        if [ -f "$patch_file" ] && [[ "$file_data" == *"<<<<<"* ]]; then
            export summary_fail="$summary_fail\nFAILED TO APPLY: $i"
        else
            $git_command add --force "$i"
            export summary_good="$summary_good\nAPPLIED CLEAN: $i"
        fi
    done
    echo -e "$summary_good"
    echo -e "$summary_fail"
    if [[ "$errors" == *"No such file"* ]]; then
        echo "===========================";
        echo " "
        echo " MISSING FILES"
        echo $(echo "$errors" | grep "No such file")
        echo -e "$missing_files"
        echo " "
        echo "===========================";
    fi
    $git_command status
    $git_command diff
)

# Move the patch file to a new name if "--noapplied" is not specified and it's not already an applied patch
if [[ "$no_apply" != "1" ]] && [[ "$patch_file" != *-applied.patch ]]; then
    mv "$patch_file" "$applied_patch"
fi
