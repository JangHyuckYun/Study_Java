package main.controller;

import utils.ContentType;
import utils.FileUtils;
import utils.HttpStatus;
import utils.response.HttpResponse;
import main.controller.design.AbstractController;
import utils.request.HttpRequest;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class HelloController extends AbstractController {

    public HelloController() {
        super();
    }

    @Override
    public HttpResponse doGet(HttpRequest request, HttpResponse response) {
        System.out.println("Hello Controller!!");
        String responseBody = "";
        try {
            responseBody = FileUtils.fileToString("hello.html");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        response.setResponse(HttpStatus.OK, responseBody, ContentType.HTML);
        return response;
    }

    @Override
    public HttpResponse doPost(HttpRequest request, HttpResponse response) {

        response.setResponse(HttpStatus.OK, "{'test':'aaa'}", ContentType.JSON);
        return response;

    }
}
