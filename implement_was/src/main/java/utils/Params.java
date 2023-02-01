package utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Params {
    private final static String QUESTION_MARK = "\\?";
    private final static String AMPERSAND = "&";
    private final static String EQUAL_SIGN = "=";

    public static Map<String,String> StrParamToMap(String paramString) {
        Map<String,String> result = new HashMap<String, String>();

        for(String param : paramString.split(AMPERSAND)) {
            String[] keyValue = param.split(EQUAL_SIGN);
            System.out.println(Arrays.toString(keyValue));
            result.put(keyValue[0], keyValue[1]);
        }

        return result;
    }

}
