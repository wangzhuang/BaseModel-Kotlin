package com.model.basemodel.util.webview;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceResponse;

import com.tencent.sonic.sdk.BuildConfig;
import com.tencent.sonic.sdk.SonicRuntime;
import com.tencent.sonic.sdk.SonicSessionClient;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SonicRuntimeImpl
 * @Description: 描述
 * @Author: WangZhuang
 * @CreateDate: 2019/7/18 0018 上午 10:13
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/18 0018 上午 10:13
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class SonicRuntimeImpl extends SonicRuntime {
    public SonicRuntimeImpl(Context context) {
        super(context);
    }

    /**
     * 获取用户UA信息，这里的返回值会放在header的UserAgent中
     * @return
     */
    @Override
    public String getUserAgent() {
        return "";
    }

    /**
     * 获取用户ID信息，避免多个用户切换可能使用到相同的缓存
     * @return
     */
    @Override
    public String getCurrentUserAccount() {
        return "sonic-demo-master";
    }

    @Override
    public String getCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        return cookieManager.getCookie(url);
    }

    @Override
    public void log(String tag, int level, String message) {
        switch (level) {
            case Log.ERROR:
                Log.e(tag, message);
                break;
            case Log.INFO:
                Log.i(tag, message);
                break;
            default:
                Log.d(tag, message);
        }
    }

    @Override
    public Object createWebResourceResponse(String mimeType, String encoding, InputStream data, Map<String, String> headers) {
        WebResourceResponse resourceResponse =  new WebResourceResponse(mimeType, encoding, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resourceResponse.setResponseHeaders(headers);
        }
        return resourceResponse;
    }

    @Override
    public void showToast(CharSequence text, int duration) {

    }

    @Override
    public void notifyError(SonicSessionClient client, String url, int errorCode) {

    }
    // 这里可以设置某个url是否为SonicUrl，如果指定为不是，则不会通过Sonic的方式加载url。
    @Override
    public boolean isSonicUrl(String url) {
        return true;
    }

    @Override
    public boolean setCookie(String url, List<String> cookies) {
        if (!TextUtils.isEmpty(url) && cookies != null && cookies.size() > 0) {
            CookieManager cookieManager = CookieManager.getInstance();
            for (String cookie : cookies) {
                cookieManager.setCookie(url, cookie);
            }
            return true;
        }
        return false;
    }
    // 判断网络连接情况
    @Override
    public boolean isNetworkValid() {
        return true;
    }

    @Override
    public void postTaskToThread(Runnable task, long delayMillis) {
        Thread thread = new Thread(task, "SonicThread");
        thread.start();
    }

    @Override
    public File getSonicCacheDir() {
        if (BuildConfig.DEBUG) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "sonic/";
            File file = new File(path.trim());
            if(!file.exists()){
                file.mkdir();
            }
            return file;
        }
        return super.getSonicCacheDir();
    }

    @Override
    public String getHostDirectAddress(String url) {
        return null;
    }
}