# AllMusic插件

一个全服点歌插件，需要配合 [Mod](https://github.com/HeartAge/AllMusic_M/) 使用

## 使用方法
>1. 安装AllMusic插件  
>支持CraftBukkit/Spigot/Bungeecord服务器  
>复制`AllMusic-2.14.7-all.jar`到你的`plugins`文件夹  
>2. 安装客户端mod  
>目前只支持  
>`forge:1.7.10 1.8 1.12.2 1.14 1.15 1.16 1.17 1.18`  
>`fabric:1.14 1.15 1.16 1.17 1.18`  
>复制`[forge-xxx]AllMusic-2.11.0`到客户端的`mods`文件夹  
>fabric同理  

>重载插件

## 播放VIP歌曲
1. 在插件配置文件的`LoginUser`填写手机号
2. 重载插件或者重启服务器
3. 输入/music code 获取手机验证码
4. 输入/music login 验证码 登录账户

如果登录失效，请删除`cookie.json`再打`/music reload`

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
