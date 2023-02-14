package tomcat.org.apache.coyote.http11;

import tomcat.org.apache.coyote.Processor;
import utils.ContentType;
import utils.FileUtils;
import utils.HttpStatus;
import utils.response.HttpResponse;
import main.controller.design.Controller;
import utils.request.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Http11Processor implements Runnable, Processor {

    Socket connection;

    private Map<String, Controller> controllers = null;
    private CountDownLatch countDownLatch = null;

    @Override
    public void run() {
        try {
            process(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Http11Processor(Socket connection, Map<String, Controller> controllers) {
        this.connection = connection;
        this.controllers = controllers;
    }

    public Http11Processor(Socket connection, Map<String, Controller> controllers, CountDownLatch countDownLatch) {
        this.connection = connection;
        this.controllers = controllers;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(Socket connection) throws IOException {
        InputStreamReader isr = null;
        BufferedReader br = null;
        HttpRequest httpRequest = new HttpRequest();
        HttpResponse httpResponse = null;
        try {
            isr = new InputStreamReader(connection.getInputStream(), "utf8");
            br = new BufferedReader(isr);


            List<String> lines = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.equals("")) {
                    System.out.println(lines.toString());
                    httpRequest.setHttpRequest(lines);
                    if (httpRequest.getHeaders().containsKey("Content-Length")) {
                        httpRequest.initialParams(br);
                    }
                    httpResponse = createResponse(httpRequest, connection.getOutputStream());

                    break;
                } else {
                    lines.add(line);
                }
            }

            if(httpResponse != null) {
                httpResponse.sendResponse();
                countDownLatch.countDown();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if(connection != null) {
                System.out.println("connection close...");
                connection.close();
            }
        }
    }

    private String refineControllerName(String url) {
        return url.replace("/", "").toLowerCase();
    }

    private HttpResponse createResponse(HttpRequest request, OutputStream out) throws IOException {
//        URL staticUrl = this.getClass().getClassLoader().getResource(request.getUrl());
        HttpResponse response = new HttpResponse(out);
        String url = request.getUrl();

        if (FileUtils.isStaticRequest(url)) {
            // 정적 요청
            System.out.println("정적요청 o");

            String fileStr = FileUtils.fileToString(url);
            response.setResponse(HttpStatus.OK, fileStr, ContentType.getContentType(url));
        } else {
            // 동적 요청
            System.out.println("정적요청 x");
            String controllerName = refineControllerName(request.getUrl());
            try {
                Controller controller = controllers.get(controllerName);
                if (controller == null) { // 해당 url에 맞는 controllr가 없는 경우 에러
                    response.setResponse(HttpStatus.NULL, "Can d not find URL \"" + request.getUrl() + "\" ", ContentType.TEXT);
                    response.sendResponse();
                    throw new RuntimeException("cannot find controller [" + controllerName + "]");
                }
                return controller.service(request, response);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return response;
    }

}
