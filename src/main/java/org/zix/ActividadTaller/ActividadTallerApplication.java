package org.zix.ActividadTaller;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.zix.ActividadTaller.console.ConsoleMenu;

@SpringBootApplication
public class ActividadTallerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ActividadTallerApplication.class, args);
        ConsoleMenu consoleMenu = context.getBean(ConsoleMenu.class);
        consoleMenu.start();
    }
}

