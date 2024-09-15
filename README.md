




com.github.mmm3w.armory:base:v1.0.1
com.github.mmm3w.armory:adapter:v1.0.1
com.github.mmm3w.armory:imagegesture:v1.0.1



# Adapter


# Base

### Nsd

### File
方法|说明
-|-
File.ensureDir()|保证文件目录可用性
File.clear()|删除文件/目录
File.ignoreSuffixName()|去除后缀名后的文件名
File.allFiles()|列出所有文件列表

### Coroutines Mutex 
方法|说明
-|-
Mutex.tryUnlock()|不会报错的解锁
Mutex.justLock()|阻塞挂起一下

### Size
方法|说明
-|-
dp2px|单位转换
px2dp|单位转换

### View
方法|说明
-|-
View.oval()|将View裁剪到圆形
View.corners()|为View增加圆角

### ViewBinding
方法|说明
-|-
Activity.viewBinding(inflate: (LayoutInflater) -> VB)|可以在Activity中直接使用VB委托

### SpannableBuilder
快捷多样式文本
```kotlin
val span = SpannableBuilder().append("text") {
            //文字颜色
            textColor(0xffff0000.toInt())
            //背景涩
            backgroundColor(0xffffffff.toInt())
            //文字大小，绝对大小
            absoluteSize(10)
            //文字大小，相对大小
            relativeSize(1.5f)
            //点击事件
            click { /* click action */ }
            //下划线
            underline()
            //删除线
            strikethrough()
            //斜体
            italic()
            //粗体
            bold()
            //自定义样式
//            custom("tag")
//            forEach {  }
        }.build()
textView.text = span
```

### View

#### HeartDrawable
仿推特点赞样式




# HttpRookie

