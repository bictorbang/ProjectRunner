import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception{

            primaryStage.setTitle("RUNNER 2077 by Victor WANG");
            Group rootGame = new Group();
            Group rootHome = new Group();

            GameScene gs = new GameScene(rootGame,800,400);
            HomeScene hs = new HomeScene(rootHome);

            primaryStage.setScene(hs);

            hs.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.SPACE) {
                    hs.clicksound();
                    hs.stopBGM();
                    hs.timer.stop();
                    primaryStage.setScene(gs);
                }
            });


            primaryStage.setResizable(false);
            primaryStage.show();



        }

        public static void main(String[] args) {


            launch(args);
        }
    }
