package com.sistema.mvsistema;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class MainApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init(){
        applicationContext = new SpringApplicationBuilder(Main.class).run();
    }

    @Override
    public void start(Stage stage){
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop(){
        applicationContext.close();
        Platform.exit();
    }

    public static class StageReadyEvent extends ApplicationEvent {
        private StageReadyEvent(Stage stage){
            super(stage);
        }

        public Stage getStage(){
            return ((Stage) getSource());
        }
    }
}
