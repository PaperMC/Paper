#!/usr/bin/env bash
if [ -z "$1" ]; then
	echo "$0 <prID>"
	exit 1;
fi
repo=$(git remote get-url origin | sed -E 's/(.*@)?github.com(:|\/)//g' | sed 's/.git$//g')
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
read -p "Press 'm' to merge, 'r' to rebase, or 'n' for nothing" -n 1 -r >&2
echo
if [[ "$REPLY" =~ ^[Mm]$ ]]; then
	git merge origin/$prevbranch
elif [[ "$REPLY" =~ ^[Rr]$ ]]; then
	git rebase master
fi
echo "Dropping to new shell, exit to delete the refs"
"${SHELL:-bash}" -i

read -p "Press 'p' to push. " -n 1 -r >&2
echo
pushed=0
if [[ "$REPLY" =~ ^[Pp]$ ]]; then
	git push $up $branch:$ref -f
	pushed=1
	echo "Pushed" >&2
fi

echo "Deleting branch/upstream"
git checkout $prevbranch
if [[ "$pushed" == "1" ]]; then
	read -p "Press 'm' to merge or 'r' to rebase merge " -n 1 -r >&2
	if [[ "$REPLY" =~ ^[Mm]$ ]]; then
		git merge $branch
	fi
	if [[ "$REPLY" =~ ^[Rr]$ ]]; then
		git merge --ff-only $branch
	fi
fi

git branch -D $branch
git remote remove $up
git gc
#git branch -u $up/$ref $branch
#git checkout $branch
