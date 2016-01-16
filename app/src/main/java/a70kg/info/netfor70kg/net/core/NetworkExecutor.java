package a70kg.info.netfor70kg.net.core;


import android.util.Log;

import java.util.concurrent.BlockingQueue;

import a70kg.info.netfor70kg.net.cache.Cache;
import a70kg.info.netfor70kg.net.cache.LruMemCache;
import a70kg.info.netfor70kg.net.httpstacks.HttpStack;
import a70kg.info.netfor70kg.net.request.Request;
import a70kg.info.netfor70kg.net.response.Response;

/**
 * Created by Mr_Wrong on 16/1/16.
 */
public class NetworkExecutor extends Thread {

    private BlockingQueue<Request<?>> mRequestQueue;
    private HttpStack mHttpStack;
    private static ResponseDelivery mResponseDelivery = new ResponseDelivery();

    private static Cache<String, Response> mReqCache = new LruMemCache();
    private boolean isStop = false;

    public NetworkExecutor(BlockingQueue<Request<?>> queue, HttpStack httpStack) {
        mRequestQueue = queue;
        mHttpStack = httpStack;
    }

    @Override
    public void run() {

        try {
            while (!isStop) {
                final Request<?> request = mRequestQueue.take();
                if (request.isCancel()) {
                    Log.d("### ", "### 取消执行了");
                    continue;
                }
                Response response = null;

                if (isUseCache(request)) {
                    // 从缓存中取
                    response = mReqCache.get(request.getUrl());
                } else {
                    response = mHttpStack.performRequest(request);
                    if (request.isShouldCache() && isSuccess(response)) {
                        mReqCache.put(request.getUrl(), response);
                    }
                }
                mResponseDelivery.deliveryResponse(request, response);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();

        }


        super.run();
    }

    public void quit() {
        isStop = true;
        interrupt();
    }

    private boolean isSuccess(Response response) {
        return response != null && response.getStatusCode() == 200;
    }

    private boolean isUseCache(Request<?> request) {
        return request.isShouldCache() && mReqCache.get(request.getUrl()) != null;
    }
}
