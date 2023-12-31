# AllMusic插件

一个全服点歌插件

![GIF.gif](img%2FGIF.gif)

注：需要配合[客户端Mod](https://github.com/Coloryr/AllMusic_Client)使用

[下载地址](https://www.aliyundrive.com/s/fKHnLh1N5nC)

支持的服务器
- LoliServer
- CatServer
- CraftBukkit
- Spigot
- Paper
- Bungeecord
- Velocity
- KCauldron
- Uranium
- Thermos
- Mirai

## 使用方法
>1. 安装AllMusic插件  
>复制`AllMusic-2.19.2-all.jar`到你的`plugins`文件夹  
>重启过服务器
>2. 安装客户端mod  
>目前只支持  
>`forge:1.7.10 1.8 1.12.2 1.14 1.15 1.16 1.17 1.18 1.19 1.20`  
>`fabric:1.14 1.15 1.16 1.17 1.18 1.19 1.20`  
>复制`[forge-xxx]AllMusic-2.6.8`到客户端的`mods`文件夹  
>fabric同理

## 播放VIP歌曲
1. 手机号创建网易云账户并购买网易云音乐VIP
2. 在插件配置文件的`LoginUser`填写手机号
3. 重载插件或者重启服务器
4. 输入/music code 获取手机验证码
5. 输入/music login 验证码 登录账户

如果登录失效，请删除`cookie.json`再打`/music reload`再重新登录

## PAPI变量  
> %allmusic_now_music_name% 歌曲名字  
> %allmusic_now_music_al% 歌曲专辑  
> %allmusic_now_music_alia% 歌曲原曲  
> %allmusic_now_music_author% 歌曲作者  
> %allmusic_now_music_call% 点歌人  
> %allmusic_now_music_info% 歌曲所有信息  
> %allmusic_list_size% 队列大小  
> %allmusic_music_list% 队列歌曲  
> %allmusic_lyric% 歌词  
> %allmusic_tlyric% 翻译的歌词
> %allmusic_klyric% KTV歌词  

### 更新日志

```

插件：
1.0.0：完成本体
1.5.0：增加bukkit服务器支持
1.6.0：增加歌曲搜索
1.7.0：新增VV支持
1.8.0：优化体验
1.9.0：修复bug
1.10.0：多API支持
1.11.0：新的语言文件！
1.12.0：新增PAPI
1.12.1：新增本地API支持
1.13.0：删除冗余代码
1.13.3：删除API1
1.14.0：增加本地API
1.17.0：改进VV钩子
2.0.0：显示HUD
2.1.0：修复不能听歌的bug
2.4.0：修bug
2.5.0：增加花钱点歌
2.6.2：删除spigot依赖
2.7.0：支持个人歌单播放，修复登录bug
2.8.0：Spigot和Bukkit分开支持
2.9.0：自动安装外置API
2.10.0：Velocity支持
2.12.0：图片支持，完全内置API
2.12.2：修复各种bug
2.12.7：修复各种bug
2.14.0：支持中途加入听歌
2.14.5：修复网易云登录问题
2.14.6：合并1.7.10分支，修复各种bug
2.14.7：（我说是debug你信吗
2.14.8：添加1.8支持
2.14.10：修复1.7.10兼容问题
2.16.0：新增群组服也能获取PAPI
2.16.4：npc检测
2.16.5：登录错误修复
2.17.5：KTV歌词
2.17.8：修复歌词问题
2.17.9：修复播放检查问题
2.17.10：修复播放完结列表错误
2.18.0：图片旋转功能
2.18.1：修复中途加入播放问题
2.18.2：修复歌词问题
2.18.3：修复BC的播放问题
2.18.5：加入Mod服务器支持
2.18.6：修复Mod服务器问题
2.18.7：加入空白歌词设置
2.18.8：修复Mod服务器点击事件问题
2.18.9：添加folia服务端支持
2.18.10：BC改名Top
2.19.0：新增玩家点歌限制
2.19.2：修复潜在的问题

MOD：
1.2.0：无需指令就能修改音量
1.3.0：优化问题，修复若干bug，新增自动停止播放音乐
2.0.0：显示Hud
2.0.5：1.16支持
2.0.6：不知道什么更新
2.1.0：修bug
2.2.0：修复放歌中断
2.5.0：图片支持
2.5.9：FLAC支持，使用AL10，兼容boat
2.5.10：AL10修改，GUI底层显示
2.5.11：增加中途加入听歌
2.6.0：新增图片大小调整
2.6.3：1.19支持
2.6.5：图片旋转
2.6.8：尝试修复掉帧问题
```
