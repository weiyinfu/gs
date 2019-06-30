import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.Test;
import weiyinfu.gs.BeanGs;

import java.util.ArrayList;
import java.util.List;

public class TestBean {
public class User {
    String name;
    int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

@Test
public void testBeanGs() {
    User haha = new User();
    BeanGs getterAndSetter = new BeanGs(haha, false);
    getterAndSetter.set("name", "wyf");
    getterAndSetter.set("age", 19);
    List<String> li = new ArrayList<>(getterAndSetter.attrs());
    System.out.println(new JsonArray(li));
    System.out.println(JsonObject.mapFrom(haha).toString());
}

@Test
public void testBeanGsIgnoreCase() {
    User haha = new User();
    haha.setAge(18);
    haha.setName("haha");
    BeanGs gs = new BeanGs(haha, true);
    System.out.println(gs.get("name"));
    System.out.println(gs.get("age"));
}
}
