package utils;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    NOVALUE("");

    final private String method;
    public String getMethod() {
        return method;
    }
    private HttpMethod(String method) {
        this.method = method;
    }

    public static HttpMethod compare(String method) {
        try {
            return valueOf(method);
        } catch (Exception e) {
            return NOVALUE;
        }
    }
}
