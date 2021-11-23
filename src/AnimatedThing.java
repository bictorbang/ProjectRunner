import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

abstract public class AnimatedThing {
    double x,y;                     // position
    private Integer attitude;       // 0 = stop, 1 = running, 2 = jumping

    private long invincibility = 0;

    //Image imInv;
    private ImageView animatedView;

    //private ImageView invincible; // invincible png

    private int index;              // index
    private double dt;              // duration between two frames
    private int indexMax;           // max index
    private int invIndex;

    private double vy=0;
    private double ay=0;
    private double vx=0;
    private double ax=0;
    private double g=0.0027;
    private final double f=0.002;

    // I need all those arrays for stuff to work accurately. It is VERY awful though.
    // I gave up the offset method because the sprites were way off the hitboxes, resulting in
    // bad gameplay. This is a consequence of sprites not being the same size, and modifying
    // the offset couldn't get over the issue while keeping constant window size.
    private Rectangle2D hitbox;
    private Integer[] indexX = new Integer[6];
    private Integer[] indexW = new Integer[6];
    private Integer[] indexY = new Integer[6];
    private Integer[] indexH = new Integer[6];

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getAttitude() {
        return attitude;
    }

    public ImageView getAnimatedView() {
        return animatedView;
    }

    public long getInvincibility() {
        return invincibility;
    }

    public void setInvincibility(long value){
        this.invincibility = value;
    }

    public boolean isInvincible(){
        if (invincibility > 0) return true;
        else return false;
    }

/*
    public ImageView getInvincible() {
        return invincible;
    }
     */

    public AnimatedThing(double x, double y, double dt, String filename/*,String filename2*/){
        this.x = x;
        this.y = y;
        this.animatedView =new ImageView(new Image(filename));
        //imInv=new Image(filename2);
        //this.invincible= new ImageView(imInv);
        attitude=0; //still
        index=0;
        invIndex=0;
        this.dt = dt;
        indexMax=5;

        setXindexes();
        setYindexes();
        setHindexes();
        setWindexes();

        animatedView.setViewport(new Rectangle2D(indexX[index],indexY[index],indexW[index],indexH[index]));


    }


    void update(long time, double elapsedTime){

        if (attitude!=0) {
            if (attitude == 1) {
                index = (int) (((time / dt)) % (indexMax - 1));
                animatedView.setViewport(new Rectangle2D(indexX[index],indexY[index],indexW[index],indexH[index]));
            }
            if (x > 4000) {
                this.ax = (int) (x / 4000);
            }
            this.vx = 0.5 + ax / 100; //50
            this.x += this.vx * elapsedTime;
            g = 2 * 150 / (Math.pow(200 / vx, 2)); //300
        }

        if (attitude==2) {
            if (vy * elapsedTime > 0){
                animatedView.setViewport(new Rectangle2D(25,164,45,96));
            }else{
                animatedView.setViewport(new Rectangle2D(96,164,61,91));
            }
            vy += g * elapsedTime;
            y += vy * elapsedTime;
        }
        if ((y>=300)&(attitude==2))
        {
            y=300;
            vy=0;
            attitude=1;
        }

        this.hitbox = getHitBox();

        if (this.isInvincible()) this.setInvincibility(this.getInvincibility()-1);


    }

    public void stop()
    {
        this.attitude=0;
    }

    public void run() {
        this.attitude=1;
    }

    public void jump() {
        if (this.attitude != 2) {
            this.attitude = 2;
            //sound();
            if (y == 300) this.vy = -Math.sqrt(2 * 150 * this.g);
            vy = -0.75;
        }
    }

    void setXindexes(){
        indexX[0] = 22;
        indexX[1] = 96;
        indexX[2] = 174;
        indexX[3] = 274;
        indexX[4] = 350;
        indexX[5] = 428;
    }

    void setYindexes(){
        indexY[0] = 14;
        indexY[1] = 4;
        indexY[2] = 16;
        indexY[3] = 14;
        indexY[4] = 5;
        indexY[5] = 18;
    }

    void setWindexes(){
        indexW[0] = 52;
        indexW[1] = 64;
        indexW[2] = 75;
        indexW[3] = 54;
        indexW[4] = 60;
        indexW[5] = 71;
    }

    void setHindexes(){
        indexH[0] = 83;
        indexH[1] = 93;
        indexH[2] = 81;
        indexH[3] = 83;
        indexH[4] = 92;
        indexH[5] = 79;
    }

    public Rectangle2D getHitBox(){
        if (attitude == 1){
            return (new Rectangle2D(this.getAnimatedView().getX(),this.getAnimatedView().getY(),indexW[index],indexH[index]));
        }else{  // hero is jumping
            return (new Rectangle2D(this.getAnimatedView().getX(),this.getAnimatedView().getY(),45,96));
        }
    }

}
