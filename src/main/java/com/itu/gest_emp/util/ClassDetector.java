package com.itu.gest_emp.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassDetector {

        public static List<Class<?>> getClasses(String packageName) throws Exception {
            List<Class<?>> classes = new ArrayList<>();

            // Récupérer tous les noms de classes chargées dans le classpath
            String path = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(path);
            
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.toURI());

                if (directory.exists()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.getName().endsWith(".class")) {
                                String className = packageName + '.' + file.getName().replace(".class", "");
                                classes.add(Class.forName(className));
                            }
                        }
                    }
                }
            }

            

            return classes;
        }

}
