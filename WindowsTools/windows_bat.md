* [常用命令](#%E5%B8%B8%E7%94%A8%E5%91%BD%E4%BB%A4)
  * [<strong>1\. echo</strong> 命令](#1-echo-%E5%91%BD%E4%BB%A4)
    * [1\.1 echo 打印](#11-echo-%E6%89%93%E5%8D%B0)
    * [2\.2 echo off 关闭命令回显](#22-echo-off-%E5%85%B3%E9%97%AD%E5%91%BD%E4%BB%A4%E5%9B%9E%E6%9
8%BE)
      * [2\.2\.1 测试1](#221-%E6%B5%8B%E8%AF%951)
      * [2\.2\.2 测试2](#222-%E6%B5%8B%E8%AF%952)
      * [2\.2\.3 测试3](#223-%E6%B5%8B%E8%AF%953)
    * [2\.3 小结](#23-%E5%B0%8F%E7%BB%93)
  * [<strong>2\. 参数传入</strong>](#2-%E5%8F%82%E6%95%B0%E4%BC%A0%E5%85%A5)
    * [2\.1 参数](#21-%E5%8F%82%E6%95%B0)
    * [%~dp0](#dp0)
  * [<strong>3\. 变量</strong>](#3-%E5%8F%98%E9%87%8F)
    * [3\.1 设置和使用变量](#31-%E8%AE%BE%E7%BD%AE%E5%92%8C%E4%BD%BF%E7%94%A8%E5%8F%98%E9%87%8F)
    * [3\.2 变量延迟扩展](#32-%E5%8F%98%E9%87%8F%E5%BB%B6%E8%BF%9F%E6%89%A9%E5%B1%95)
  * [<strong>4\. FOR 循环</strong>](#4-for-%E5%BE%AA%E7%8E%AF)
    * [4\.1 FOR 命令](#41-for-%E5%91%BD%E4%BB%A4)
    * [4\.2 含有 /F 的 FOR 循环](#42-%E5%90%AB%E6%9C%89-f-%E7%9A%84-for-%E5%BE%AA%E7%8E%AF)



# 常用命令

----------
## **1. `echo`** 命令
### 1.1 `echo` 打印

```
// 控制台输出test1, 2
echo test1, 2
```

### 2.2 `echo off` 关闭命令回显
#### 2.2.1 测试1

``` shell
/// test.bat
// sample
echo off
echo 这是测试内容的第1行
echo 这是测试内容的第2行
echo end
pause
```
输出结果如下：
>
> C:\Users\**\Desktop\TestCmake\build>echo off
> 这是测试内容的第1行
> 这是测试内容的第2行
> end
> 请按任意键继续. . .

#### 2.2.2 测试2
修改第一行为`@echo off`，`@` 表示不显示@后面的命令
输出结果如下：
>
> 这是测试内容的第1行
> 这是测试内容的第2行
> end
> 请按任意键继续. . .

#### 2.2.3 测试3
修改第一行为`::@echo off`
输出结果如下：
>
> C:\Users\**\Desktop\TestCmake\build>echo 这是测试内容的第1行
> 这是测试内容的第1行
> C:\Users\**\Desktop\TestCmake\build>echo 这是测试内容的第2行
> 这是测试内容的第2行
> C:\Users\**\Desktop\TestCmake\build>echo end
> end
> C:\Users\**\Desktop\TestCmake\build>pause
> 请按任意键继续. . .

### 2.3 小结
+ `echo off`表示执行了这条命令后关闭该命令后所有命令的回显。
+ `@echo off`表示执行了这条命令后关闭所有命令(包括本身这条命令)的回显。

----------
## **2. 参数传入**
### 2.1 参数
批处理文件中可引用的参数为%0~%9，%0是指批处理文件的本身，也可以说是一个外部命令；%1~%9是批处理参数，也称形参。
Test:
``` shell
@echo off
echo param[0] = %0
echo param[0] = %1
echo param[0] = %2
echo end
pause
```
>
> 执行该命令`test.bat 50`后输出：
> param[0] = test.bat
> param[0] = 50
> param[0] =
> end
> 请按任意键继续. . .

### `%~dp0`
+ `%0`代表当前bat文件，返回绝对路径
+ `%~dp0`代表当前bat文件所在路径 

``` shell
echo %0
echo %~dp0
```
输出结果：
>
> test.bat
> C:\Users\***\Desktop\TestCmake\build\

----------
## **3. 变量**

### 3.1 设置和使用变量
``` shell
set root_path=C:\Users\Administrator\Desktop\
:: %%使用
ROOT_PATH=%root_path%
```

### 3.2 变量延迟扩展

``` shell
setlocal enabledelayedexpansion
```
sample:

``` shell
@echo off
setlocal enabledelayedexpansion    ::注意这里
set str=test        :: set 变量=前后不要有空格！！！！！
if %str%==test (
    set str=another test
    echo !str!      ::注意这里
    echo %str%      ::区别
)
```
> 结果：第一行输出another test，第二行输出test

1. windows在解释执行此代码段时，在遇到if语句后的括号后，只把它当一条语句处理而不是两条语句
2. `setlocal enabledelayedexpansion`用于开启变量延迟，这是告诉解释器，在遇到复合语句的时候，不要将其作为一条语句同时处理，而仍然一条一条地去解释。但是这时必须用`!str!`来引用变量，如果仍然用`%str%`引用是不起作用的。

``` shell
setlocal disabledelayedexpansion   :: 关闭变量扩展延迟
```
在处理`echo`的变量带有`!`时，若开启变量扩展延迟，会导致`!`不见了，可以在处理带`!`打印时关闭变量扩展延迟，之后再打开。

----------
## **4. `FOR` 循环**

### 4.1 `FOR` 命令 
``` shell
:: 在cmd窗口中：
for %I in (command1) do command2
:: 在批处理文件中：
for %%I in (command1) do command2
```
Attention:

 + `FOR` 循环的变量`%%I`在批处理中是`%%`，在命令行中为`%`；
 + `FOR` `IN` `DO` 是for语句的关键字，它们三个缺一不可；
 + `%%I` 是for语句中对形式变量的引用(`%%I`为形参，可以使用26个字母中的其中一个，区分大小写)，就算它在`DO`后的语句中没有参与语句的执行，也是必须出现的；
 + `IN`之后，`DO`之前的括号不能省略；
 + command1表示字符串或变量，command2表示字符串、变量或命令语句，command1表示的字符串或变量可以是一个，也可以是多个，每一个字符串或变量，我们称之为一个元素，每个元素之间，用空格键、跳格键、逗号、分号或等号分隔；

### 4.2 含有 `/F` 的 `FOR` 循环
``` shell
:: 主要用来处理文件和一些命令的输出结果
FOR /F ["options"] %%variable  IN (file-set) DO command
FOR /F ["options"] %%variable  IN ("string") DO command
FOR /F ["options"] %%variable  IN ('command') DO command
```
 说明：包含`/F`的参数可以处理文件内容（file-set）、字符串("string")以及执行指定命令('command')返回回的值。可以通过设置["options"]值实现相关需求，["options"]可省略。