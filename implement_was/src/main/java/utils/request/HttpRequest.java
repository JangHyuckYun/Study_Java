package utils.request;

import utils.Params;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private HttpRequestHeader httpRequestHeader;
    private static final String COLON = ":";
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> requestBody = new HashMap<String, String>();
    public HttpRequest() {}
    public void setHttpRequest(List<String> lines) {
        httpRequestHeader = new HttpRequestHeader(lines.get(0));
        lines.remove(0);

        for (String line : lines) {
            String[] keyValue = line.split(COLON);
            headers.put(keyValue[0].trim(), keyValue[1].trim());
        }
    }

    public HttpRequest(List<String> lines) {
        setHttpRequest(lines);
    }

    public String getUrl() {
        return this.httpRequestHeader.getUrl();
    }

    public String getMethod() {
        return this.httpRequestHeader.getMethod();
    }

    public String getVersion() {
        return this.httpRequestHeader.getVersion();
    }

    public Map<String, String> getParams() {
        return this.httpRequestHeader.getParams();
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    /**
     * @description
     *  - 요청 정보 중, Content-Length가 존재시 실핼되는 메서드로,
     *    Content-Length 길이만큼 반복문을 돌려, 바이트 형태의 데이터를 -> 문자열로 변환한다.
     *  - BufferedReader.readLine()이 아닌, 반복문을 사용하는 이유는,
     *    BufferedReader.readLine()의 경우, 마지막 데이터가 \n인 경우 + 다음 값이 null인 경우에만 다음으로 넘어가진다.
     *    위 조건을 충족하지 못하는 경우 무한 대기 현상이 발생하게 된다.
     *    요청 정보의 파라미터 값 마지막에는 \n값이 존재하지 않기 때문에, 무한 대기현상이 발생하게 된다.
     *    요청자가 임의로 \n로 넣는 상황은 별로 없기에, 서버단에서 처리하기 위해, readLine()이 아닌,
     *    일반 반복문을 사용했다.
     * */
    public void initialParams(BufferedReader br) throws IOException {
        StringBuffer buffer = new StringBuffer();
        int len = Integer.parseInt(this.headers.get("Content-Length"));

        if(len > 0) {
            try {
                for(int i = 0; i < len; i++) {
                    int ch = br.read();
                    buffer.append((char) ch);
                }
                String paramsString = buffer.toString();
                requestBody = Params.StrParamToMap(paramsString);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
