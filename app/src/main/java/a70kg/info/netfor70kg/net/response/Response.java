package a70kg.info.netfor70kg.net.response;

import org.apache.http.HttpEntity;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by Mr_Wrong on 16/1/13.
 */


public class Response extends BasicHttpResponse {
    private int StatusCode;
    public byte[] rawData = new byte[0];
    private String Message;

    public Response(StatusLine statusline) {
        super(statusline);
    }

    public Response(StatusLine statusline, ReasonPhraseCatalog catalog, Locale locale) {
        super(statusline, catalog, locale);
    }

    @Override
    public void setEntity(HttpEntity entity) {
        super.setEntity(entity);
        rawData = entityToBytes(entity);
    }


    private byte[] entityToBytes(HttpEntity entity) {
        try {
            return EntityUtils.toByteArray(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];

    }

    public byte[] getRawData() {
        return rawData;
    }


    public String getMessage() {
        return getStatusLine().getReasonPhrase();
    }


    public int getStatusCode() {
        return getStatusLine().getStatusCode();
    }

}
