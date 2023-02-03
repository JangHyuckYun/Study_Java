package utils.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.ContentType;
import utils.HttpStatus;
import utils.request.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    DataOutputStream dos;
    int status;

    Map<String, String> headers = new HashMap<String, String>();
    String responseBody = "";
    public HttpResponse() {

    }

    public HttpResponse(OutputStream out) throws IOException {
        System.out.println("set dos...");
        this.dos = new DataOutputStream(out);
    }

    public HttpResponse(HttpStatus status, String responseBody, ContentType contentType) {
        setResponse(status, responseBody, contentType);
    }

    public void setResponse(HttpStatus status, String responseBody, ContentType contentType) {
        this.setStatus(status);
        this.responseBody = responseBody;
        this.headers.put("Content-Type", contentType.getContentType());
        this.headers.put("Content-Length", String.valueOf(responseBody.toString().length()));
    }

    public void setStatus(HttpStatus status) {
        this.status = status.getStatus();
    }

    public int getStatus() {
        return this.status;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public void sendResponse() throws IOException {
        try {
            String resultTxt = this.status >= 200 && this.status < 300 ? "OK" : "Not Found";
            System.out.println("Send Response...");
            System.out.println("HTTP/1.1 " + this.status +" "+ resultTxt);
            System.out.println("Content-Type: "+this.headers.get("Content-Type") + ";charset=UTF-8");
            System.out.println("Content-Length: "+this.responseBody.toString().length());

            byte[] body = this.responseBody.getBytes(StandardCharsets.UTF_8);
            this.dos.writeBytes("HTTP/1.1 " + this.status +" "+ resultTxt +" \r\n");
            this.dos.writeBytes("Content-Type: "+this.headers.get("Content-Type") + ";charset=UTF-8\r\n");
            this.dos.writeBytes("Content-Length: "+this.responseBody.toString().length()+"\r\n");
            this.dos.writeBytes("\r\n");
            this.dos.write(body);
            this.dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(this.dos != null) {
                this.dos.close();
            }
        }
    }

    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("{asd}".substring(1,4));
        try {
            String headerStr = objectMapper.writeValueAsString(this.headers);
            headerStr = headerStr.substring(1, headerStr.length() - 1);
//            return "{" + "\"" + "responseBody" +"\""+ ": \"" +"test" + "\", " + headerStr + "}"  ;
            return "{\"responseBody\": \"test\", \"Content-Length\":4,\"Content-Type\":\"text/plain\"}";
        } catch (JsonProcessingException e) {
//            return "{" + "\"" + "responseBody" +"\""+ ": \"" +"test" + "\", " + "{" + "\"" + "headers" +"\"" + ": \""+ this.headers +"\""  + "}";
            return "";
        }
    }
}
