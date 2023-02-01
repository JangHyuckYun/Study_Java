package tomcat.org.apache.catalina.startup;

import main.controller.design.Controller;
import tomcat.org.apache.catalina.connector.Connector;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Tomcat {

    private Map<String, Controller> controllers = new HashMap<String, Controller>();
    private final static String CONTROLLER_PATH = "src\\main\\java\\main\\controller\\";
    public void start() {
        initialControllers();

        Connector connector = new Connector(controllers);
        connector.start();

        /**
         * @description
         * 블로그를 참조해서 사용하였다.
         * 아래 코드 없이 CustomTomcat을 실행하려 해봤지만, 유저가 접속하기도 전에 자꾸 꺼지길래 추가하였다.
         * tomcat.join()으로 대기중이면 되지 않을까 생각했지만, 결과는 실패였고, 방법을 찾는 도중에,
         * 아래 코드 형식으로 대기를 시키는 것을 찾아 사용하였다.
         * */
        try {
            // make the application wait until we press any key.
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally");
            connector.stop();
        }
    }

    /**
     *
     * @description
     * controller 패키지에 있는 .java로 끝나는 Controller 자바 파일들을 가져온다.
     * 가져온 자바 파일들의 이름을 따와, 동적으로 생성자를 만들고, Map안에 넣어둔다.
     * 추후 Controller Handler에서 사용하기 위해 만들었다.
     * */
    private void initialControllers() {
        File rootFile = new File(".");
        String rootPath = String.valueOf(rootFile.getAbsoluteFile());
        rootPath = rootPath.substring(0, rootPath.length() - 1);
        // 다른 os에서도 동작하기 위해 slash를 File.separator로 교체하는 작업을 하였다.
        String url = (rootPath + CONTROLLER_PATH).replace("\\", File.separator);

        List<String> fileNameList = Arrays.stream((new File(url)).listFiles())
                .filter(file -> file.getName().contains(".java"))
                .map(file -> file.getName().replace(".java", ".class"))
                .collect(Collectors.toList());

        String controllerPackagePath = CONTROLLER_PATH.replace("src\\main\\java\\","").replace("\\",".");

        for(String fileName : fileNameList) {
            System.out.println("fileName:" +fileName);
            try {
                String controllerName = fileName.replace(".class", "");
                Class<?> findClass = Class.forName(controllerPackagePath+controllerName);

                // newInstance 메서드를 호출해야, 해당 클래스의 생성자가 실행된다.
                // controller 상단의 이름 [ ex) HelloController -> hello ]과 url [ /hello -> hello ]이 일치하는 경우 해당 Controller 일치할 경우 실행
                controllers.put(controllerName.replace("Controller","").toLowerCase(), (Controller) findClass.newInstance());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
