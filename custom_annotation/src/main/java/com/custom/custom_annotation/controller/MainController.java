package com.custom.custom_annotation.controller;

import com.custom.custom_annotation.annotation.MyAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class MainController {

    @MyAnnotation(name = "CheckClassCheckClass", age = 111)
    @ResponseBody
    @GetMapping("/test")
    public String main() {
        log.info("===> MainController->main()...");
        test();
        return "test";
    }

    @ResponseBody
    @GetMapping("/test2")
    public String main2() {
        log.info("===> MainController->main2()...");
        return "test";
    }

    @MyAnnotation(name = "MainController->test()", age = 222)
    public String test() {
        log.info("===> MainController->test()...");

        return "asd";
    }
}
