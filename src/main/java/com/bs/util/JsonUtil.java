package com.bs.util;


import com.bs.pojo.User;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description: JSON的序列化和反序列化的操作
 * @Auther: 杨博文
 * @Date: 2019/6/21 22:52
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static{
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);

        //忽略空Bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);

        //所有的日期格式都统一为以下的样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }
    /**
     * @Description: 对象转字符串
     * @Auther: 杨博文
     * @Date: 2019/6/21 23:15
     */
    public static <T> String obj2String(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error",e);
            return null;
        }
    }

    /**
     * @Description: 对象转的字符串（格式化后）
     * @Auther: 杨博文
     * @Date: 2019/6/21 23:15
     */
    public static <T> String obj2StringPretty(T obj){
        if(obj == null){
            return null;
        }
        try {
            return obj instanceof String ? (String)obj :  objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse Object to String error",e);
            return null;
        }
    }
    /**
     * @Description: 字符串转对象
     * @Auther: 杨博文
     * @Date: 2019/6/21 23:17
     */
    public static <T> T string2Obj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str) || clazz == null){
            return null; 
        }

        try {
            return clazz.equals(String.class)? (T)str : objectMapper.readValue(str,clazz);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    /**
     * @Description: 字符串转复杂对象（List，Map，Set等）
     * @Auther: 杨博文
     * @Date: 2019/6/22 0:20
     */
    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str) || typeReference == null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)? str : objectMapper.readValue(str,typeReference));
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }

    /**
     * @Description: 字符串转复杂对象（可变长）
     * @Auther: 杨博文
     * @Date: 2019/6/22 0:21
     */
    public static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>... elementClasses){
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("Parse String to Object error",e);
            return null;
        }
    }









    public static void main(String[] args) {
        User u1 = new User();
        u1.setId(1);
        u1.setEmail("galaace@qq.com");

        User u2 = new User();
        u2.setId(2);
        u2.setEmail("galaace2@qq.com");

        String u1Json = JsonUtil.obj2String(u1);
        String u1JsonPretty = JsonUtil.obj2StringPretty(u1);
        log.info("u1Json:{}",u1Json);
        log.info("u1JsonPretty：{}",u1JsonPretty);

        User user = JsonUtil.string2Obj(u1Json,User.class);

        List<User> userList = Lists.newArrayList();
        userList.add(u1);
        userList.add(u2);

        String s = obj2StringPretty(userList);

        log.info("=============");
        log.info(s);

        List<User> list = JsonUtil.string2Obj(s, new TypeReference<List<User>>() {
        });

        List<User> list2 = JsonUtil.string2Obj(s,List.class,User.class);
        System.out.println("end");
    }

}
