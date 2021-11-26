import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameScene extends Scene {
    public Camera camera;                       // camera
    private Group parent;                       // for constructor

    private staticThing left;                   // left background image
    private staticThing right;                  // right background image
    private staticThing frontFirst;
    private staticThing frontSecond;
    private staticThing frontThird;
    private staticThing hearts;                 // lives
    private staticThing pressKey;

    private staticThing gameOver;
    private Image spark;
    private ImageView sparkGif;

    private Hero hero;

    private int numberOfLives;                  // life counter
    private int score;                          // current score
    private Text scoreText;
    private Text hiScore;
    private Text speed;

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
            updateGS(time);
        }
    };





    public GameScene(Group parent, int width, int height){
        super(parent, width, height);
        this.parent=parent;


        numberOfLives = 3;

        score = 0;
        scoreText= new Text();
        scoreText.setX(350);
        scoreText.setText("SCORE  =  "+score);
        scoreText.setY(30);
        scoreText.setFill(Color.DARKVIOLET);
        scoreText.setFont(Font.font("impact", FontWeight.BOLD, FontPosture.REGULAR, 20));

        hiScore= new Text();
        hiScore.setX(600);
        hiScore.setText("HIGH SCORE  =  "+getHighScore());
        hiScore.setY(30);
        hiScore.setFill(Color.BLUEVIOLET);
        hiScore.setFont(Font.font("impact", FontWeight.BOLD, FontPosture.REGULAR, 20));

        left = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\backgroundMMX2.png",400,800);
        right = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\backgroundMMX2.png",400,800);

        frontFirst = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\firstbg4.png",250,400);
        frontSecond = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\firstbg4.png",250,200);
        frontThird = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\firstbg4.png",250,400);

        Image ground = new Image(new File("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\groundgif.gif").toURI().toString());
        ImageView groundGif = new ImageView(ground);


        hearts = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\healthbar2.png", 476, 845 );

        hero = new Hero(200, 300, 100000000, "C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\heros.png" );

        gameOver = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\gameover.png",360,640);
        pressKey = new staticThing("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\presskey3.png",80,293);

        spark = new Image(new File("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\spark.gif").toURI().toString());
        sparkGif = new ImageView(spark);

        speed= new Text();
        speed.setX(400);
        speed.setY(60);
        speed.setFill(Color.DEEPSKYBLUE);
        speed.setFont(Font.font("impact", FontWeight.MEDIUM, FontPosture.ITALIC, 10));
        speed.setVisible(false);

        Random r = new Random();
        createFoes(r);

        parent.getChildren().add(left.getViewBack());
        parent.getChildren().add(right.getViewBack());
        left.getViewBack().setY(-50);
        right.getViewBack().setY(-50);
        parent.getChildren().add(groundGif);
        groundGif.setY(300);
        parent.getChildren().add(frontFirst.getViewBack());
        parent.getChildren().add(frontSecond.getViewBack());
        parent.getChildren().add(frontThird.getViewBack());
        parent.getChildren().add(hero.getAnimatedView());
        parent.getChildren().add(hearts.getViewBack());
        parent.getChildren().add(scoreText);
        parent.getChildren().add(hiScore);
        parent.getChildren().add(speed);
        parent.getChildren().add(gameOver.getViewBack());
        parent.getChildren().add(pressKey.getViewBack());
        parent.getChildren().add(sparkGif);
        sparkGif.setImage(null);

        gameOver.hideImage();
        pressKey.hideImage();

        for (Foe sfoe: foeList)
        {
            parent.getChildren().add(sfoe.getAnimatedView());
        }

        camera = new Camera(0, 50, hero);

        timer.start();

        gamesound();
        bgm.stop();


    }

    public void screen(){
        // for a parallax effect we put different values of offsets.

        double offset1 = (0.1*camera.getX())%left.getWidth();
        left.getViewBack().setViewport(new Rectangle2D(offset1, 0, left.getWidth()-offset1, left.getHeight()));
        right.getViewBack().setX(left.getWidth()-offset1);

        double offset2 = (0.3*camera.getX())%frontFirst.getWidth();
        frontFirst.getViewBack().setViewport(new Rectangle2D(offset2, 0, frontFirst.getWidth()-offset2, frontFirst.getHeight()));
        frontFirst.getViewBack().setY(50);
        frontSecond.getViewBack().setX(frontFirst.getWidth()-offset2); frontSecond.getViewBack().setY(50);
        frontThird.getViewBack().setX(2*frontFirst.getWidth()-offset2); frontThird.getViewBack().setY(50);
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
        updateScore();
        displaySpeed();

        for (Foe ufoe : foeList) {
            ufoe.update(time, camera);
            checkCollision(ufoe);
        }
    }


    public void displaySpeed(){
        if (hero.getSpeedText()>0) {
            if (hero.getSpeedText() % 20 > 10) {
                if(hero.getaX() == 10){
                    speed.setText("MAX GRAVITY !!!!");
                }else{
                    speed.setText("Gravity increased !!! (" + (int) (hero.getaX()) + "/10)");
                }
                speed.setVisible(true);
            } else {
                speed.setVisible(false);
            }
        }
    }

    public void startRunning() {
        if (hero.getAttitude()==0) {
            this.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.ENTER) {
                    hero.run();
                    startsound();
                    if (!bgm.getStatus().equals(MediaPlayer.Status.PLAYING))
                    gamesound();
                }
            });
        }
    }
    public void jump(){
        if (hero.getAttitude()==1) {
            this.setOnKeyPressed(ev -> {
                if (ev.getCode() == KeyCode.SPACE && hero.getAttitude() == 1) {
                    hero.jump();
                    jumpsound();
                }
            });
        }
    }

    public void tryAgain(){
        this.setOnKeyPressed(ev -> {
                hero.tryAgain();
                retrysound();
                timer.start();
                camera.startAgain();
                gameOver.hideImage();
                pressKey.hideImage();
                sparkGif.setImage(null);
                hiScore.setText("HIGH SCORE  =  "+getHighScore());
                numberOfLives=3;
                Random r = new Random();
                min_dist = 300;
                createFoes(r);
                for (Foe sfoe: foeList){
                    parent.getChildren().add(sfoe.getAnimatedView());
                }
        });
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
                hitsound();
                if (numberOfLives == 2) alertsound();
                if (numberOfLives > 1) {
                    hero.setInvincibility(300);
                    numberOfLives--;
                }else{                  // you lose your last life -> GAME OVER
                    numberOfLives--;
                    displayLives();
                    gameOver();
                    if (getHighScore() > score) losesound();
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
                if (camera.getX()%300 < 150) {
                    hearts.showImage();
                    hearts.getViewBack().setViewport(new Rectangle2D(147, 394, 137, 35));
                }else{
                    hearts.hideImage();
                }
                break;
            case 0:            // game over
                hearts.showImage();
                hearts.getViewBack().setViewport(new Rectangle2D(147, 437, 137, 38));
                break;
        }
        hearts.getViewBack().setX(10);
        hearts.getViewBack().setY(10);
    }

    public void updateScore(){
        score=(int)((hero.getX()-200)/10);
        scoreText.setText("SCORE= "+score);
    }

    public int getHighScore() {
        try {
            Scanner scanner = new Scanner(new File("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\high_score.txt"));
            return (scanner.nextInt());
        } catch (IOException e){
            return 0;
        }
    }

    public void writeFile(String filename, int text)
    {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {} //nothing happens (no need to specify it already exists)
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(""+text);
            myWriter.close();
            System.out.println("Updated high score.");
        } catch (IOException e) {
            System.out.println("An error occurred. Couldn't save the score...");
            e.printStackTrace();
        }
    }

    public void gameOver(){
        timer.stop();
        gameOver.getViewBack().setX(80);
        gameOver.showImage();
        pressKey.getViewBack().setX(253);
        pressKey.getViewBack().setY(290);
        pressKey.showImage();

        sparkGif.setX(150); sparkGif.setY(53);
        sparkGif.setImage(spark);

        System.out.println("Game Over");
        if (getHighScore() < score){
            winsound();
            newHiScoresound();
            writeFile("C:\\Users\\rotci\\IdeaProjects\\ProjectRunner\\runner_img\\high_score.txt",score);
        }
        clearFoes();
        tryAgain();
    }

    public void clearFoes(){
        for (Foe sfoe: foeList)
        {
            sfoe.getAnimatedView().setViewport(new Rectangle2D(0,0,1,1));
        }

        foeList = null;
    }

    MediaPlayer bgm;
    public void gamesound(){
        Media h=new Media(Paths.get("runner_music/MiamiNights1984.mp3").toUri().toString());
        bgm= new MediaPlayer(h);
        bgm.setOnEndOfMedia(() -> bgm.seek(Duration.ZERO));
        bgm.play();
    }

    MediaPlayer startsfx;
    public void startsound(){
        Media h = new Media(Paths.get("runner_music/startsfx.wav").toUri().toString());
        startsfx = new MediaPlayer(h);
        startsfx.play();
    }

    MediaPlayer jumpsfx;
    public void jumpsound(){
        Media h = new Media(Paths.get("runner_music/jumpsfx.wav").toUri().toString());
        jumpsfx = new MediaPlayer(h);
        jumpsfx.play();
    }

    MediaPlayer hitsfx;
    public void hitsound(){
        Media h = new Media(Paths.get("runner_music/hitsfx.wav").toUri().toString());
        hitsfx = new MediaPlayer(h);
        hitsfx.play();
    }

    MediaPlayer alertsfx;
    public void alertsound(){
        Media h = new Media(Paths.get("runner_music/alertsfx.wav").toUri().toString());
        alertsfx = new MediaPlayer(h);
        alertsfx.play();
    }

    MediaPlayer losesfx;
    public void losesound(){
        Media h = new Media(Paths.get("runner_music/losesfx.wav").toUri().toString());
        losesfx = new MediaPlayer(h);
        losesfx.play();
    }

    MediaPlayer retrysfx;
    public void retrysound(){
        Media h = new Media(Paths.get("runner_music/retrysfx.wav").toUri().toString());
        retrysfx = new MediaPlayer(h);
        retrysfx.play();
    }

    MediaPlayer newHiScoresfx;
    public void newHiScoresound(){
        Media h = new Media(Paths.get("runner_music/newHiScoresfx.mp3").toUri().toString());
        newHiScoresfx = new MediaPlayer(h);
        newHiScoresfx.play();
    }

    MediaPlayer winsfx;
    public void winsound(){
        Media h = new Media(Paths.get("runner_music/winsfx.wav").toUri().toString());
        winsfx = new MediaPlayer(h);
        winsfx.play();
    }

}
