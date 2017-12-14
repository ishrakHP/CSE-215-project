package Main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class GameMain extends Application {
	final static int WIDTH = 1280;
	final static int HEIGTH = 640;

	final static Image BACKGROUND = new Image(GameMain.class.getResource("background.png").toString());
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {final ImageView background = new ImageView(BACKGROUND);
		final Group root = new Group(background);
		primaryStage.setTitle("Game");
		primaryStage.setScene(new Scene(root, WIDTH, HEIGTH));
		primaryStage.show();
	}
}
