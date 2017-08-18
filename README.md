# Bot_News_Android_SDK

## 嵌入 SDK
Android Studio

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

module 下的 build.gradle 中添加依赖
```
compile 'ai.botbrain.ttcloud:libraryTtc:1.0.0'
```

## 修改 AndroidManifest.xml
```
<application
    .../>
    <meta-data android:name="TTC_APPID" android:value="申请的AppId" />
<application>
```

## 初始化SDK
```
public class XXApplication extends Application {

    public void onCreate() {
        super.onCreate();
        TtCloudManager.init(this);
    }
}
```

## 接口回调
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

## 注意事项
嵌入 Fragment 的 Activity 需要继承 AppCompatActivity 

