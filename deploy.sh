#!/bin/bash

echo -e "\033[0;32mDeploying lomorage.apk to GitHub...\033[0m"
 
### mm/dd/yyyy ###
now=$(date +'%Y-%m-%d_%H-%M-%S')
echo $now

rm -fr *.apk

### $1
orig_apk_path=$1
version_code=$3
release_time=$now

homepage='/h/myproject/lomoware/homepage'

echo "orig_apk_path="$orig_apk_path
if [[ "$orig_apk_path" == "" ]]; then
    orig_apk_path='/h/myproject/lomoware/lomo-android/app/release/'
fi

### $2
orig_apk_file=$2
echo "orig_apk_file="$orig_apk_file
if [[ "$orig_apk_file" == "" ]]; then
    orig_apk_file="com.lomoware.lomorage-v9(0.72)-release"
fi


echo $orig_apk_file

orig_apk_file_path=$orig_apk_path$orig_apk_file'.apk'

echo $orig_apk_file_path

cp $orig_apk_file_path ./

release_apk_file=$orig_apk_file$now.apk

echo $release_apk_file

mv "./"$orig_apk_file".apk" "./"$release_apk_file



tag=$now
hub release delete $now
hub release create -a $release_apk_file -m "latest release $now" $now

cd $homepage
./updatever.sh -p android -D $version_code -T $release_time