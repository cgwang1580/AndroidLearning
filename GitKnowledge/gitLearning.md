## .git folder
下图为.git文件夹中比较重要的文件夹和文件的结构和意义：

##git HEAD
git HEAD可以指向一个分支，也可以指向commit，指向commit时为分离头指针状态；
几个和HEAD相关的git命令：

```
git diff HEAD HEAD^  (HEAD^^)
or
git diff HEAD HEAD~1  (HEAD~2)
```
HEAD^为HEAD的上一次commit

##shell git
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

## git commit
###1. 提交
```
git commit -m 'message'		//提交修改
```
###2. 修改上次/某次commit的message 
```
git commit --amend		//修改最近一次提交的message
git rebase -i 'commit_id'	//修改某次commit的message，commit_id应该为需要修改的commit的
	//上一级commit，之后会进入选择操作，改第一行为 reword/r,保存退出，然后会自动进入修改message
	//的界面，修改message，保存退出即可
```	
###3. 合并连续的几次commmit
```
git rebase -i 'commit_id'	//commit_id为需要合并的几次commit中最靠前的commit的上一个，进
入选择操作交互，第一个选pick，最后一个也是pick，中间几次需要合并的选择 squash/s，然后保存退出，然
后会跳转到合并提交commit的页面，添加合并log即可
```
###4. 合并不连续的几次commit
```
git rebase -i 'commit_id'	//commit_id为需要合并的几次commit中最靠前的commit的上一个，进
入选择操作交互后，第一个选pick，将需要合并的commit copy到将要合并到的commit的下面，改变状态为
squash/s，并删除刚刚复制的原commi记录，保存退出后会跳转到合并提交commit的页面，添加合并log即可。
```
>
注：若需要合并到的commit已经为根commit，则选择该commit作为rebase -i的commit_id，进入state修改界面之后
>


## Git log使用

最简单的看log的命令：git log   

后面可带的参数

```
--one line	//简介显示log，不包含修改者信息
--all	//查看所有分支的log	
--n4	//显示最近的4条记录
--graph		//以tree的方式查看commit的变化（多分支时好用）
```
以上几个参数可以同时使用！！

## git branch

```
git branch -va		//查看所有分支
git checkout 'branch_name'	//切换分支
git checkout -b 'branch_name'    //新建本地分支
git branch -d/-D 'branch_name' 	//删除本地分支，-d报错，-D强制删除
```
## GUI git
命令行输入：gitk
