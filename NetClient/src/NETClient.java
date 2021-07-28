import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**@author KR Tambo
 * network project
 * 
 * */


public class NETClient extends Application{

	public static void main(String[] args) {
		
		launch(args);
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		NETClientPane myPane = new NETClientPane();
		
		Scene scene = new Scene(myPane,1080,600);
		primaryStage.setTitle("NETCLIENT");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

}
