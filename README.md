# Bot_News_Android_SDK

<p align="center">
    <a href="http://developer.android.com/index.html">
        <img src="https://img.shields.io/badge/platform-android-green.svg">
    </a>
    <a href="">
        <img src="https://img.shields.io/badge/Maven%20Central-5.8.1-green.svg">
    </a>
</p>

## Effect

**[ttcloud-1.0.0.apk](https://github.com/lugq/Bot_News_Android_SDK/releases/download/v1.2.1/ttcloud-1.0.0.apk)**

<img src="https://raw.githubusercontent.com/lugq/Bot_News_Android_SDK/master/SampleNews/effectPicture/device-2017-08-18-182954.png" width = "320" height = "600" alt="图片名称" align=center /> <img src="https://raw.githubusercontent.com/lugq/Bot_News_Android_SDK/master/SampleNews/effectPicture/device-2017-08-18-183029.png" width = "320" height = "600" alt="图片名称" align=center />


## Usage
### 集成
1.Import library
在 project 下的 build.gradle 中添加
```
allprojects {
    repositories {
        jcenter()
        // 添加私服地址
        maven {
            url 'https://dl.bintray.com/luguoqiang/maven'
        }
    }
}
```

```
compile 'ai.botbrain.ttcloud:libraryTtc:1.2.4'
```

2. In AndroidManifest.xml
```
<application
    .../>
    <meta-data android:name="TTC_APPID" android:value="申请的AppId" />
<application>
```

3. In Application
```
public class XXApplication extends Application {

    public void onCreate() {
        super.onCreate();
        TtCloudManager.init(this);
    }
}
```

4. In Activity
```
...
    private IndexFragment mNewsIndexFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsIndexFragment = new IndexFragment();
        initView();
        initSchema();

        if (!mNewsIndexFragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, mNewsIndexFragment);
            fragmentTransaction.commit();
        }

    }
...
```

5. 回调监听
```
TtCloudManager.setCallBack(new TtCloudListener() {
            @Override
            public void onBack(ImageView iv_back) {

            }

            @Override
            public boolean onShare(Article article, User user, ResultCallBack callBack) {
                return false;
            }

            @Override
            public boolean onLiked(Article article, User user) {
                return false;
            }

            @Override
            public boolean onComment(Article article, User user) {
                return false;
            }
        });
```


### IndexFragment 对外提供的接口
1.隐藏滑动标签控件
```
void hideTabLayout();
```

2.控制 TabLayout 最多显示的标签个数
```
void setTabLayoutMaxItemCount(int maxCount);
```
3.向轮播图底部添加自定义视图
```
void addCustomView(View view);
```

## ProGuard
If you are using ProGuard you might need to add the following options:
```
# sdk
-keep class ai.botbrain.ttcloud.sdk.model.** { *; }

-keep class ai.botbrain.ttcloud.api.**{*;}
-keep class ai.botbrain.ttcloud.sdk.callback.**{*;}
-keep class ai.botbrain.ttcloud.sdk.fragment.IndexFragment {*; }

# qq.e ad
-keep class com.qq.e.** {
    public protected *;
}
-keep class android.support.v4.app.NotificationCompat**{
    public *;
}

# 360 ad
-keep class com.ak.android.** {*;}
-keep class android.support.v4.app.NotificationCompat**{
      public *;
}

# Glide 4.0
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# eventBus 3.0
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
```


## 注意事项
嵌入 Fragment 的 Activity 需要继承 AppCompatActivity 

