## 教考系统说明

> 系统包含Java和Kotlin两种语言的编译测试功能

#### 在Windows(测试)运行
1. 配置config.properties目录

        config.dir.root=E:/cms/newstrap/
        config.dir.source=E:/cms/newstrap/exam/

2. 配置redis服务器(需使用外网)

        redis.host=120.76.238.32
        redis.port=6379

3. 修改RunController.java的runService注解

        @Resource(name = "runServiceLocalImpl")   // 本地直接编译
        RunService runService;

#### 在Linux (包含Docker)运行
1. 配置config.properties目录

        config.dir.root=/root/newstrap/
        config.dir.source=/root/newstrap/exam/

2. 配置redis服务器(不需要外网, 本地需要搭建redis)

        redis.host=127.0.0.1
        redis.port=6379

3. 修改RunController.java的runService注解

        @Resource(name = "runServiceDockerImpl")   // 本地直接编译
        RunService runService;

### 编译及测试环境需求
#### Java
* 测试运行环境需要安装了jdk, 用于执行TestNG命令

#### Kotlin
* 编译环境
> 因为执行kotlin编译, 需要-kotlin-home环境, 该环境下需要包含kotlinc依赖的lib文件夹, 不同的系统目录可通过修改
> config.properties实现切换. 如window的路径`kotlin.home=G:/JetBrains/ideaIU-2017.1.4.win/plugins/Kotlin/kotlinc`

* 测试运行环境
> Kotlin测试运行时, 有两个注意事项:
> 一是将编译生成的Itheima.jar拼接到-cp参数中, 使其可以自动拆包获取到里边的Itheima.class
> 二是需要将其运行时环境放到newstrap的子目录E:\cms\newstrap\kotlin\libs中
> 将kotlin-script-runtime.jar,kotlin-compiler.jar,kotlin-reflect.jar,kotlin-runtime.jar四个jar放到此libs里.