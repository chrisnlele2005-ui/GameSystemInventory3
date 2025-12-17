import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(Main.class.getResource("/database/resources/inventory.fxml"));

            Scene scene = new Scene(loader.load());

            stage.setTitle("Game Inventory System");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            System.out.println("Fejl ved indlæsning af JavaFX:");
            e.printStackTrace();
        }
    }
}