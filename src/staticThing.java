import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class staticThing {
    private double height;
    private double width;
    private Image staticImage; // not needed ?
    private ImageView viewBack;

    public staticThing(String fileName, double height, double width){
        this.staticImage = new Image(fileName);
        this.height=height;
        this.width=width;
        this.viewBack=new ImageView(staticImage);
    }

    public ImageView getViewBack() { return viewBack; }

    public double getHeight() { return height; }

    public double getWidth() { return width; }

}