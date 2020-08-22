Getter and Setter 封装

* JsonGs：JsonObject的getter和setter
* MapGs：Map对象的getter和setter
* ResultSetGs：ResultSet对象的getter和setter
* StaticFieldGs：静态类型的getter和setter
* BeanGs：Bean对象的getter和setter
* Gs：入口类，封装各种Gs


```java
package cn.weiyinfu.gs;

import io.vertx.core.json.JsonObject;

public class JsonGs implements GetterAndSetter {
boolean ignoreCase = false;
JsonObject real;
JsonObject avatar = new JsonObject();

public JsonGs(JsonObject obj, boolean ignoreCase) {
    this.ignoreCase = ignoreCase;
    if (this.ignoreCase) {
        for (String attr : obj.getMap().keySet()) {
            this.avatar.put(attr.toLowerCase(), obj.getValue(attr));
        }
    }
    this.real = obj;
}

public Object get(String attr) {
    if (ignoreCase) attr = attr.toLowerCase();
    return avatar.getValue(attr);
}

public void set(String attr, Object valueObj) {
    avatar.put(attr.toLowerCase(), valueObj);
    real.put(attr, valueObj);
}
}
```

使用时
```plain

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

```

