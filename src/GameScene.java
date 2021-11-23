import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameScene extends Scene {
    public Camera camera;                       // camera
    private Group parent;                       // for constructor
    private staticThing left;                   // left background image
    private staticThing right;                  // right background image
    private staticThing hearts;                 // lives

    private Hero hero;

    private Button retryB;                      // to start over a run
    private Button homeB;                       // to exit the run

    private int numberOfLives;                  // life counter
    private int score;                          // current score

    private long lastTime=0;

    private ArrayList<Foe> foeList=null;
    private int min_dist=1000;
    private int numberOfFoes;


    public int getScore() {
        return score;
    }


    AnimationTimer timer = new AnimationTimer()
    {
        @Override
        public void handle(long time){
            double elapsedTime=(double) (time-lastTime)/1000000;
            if (elapsedTime>100) elapsedTime=0;

            hero.update(time,elapsedTime);

            camera.update(elapsedTime);
            lastTime=time;
            //render(time);
            updateGS(time);

            System.out.println(hero.getInvincibility());
            System.out.println(hero.isInvincible());
        }
    };





    public GameScene(Group parent, int width, int height){
        super(parent, width, height);
        this.parent=parent;


        numberOfLives = 3;


        left = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\desert.png",400,800);
        right = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\desert.png",400,800);
        hearts = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\healthbar2.png", 476, 845 );

        hero = new Hero(200, 300, 100000000, "C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\heros.png" );

        Random r = new Random();
        createFoes(r);

        parent.getChildren().add(left.getViewBack());
        parent.getChildren().add(right.getViewBack());
        parent.getChildren().add(hero.getAnimatedView());
        parent.getChildren().add(hearts.getViewBack());

        for (Foe sfoe: foeList)
        {
            parent.getChildren().add(sfoe.getAnimatedView());
        }

        camera = new Camera(0, 50, hero);

        timer.start();


    }

    public void screen(){
        double offset = (0.8*camera.getX())%left.getWidth();
        left.getViewBack().setViewport(new Rectangle2D(offset, 0, left.getWidth()-offset, left.getHeight()));
        right.getViewBack().setX(left.getWidth()-offset);
    }


    public void followHero(){
        hero.getAnimatedView().setX(hero.getX()-camera.getX());
        hero.getAnimatedView().setY(hero.getY()-camera.getY());
    }

    public void updateGS(long time){
        screen();
        displayLives();
        startRunning();
        jump();
        followHero();

        for (Foe ufoe : foeList) {
            ufoe.update(time, camera);
            checkCollision(ufoe);
        }
    }

    public void startRunning()
    {
        if (hero.getAttitude()==0) {
            this.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.ENTER) {
                    //System.out.println("RUN");
                    hero.run();

                }
            });
        }
    }
    public void jump(){
        if (hero.getAttitude()==1) {
            this.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.SPACE) {
                    System.out.println("JUMP");
                    hero.jump();
                }
            });
        }
    }

    public void createFoes(Random r){
        foeList=new ArrayList<Foe>();
        numberOfFoes= r.nextInt(200) + 100;
        for (int i=0;i<numberOfFoes;i++) {
            min_dist += r.nextInt(1000) + 800;
            Foe newFoe = new Foe(min_dist, 320, "C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\megaman2.png");
            foeList.add(newFoe);
        }
    }

    public void checkCollision(Foe foe){
        if (!hero.isInvincible()){        // only check collision when hero is vulnerable
            if (hero.getHitBox().intersects(foe.getHitBox())) {
                if (numberOfLives > 0) {
                    hero.setInvincibility(200);
                    numberOfLives--;
                }else{                  // you lose your last life -> GAME OVER
                    gameOver();
                }
            }
        }
    }

    public void displayLives(){
        switch(numberOfLives) {
            case 3:
                hearts.getViewBack().setViewport(new Rectangle2D(147, 310, 137, 35));
                break;
            case 2:
                hearts.getViewBack().setViewport(new Rectangle2D(147, 352, 137, 35));
                break;
            case 1:
                hearts.getViewBack().setViewport(new Rectangle2D(147, 394, 137, 35));
                break;
            case 0:            // game over
                hearts.getViewBack().setViewport(new Rectangle2D(147, 437, 137, 38));
                break;
        }
        hearts.getViewBack().setX(10);
        hearts.getViewBack().setY(10);
    }

    public void writeFile(String filename, int text)
    {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(""+text);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void gameOver(){
        timer.stop();
        System.out.println("Game Over");
        writeFile("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\high_score.txt",score);
    }
}
