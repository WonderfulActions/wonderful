package com.letv.android.wonderful.util;

import android.util.Log;

import com.letv.android.wonderful.Tags;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * GET POST OPTIONS HEAD PUT DELETE TRACE
 * @author michael
 */
public class HttpUrlConnectionUtil {
    public interface RequestMethod {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    public static String doGet(String urlStr) {
        final String result = doRequest(urlStr, RequestMethod.GET, null, null);
        return result;
    }

    public static String doPost(String urlStr, HashMap<String, String> params) {
        final String result = doRequest(urlStr, RequestMethod.POST, null, params);
        return result;
    }

    public static String doPut(String urlStr, Map<String, String> params) {
        final String result = doRequest(urlStr, RequestMethod.PUT, null, params);
        return result;
    }

    public static String doDelete(String urlStr, Map<String, String> params) {
        final String result = doRequest(urlStr, RequestMethod.DELETE, null, params);
        return result;
    }

    public static String doRequest(String urlStr, String method, Map<String, String> headers, Map<String, String> params) {
        try {
            final URL url = new URL(urlStr);
            final HttpURLConnection connection = openConnection(url);
            // add headers
            if (headers != null && headers.size() > 0) {
                for (String headerName : headers.keySet()) {
                    final String headerValue = headers.get(headerName);
                    connection.addRequestProperty(headerName, headerValue);
                }
            }
            // set request method
            // connection.setRequestMethod(RequestMethod.PUT);
            connection.setRequestMethod(method);
            // add body
            if (params != null && params.size() > 0) {
                addParams(connection, params);
            }
            // get response code
            final int responseCode = connection.getResponseCode();
            Log.i(Tags.WONDERFUL_REQUEST, "responseCode = " + responseCode);
            if (responseCode == 200) {
                // get response result
                final InputStream  inputStream = connection.getInputStream();
                final String responseResult = getResponseResult(inputStream);
                // Log.i(Tags.HTTP_REQUEST, "responseResult = " + responseResult);
                inputStream.close();
                connection.disconnect();
                return responseResult;
            }
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getResponseResult(InputStream inputStream) throws IOException {
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = bufferedInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        final byte[] byteArray = outputStream.toByteArray();
        outputStream.close();
        final String result = new String(byteArray, DEFAULT_ENCODING);
        return result;
    }

    private static void addParams(HttpURLConnection connection, Map<String, String> params) throws IOException {
        connection.setDoOutput(true);
        // set content type
        connection.addRequestProperty(CONTENT_TYPE, getDefaultContentType());
        // write encoded params
        final byte[] byteArray = encodeParams(params);
        connection.connect();
        final DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(byteArray);
        outputStream.flush();
        outputStream.close();
    }

    private static final String WALLPAPERS_FORMAT = "{\"wallpapers\":[ID]}";
    private static final String KEY_WALLPAPERS = "wallpapers";
    private static byte[] encodeParams(Map<String, String> params) throws UnsupportedEncodingException {
        /*
        // 1 key value style
        final StringBuilder encodedParams = new StringBuilder();
        final Set<String> keySet = params.keySet();
        for (String key : keySet) {
            final String encodedKey = URLEncoder.encode(key, DEFAULT_ENCODING);
            final String encodedValue = URLEncoder.encode(key, DEFAULT_ENCODING);
            encodedParams.append(encodedKey);
            encodedParams.append('=');
            encodedParams.append(encodedValue);
            encodedParams.append('&');
        }
        return encodedParams.toString().getBytes();
        */

        // 2 json style
        // {"wallpapers":[123,145,155]}
        final String id = params.get(KEY_WALLPAPERS);
        final String ids = WALLPAPERS_FORMAT.replace("ID", id);
        // final String encodedParams = URLEncoder.encode(ids, DEFAULT_ENCODING);
        final String encodedParams = ids;
        Log.i(Tags.WONDERFUL_REQUEST, "encodedParams = " + encodedParams);
        return encodedParams.getBytes();
    }

    private static final int CONNECT_TIMEOUT = 5 * 1000;
    private static final int READ_TIMEOUT = 9 * 1000;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String DEFAULT_ENCODING = "UTF-8";

    private static HttpURLConnection openConnection(URL url) throws IOException{
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        // connection.setRequestProperty("User-Agent", UserAgent.USER_AGENT);
        return connection;
    }

    private static String getDefaultContentType() {
        return "application/x-www-form-urlencoded; charset=" + DEFAULT_ENCODING;
    }
}
