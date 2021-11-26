public class Camera {

    private double x;
    private double y;
    private double vx;
    private double vy; // not needed ?
    private double ax;
    private double ay;


    private final double k=0.00001;
    private final double f=0.002;

    private Hero focus;

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public Camera(int x, int y, Hero focus){
        this.x = x;
        this.y = y;
        this.focus = focus;
        this.vx=0;
        this.vy=0;
        this.ay=0;
        this.ax=0;
    }

    @Override
    public String toString() { return x + " , " + y; }


    public void startAgain(){
        x=200;
    }


    public void update(double time){
        ax=(focus.getX()-x)*k-(f*vx);
        vx=vx+ax*time;
        x=x+vx*time;

    }

}
