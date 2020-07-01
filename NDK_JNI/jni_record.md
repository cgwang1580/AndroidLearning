[TOC]

## NDK_JNI开发

本片文章主要记录自己在做Native开发过程中接触到的知识。

### JNI Java层与C层数据传输

#### C层获取Java层数据

##### 成员变量获取

将类数据从C层传到Java层主要通过以下几个步骤：

1. 使用`GetObjectClass`函数获取Java类的`jclass`指针
2. 利用`GetFieldID`函数和`jclass`指针来获取每个成员变量的filed id，需要注意不同类型的成员变量需要使用不同的方法签名
3. 采用`GetFloatField`或`GetIntField`等函数由filed id和`jobject`指针获取成员变量的值

`GetFieldID`方法定义如下：

``` c
jfieldID GetFieldID(jclass clazz, const char* name, const char* sig)
```

其中`name`参数为Java类成员变量的名字，`sig`参数为方法签名，需要根据类成员变量的类型更换，Java层参数类型对应的方法签名对照关系如下：

| Java Language Type | Field Descriptor |
| :----------------- | :--------------- |
| `boolean`          | `Z`              |
| `byte`             | `B`              |
| `char`             | `C`              |
| `short`            | `S`              |
| `int`              | `I`              |
| `long`             | `J`              |
| `float`            | `F`              |
| `double`           | `D`              |

获取成员变量时，`String`变量比较特殊。String作为Java的一种类，在获取filed id时的方法签名为`"Ljava/lang/String;"`，String类在java/lang这个包中，因为String是一个类，所以后续取数据流程需要做一些改动，涉及到几个函数：

`GetObjectClass`

`GetFieldID`

`GetObjectField`

`GetStringUTFChars`		

`GetStringLength`			// 获取java层数据块长度，当C层需要分配内存时需要用到

``` c
// 假设如上MotionStateGL类中有变量String name
jclass jMotionClass = nullptr;
jstring jstr = nullptr;
jfieldID nameID = nullptr;
jclass jMotionClass = env->GetObjectClass(jMotionObject);
jfieldID nameID = env->GetFieldID(jMotionClass, "name", "Ljava/lang/String;");
if (nullptr == nameID) return ERROR_INPUT;
// 先获取jstring类型值，然后根据jstring转换为char*值
jstr = env->GetObjectField (jMotionObject, nameID);
const char* str = env->GetStringUTFChars (jstr, NULL);
if (nullptr == str) return ERROR_INPUT;
env->DeleteLocalRef (jMotionClass);
```

##### 静态变量获取

对于静态变量的获取，基本流程和成员变量获取是一样的，只是函数需要略作变更：

```c
// 成员变量				静态变量
GetFieldID			GetStaticFieldID
GetIntField			GetStaticIntField
GetObjectField    	GetStaticObjectField
```

##### Sample

从C层获取Java层数据代码示例，包括成员变量，静态变量获取：

``` java
// java层MotionStateGL类定义
MotionStateGL {
    public int mMotionType;
    public float rotate_x, rotate_y, rotate_z;
}
```

``` java
// java层MyGLRenderer类定义，native函数SetMotionState声明
public class MyGLRenderer {
    private final static String TAG = "MyGLRenderer";
    ///**** test for jni begin ***///
    private final static int TEST_STATIC_NUM = 10;
    private final int TEST_NUM = 20;
    private final String TEST_STRING = "test_string";
    ///**** test for jni end *****///
    
    private MotionStateGL motionStateGL;    
    public void start () {
        SetMotionState (motionStateGL);
    }
   	public native int SetMotionState (MotionStateGL motionStateGL); 
}
```

