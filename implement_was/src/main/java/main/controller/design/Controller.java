package main.controller.design;

import utils.request.HttpRequest;
import utils.response.HttpResponse;

public interface Controller {
    HttpResponse service(HttpRequest request, HttpResponse response) throws Exception;
}
