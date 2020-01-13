package cn.weiyinfu.gs;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 将Class的静态字段映射为Map
 */
public class StaticFieldGs implements GetterAndSetter {
Class<?> cls;
Map<String, Field> map = new TreeMap<>();
boolean ignoreCase = false;
final static int PUBLIC_STATIC = Modifier.PUBLIC | Modifier.STATIC;

public StaticFieldGs(Class<?> cls, boolean ignoreCase) {
    this.cls = cls;
    this.ignoreCase = ignoreCase;
    if (this.ignoreCase) {
        for (Field i : cls.getDeclaredFields()) {
            if (i.getModifiers() == PUBLIC_STATIC) {
                map.put(i.getName().toLowerCase(), i);
            }
        }
    }
}


@Override
public Object get(String attr) {
    try {
        Field field = null;
        if (this.ignoreCase) {
            field = map.get(attr.toLowerCase());
        } else {
            field = cls.getDeclaredField(attr);
        }
        if (field == null) return null;
        if (field.getModifiers() == PUBLIC_STATIC) {
            return field.get(cls);
        } else {
            return null;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

@Override
public void set(String attr, Object valueObj) {
    try {
        Field field = null;
        if (this.ignoreCase) {
            field = map.get(attr.toLowerCase());
        } else {
            field = cls.getDeclaredField(attr);
        }
        if (field == null) {
            return;
        }
        if (field.getModifiers() == PUBLIC_STATIC) {
            if (field.getType() == Integer.class && valueObj instanceof String) {
                field.set(cls, Integer.parseInt(valueObj.toString()));
            } else if (field.getType() == Boolean.class && valueObj instanceof String) {
                field.set(cls, Boolean.parseBoolean(valueObj.toString()));
            } else {
                field.set(cls, valueObj);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public Collection<String> staticFields() {
    List<String> a = new ArrayList<>();
    for (Field i : cls.getDeclaredFields()) {
        if (i.getModifiers() == PUBLIC_STATIC) {
            a.add(i.getName());
        }
    }
    return a;
}
}
