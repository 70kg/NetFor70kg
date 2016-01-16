package a70kg.info.netfor70kg.net.cache;

import android.support.v4.util.LruCache;

import a70kg.info.netfor70kg.net.response.Response;

/**
 * Created by Mr_Wrong on 16/1/16.
 */
public class LruMemCache implements Cache<String, Response> {

    private LruCache<String, Response> mResponseCache;

    public LruMemCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mResponseCache = new LruCache<String, Response>(cacheSize) {
            @Override
            protected int sizeOf(String key, Response response) {
                return response.rawData.length / 1024;
            }
        };
    }

    @Override
    public Response get(String key) {
        return mResponseCache.get(key);
    }

    @Override
    public void put(String key, Response value) {
        mResponseCache.put(key, value);
    }

    @Override
    public void remove(String key) {
        mResponseCache.remove(key);
    }
}
