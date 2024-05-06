# AllMusic
一个全服点歌插件

![GIF.gif](img/allmusic.jpg)

![GIF.gif](img/GIF.gif)

注：需要配合[客户端Mod](https://github.com/Coloryr/AllMusic_Client)使用  
**不兼容旧版客户端Mod，需要新版客户端Mod**

[下载地址](https://www.123pan.com/s/Nh4zVv-BjOAH.html)

插件支持的服务器
- CatServer(LoliServer)
- Spigot(CraftBukkit)
- Paper
- Bungeecord
- Velocity
兼容但未测试服务器
- KCauldron
- Uranium
- Thermos
模组支持的服务器
- Forge(<1.20.4)
- NeoForge(>=1.20.5)
- Fabric

## 使用方法
1. 安装AllMusic插件  
复制`AllMusic-3.0.0-all.jar`到你的`plugins`文件夹  
重启过服务器
2. 安装客户端mod  
复制`[forge-xxx]AllMusic-3.0.0`到客户端的`mods`文件夹  
fabric同理

如果你是forge或者fabric服务器，复制`服务器mod`到你的mods文件夹下即可  
需要补全前置mod kotlinforforge 或者 kotlinforfabirc

## 播放VIP歌曲
1. 手机号创建网易云账户并购买网易云音乐VIP
2. 输入/music code 手机号码 获取手机验证码
3. 输入/music login 验证码 登录账户

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
3.0.0：将插件大部分重写
```
