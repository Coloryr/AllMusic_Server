# AllMusic插件

一个全服点歌插件，需要配合 [Mod](https://github.com/HeartAge/AllMusic_M/) 使用

## 使用方法
>1. 安装AllMusic插件  
>支持CraftBukkit/Spigot/Bungeecord服务器  
>复制`AllMusic-2.6.1.jar`到你的`plugins`文件夹  
>2. 安装客户端mod  
>目前只支持  
>`forge:1.12.2 1.14 1.15 1.16`  
>`fabric:1.14 1.15 1.16`  
>复制`[forge-1.1x]AllMusic-2.3.0(hotfix)`到你的`mods`文件夹  
>fabric同理  
>3. 启动外置API（可选）  
>下载群文件的`NeteaseCloudMusicApi.7z`并解压到一个地方  
>windows双击`start.cmd`  
>Linux`$ sudo ./start.sh`  
>出现  
>```
>server running @ http://localhost:4000
>```  
>为启动成功

## 播放VIP歌曲
1. 使用外置AIP
2. 按照 [这个步骤](https://binaryify.github.io/NeteaseCloudMusicApi/#/?id=登录) 登录黑胶账户

## PAPI变量  
> %AllMusic_NowMusicName%歌曲名字  
> %AllMusic_NowMusicAl%歌曲专辑  
> %AllMusic_NowMusicAlia%歌曲原曲  
> %AllMusic_NowMusicAuthor%歌曲作者  
> %AllMusic_NowMusicCall%点歌人  
> %AllMusic_NowMusicInfo%歌曲所有信息  
> %AllMusic_ListSize%队列大小  
> %AllMusic_MusicList%队列歌曲  
> %AllMusic_Lyric%歌词  
> %AllMusic_TLyric%翻译的歌词