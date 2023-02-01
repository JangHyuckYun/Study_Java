package main;

import tomcat.org.apache.catalina.startup.Tomcat;

public class Application {
    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        tomcat.start();
    }
}
