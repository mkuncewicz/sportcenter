package com.example.sportcenterv1;

import com.example.sportcenterv1.util.SpringFXMLLoader;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        // Przygotowanie argumentów dla Spring Boot
        String[] args = getParameters().getRaw().toArray(new String[0]);

        // Konfiguracja i uruchomienie kontekstu Spring Boot
        applicationContext = new SpringApplicationBuilder()
                .sources(Sportcenterv1Application.class) // Główna klasa aplikacji Spring Boot
                .run(args); // Przekazanie argumentów do aplikacji
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Tworzenie interfejsu użytkownika JavaFX przy użyciu SpringFXMLLoader
        SpringFXMLLoader springFXMLLoader = applicationContext.getBean(SpringFXMLLoader.class);
        // Załaduj FXML i zwróć korzeń (np. AnchorPane)
        Parent root = springFXMLLoader.load(getClass().getResource("/com/example/sportcenterv1/menu.fxml"));

        // Tworzenie sceny z załadowanym korzeniem FXML
        Scene scene = new Scene(root, 1920, 1080);
        stage.setMinWidth(1920);
        stage.setMinHeight(1080);
        stage.setTitle("Centrum Sportowe!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception{
        super.stop();
        if (applicationContext != null) {
            applicationContext.close();
        }
    }
}