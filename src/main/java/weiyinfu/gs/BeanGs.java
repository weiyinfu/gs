package weiyinfu.gs;


import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

//Java Bean属性获取、设置接口
public class BeanGs implements GetterAndSetter {
Object obj;//缓存下来，避免每次都遍历
static Map<Class<?>, Set<String>> classAttrs = new HashMap<>();
boolean ignoreCase = false;
Map<String, Method> methodMap = new TreeMap<>();
Logger logger = Logger.getLogger(BeanGs.class.getName());

public BeanGs(Object obj, boolean ignoreCase) {
    this.obj = obj;
    this.ignoreCase = ignoreCase;
    if (this.ignoreCase) {
        for (Method m : obj.getClass().getMethods()) {
            methodMap.put(m.getName().toLowerCase(), m);
        }
    }
}


public Method getMethod(String attr) {
    if (this.ignoreCase) {
        return methodMap.get("get" + attr.toLowerCase());
    } else {
        String methodName = "get" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
        try {
            return obj.getClass().getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}

Method setMethod(String attr, Class<?> paramClass) {
    if (this.ignoreCase) {
        return methodMap.get("set" + attr.toLowerCase());
    } else {
        String methodName = "set" + attr.substring(0, 1).toUpperCase() + attr.substring(1);
        ArrayList<Class<?>> tryParamClasses = new ArrayList<>();
        if (paramClass == Integer.class || paramClass == int.class) {
            tryParamClasses.add(Integer.class);
            tryParamClasses.add(int.class);
        } else if (paramClass == Double.class || paramClass == double.class) {
            tryParamClasses.add(Double.class);
            tryParamClasses.add(double.class);
        } else if (paramClass == Short.class || paramClass == short.class) {
            tryParamClasses.add(Short.class);
            tryParamClasses.add(short.class);
        } else if (paramClass == Float.class || paramClass == float.class) {
            tryParamClasses.add(Float.class);
            tryParamClasses.add(float.class);
        } else if (paramClass == Character.class || paramClass == char.class) {
            tryParamClasses.add(Character.class);
            tryParamClasses.add(char.class);
        } else if (paramClass == Boolean.class || paramClass == boolean.class) {
            tryParamClasses.add(Boolean.class);
            tryParamClasses.add(boolean.class);
        } else if (paramClass == Long.class || paramClass == long.class) {
            tryParamClasses.add(Long.class);
            tryParamClasses.add(long.class);
        } else {
            tryParamClasses.add(paramClass);
        }
        for (Class<?> cls : tryParamClasses)
            try {
                return obj.getClass().getMethod(methodName, cls);
            } catch (NoSuchMethodException e) {
                if (cls == tryParamClasses.get(tryParamClasses.size() - 1)) {
                    e.printStackTrace();
                }
            }
        return null;
    }
}

@Override
public Object get(String attr) {
    try {
        Method method = getMethod(attr);
        if (method == null) return null;
        return method.invoke(obj);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

@Override
public void set(String attr, Object valueObj) {
    try {
        Method method = setMethod(attr, valueObj.getClass());
        if (method == null) {
            return;
        }
        method.invoke(obj, valueObj);
    } catch (Exception e) {
        logger.warning(String.format("set %s error", attr));
        e.printStackTrace();
    }
}

public Set<String> attrs() {
    Set<String> a = classAttrs.get(obj.getClass());
    if (a != null) return a;
    a = new TreeSet<>();
    for (Method m : obj.getClass().getMethods()) {
        if (m.getName().equalsIgnoreCase("getclass")) continue;
        if (m.getName().startsWith("get") || m.getName().startsWith("set")) {
            a.add(Gs.lowerFirst(m.getName().substring(3)));
        }
    }
    classAttrs.put(obj.getClass(), a);
    return a;
}
}
