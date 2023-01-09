# AllMusic插件

一个全服点歌插件

![GIF.gif](img%2FGIF.gif)

注：需要配合[客户端Mod](https://github.com/HeartAge/AllMusic_M/)使用

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
>复制`AllMusic-2.18.0-all.jar`到你的`plugins`文件夹  
>重启过服务器
>2. 安装客户端mod  
>目前只支持  
>`forge:1.7.10 1.8 1.12.2 1.14 1.15 1.16 1.17 1.18 1.19`  
>`fabric:1.14 1.15 1.16 1.17 1.18 1.19`  
>复制`[forge-xxx]AllMusic-2.6.5`到客户端的`mods`文件夹  
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
