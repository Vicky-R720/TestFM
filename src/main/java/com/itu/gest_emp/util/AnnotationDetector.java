package com.itu.gest_emp.util;

import java.util.List;

import com.itu.gest_emp.annotation.Controller;

public class AnnotationDetector {
    public static void detectionTest(String packageName) {
        try {
            List<Class<?>> classes = ClassDetector.getClasses(packageName);
            for (Class<?> class1 : classes) {
                System.out.println(class1.getSimpleName() + ": " + class1.isAnnotationPresent(Controller.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
