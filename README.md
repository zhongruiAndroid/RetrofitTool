### RetrofitTool
这个工具类主要封装了Retrofit的基本用法

```
首先在Application里面初始化
(必传的三个参数:1:Context,2:BaseUrl-请求地址固定的前半部分,3:是否是debug模式true:debug,false:release)

基本用法
BaseUrl必须以/结尾
设置debug模主要打印请求的参数以及请求头和返回结果
NetWorkManager.getInstance(this,"http://www.***.***/",BuildConfig.DEBUG).complete();

详细用法
NetWorkManager.getInstance(this,"http://www.***.***/",BuildConfig.DEBUG)
              .setCacheSize(20*1024*1024) 设置缓存数据大小,单位K,默认20MB  20*1024*1024
              .setCachTime(60)            设置缓存时间,单位秒,默认60秒
              .setHttpConnectTimeout(15)  设置请求超时时间,单位秒,默认15秒
              .setHttpReadTimeout(20)     设置读取时间,单位秒,默认20秒
              .setHttpWriteTimeout(20)    设置写入时间,单位秒,默认20秒
              .setNoNetWorkMsg("无网络连接,请稍后再试")设置无网络连接时请求提示-结合rxjava2才生效
              .complete();



```
    NetWorkManager.getCommonClient();返回对象或者集合无缓存
    NetWorkManager.getCommonWithCacheClient();返回对象或者集合有缓存
    NetWorkManager.getStringClient();返回String无缓存
    NetWorkManager.getStringWithCacheClient();返回String有缓存
    ---------------↑结合Rxjava2使用的客户端----------------------------------------
    
    ---------------↓不结合Rxjava2使用的客户端--------------------------------------
    NetWorkManager.getGeneralClient();返回对象或者集合无缓存
    NetWorkManager.getGeneralWithCachClient();返回对象或者集合有缓存
    NetWorkManager.getGeneralStringClient();返回String无缓存
    NetWorkManager.getGeneralStringWithCachClient();返回String有缓存
    
    至于这里为什么提供这么多客户端,动态设置不行吗?之前我也想过,具体不多做解释,实践之后就会了解的
```
如果某个接口的baseUrl和application设置的不一样,可在上面提供的方法中传入当前接口的baseUrl
比如 NetWorkManager.getCommonClient(yourBaseUrl);
```

[ ![Download](https://api.bintray.com/packages/zhongrui/mylibrary/RetrofitTool/images/download.svg) ](https://bintray.com/zhongrui/mylibrary/RetrofitTool/_latestVersion)
```
dependencies{
   compile 'com.github:retrofitutil:最新版本号看上面的蓝色小图片'
}
```
---
以下传参以map为例(还可以传对象,具体用法可以参考Retrofit官网)</br>
####不结合Rxjava2使用方法(没提供请求之前检查网络连接的功能,和Rxjava2结合的有提供)
</br></br>IRequest.java(注意,这是一个接口,所以没有方法体)
```
@FormUrlEncoded
@POST("mobile/login")
Call<LoginBean> generalLogin(@FieldMap Map<String,String> map);

@GET("mobile/login")
Call<LoginBean> generalLogin(@QueryMap Map<String,String> map);
```
ApiRequest.java
```
public static void login(Map<String,String> map,final Callback<LoginBean> callBack){
        Call<LoginBean> call =  NetWorkManager.getGeneralClient()
                     			.create(IRequest.class)
	                 			.generalLogin(map);
        call.enqueue(callBack);//异步请求
}
call.enqueue(callBack);		 //异步请求
call.execute().body();		 //同步请求-返回对象
call.execute().body().toString();//同步请求-返回String
```
Activity
```
map.put("参数名","参数");
ApiRequest.login(map, new RetrofitCallBack<T>() {
     @Override
     protected void onSuccess(T response) {
         
     }
     @Override
     protected void onError(Throwable t) {
         
     }
});
```
---
####结合Rxjava2使用方法(提供了请求之前检查网络连接的功能)
</br></br>IRequest.java(注意,这是一个接口,所以没有方法体)
```
@FormUrlEncoded
@POST("mobile/login")
Observable<T> generalLogin(@FieldMap Map<String,String> map);
```
ApiRequest.java
```
public static Observable<T> login(Map<String,String> map){
     return NetWorkManager.getCommonClient()
	        .create(IRequest.class)
            .generalLogin(map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
}
```
Activity
```
map.put("参数名","参数");
ApiRequest.login(map).subscribe(new Subscriber<T>() {
	    @Override
	    public void onCompleted() {
	    }
	    @Override
	    public void onError(Throwable e) {
	    }
	    @Override
	    public void onNext(T response) {
	    }
})
```
