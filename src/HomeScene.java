import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class HomeScene extends Scene {

    private staticThing title;
    private staticThing text;
    private ImageView backGif;

    public HomeScene(Group parent){
        super(parent,800,400);

        Image back = new Image(new File("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\sunsetgif.gif").toURI().toString());
        backGif = new ImageView(back);

        title = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\title.png",400,800);
        title.getViewBack().setX(190); title.getViewBack().setY(60);

        text = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\pressbar2.png",51,600);
        text.getViewBack().setX(100); text.getViewBack().setY(340);


        parent.getChildren().add(backGif);
        parent.getChildren().add(title.getViewBack());
        parent.getChildren().add(text.getViewBack());
    }
}
