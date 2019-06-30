package weiyinfu.gs;

import io.vertx.core.json.JsonObject;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 将JavaBean转换为Map对象
 * 将Map对象转换为JavaBean对象
 * 数据类型必须匹配
 */
public class Gs {
/**
 * 将Map类型转换为Bean类型
 */
public static <T> T map2Bean(Map<String, Object> map, Class<T> cls, boolean ignoreCase) {
    try {
        T t = cls.getDeclaredConstructor().newInstance();
        MapGs mapGs = new MapGs(map, ignoreCase);
        BeanGs beanGs = new BeanGs(t, ignoreCase);
        Gs.assign(beanGs, mapGs, map.keySet());
        return t;
    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        e.printStackTrace();
    }
    return null;
}

public static <T> T json2Bean(JsonObject obj, Class<T> cls, boolean ignoreCase) {
    try {
        T t = cls.getDeclaredConstructor().newInstance();
        Map<String, Object> map = obj.getMap();
        MapGs mapGs = new MapGs(map, ignoreCase);
        BeanGs beanGs = new BeanGs(t, ignoreCase);
        Gs.assign(beanGs, mapGs, map.keySet());
        return t;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

/**
 * 根据类型将x转化为type类型的数据
 * 其中检查七种基本数据类型
 */
public static Object seven(Object x, Class<?> type) {
    Object value = null;
    if (type == int.class || type == Integer.class) {
        value = Integer.parseInt(x.toString());
    } else if (type == double.class || type == Double.class) {
        value = Double.parseDouble(x.toString());
    } else if (type == char.class || type == Character.class) {
        value = x.toString().charAt(0);
    } else if (type == float.class || type == Float.class) {
        value = Float.parseFloat(x.toString());
    } else if (type == long.class || type == Long.class) {
        value = Long.parseLong(x.toString());
    } else if (type == short.class || type == Short.class) {
        value = Short.parseShort(x.toString());
    } else if (type == String.class) {
        value = x.toString();
    } else {
        value = null;
    }
    return value;
}

static String lowerFirst(String s) {
    return s.substring(0, 1).toLowerCase() + s.substring(1);
}

public static Map<String, Object> bean2Map(Object obj, boolean ignoreCase) {
    BeanGs beanGs = new BeanGs(obj, ignoreCase);
    Map<String, Object> map = new HashMap<>();
    MapGs mapGs = new MapGs(map, ignoreCase);
    Gs.assign(mapGs, beanGs, beanGs.attrs());
    return map;
}

public static Map<String, Object> staticField2Map(Class<?> cls, boolean ignoreCase) {
    StaticFieldGs staticGs = new StaticFieldGs(cls, ignoreCase);
    Map<String, Object> ans = new TreeMap<>();
    MapGs mapGs = new MapGs(ans, ignoreCase);
    assign(mapGs, staticGs, staticGs.staticFields());
    return ans;
}

/**
 * resultset的当前行转换为Bean
 */
public static <T> T resultSet2Bean(ResultSetGs resultSetGs, Class<T> cls) throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, NoSuchMethodException {
    T obj = cls.getDeclaredConstructor().newInstance();
    BeanGs beanGs = new BeanGs(obj, true);
    Gs.assign(beanGs, resultSetGs, beanGs.attrs());
    return obj;
}


/**
 * resultset的当前行转换为Map&lt;String,Object&gt;
 */
public static Map<String, Object> resultSet2Map(ResultSet resultSet) throws SQLException {
    Map<String, Object> obj = new HashMap<>();
    try {
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            obj.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return obj;
}

public static void map2StaticField(Class<?> cls, Map<String, Object> map, boolean ignoreCase) {
    MapGs mapGs = new MapGs(map, ignoreCase);
    StaticFieldGs staticGs = new StaticFieldGs(cls, ignoreCase);
    assign(staticGs, mapGs, map.keySet());
}

public static JsonObject bean2json(Object obj) {
    JsonObject json = new JsonObject();
    BeanGs beanGs = new BeanGs(obj, false);
    for (String attr : beanGs.attrs()) {
        json.put(attr, beanGs.get(attr));
    }
    return json;
}

//从resultset获取一个JsonObject，可以进一步使用JsonObject的mapTo函数转换为对象
public static JsonObject resultSet2Json(ResultSet res) {
    JsonObject obj = new JsonObject();
    try {
        for (int i = 1; i <= res.getMetaData().getColumnCount(); i++) {
            obj.put(res.getMetaData().getColumnName(i), res.getObject(i));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return obj;
}

private static void assign(GetterAndSetter gsDes, GetterAndSetter gsSrc, Collection<String> attrs) {
    for (String i : attrs) {
        Object obj = gsSrc.get(i);
        if (obj == null) continue;
        gsDes.set(i, obj);
    }
}

}
