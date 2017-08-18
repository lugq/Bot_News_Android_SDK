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

## Usage

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
compile 'ai.botbrain.ttcloud:libraryTtc:1.0.0'
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

## 注意事项
嵌入 Fragment 的 Activity 需要继承 AppCompatActivity 

