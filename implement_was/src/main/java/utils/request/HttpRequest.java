package utils.request;

import utils.Params;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private HttpRequestHeader httpRequestHeader;

    private static final String COLON = ":";
//    private HttpRequestHeader httpRequestHeader;

    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> requestBody = new HashMap<String, String>();

    public HttpRequest() {

    }

    public void setHttpRequest(List<String> lines) {
        httpRequestHeader = new HttpRequestHeader(lines.get(0));
//        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(lines.get(0));
//        this.httpRequestHeader = httpRequestHeader;
        lines.remove(0);

        for (String line : lines) {
            String[] keyValue = line.split(COLON);
            headers.put(keyValue[0].trim(), keyValue[1].trim());
        }
    }

    public HttpRequest(List<String> lines) {
        setHttpRequest(lines);
//        httpRequestHeader = new HttpRequestHeader(lines.get(0));
////        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(lines.get(0));
////        this.httpRequestHeader = httpRequestHeader;
//        lines.remove(0);
//
//        int headerLastIdx = lines.indexOf("");
//        List<String> headerList = lines.subList(0,headerLastIdx);
//        List<String> dataList = lines.subList(headerLastIdx + 1, lines.size());
//
//        for (String header : headerList) {
//            String[] keyValue = header.split(COLON);
//            headers.put(keyValue[0], keyValue[1]);
//        }
//
//        for (String data : dataList) {
//            String[] keyValue = data.split("=");
//            requestBody.put(keyValue[0], keyValue[1]);
//        }
    }

    public String getUrl() {
        return this.httpRequestHeader.getUrl();
    }

    public String getMethod() {
        return this.httpRequestHeader.getMethod();
    }

    public String getVersion() {
        return this.httpRequestHeader.getVersion();
    }

    public Map<String, String> getParams() {
        return this.httpRequestHeader.getParams();
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void initialParams(BufferedReader br) throws IOException {
        StringBuffer buffer = new StringBuffer();
        int len = Integer.parseInt(this.headers.get("Content-Length"));

        if(len > 0) {
            try {
                for(int i = 0; i < len; i++) {
                    int ch = br.read();
                    buffer.append((char) ch);
                }
                String paramsString = buffer.toString();
                requestBody = Params.StrParamToMap(paramsString);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
