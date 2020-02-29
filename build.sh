#!/bin/bash

if [[ $# -ne 2 ]]; then
	echo "Bad args"
	echo "1 - dirname"
	echo "2 - task number"
	exit 1
fi

dir=$1
no=$2
outname="Mleczko_Antoni_$no"

cp -r $dir $outname
zip -r $outname.zip $outname

rm -r $outname

