import org.itheima.edu.tutorials.utils.JsonUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Poplar on 2017/6/17.
 */
public class Test {

    public static void main(String[] args){
//        jedisTest();

//        jsonTest();

        System.out.println(UUID.randomUUID());
    }

    private static void jsonTest() {
        HashMap<String, Object> map = new HashMap<>();
        ArrayList<Object> value = new ArrayList<>();
        value.add("123456");
        value.add("12345611");
        map.put("haha", value);
        String s = JsonUtils.toWrapperJson(map);
        System.out.println(s);
    }

    private static void jedisTest() {
        Jedis jedis = new Jedis("127.0.0.1");
        System.out.println("Connection to server sucessfully");
        //查看服务是否运行
        System.out.println("Server is running: "+jedis.ping());


        //设置 redis 字符串数据
        jedis.set("runoobkey", "Redis tutorial");
        // 获取存储的数据并输出
        System.out.println("Stored string in redis:: "+ jedis.get("runoobkey"));
    }

}
