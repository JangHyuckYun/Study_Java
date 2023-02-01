# implement_was

## 프로젝트 계획 계기
회사에서 개인 공부하는 시간이 주어져, 무엇을 공부할지 고민했다.
spring 관련 강의를 들을까 고민을 했지만, 막연하게 따라하고, 사용하는 방법만 배울것 같아서
원리를 배우자고 고민하게 되었다.  
spring을 공부하기에 앞서, 사용자에게 웹 페이지를 보여주는 과정을
먼저 공부해보고 싶어져서, WAS ( Web Application Server )를 custom으로 만들게 되었다.


## 참고 사이트
 - https://webfirewood.tistory.com/78
 - https://os94.tistory.com/171


## implement_was의 흐름 ( 쓰레드 풀 적용 전 )
1. 첫 번째는 ``src/main/java/main/controller/Application.java``에서 시작한다.
   1.  Application.java에서 메인 메서드를 실행하면 Custom Tomcat(``src/main/java/tomcat/org/apache/catalina/startup/Tomcat.java``)의 start 메서드가 실행된다.
        ```java
       // Application.java
        public class Application {
            public static void main(String[] args) {
                Tomcat tomcat = new Tomcat();
                tomcat.start();
            }
        }
        ```
2. Tomcat ``start()``메서드에서 먼저 컨트롤러를 전부 **controllers** 변수안에 담아주는 작업을 한다. ( **initialControllers()** )
3. 그 후 **Connector 생성자**를 생성해, **start()** 메서드를 실행한다. ( 유연성을 위해 Connector 사용, Http와 통신하는 Http11Processer와 연결하기 위함)
   ```java
    public class Tomcat {
        public interface Controller { // 설명을 위해 임시로 추가
            HttpResponse service(HttpRequest request, HttpResponse response) throws Exception;
        }
   
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
    ```
   1. enum으로 관리를 할 까 생각을 했지만, Controller가 생성될 때마다 enum에 추가를 해줘야 하기 때문에, 자동화를  
      하기 위해, 위와 같은 방식으로 만들었다.
4. Connector의 start()메서드에서 쓰레드 (자신)을 생성하여 실행한다.
   ```java
    public class Connector implements Runnable {
        ...
        public Connector() {
            this(DEFAULT_PORT, DEFAULT_MAX_THREAD_COUNT, null);
        }

        public Connector(Map<String, Controller> controllers) {
            this(DEFAULT_PORT, DEFAULT_MAX_THREAD_COUNT, controllers);
        }

        public Connector(int port, int maxThreadCount, Map<String, Controller> controllers) {
            try {
                this.controllers = controllers;
                serverSocket = new ServerSocket(8200);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public void start() {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();

            stopped = false;
        }
        ...
    }
    ``` 
   1. 자신을 실행한 Connector는 **run()** 메서드를 실행하게 된다.
   2. Connector 생성자에서 만든 serverSocket의 값이 null이 아닐 경우, 요청을 받을 준비를 한다.
      1. **serverSocket.accept()**는 serverSocket에서 **지정한 포트로 요청**이 올 때까지 대기를 하게 된다.
      2. 요청이 올 경우 **serverSocket.accept()**는 **Socket값을 반환**하며 아래 코드들이 실행한다.
      3. 수행을 다 하면 종료되고, **더이상 요청을 받을 수 없게** 된다.
      4. 하지만 웹 서버는 **지속적으로 요쳥**을 받아야 하는 만큼, **while문을 통해 지속적**으로 **요청을 받을 수 있게** 만들었다.
   3. while문을 통해 connect()함수를 실행하고, connect()함수에서 process()함수에 serverSocket.accept()를 넘긴다.
   4. process함수는 요청이 오면 실행이 되고, 요청이 올 해당 요청에 대한 쓰레드를 만들어 실행한다.
   ```java
    public class Connector implements Runnable {
        ...
        @Override
        public void run() {
            System.out.println("connector run...");
            if (serverSocket != null) {
               while(!stopped) {
                   connect();
               }
            }
        }
   
        public void connect() {
            try {
                process(serverSocket.accept());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void process(Socket socket) throws IOException {
            if (serverSocket == null) return;
    
            Runnable runnable = new Http11Processor(socket, controllers);
            (new Thread(runnable)).start();
        }
        ...
    }
    ```
5. Http11Processor 관련 내용 작성 예정