``` c++
// c层相关结构体定义
typedef struct _tag_motion_state_
{
	MotionType eMotionType;
	float transform_x;
    float transform_y;
	float transform_z;
}
// c层native函数定义, thiz为当前类的object指针，motion_state_gl为MotionStateGL类型的变量motionStateGL对应的object指针，因为我们需要获取motionStateGL对象的数据，所以GetObjectClass使用的jobject参数为motion_state_gl而不是thiz
Java_com_cgwang1580_senioropengles_MyGLRenderer_SetMotionState(JNIEnv *env, jobject thiz, jobject motion_state_gl)
{
	// TODO: implement SetMotionState()
	LOGD("Java_com_cgwang1580_senioropengles_MyGLRenderer_SetMotionState");
	MotionState motionState;
	int ret = ConvertMotionState (env, motion_state_gl, motionState);
	LOGD("ConvertMotionState ret = %d", ret);
    
    ret = ConvertJavaClassVariableTest (env, thiz);
	LOGD("ConvertJavaClassVariableTest ret = %d", ret);
    
	return ret;
}
// ConvertMotionState 函数定义，主要的转换过程
int ConvertMotionState (JNIEnv *env, jobject jMotionObject, MotionState &motionState)
{
	LOGD ("ConvertMotionState");
	if (nullptr == env || nullptr == jMotionObject) return ERROR_INPUT;

    // 首先由jobect类对象获取java类的jclass指针
	jclass jMotionClass = env->GetObjectClass(jMotionObject);
	if (nullptr == jMotionClass)
	{
		LOGE("ConvertMotionState jMotionClass is nullptr");
		return ERROR_INPUT;
	}
	// 接着根据jMotionClass获取java类中的成员变量的 filed id
	jfieldID motionTypeID = env->GetFieldID(jMotionClass, "mMotionType", "I");
	jfieldID transformXID = env->GetFieldID(jMotionClass, "translate_x", "F");
	jfieldID transformYID = env->GetFieldID(jMotionClass, "translate_y", "F");
	jfieldID transformZID = env->GetFieldID(jMotionClass, "translate_z", "F");
    // 之后根据jMotionObject和filed id获取成员变量的值
	motionState.eMotionType = (MotionType)env->GetIntField(jMotionObject, motionTypeID);
	motionState.transform_x = env->GetFloatField(jMotionObject, transformXID);
	motionState.transform_y = env->GetFloatField(jMotionObject, transformYID);
	motionState.transform_z = env->GetFloatField(jMotionObject, transformZID);

	LOGD("ConvertMotionState motionState %d, (%f %f %f)", motionState.eMotionType,
			motionState.transform_x, motionState.transform_y, motionState.transform_z);
	env->DeleteLocalRef(jMotionClass);

	return ERROR_OK;
}

// 定义个函数ConvertJavaClassVariableTest用于获取MyGLRenderer类中的TAG，TEST_STATIC_NUM，TEST_NUM，TEST_STRING变量
int ConvertJavaClassVariableTest (JNIEnv *env, jobject thiz)
{
	LOGD ("ConvertJavaClassVariableTest");
	if (nullptr == env || nullptr == thiz)
	{
		LOGE("ConvertJavaClassVariableTest nullptr");
		return ERROR_INPUT;
	}
	jclass myGLRenderClazz = env->GetObjectClass(thiz);
	if (nullptr == myGLRenderClazz)
		return ERROR_INPUT;
    
	// get variable like int/double in thiz class
	jfieldID testNunID = env->GetFieldID(myGLRenderClazz, "TEST_NUM", "I");
	int testNum = env->GetIntField(thiz, testNunID);
	LOGD("ConvertJavaClassVariableTest testNum = %d", testNum);

	// get variable like String
	jfieldID testStringID = env->GetFieldID(myGLRenderClazz, "TEST_STRING", "Ljava/lang/String;");
	jstring jStringTestString = (jstring)env->GetObjectField(thiz, testStringID);
	const char *testString = env->GetStringUTFChars(jStringTestString, 0);
	LOGD("ConvertJavaClassVariableTest testString = %s", testString);

	// get static variable like int/double
	jfieldID testStaticNumID = env->GetStaticFieldID(myGLRenderClazz, "TEST_STATIC_NUM", "I");
	int testStaticNum = env->GetStaticIntField(myGLRenderClazz, testStaticNumID);
	LOGD("ConvertJavaClassVariableTest testStaticNum = %d", testStaticNum);

	// get static variable String
	jfieldID TAGFiledID = env->GetStaticFieldID(myGLRenderClazz, "TAG", "Ljava/lang/String;");
	jstring jstringTAG = (jstring)env->GetStaticObjectField(myGLRenderClazz, TAGFiledID);
	const char *sTag = env->GetStringUTFChars(jstringTAG, 0);
	LOGD("ConvertJavaClassVariableTest sTag = %s", sTag);

	// release
	env->DeleteLocalRef(myGLRenderClazz);
	return ERROR_OK;
}
```

#### C层获取Java层方法

C层获取Java层方法意味着在C层调用Java层的方法。

##### 成员方法获取



##### 静态方法获取

#### C层传数据到Java层