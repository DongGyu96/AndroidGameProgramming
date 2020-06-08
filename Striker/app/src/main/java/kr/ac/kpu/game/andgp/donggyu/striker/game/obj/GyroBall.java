package kr.ac.kpu.game.andgp.donggyu.striker.game.obj;

import android.util.Log;

import kr.ac.kpu.game.andgp.donggyu.striker.framework.input.sensor.GyroSensor;

public class GyroBall extends Ball {
    private static final int DEGREE_TO_SPEED_MULTIPLIER = 50;
    public GyroBall(float x, float y) {
        super(x, y, 0, 0);
    }

    @Override
    public void update() {
        GyroSensor gyro = GyroSensor.get();
        this.dx = gyro.getPitchDegree() * DEGREE_TO_SPEED_MULTIPLIER;
        this.dy = gyro.getRollDegree() * DEGREE_TO_SPEED_MULTIPLIER;
        Log.d("GyroBall", dx + ", " + dy);
        super.update();
    }
}
