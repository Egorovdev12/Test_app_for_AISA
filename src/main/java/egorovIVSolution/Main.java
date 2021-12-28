package egorovIVSolution;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class Main {
    public static void main(String[] args) throws IOException, LifecycleException {
        // Создание объекта томкэта
        final Tomcat tomcat = new Tomcat();
        final Path baseDir = Files.createTempDirectory("tomcat");
        baseDir.toFile().deleteOnExit();
        tomcat.setBaseDir(baseDir.toAbsolutePath().toString());

        // Конфигурация порта через класс Connector
        final Connector connector = new Connector();
        connector.setPort(8088);
        tomcat.setConnector(connector);

        // Указание корневой дериктории
        tomcat.getHost().setAppBase(".");
        tomcat.addWebapp("", ".");

        // Запуск сервера
        tomcat.start();
        tomcat.getServer().await();
    }
}