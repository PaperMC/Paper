#!/usr/bin/env bash
if [ -z "$1" ]; then
	echo "$0 <prID>"
	exit 1;
fi
repo=$(git remote get-url origin | sed -E 's/github.com(:|\/)//g')
data=$(curl -q https://api.github.com/repos/$repo/pulls/$1 2>/dev/null)
url=$(echo -e "$data" | grep --color=none ssh_url | head -n 1 |awk '{print $2}' | sed 's/"//g' | sed 's/,//g')
ref=$(echo -e "$data" | grep --color=none '"head":' -A 3 | grep ref | head -n 1 |awk '{print $2}' | sed 's/"//g' | sed 's/,//g')
prevbranch=$(\git branch --no-color 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/\1/')
branch="pr/$1"
up="pr-$1"
git remote remove $up 2>&1 1>/dev/null
git remote add -f $up $url
git branch -D $branch 2>/dev/null 1>&2
git checkout -b $branch $up/$ref 2>/dev/null|| true
echo "Merging $prevbranch into $branch"
git fetch origin
git merge origin/$prevbranch

echo "Dropping to new shell, exit to delete the refs"
bash -i

read -p "Press 'p' to push. " -n 1 -r >&2
echo
if [[ "d$REPLY" =~ ^d[Pp]$ ]]; then
	git push $up $branch:$ref -f

	echo "Pushed" >&2
fi

echo "Deleting branch/upstream"
git checkout $prevbranch
git branch -D $branch
git remote remove $up
git gc
#git branch -u $up/$ref $branch
#git checkout $branch