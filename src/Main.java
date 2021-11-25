import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception{

            primaryStage.setTitle("RUNNER 2077");
            Group rootGame = new Group();
            Group rootHome = new Group();
            //root.getChildren().add(sprite);
           // Pane pane = new Pane(root);
            //Scene theScene = new Scene(pane, 600, 400,true);
           // primaryStage.setScene(theScene);

            GameScene gs = new GameScene(rootGame,800,400);
            HomeScene hs = new HomeScene(rootHome);

            primaryStage.setScene(hs);

            hs.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.SPACE) {
                    primaryStage.setScene(gs);
                }
            });

            //primaryStage.getScene().setOnKeyPressed(ev -> {

            //        });


            primaryStage.setResizable(false);
            primaryStage.show();



        }

        public static void main(String[] args) {


            launch(args);
        }
    }
