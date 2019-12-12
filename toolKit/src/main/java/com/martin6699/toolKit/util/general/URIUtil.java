package main.java.com.martin6699.toolKit.util.general;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author martin
 * @date 2019/12/11
 **/
public class URIUtil {

    /**
     * 提取url中的params
     * URI与URL区别 参考 https://www.baeldung.com/java-url-vs-uri
     * @throws URISyntaxException
     */
    public static void extractURIPathOrParams() throws URISyntaxException {
        String uri = "http://www.baidu.com/xxx/aaa?userId=12222&code=9999";
        URI uriObj = new URI(uri);

        List<NameValuePair> params = URLEncodedUtils.parse(uriObj, Charset.forName("UTF-8"));

        String userId = null;
        String code = null;
        for(NameValuePair param : params) {
            if("userId".equals(param.getName())) {
                userId = param.getValue();
                continue;
            }

            if("code".equals(param.getName())) {
                code = param.getValue();
            }
        }
    }
}
