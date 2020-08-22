import cn.weiyinfu.gs.BeanGs;
import cn.weiyinfu.gs.Gs;
import org.junit.Test;

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

@Test
public void testGs2Map() {
    User haha = new User();
    haha.setAge(18);
    haha.setName("haha");
    var ma = Gs.bean2Map(haha, false);
    System.out.println(ma.get("name"));
    System.out.println(ma.get("age"));
}
}
