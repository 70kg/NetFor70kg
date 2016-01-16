package a70kg.info.netfor70kg.net.core;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

import a70kg.info.netfor70kg.net.request.Request;
import a70kg.info.netfor70kg.net.response.Response;

/**
 * Created by Mr_Wrong on 16/1/16.
 */
public class ResponseDelivery implements Executor {

    Handler mResponseHandler = new Handler(Looper.getMainLooper());

    public void deliveryResponse(final Request<?> request, final Response response) {
        Runnable respRunnable = new Runnable() {
            @Override
            public void run() {
                request.deliveryResponse(response);
            }
        };

        execute(respRunnable);
    }

    @Override
    public void execute(Runnable command) {
        mResponseHandler.post(command);
    }
}
