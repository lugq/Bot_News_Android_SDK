package com.peter.newssdkdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ai.botbrain.ttcloud.api.TtCloudListener;
import ai.botbrain.ttcloud.api.TtCloudManager;

/**
 * Description：
 * Creator: Created by peter.
 * Date: 17/4/17.
 */

public class TestApplication extends Application {
    private static final String TAG = TestApplication.class.getSimpleName();

    private static final String userId = "121";
    private static final String userNick = "孙悟空";
    private static final String avatar = "21323";

    @Override
    public void onCreate() {
        super.onCreate();

        TtCloudManager.init(this);

        TtCloudManager.setCallBack(new TtCloudListener() {
            @Override
            public void onBack(ImageView iv_back) {
                Log.i(TAG, "on back click");
                Context context = iv_back.getContext();
                ((Activity) context).finish();
            }

            @Override
            public void onShare(View view, Article article, User user, ResultCallBack callvoidBack) {
                Log.i(TAG, article.toString());
                showDialog((Activity) view.getContext(), "是否分享", TYPE_SHARE, callvoidBack);
                if (TtCloudManager.isLogin()) {
                    Log.i(TAG, user.toString());
                }
            }

            @Override
            public void onLiked(Article article, User user) {
                Log.i(TAG, "onLiked" + article.toString());
            }

            @Override
            public void onComment(View view, Article article, User user) {
                if (!TtCloudManager.isLogin()) {
                    showDialog((Activity) view.getContext(), "是否登录", TYPE_LOGIN, null);
                } else {
                    Log.i(TAG, article.toString());
                    Log.i(TAG, user.toString());
                }
            }
        });
    }

    public void login() {
        TtCloudListener.User user = new TtCloudListener.User();
        user.setUserAvatar(avatar);
        user.setUserId(userId);
        user.setUserNickName(userNick);
        TtCloudManager.login(user);
    }

    private static final int TYPE_LOGIN = 1;
    private static final int TYPE_SHARE = 2;

    private void showDialog(final Activity activity, String message, final int TYPE, final TtCloudListener.ResultCallBack callvoidBack) {
        Dialog dialog = new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TYPE == TYPE_LOGIN) {
                            login();
                            Toast.makeText(activity, "登录成功!", Toast.LENGTH_SHORT).show();

                        } else if (TYPE == TYPE_SHARE) {
                            callvoidBack.success();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
}
