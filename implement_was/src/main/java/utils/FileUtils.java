package utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class FileUtils {
    public final static String DEFAULT_RESOURCE_STATIC_PATH = "static/";

    public static boolean isStaticRequest(String url) {
        return url.matches("^.*\\.(html|css|js|ico).*");
    }

    public static URL getResourceURL(String path) {
        if(path.charAt(0) == '/') {
            path = path.substring(1);
        }
        return FileUtils.class.getClassLoader().getResource(DEFAULT_RESOURCE_STATIC_PATH+path);
    }

    public static String getResourcePath(String path) {
        return getResourceURL(path).getPath();
    }

    public static String getResourceFile(String path) {
        return getResourceURL(path).getFile();
    }

    public static String fileToString(String path) throws IOException {
        // "/path/to" => "path/to"
        // resources에서 파일을 찾기 위해 조건 설정
        URL url = getResourceURL(path);
        if(url == null) throw new FileNotFoundException("Can not find \""+path+"\"");

        return fileToString(new File(url.getPath()));
    }
    public static String fileToString(File file) throws IOException {
        BufferedReader bf = null;
        if(!file.isFile()) throw new FileNotFoundException("Can not find File");
        bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        String line = "";
        StringBuffer sbf = new StringBuffer();
        while((line = bf.readLine()) != null) {
            sbf.append(line);
        }

        return sbf.toString();
    }

    public static byte[] fileToBytes(String path) throws IOException {
        return fileToString(path).getBytes();
    }

    public static byte[] fileToBytes(File file) throws IOException {
        return fileToString(file).getBytes();
    }

}
