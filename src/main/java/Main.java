import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// import com.itu.gest_emp.controller.HelloController;
import com.itu.gest_emp.controller.TestController;

import servlet.ModelView;
import servlet.annotations.Url;

public class Main {
    public static void main(String[] args) {
        Class<?> class1 = TestController.class;
        Method[] methods = class1.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Url.class)) {
                try {
                    Url url = method.getAnnotation(Url.class);
                    Object object = method.invoke(new TestController());
                    System.out.println(url.value() + ": " + object);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        ModelView mv = new ModelView();
        mv.setView("test.jsp");
        System.out.println("Nom de la vue : " + mv.getView());

    }
}
