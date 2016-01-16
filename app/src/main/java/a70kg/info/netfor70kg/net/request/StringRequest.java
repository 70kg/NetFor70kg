package a70kg.info.netfor70kg.net.request;

import a70kg.info.netfor70kg.net.response.Response;

/**
 * Created by Mr_Wrong on 16/1/16.
 */
public class StringRequest extends Request<String> {

    public StringRequest(HttpMethod method, String url, RequestListener listener) {
        super(method, url, listener);
    }

    @Override
    public String parseResponse(Response response) {
        return new String(response.getRawData());
    }
}
