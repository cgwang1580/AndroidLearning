[TOC]

[shared_ptr](C++11 智能指针)

​	[shared_ptr初始化](shared_ptr初始化)

​	[shared_ptr 管理动态数组](shared_ptr 管理动态数组)

## C++11 智能指针

### shared_ptr

#### shared_ptr初始化

```c++
std::shared_ptr <int> sptr1(new int(1));
std::shared_ptr <int> sptr2 = std::make_shared <int>(1);
// 使用lamda表达式指定delete函数
std::shared_ptr <int> sptr3(new int(1), [](int *p) {delete p;});  
```

#### shared_ptr 管理动态数组

```C++
// shared_ptr没有实现make_shared初始化动态数组的功能
int len = 10;
auto sptr4(new int[10], [](int *p) {delete[]p;});
```

可以使用shared_ptr为动态数组创建一个工厂函数，返回一个该类型的指针

```c++
template <typename T>
shared_ptr <T> make_shared_array(int length) {
	return shared_ptr<T>(new T[length], default_delete<T[]>());
}
```



### unique_ptr

在使用shared_ptr和unique_ptr管理动态数组时，需要注意shared_ptr没有提供[]操作符，所以需要使用 `sp.get()`先获取原始指针，再对原始指针进行下标操作。而unique_ptr对动态数组提供了支持，因此可以使用[]进行直接操作。

### weak_ptr



### 参考

[C++11中使用shared_ptr和unique_ptr管理动态数组](https://blog.csdn.net/wks19891215/article/details/50992171)



