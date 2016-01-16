package a70kg.info.netfor70kg.net.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import a70kg.info.netfor70kg.net.response.Response;

/**
 * Created by Mr_Wrong on 16/1/13.
 */
public abstract class Request<T> implements Comparable<Request<T>> {
    public static enum HttpMethod {//请求的方法
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE");

        HttpMethod(String httpMethod) {
            mHttpMethod = httpMethod;
        }

        String mHttpMethod = "";

        @Override
        public String toString() {
            return mHttpMethod;
        }
    }

    public static enum Priority {//请求的优先级
        LOW, NORMAL, HIGH, IMMEDIATE;
    }

    public final static String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String DEFAULE_PARAMS_ENCODING = "UTF-8";

    protected int mSerialNum = 0;//请求号

    protected Priority mPriority = Priority.NORMAL;//默认的优先级

    protected boolean isCancel = false;//是否取消

    protected boolean mShouldCache = true;//是否缓存

    protected RequestListener mRequestListener;
    private String url = "";
    private HttpMethod mMethod = HttpMethod.GET;//默认的请求方法
    private HashMap<String, String> mHeaders = new HashMap<>();//请求头
    private HashMap<String, String> mBodyParams = new HashMap<>();//请求参数

    public Request(HttpMethod method, String url, RequestListener listener) {
        this.mMethod = method;
        this.url = url;
        this.mRequestListener = listener;
    }

    public abstract T parseResponse(Response response);

    public final void deliveryResponse(Response response) {//处理Response,在UI线程
        T result = parseResponse(response);
        if (mRequestListener != null) {
            int code = response != null ? response.getStatusCode() : -1;
            String msg = response != null ? response.getMessage() : "未知错误";
            mRequestListener.onComplete(code, result, msg);//回调回去
        }
    }

    /**
     * 根据加入队列的顺序和优先级对请求进行排序
     *
     * @param another
     * @return
     */
    @Override
    public int compareTo(Request<T> another) {

        Priority priority = this.getPriority();
        Priority anotherPrioriy = another.getPriority();
        return priority.equals(anotherPrioriy) ?
                getSerialNum() - another.getSerialNum() :
                priority.ordinal() - anotherPrioriy.ordinal();
    }


    public byte[] getBody() {
        Map<String, String> params = getBodyParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamEncoding());
        }
        return null;
    }

    /**
     * 将参数转换成URL
     *
     * @param params
     * @param paramsEncoding
     * @return
     */
    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                encodedParams.append('&');
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }


    public String getParamEncoding() {
        return DEFAULE_PARAMS_ENCODING;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamEncoding();
    }

    public boolean isHttps() {
        return url.startsWith("https");
    }

    public int getSerialNum() {
        return mSerialNum;
    }

    public void setSerialNum(int serialNum) {
        mSerialNum = serialNum;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public boolean isShouldCache() {
        return mShouldCache;
    }

    public void setShouldCache(boolean shouldCache) {
        mShouldCache = shouldCache;
    }

    public RequestListener getRequestListener() {
        return mRequestListener;
    }

    public void setRequestListener(RequestListener requestListener) {
        mRequestListener = requestListener;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getMethod() {
        return mMethod;
    }

    public void setMethod(HttpMethod method) {
        mMethod = method;
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public void setHeaders(HashMap<String, String> headers) {
        mHeaders = headers;
    }

    public HashMap<String, String> getBodyParams() {
        return mBodyParams;
    }

    public void setBodyParams(HashMap<String, String> bodyParams) {
        mBodyParams = bodyParams;
    }

    public static interface RequestListener<T> {
        public void onComplete(int code, T response, String errMsg);
    }
}
