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
