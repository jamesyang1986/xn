package com.xiaonei.db.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

/**
 * @author Peter
 */
public class JsonUtils {
    private static Logger logger = Logger.getLogger(JsonUtils.class);

    //each thread has its own ObjectMapper instance
    private static ThreadLocal<ObjectMapper> objMapperLocal = new ThreadLocal<ObjectMapper>(){
        @Override
        public ObjectMapper initialValue(){
            return new ObjectMapper().configure(JsonParser.Feature.INTERN_FIELD_NAMES, false);
        }
    };

    public static String toJSON(Object value) {
        String result = null;
        try {
            result = objMapperLocal.get().writeValueAsString(value);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        // Fix null string
        if ("null".equals(result)) {
            result = null;
        }
        return result;
    }
    
    public static <T> T toT(String jsonString, Class<T> clazz) {
        try {
            return objMapperLocal.get().readValue(jsonString, clazz);
        } catch (Exception e) {
        } 
        return null;
    }
    
    public static <T> List<T> toTList(String jsonString, Class<T> clazz) {
        try {
              return objMapperLocal.get().readValue(jsonString, TypeFactory.collectionType(List.class, clazz));
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String jsonString) {
        return toT(jsonString, Map.class);
    }
    
    public static String prettyPrint(Object value) {
        String result = null;
        try {
            result = objMapperLocal.get().defaultPrettyPrintingWriter().writeValueAsString(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Fix null string
        if ("null".equals(result)) {
            result = null;
        }
        return result;
    }

   
    static class Message {
        String uid;
        Date opr_time;
        @JsonIgnore
        String content;
        public String getUid() {
            return uid;
        }
        public void setUid(String uid) {
            this.uid = uid;
        }
        public Date getOpr_time() {
            return opr_time;
        }
        public void setOpr_time(Date opr_time) {
            this.opr_time = opr_time;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    }
    
}
