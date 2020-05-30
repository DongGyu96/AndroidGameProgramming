package kr.ac.kpu.game.andgp.donggyu.blocksample.game.world;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

import kr.ac.kpu.game.andgp.donggyu.blocksample.R;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.framework.GameWorld;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.iface.GameObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.Ball;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.EnemyGenerator;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.Fighter;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.Joystick;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.Plane;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.ScoreObject;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.bg.ImageScrollBackground;
import kr.ac.kpu.game.andgp.donggyu.blocksample.game.obj.bg.TileScrollBackground;

public class MainWorld extends GameWorld {
    private static final int BALL_COUNT = 10;
    public static final String PREFS_NAME = "Prefs";
    public static final String PREF_KEY_HIGHSCORE = "highscore";
    private Fighter fighter;
    private EnemyGenerator enemyGenerator = new EnemyGenerator();
    private Plane plane;
    private ScoreObject scoreObject;
    private ScoreObject highscoreObject;
    private PlayState playState = PlayState.Normal;
    private Joystick joystick;

    public static void create() {
        if(singleton != null) {
            return;
        }
        singleton = new MainWorld();
    }

    public static MainWorld get() {
        return (MainWorld) singleton;
    }

    private enum PlayState {
        Normal, Paused, GameOver
    }

    // java는 enum이 정수와 호환 x
    public enum Layer {
        BackGround,
        Missile,
        Enemy,
        Player,
        UI,
        END
    }

    @Override
    protected void initObjects() {
        Resources res = view.getResources();
        Random random = new Random();
        for(int i = 0; i < BALL_COUNT; ++i)
        {
            float x = random.nextFloat() * 1000;
            float y = random.nextFloat() * 1000;
            float dx = random.nextFloat() * 50.0f - 25.0f;
            float dy = random.nextFloat() * 50.0f - 25.0f;
            add(Layer.Missile, new Ball(x, y, dx, dy));
        }
        float playerY = rect.bottom - 100;
        plane = new Plane(500, playerY, 0.0f, 0.0f);
        add(Layer.Player, plane);
        fighter = new Fighter(200, 700);
        add(Layer.Player, fighter);

//        scorePaint.setTextSize(100);
//        scorePaint.setColor(Color.BLACK);
//        scoreAnimator = ObjectAnimator.ofInt(this, "scoreDisplay", 0);
        scoreObject = new ScoreObject(800, 100, R.mipmap.number_64x84);

        highscoreObject = new ScoreObject(800, 20, R.mipmap.number_24x32);
        add(Layer.UI, scoreObject);

//        add(Layer.BackGround, new ImageScrollBackground(R.mipmap.bg_city,
//                ImageScrollBackground.Orientation.vertical, 25));

        add(Layer.BackGround, new TileScrollBackground(R.raw.earth,
                TileScrollBackground.Orientation.vertical, 25));

        add(Layer.BackGround, new ImageScrollBackground(R.mipmap.clouds,
                ImageScrollBackground.Orientation.vertical, 100));

        joystick = new Joystick(300, rect.bottom - 400, Joystick.Direction.normal, 100);
        add(Layer.UI, joystick);

        plane.setJoystick(joystick);

        startGame();
    }

    @Override
    protected int getLayerCount() {
        return Layer.END.ordinal();
    }

    private void startGame() {
        playState = PlayState.Normal;
        scoreObject.reset();

        SharedPreferences prefs = view.getContext().getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        int highscore = prefs.getInt(PREF_KEY_HIGHSCORE, 0);
        highscoreObject.setScore(highscore);
    }

    public void endGame() {

        playState = PlayState.GameOver;
        int score = scoreObject.getScore();

        SharedPreferences prefs = view.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int highscore = prefs.getInt(PREF_KEY_HIGHSCORE, 0);
        if(score > highscore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(PREF_KEY_HIGHSCORE, score);
            editor.commit();
        }
    }

    public ArrayList<GameObject> objectsAtLayer(Layer layer) {
        return layers.get(layer.ordinal());
    }

    public void add(Layer layer, final GameObject object) {
        add(layer.ordinal(), object);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        joystick.onTouchEvent(event);
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN) {
            if(playState == PlayState.GameOver) {
                startGame();
                return false;
            }
            doAction();
            //plane.head(event.getX(), event.getY());
        }
        else if(action == MotionEvent.ACTION_MOVE) {
            //plane.head(event.getX(), event.getY());
        }
        return true;
    }

    @Override
    public void addScore(int score) {
        scoreObject.addScore(score);
    }

    @Override
    public void update(long frameTimeNanos) {
        super.update(frameTimeNanos);

        if(playState != PlayState.Normal) {
            return;
        }

        enemyGenerator.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void doAction() {
        fighter.fire();
    }
}
