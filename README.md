# lomo-android-apk-release
**Lomorage Android App APK package release**
## Latest update:
**1- This repo will only for pre-store release APK.**

**2- Official package, please go to Google Play Store or XiaoMi App Store.**

## 03/05/2021
- Added Local directory manager: filter which albumn need to backup
- Added Lomoframe register
- Added Cache Offline assets with preview quality, you can use Lomorage offline
- Performance improved
- [Check detail release with snapshots](./release20210305.md)




## 01/14/2021:
- Add white list for support formats to reduce retry-uploading


## 12/23/2020
For google play store.

# older version:

## ！！ 支持中文了

## Log

## 06/18/2020
> ## EN:
> - add search by YYYYmmDD, like: 20160520 will search out the asseets on May 20, 2016
> - also support search by year like: 2016, year + month: 201605. Enjoy to find the moments
> - fix some issues on Android 7.1
> - fully support Android 9.0+, esp. for **HUAWEI** phone
> - page navigation for many assets after search
> - improve speed: Fast as lightning

> ## Zh:
> **周末上新：安卓APP：**
> - 增加按年月日搜索，快速定位，翻看您的回忆
> - 修复了几个Android 7.1 上的问题
> - 全力支持Android 9.0+ 的手机，尤其是**华为**的手机
> - 分页显示搜索结果
> - 提高速度：快如闪电

## 20200510:
> ## EN:
>  - add clicable login on first time using
>  - add support Chinese OS

> ## Zh:
> **周末上新：安卓APP：**
> - 增加第一次提示登录功能
> - 支持中文系统



## 20200405: 
> ## EN:
>  - add batch share to lomorage user in the same network 
>  - add redundanc backup setting
>  - add view all users list in the same server
>  - add logout feature

> ## Zh:
> **周末上新：安卓APP：**
> - 增加在同一网络下的批量分享
> - 增加冗余备份设置
> - 增加查看用户列表
> - 增加登出功能

## 20200326: 
> ## EN:
>  - add batch detete 
>  - add show small pic setting
>  - add scan QR to add lomo server
>  - add refresh to find lomo server
>  - improve importing speed
>  - improve feedback
>  - improve find/delete duplicate assets
> ## Zh:
> **周末上新：安卓APP：**
> - 增加批量删除功能
> - 增加显示小图片设置
> - 增加扫描二维码添加服务器功能
> - 增加刷线查找服务器功能
> - 改进导入速度
> - 改进发送反馈功能
> - 增强查找去除重复 照片/视频 功能

# Deploy


> ./deploy.sh "h:/myproject/lomoware/lomo-android/app/release/" "com.lomoware.lomorage-v13(0.76)-release"
