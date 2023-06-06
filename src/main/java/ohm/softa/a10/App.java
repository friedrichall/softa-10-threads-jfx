package ohm.softa.a10;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
    	launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// note: var is the dynamic type; specify template argument b/c compiler can't infer it!
    	var root = FXMLLoader.<Parent>load(getClass().getResource("views/main.fxml"));
		//FXMLLoader loader = new FXMLLoader(getClass().getResource("views/main.fxml"));
		//Parent root = loader.load();
    	primaryStage.setTitle("THRO Diner");
    	primaryStage.setScene(new Scene(root, 800, 600));
    	primaryStage.show();
	}
}
