package weiyinfu.gs;

import java.util.Map;
import java.util.TreeMap;

//Map属性获取设置接口
public class MapGs implements GetterAndSetter {
Map<String, Object> avatar;
Map<String, Object> real;
boolean ignoreCase;

public MapGs(Map<String, Object> obj, boolean ignoreCase) {
    this.ignoreCase = ignoreCase;
    if (this.ignoreCase) {
        this.avatar = new TreeMap<>();
        for (String i : obj.keySet()) {
            this.avatar.put(i.toLowerCase(), obj.get(i));
        }
    }
    this.real = obj;
}


@Override
public Object get(String attr) {
    if (this.ignoreCase) attr = attr.toLowerCase();
    return avatar.get(attr);
}

@Override
public void set(String attr, Object valueObj) {
    avatar.put(attr.toLowerCase(), valueObj);
    real.put(attr, valueObj);
}
}