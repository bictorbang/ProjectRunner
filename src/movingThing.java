import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class movingThing {
    private double x;
    private double y;
    private Rectangle2D hitbox;
    private ImageView animatedView;

    private Integer[] indexX = new Integer[10];
    private Integer[] indexW = new Integer[10];
    private Integer[] indexY = new Integer[10];
    private Integer[] indexH = new Integer[10];
    private int index;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public ImageView getAnimatedView() {
        return animatedView;
    }

    public movingThing(double x, double y, String filename){
        this.index = 0;
        this.x = x;
        this.y = y;
        this.animatedView =new ImageView(new Image(filename));

        setXindexes();
        setYindexes();
        setHindexes();
        setWindexes();

        animatedView.setViewport(new Rectangle2D(indexX[index],indexY[index],indexW[index],indexH[index]));

    }

    // C'est une manière très dégueulasse de faire mais j'ai pas trouvé mieux...

    void setXindexes(){
        indexX[0] = 12;
        indexX[1] = 113;
        indexX[2] = 226;
        indexX[3] = 328;
        indexX[4] = 422;
        indexX[5] = 9;
        indexX[6] = 113;
        indexX[7] = 224;
        indexX[8] = 328;
        indexX[9] = 427;

    }

    void setYindexes(){
        indexY[0] = 50;
        indexY[1] = 50;
        indexY[2] = 48;
        indexY[3] = 48;
        indexY[4] = 50;
        indexY[5] = 152;
        indexY[6] = 150;
        indexY[7] = 150;
        indexY[8] = 150;
        indexY[9] = 152;
    }

    void setWindexes(){
        indexW[0] = 78;
        indexW[1] = 74;
        indexW[2] = 53;
        indexW[3] = 46;
        indexW[4] = 68;
        indexW[5] = 79;
        indexW[6] = 70;
        indexW[7] = 58;
        indexW[8] = 51;
        indexW[9] = 60;
    }

    void setHindexes(){
        indexH[0] = 77;
        indexH[1] = 79;
        indexH[2] = 81;
        indexH[3] = 79;
        indexH[4] = 77;
        indexH[5] = 228-152;
        indexH[6] = 228-150;
        indexH[7] = 230-150;
        indexH[8] = 228-150;
        indexH[9] = 228-152;
    }

    void update(long time, Camera camera){
        index= (int) (((time/100000000))%(9));
        animatedView.setViewport(new Rectangle2D(indexX[index],indexY[index],indexW[index],indexH[index]));
        getAnimatedView().setX(x-camera.getX());
        getAnimatedView().setY(y-camera.getY());
        //System.out.println("x="+(x-camera.getX())+" y="+(y-camera.getY()) );
        this.hitbox = getHitBox();
    }

    public Rectangle2D getHitBox(){
        //return (new Rectangle2D(x,y,animatedView.getImage().getWidth(),animatedView.getImage().getHeight()));
        return (new Rectangle2D(this.animatedView.getX(),this.animatedView.getY(),this.indexW[index],this.indexH[index]));
    }
    public double getWidth(){
        return animatedView.getImage().getWidth();
    }
}
