# BeyondPhysics

框架demo下载地址: http://server.coolwallpaper.cn:4126/apks/beyondPhysics.apk

博客地址:https://blog.csdn.net/xihuan22d/article/details/80220900


基于本框架设计的软件

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/o1.jpg)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/o2.jpg)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/o3.jpg)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/o4.jpg)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/o5.jpg)


下载地址: http://coolwallpaper.cn



相比其他开源框架的优点:

1.统一普通请求、图片请求三级缓存(支持gif)、下载、断点下载、上传请求，使得开发的软件不再是各种不同来源的开源框架糅合而成(网络框架，图片框架等等)，且这5类请求是以继承和泛型方式实现的，从而使得可以被进一步继承达到自定义

2.使用其他开源框架不曾设想过的特殊请求分发处理架构，请求核心处理层使用LinkedHasMap替代BlockingQueue完成队列，使得请求可以被更良好的移除和管理(这样可以非常方便的立即移除无效请求和实现相同图片请求key的请求回调等），当然为了解决LinkedHasMap的缺陷，可以选择启用分发层，对流畅度要求高的部分请求通过分发层分发请求(由分发线程处理核心处理层)，以便应对流畅度要求极高的场景(如图片滑动过程，下载列表获取下一页的下载进度），分发层使用BlockingQueue使得可以最大程度的和主线程脱关联，使用该分发架构不但可保证主线程的绝对流畅性，还保留了核心处理层使用LinkedHasMap所带来的各种优势，以及使得核心处理层的容器变得高度的面向对象

3.对内存严格的要求，通过大量的内存调试，保证框架极低的内存占用，图片下载完毕会通过缩放到指定大小获取，即使下载的是大图也不会导致OutOfMemory的出现，处理了所有可能出现内存泄漏的场景，主线程无论以何种方式取消请求，该请求即可立即释放对activity的引用不存在内存泄漏

4.可高度自定义的接口实现的HttpAgreement，BitmapMemoryCache，BitmapDiskCacheAnalyze，对于HttpAgreement框架提供了俩种默认实现，系统的HttpURLConnection实现和OKhttp的实现(对于https请求默认验证系统证书,若想保证https的高度安全,可使用框架内提供的单证书和多证书验证方法)，也可自定义选择适合自己的http方式

5.良好封装的recyclerView，使得对于recyclerView的开发变得非常的方便和高效，即便开发如多级评论这类复杂的复用模型也可以非常简便

6.提供阻塞方式取消请求和关闭线程池(该功能一般用于极限测试和极少数特殊场景使用)

7.高度面向对象，严格的编码命名风格和异常日志记录

8.作者花了大量的时间和心血验证调试这个框架，并提供了完整的demo，利用demo便可以非常方便的理解使用该框架，demo部分本身的结构便是一种非常可靠高效的开发模型，可用demo的结构直接应用于实际开发......
<br>
<br>
<br>
<br>
androidStudio Gradle导入:

先添加jitpack仓库:

allprojects {

    repositories {  
	
        maven { url 'https://www.jitpack.io' }
		
    }
	
}

接着导入项目依赖库即可:

dependencies {

    implementation 'com.github.xihuan22d:BeyondPhysics:1.1.1'
	
}

框架静态图:

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/beyondPhysics.png)

demo程序预览图:

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/1.png)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/2.png)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/3.png)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/4.png)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/5.png)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/6.png)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/7.png)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/8.png)

![image](https://github.com/xihuan22d/BeyondPhysics/blob/master/screenshot/9.png)


