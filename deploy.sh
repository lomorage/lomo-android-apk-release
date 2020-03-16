#!/bin/bash

echo -e "\033[0;32mDeploying lomorage.apk to GitHub...\033[0m"
 
### mm/dd/yyyy ###
now=$(date +'%Y-%m-%d_%H-%M-%S')
echo $now

rm -fr *.apk

cp '/c/xampp/htdocs/bitbucket/lomo-android-b/app/release/com.lomoware.lomorage-v1(0.1)-release.apk' ./

mv "./com.lomoware.lomorage-v1(0.1)-release.apk" "./com.lomoware.lomorage-v1(0.1)-release$now.apk"

apk_file="./com.lomoware.lomorage-v1(0.1)-release$now.apk"

tag=$now
hub release delete $now
hub release create -a $apk_file -m "latest release $now" $now