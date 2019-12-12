package main.java.com.martin6699.toolKit.util.general;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author martin
 * @date 2019/12/11
 **/
public class JsonUtil {

    /**
     * 构建JSON对象- JSONObject 不需要toString()
     * 构建JSON字符串 - JSONObject 需要toString()
     *
     * @return
     */
    public static Object buildJson() {

        JSONObject params = new JSONObject();
        params.put("name", "martin");

        return params.toString();

    }

    /**
     * 构建参数值为数组的json
     */
    public static void buildJsonArray() {
        String message;
        JSONObject json = new JSONObject();
        json.put("name", "student");

        JSONArray array = new JSONArray();
        JSONObject item = new JSONObject();
        item.put("information", "test");
        item.put("id", 3);
        item.put("name", "course1");
        array.add(item);

        json.put("course", array);

        message = json.toString();

        System.out.println(message);
    }

}
