package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.graphics.RectF;

import java.util.ArrayList;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.BoxCollidable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.util.CollisionHelper;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Bomb extends AnimObject implements Recyclable, BoxCollidable {
    private boolean collider;

    public Bomb(float x, float y, int width, int height, boolean collider) {
        super(x, y, width, height, R.mipmap.bomb_effect, 12, 14);
        this.hp = 1;
        this.collider = collider;
    }
    public static Bomb get(float x, float y, int width, int height, boolean collider) {
        RecyclePool rpool = GameScene.getTop().getGameWorld().getRecyclePool();

        Bomb explosion = (Bomb) rpool.get(Bomb.class);
        if (explosion == null) {
            explosion = new Bomb(x, y, width, height, collider);
        } else {
            explosion.x = x;
            explosion.y = y;
            explosion.width = width;
            explosion.height = height;
            explosion.fab.setBitmapResource(R.mipmap.bomb_effect);
            explosion.fab.reset();
            explosion.collider = collider;
        }
        return explosion;
    }

    @Override
    public void update() {
        if(collider)
        {
            checkCollision();
        }
        if(fab.done()) {
            remove();
        }
    }

    private void checkCollision() {
        ArrayList<GameObject> Enemys = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.enemy.ordinal());
        for (GameObject obj : Enemys) {
            if (obj instanceof Helicopter) {
                Helicopter enemy = (Helicopter) obj;
                if (CollisionHelper.collides(this, enemy)) {
                    enemy.Damage(5);
                }
            }
            else if (obj instanceof SmallPlane) {
                SmallPlane enemy = (SmallPlane) obj;
                if (CollisionHelper.collides(this, enemy)) {
                    enemy.Damage(5);
                }
            }
            else if (obj instanceof MediumPlane) {
                MediumPlane enemy = (MediumPlane) obj;
                if (CollisionHelper.collides(this, enemy)) {
                    enemy.Damage(5);
                }
            }
            else {
                continue;
            }
        }
        ArrayList<GameObject> bullets = SecondScene.get().getGameWorld().objectsAtLayer(SecondScene.Layer.enemy_bullet.ordinal());
        for (GameObject obj : bullets) {
            if (!(obj instanceof Bullet)) {
                continue;
            }
            Bullet bullet = (Bullet) obj;
            if (CollisionHelper.collides(this, bullet)) {
                bullet.remove();
                SecondScene.get().getGameWorld().add(SecondScene.Layer.bullet.ordinal(), Explosion.get(bullet.getX(), bullet.getY(), 40, 40));
            }
        }
    }

    @Override
    public void recycle() {

    }

    @Override
    public void getBox(RectF rect) {
        int width = UIBridge.x(fab.getWidth()) / 2;
        int height = UIBridge.y(fab.getHeight()) / 2;

        int hw = width / 2;
        int hh = height / 2;
        rect.left = x - hw;
        rect.top = y - hh;
        rect.right = x + hw;
        rect.bottom = y + hh;
    }
}
