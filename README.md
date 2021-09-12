# lomo-android-apk-release
**Lomorage Android App APK package release**
## Latest update:
**1- This repo will only for pre-store release APK.**

**2- Official package, please go to Google Play Store or XiaoMi App Store.**



## Latest 09/12/2021
- add meaning msg while create user fail


## 最近更新：2021-07-22

- 修复了用户反馈的问题：远程连接崩溃
- 增加了备份页面刷新获取另外设备上传的照片
- 增加了App版本更新：设置有徽章提醒
- 引入了分析报告
- App端目录配置移到共享文件夹：重新安装之后依然有效
- 应用了Material主题
- 增加了查看资源Hash值，方便确认是否重复照片

### 2021-07-16
- 增加了隐私说明
- 增加了当用户点击拒绝权限时的提示
- 修复了发送email 有时没有 App 日志的问题




## 05/19/2021 - EN
- Fix loading album crash issue on some devices.
- Add crash handler log

## 05/19/2021 - 中文
- 修复扫描本地文件崩溃问题
- 增加崩溃日志


## 04/09/2021 - EN
- Add privacy message
- Add tips for deny the permission
- Fix sending feedback issue

## 04/09/2021 - 中文
- 增加了隐私说明
- 增加了当用户点击拒绝权限时的提示
- 修复了发送email 有时没有 App 日志的问题
## 03/11/2021 - EN

- fix gallery can not be refresh issue
- add origin button on big view and setting for auto fetch original
- add preview size selection before manually cache

## 03/11/2021 - ZH_CN

- 修复备份页面有时不刷新的问题
- 增加了脱机缓存可以选大小
- 增加了设置可以不自动下载原始图（点一下下载原始图）-可配置



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


> ./deploy.sh "h:/myproject/lomoware/lomo-android-v232/app/release/" "com.lomoware.lomorage-v31(0.857)-release" 31
