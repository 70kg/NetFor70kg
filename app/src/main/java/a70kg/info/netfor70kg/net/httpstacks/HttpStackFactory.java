package a70kg.info.netfor70kg.net.httpstacks;

import android.os.Build;



/**
 * Created by Mr_Wrong on 16/1/16.
 */
public class HttpStackFactory {
    private static final int GINGERBREAD_SDK_NUM = 9;
    public static HttpStack createHttpStack() {
        int runtimeSDKApi = Build.VERSION.SDK_INT;
        if (runtimeSDKApi >= GINGERBREAD_SDK_NUM) {
            return new HttpUrlConnStack();
        }
        return new HttpClientStack();
    }
}
