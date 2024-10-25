package org.example;

import org.example.config.TodoistConfig;
import org.example.tool.TodoistExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Properties prop = new Properties();
        loadProperties(prop);
        log.info("Properties = {}", prop);

        TodoistConfig config = new TodoistConfig(prop);
        createOutputPath(config);
        TodoistExporter service = new TodoistExporter(config);
        service.start();
    }

    private static void createOutputPath(TodoistConfig config) {
        File file = new File(config.getOutputPath());
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
            if (!mkdir)
                throw new RuntimeException("Cannot create output path to export, path = " + config.getOutputPath());
        }
    }

    private static void loadProperties(Properties prop) {
        try {
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties");
            prop.load(stream);
        } catch (Exception ex) {
            log.error("Error loading properties", ex);
        }
    }
}