import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Paths;

public class HomeScene extends Scene {

    private staticThing title;
    private staticThing text;
   // private staticThing instructions;
    private ImageView backGif;
    private int index;


    AnimationTimer timer = new AnimationTimer()
    {
        @Override
        public void handle(long time){
            updateHS(time);
        }
    };

    public HomeScene(Group parent){
        super(parent,800,400);

        Image back = new Image(new File("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\sunsetgif.gif").toURI().toString());
        backGif = new ImageView(back);

        title = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\title.png",400,800);
        title.getViewBack().setX(190); title.getViewBack().setY(60);

        text = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\pressbar2.png",51,600);
        text.getViewBack().setX(100); text.getViewBack().setY(340);

      //  instructions = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\instructions2.jpg",51,600);


        //Button instButton=newButton(700,0,50,50,"", instructions.getViewBack());

        parent.getChildren().add(backGif);
        parent.getChildren().add(title.getViewBack());
        parent.getChildren().add(text.getViewBack());
       // parent.getChildren().add(instButton);

        /*
        instButton.setOnAction(e -> {
            clicksound();
            displayInstructions();
        });
         */

        homesound();
        timer.start();
    }

    public void updateHS(long time){
        index = (int)(time/1000000000L);
        if (index%2 < 1){
            text.hideImage();
        }else{
            text.showImage();
        }
    }

    MediaPlayer bgm;
    public void homesound(){
        Media h=new Media(Paths.get("runner_music/HOME.mp3").toUri().toString());
        bgm= new MediaPlayer(h);
        bgm.setOnEndOfMedia(() -> bgm.seek(Duration.ZERO));
        bgm.play();
    }

    public void stopBGM(){
        bgm.stop();
    }

    MediaPlayer clicksfx;
    public void clicksound(){
        Media h=new Media(Paths.get("runner_music/clicksfx.wav").toUri().toString());
        clicksfx= new MediaPlayer(h);
        clicksfx.play();
    }

    /*
    public Button newButton(double x, double y, double w, double h, String f, ImageView image)
    {
        Button b = new Button(f, image);
        b.setFont(Font.font("Impact", FontWeight.MEDIUM,20));
        b.setStyle("-fx-background-color: #281d61; -fx-border-color: #fec8ff;-fx-text-fill: #ffffff;");
        b.setLayoutX(x);
        b.setLayoutY(y);
        b.setPrefWidth(w);
        b.setPrefHeight(h);
        return b;
    }

    public void displayInstructions(){

    }
    */
}
