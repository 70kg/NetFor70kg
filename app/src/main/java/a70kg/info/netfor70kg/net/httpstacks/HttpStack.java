package a70kg.info.netfor70kg.net.httpstacks;

import a70kg.info.netfor70kg.net.request.Request;
import a70kg.info.netfor70kg.net.response.Response;

/**
 * Created by Mr_Wrong on 16/1/16.
 */
public interface HttpStack {
    public Response performRequest(Request<?> request);
}
