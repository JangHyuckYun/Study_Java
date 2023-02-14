package main.controller.design;

import utils.ContentType;
import utils.HttpStatus;
import utils.response.HttpResponse;
import utils.request.HttpRequest;
import utils.HttpMethod;

public abstract class AbstractController implements Controller {

    public HttpResponse doGet(HttpRequest request, HttpResponse response) {
        response.setResponse(HttpStatus.NULL, "Get Method can not load", ContentType.TEXT);
        return response;
    }
    public HttpResponse doPost(HttpRequest request, HttpResponse response) {
        response.setResponse(HttpStatus.NULL, "POST Method can not load", ContentType.TEXT);
        return response;
    }
    public HttpResponse doPut(HttpRequest request, HttpResponse response) {
        response.setResponse(HttpStatus.NULL, "PUT Method can not load", ContentType.TEXT);
        return response;
    }
    public HttpResponse doDelete(HttpRequest request, HttpResponse response) {
        response.setResponse(HttpStatus.NULL, "DELETE Method can not load", ContentType.TEXT);
        return response;
    }
    public HttpResponse doPatch(HttpRequest request, HttpResponse response) {
        response.setResponse(HttpStatus.NULL, "PATCH Method can not load", ContentType.TEXT);
        return response;
    }

    public HttpResponse doHead(HttpRequest request, HttpResponse response) {
        response.setResponse(HttpStatus.NULL, "HEAD Method can not load", ContentType.TEXT);
        return response;
    }

    public AbstractController() {

    }

    @Override
    public HttpResponse service(HttpRequest request, HttpResponse response) throws Exception {
        System.out.println("Abstract service... "+ request.getUrl() +" method: "+request.getMethod());
        switch (HttpMethod.compare(request.getMethod())) {
            case GET:
                return doGet(request, response);
            case POST:
                return doPost(request, response);
            case PUT:
                return doPut(request, response);
            case PATCH:
                return doPatch(request, response);
            case DELETE:
                return doDelete(request, response);
            case HEAD:
                return doHead(request, response);
            case NOVALUE:
                throw new Exception("can not find method");

        }

        return null;
    }
}
