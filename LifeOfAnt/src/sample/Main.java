package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Main extends Application {

    final static int WIDTH = 1200;
    final static int HEIGHT = 640;

    final static Image BACKGROUND_IMAGE = new Image(Main.class.getResource("background.png").toString());


    final static Image Tree_1_IMAGE = new Image(Main.class.getResource("Tree.png").toString());
    final static Image Tree_2_IMAGE = new Image(Main.class.getResource("Tree.png").toString());
    final static Image Tree_3_IMAGE = new Image(Main.class.getResource("Tree.png").toString());
    final static Image Tree_4_IMAGE = new Image(Main.class.getResource("Tree.png").toString());

    final static Image Ant1_IMAGE = new Image(Main.class.getResource("Ant.png").toString());
    final static Image Ant2_IMAGE = new Image(Main.class.getResource("Ant.png").toString());

    private final static Random RANDOM = new Random();

    private Group ant;
    private Animation current;

    private IntegerProperty hitCounter = new SimpleIntegerProperty(this,"hitCounter");
    private IntegerProperty shotCounter = new SimpleIntegerProperty(this,"shotCounter");

    boolean gameOver = false;

	public static void main(String[] args) {
		launch(args);
	}

    @Override
    public void start(Stage primaryStage) throws Exception{

        final ImageView tree1 = new ImageView(Tree_1_IMAGE);
        tree1.setTranslateX(50);
        tree1.setTranslateY(300);

        final ImageView tree2 = new ImageView(Tree_2_IMAGE);
        tree2.setTranslateX(360);
        tree2.setTranslateY(350);

        final ImageView tree3 = new ImageView(Tree_3_IMAGE);
        tree3.setTranslateX(660);
        tree3.setTranslateY(300);

        final ImageView tree4 = new ImageView(Tree_4_IMAGE);
        tree4.setTranslateX(970);
        tree4.setTranslateY(350);


        final Group foreground = new Group(tree1,tree2,tree3, tree4);

        foreground.setEffect(new DropShadow());

        final ImageView Ant1 = new ImageView(Ant1_IMAGE);
        final ImageView Ant2 = new ImageView(Ant2_IMAGE);

        ant = new Group(Ant1,Ant2);

        final ImageView background = new ImageView(BACKGROUND_IMAGE);

        background.setEffect(new BoxBlur());
        background.setOpacity(0.9);

        TimelineBuilder.create()
                .cycleCount(Animation.INDEFINITE)
                .keyFrames(
                        new KeyFrame(Duration.millis(150), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                ant.getChildren().setAll(Ant2);
                            }
                        }),
                        new KeyFrame(Duration.millis(300), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                ant.getChildren().setAll(Ant1);
                            }
                        })
                )
                .build().play();

        final Animation hitAnimation = SequentialTransitionBuilder.create()
                .node(ant)
                .children(
                        RotateTransitionBuilder.create()
                                .fromAngle(0)
                                .toAngle(1260)
                                .duration(Duration.millis(800))
                                .build(),
                        TranslateTransitionBuilder.create()
                                .byY(1000)
                                .duration(Duration.millis(800))
                                .build()

                )
                .onFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        startAnimation();
                    }
                })
                .build();

        ant.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                current.stop();
                hitAnimation.play();
                hitCounter.set(hitCounter.get()+1);
            }
        });


        final Text hitLabel = new Text();
        hitLabel.textProperty().bind(Bindings.concat("Hits: ",hitCounter));
        final Text shotLabel = new Text();
        shotLabel.textProperty().bind(Bindings.concat("Shots: ",shotCounter));
        final Text missLabel = new Text();
        missLabel.textProperty().bind(Bindings.concat("Misses: ",shotCounter.subtract(hitCounter)));

        final VBox hud = new VBox(hitLabel,shotLabel,missLabel);
        hud.setTranslateX(40);
        hud.setTranslateY(40);

        final Group root = new Group(background,ant,foreground,hud);

        Scene scene = new Scene(root,WIDTH,HEIGHT);

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                shotCounter.set(shotCounter.get()+1);
            }
        });

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        startAnimation();

    }


    private void startAnimation(){
        if(current != null){
            current.stop();
        }
        final  int y0 = RANDOM.nextInt(HEIGHT/2)+HEIGHT/4;
        final  int y1 = RANDOM.nextInt(WIDTH/2)+WIDTH/4;

        ant.setRotate(0);

        current = TranslateTransitionBuilder.create()
                .node(ant)
                .fromX(-100)
                .toX(WIDTH)
                .fromY(y0)
                .toY(y1)
                .duration(Duration.seconds(3))
                .onFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        startAnimation();
                    }
                }).build();
        current.play();

        if((shotCounter.getValue() - hitCounter.getValue()) > 10){
        	System.exit(1);
        }
    }
}
