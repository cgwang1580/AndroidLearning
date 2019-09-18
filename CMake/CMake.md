[<strong>常用命令</strong>](#%E5%B8%B8%E7%94%A8%E5%91%BD%E4%BB%A4)

* [<strong>set\_target\_properties &amp;&amp; get\_target\_property</strong>](#set_target_properties--get_target_property)
* [<strong>文件操作</strong>](#%E6%96%87%E4%BB%B6%E6%93%8D%E4%BD%9C)



[CMake手册翻译](https://www.zybuluo.com/khan-lau/note/254724)

# **常用命令**

## **set_target_properties && get_target_property**

``` shell
// 设置目标的一些属性来改变它们构建的方式
set_target_properties(target1 target2 ...
                        PROPERTIES prop1 value1
                        prop2 value2 ...)
// 从目标中获取属性值                        
get_target_property(VAR target property)
```

``` shell
// example: set output directory
// 默认状态下，库文件将会在于源文件目录树的构建目录树的位置被创建
add_library(my_math
            SHARED
            my_math.cpp 
)
// 设置构建路径
set_target_properties(
        my_math
        PROPERTIES
        LIBRARY_OUTPUT_DIRECTORY
        ${CMAKE_CURRENT_SOURCE_DIR}/build/out_libs
)
// 获取构建路径变量
get_target_property(
		out_put_path
		my_math
		LIBRARY_OUTPUT_DIRECTORY
)
// 打印out_put_path
MESSAGE(STATUS "out_put_path = ${out_put_path}")
```

``` shell
// 设置编译出来的库的路径，也可以使用如下方式
set(LIBRARY_OUTPUT_PATH ${CMAKE_CURRENT_SOURCE_DIR}/build/out_libs)
```

## **文件操作**

``` shell
include_directories (dir)
file(GLOB variable [RELATIVE path] [globbing expressions]...)
```

``` shell
// example 
include_directories (${CMAKE_SOURCE_DIR}/include)
// ${CMAKE_SOURCE_DIR}/src下的cpp文件都会被加入列表src_file_list中
file (GLOB src_file_list "${CMAKE_SOURCE_DIR}/src/*.cpp")

```

