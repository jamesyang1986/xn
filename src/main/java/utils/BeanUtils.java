package utils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanUtils {

    private static Map<String, Method> methodMap = new ConcurrentHashMap<String, Method>();

    public static Method genGetMethod(Class clazz, String fieldName)
            throws Exception {
        StringBuffer sb = new StringBuffer();
        if (fieldName.startsWith("test")) {
            sb.append("is");
        } else {
            sb.append("get");
        }

        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            String methodName = sb.toString();
            if (!methodMap.containsKey(methodName)) {
                methodMap.put(methodName, clazz.getMethod(methodName));
            }
            return methodMap.get(methodName);
        } catch (Exception e) {
            throw e;
        }

    }

    public static Method genSetMethod(Class clazz, String fieldName,
                                      Class[] parameterTypes) throws Exception {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));

            String methodName = sb.toString();
            if (!methodMap.containsKey(methodName)) {
                methodMap.put(methodName,
                        clazz.getMethod(methodName, parameterTypes));
            }
            return methodMap.get(methodName);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void invokeSet(Object o, String fieldName, Object value)
            throws Exception {
        try {
            Class[] parameterTypes = new Class[1];
            Class returnTypeClazz = genGetMethod(o.getClass(), fieldName)
                    .getReturnType();
            parameterTypes[0] = returnTypeClazz;

            Method method = genSetMethod(o.getClass(), fieldName,
                    parameterTypes);

            if (parameterTypes[0].toString().equals("int") && value != null) {
                value = Integer.parseInt(value.toString().trim());
            } else if ("true".equalsIgnoreCase(value.toString())
                    || "false".equalsIgnoreCase(value.toString())) {
                value = Boolean.parseBoolean(value.toString());
            }
            method.invoke(o, new Object[]{value});
        } catch (Exception e) {
            throw e;
        }
    }

    public static Object invokeGet(Object o, String fieldName) throws Exception {
        try {
            Method method = genGetMethod(o.getClass(), fieldName);
            return method.invoke(o, new Object[0]);
        } catch (Exception e) {
            throw e;
        }
    }

}
