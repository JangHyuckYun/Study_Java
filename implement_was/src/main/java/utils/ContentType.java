package utils;

import java.util.Arrays;

public enum ContentType {
    TEXT("text/plain"),
    JSON("application/json"),
    HTML("text/html"),
    CSS("text/css"),
    JAVASCRIPT("text/javascript"),
    ICO("image/x-icon"),

    NULL("null"),
    XML("application/xml");

    private String contentType;

    private ContentType(String type){
        this.contentType = type;
    }

    public String getContentType() {
        return this.contentType;
    }

    public static ContentType getContentType(String url) {
        System.out.println("String url: "+url);
        String[] urlSplit = url.split("\\.");
        String type = urlSplit[urlSplit.length - 1];

        switch(type) {
            case "html":
                return HTML;
            case "css":
                return CSS;
            case "js":
                return JAVASCRIPT;
            case "ico":
                return ICO;
            default:
                return TEXT;

        }
    }
}
