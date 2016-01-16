package a70kg.info.netfor70kg.net.core;


import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import a70kg.info.netfor70kg.net.httpstacks.HttpStack;
import a70kg.info.netfor70kg.net.httpstacks.HttpStackFactory;
import a70kg.info.netfor70kg.net.request.Request;

/**
 * Created by Mr_Wrong on 16/1/16.
 */
public class RequestQueue {
    //请求队列
    private BlockingQueue<Request<?>> mRequestQueue = new PriorityBlockingQueue<Request<?>>();
    //请求的序列化生成器
    private AtomicInteger mSerialNumGneerator = new AtomicInteger(0);

    //默认核心数
    public static int DEFAULT_CORE_NUM = Runtime.getRuntime().availableProcessors() + 1;

    private int mDispatcherNums = DEFAULT_CORE_NUM;

    private NetworkExecutor[] mDispatchers = null;

    private HttpStack mHttpStack;

    protected RequestQueue(int coreNums, HttpStack httpStack) {
        mDispatcherNums = coreNums;
        mHttpStack = httpStack != null ? httpStack : HttpStackFactory.createHttpStack();
    }

    private final void startNetworkExecutors() {
        mDispatchers = new NetworkExecutor[mDispatcherNums];
        for (int i = 0; i < mDispatcherNums; i++) {
            mDispatchers[i] = new NetworkExecutor(mRequestQueue, mHttpStack);
            mDispatchers[i].start();
        }
    }

    public void start() {
        stop();
        startNetworkExecutors();
    }

    public void stop() {
        if (mDispatchers != null && mDispatchers.length > 0) {
            for (int i = 0; i < mDispatchers.length; i++) {
                mDispatchers[i].quit();
            }
        }
    }

    public void clear() {
        mRequestQueue.clear();
    }

    public BlockingQueue<Request<?>> getAllRequests() {
        return mRequestQueue;
    }

    public void addRequest(Request<?> request) {
        if (!mRequestQueue.contains(request)) {
            request.setSerialNum(this.generateSerialNumber());
            mRequestQueue.add(request);
        } else {
            Log.d("", "### 请求队列中已经含有");
        }
    }

    private int generateSerialNumber() {
        return mSerialNumGneerator.incrementAndGet();
    }
}
