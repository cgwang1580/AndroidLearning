##Shell Git
###1. Git配置   
```
git config --global/local/system user.name/email
```
```
--global  //对所有仓库有效
--local   //仅对当前仓库有效
--system  //对所有登录用户有效
```

###2. git常用命令

```
git mv readme readme.rd  //git重命名文件
```

###3. Git log使用

最简单的看log的命令：git log   

后面可带的参数

```
--one line	//简介显示log，不包含修改者信息
--all	//查看所有分支的log	
--n4	//显示最近的4条记录
--graph		//以tree的方式查看commit的变化（多分支时好用）
```
以上几个参数可以同时使用！！

##GUI Git
##图形界面
命令行输入：gitk
