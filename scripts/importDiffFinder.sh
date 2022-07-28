commits=$(git rev-list base..HEAD | tr ' ' '\n' | tac | tr '\n' ' ')

susPatches=0

for rev in $commits
do
	commit=$(git show --name-status --pretty=oneline $rev)
	readarray -t array <<< "$commit"

	fileCountChanged=$(expr ${#array[@]} - 1)

	mod=0

	toPrint=$array

	for ((i=1;i<=fileCountChanged;i++)); do
		case ${array[i]} in
			M*)
				filename=$(echo ${array[i]} | sed -e "s/^M //")
				git --no-pager diff $rev~ $rev $filename | grep -q -e "+import" -e "-import"
				if [ $? -eq 0 ]; then
					mod=$(($mod+1))
					toPrint="$toPrint\n$filename"
				fi
			;;
		esac
	done

	if [ $mod -ne 0 ]; then
		echo -e $toPrint
		susPatches=$(($susPatches+1))
	fi
done

echo $susPatches patches contain evil import diffs
