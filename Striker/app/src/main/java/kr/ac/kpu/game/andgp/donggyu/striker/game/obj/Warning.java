package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import kr.ac.kpu.game.andgp.donggyu.striker.R;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.iface.Recyclable;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameScene;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.GameTimer;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.RecyclePool;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.main.UIBridge;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.obj.AnimObject;
import kr.ac.kpu.game.andgp.donggyu.striker.framework.res.sound.SoundEffects;
import kr.ac.kpu.game.andgp.donggyu.striker.game.scene.SecondScene;

public class Warning extends AnimObject {
    private static final float MAX_REMAIN_TIME = 4.f;
    private final int boss;
    private float remainTime;

    public Warning(float x, float y, int width, int height, int boss) {
        super(x, y, width, height, R.mipmap.warning,0, 1);
        this.hp = 1;
        this.boss = boss;
        this.remainTime = MAX_REMAIN_TIME;
        if(boss == 0) {
            SoundEffects.get().play(R.raw.nuclear, 1.f, 1.f, 3, 0, 1.f);
        }
        else {
            SoundEffects.get().play(R.raw.boss_intro, 1.f, 1.f, 3, 0, 1.f);
        }
    }

    @Override
    public void update() {
        float seconds = GameTimer.getTimeDiffSeconds();
        remainTime -= seconds;
        if(remainTime < 0.f) {
            remove();
            if(boss == 0) {
                GameObject obj = new Boss_Bomber(UIBridge.metrics.size.x / 2, -100, 0.f, 300.f);
                SecondScene.get().getGameWorld().add(SecondScene.Layer.boss.ordinal(), obj);
            }
            else {
                GameObject obj = new Boss_UFO(UIBridge.metrics.size.x / 2, -100, 0.f, 300.f);
                SecondScene.get().getGameWorld().add(SecondScene.Layer.boss.ordinal(), obj);
            }
        }
    }
}
