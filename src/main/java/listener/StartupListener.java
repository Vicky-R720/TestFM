package com.itu.gest_emp.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import com.itu.gest_emp.util.AnnotationDetector;

@WebListener
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("=== DEMARRAGE DETECTION ===");

        String packageName = "com.itu.gest_emp.controller";
        AnnotationDetector.detectionTest(packageName);

        System.out.println("=== FIN DETECTION ===");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) { }
}
