package utils.request;

import utils.HttpMethod;
import utils.Params;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestHeader {
    private final static String QUESTION_MARK = "\\?";
    private final static String AMPERSAND = "&";
    private final static String EQUAL_SIGN = "=";
    private HttpMethod method;
    private String url;
    private Map<String, String> params;
    private String version;


    public HttpRequestHeader() {

    }
    public HttpRequestHeader(HttpRequestHeader httpRequestHeader) {
        this.method = httpRequestHeader.method;
        this.url = httpRequestHeader.url;
        this.params = httpRequestHeader.params;
        this.version = httpRequestHeader.version;
    }
    public HttpRequestHeader(String line) {
        params = new HashMap<String, String>();
        String[] division = line.split(" ");
        this.method = HttpMethod.valueOf(division[0]);

        if(division[1].matches("(.*)"+QUESTION_MARK+"(.*)")) {
            String[] division_url = division[1].split(QUESTION_MARK);
            this.url = division_url[0];
            params = Params.StrParamToMap(division_url[1]);
        } else {
            this.url = division[1];
        }

        this.version = division[2];
    }

    public String getMethod() {
        return this.method.getMethod();
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public String getVersion() {
        return this.version;
    }
}
