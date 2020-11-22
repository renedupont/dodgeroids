package android.games.datamanagers;

import android.app.Activity;
import android.content.Context;
import android.games.graphics.Vector;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class GameSensorManager implements SensorEventListener { // ,
    // KeyEventListener
    // { // TODO:
    // KeyEventListener
    // ist tempor�r

    private static GameSensorManager instance;
    private final float[] orientation = new float[3];
    private final float[] adjustedSensorValues = new float[3];
    private final float R[] = new float[9];
    private final float I[] = new float[9];
    private SensorManager m_sensorManager;
    private float[] calibratedOrientation = new float[3];
    private float[] latestAccel = new float[3];
    private float[] latestMagnetic = new float[3];
    private boolean isSensorCalibrated = false;

    private GameSensorManager() {
    }

    private GameSensorManager(final Activity activity) {
        m_sensorManager = (SensorManager) activity
                .getSystemService(Context.SENSOR_SERVICE);
        register();

        // F�r Tastatur wieder Interface implementieren...
        // KeyInputManager.getInstance().addListener(this); // TODO: TEMPOR�R
    }

    public static void init(final Activity activity) {
        instance = new GameSensorManager(activity);
    }

    public static synchronized GameSensorManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("Use init() first!");
        }
        return instance;
    }

    public void register() {
        m_sensorManager.registerListener(this,
                m_sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        m_sensorManager.registerListener(this,
                m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregister() {
        m_sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(final Sensor arg0, final int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        if (isSensorCalibrated) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                latestAccel = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                latestMagnetic = event.values.clone();
            }
            if (latestAccel != null && latestMagnetic != null) {
                computeOrientation();
                adjustedSensorValues[0] = orientation[0]
                        - calibratedOrientation[0];
                adjustedSensorValues[1] = orientation[1]
                        - calibratedOrientation[1];
                adjustedSensorValues[2] = orientation[2]
                        - calibratedOrientation[2];
            }
        } else {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                latestAccel = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                latestMagnetic = event.values.clone();
            }
        }
    }

    private void computeOrientation() {
        if (SensorManager.getRotationMatrix(R, I,
                latestAccel, latestMagnetic)) {
            SensorManager.getOrientation(R, orientation);
        }
    }

    public void calibrate() {
        if (!isSensorCalibrated) {
            isSensorCalibrated = true;
            computeOrientation();
            calibratedOrientation = orientation.clone();// latestAccel.clone();//accelerometerValues.clone();
        }
    }

    // TODO folgende implementierung versucht ohne compass auszukommen, ist aber
    // noch verbesserungsbed�rftig
    // @Override
    // public void onSensorChanged(SensorEvent event) {
    // if (isSensorCalibrated) {
    // if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
    //
    // gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
    // gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
    // gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
    //
    // latestAccel[0] = event.values[0] - gravity[0];
    // latestAccel[1] = event.values[1] - gravity[1];
    // latestAccel[2] = event.values[2] - gravity[2];
    //
    // adjustedSensorValues[0] = Math.abs(latestAccel[0] -
    // calibratedOrientation[0]) > 0.15f ?
    // (latestAccel[0] - calibratedOrientation[0]) : adjustedSensorValues[0];
    //
    // adjustedSensorValues[1] = Math.abs(latestAccel[1] -
    // calibratedOrientation[1]) > 0.15f ?
    // (latestAccel[1] - calibratedOrientation[1])/2 : adjustedSensorValues[1];
    //
    // adjustedSensorValues[2] = Math.abs(latestAccel[2] -
    // calibratedOrientation[2]) > 0.15f ?
    // (latestAccel[2] - calibratedOrientation[2])/2 : adjustedSensorValues[2];
    //
    // // double accX = -x/SensorManager.GRAVITY_EARTH;
    // // double accY = -y/SensorManager.GRAVITY_EARTH;
    // // double accZ = z/SensorManager.GRAVITY_EARTH;
    // double totAcc =
    // Math.sqrt((adjustedSensorValues[0]*adjustedSensorValues[0])+(adjustedSensorValues[1]*adjustedSensorValues[1])+(adjustedSensorValues[2]*adjustedSensorValues[2]));
    // adjustedSensorValues[0] =
    // (float)Math.asin(adjustedSensorValues[0]/totAcc)/4;
    // adjustedSensorValues[1] =
    // (float)Math.asin(adjustedSensorValues[1]/totAcc)/4;
    // adjustedSensorValues[2] =
    // (float)Math.asin(adjustedSensorValues[2]/totAcc)/4;
    // }
    // }
    //
    // }
    // final float alpha = 0.8f;
    // float[] gravity = new float[3];
    // public void calibrate() {
    // if (!isSensorCalibrated) {
    // isSensorCalibrated = true;
    //
    // calibratedOrientation =
    // latestAccel.clone();//latestAccel.clone();//accelerometerValues.clone();
    // }
    // }

    public void setCalibrated(final boolean calibrated) {
        isSensorCalibrated = calibrated;
    }

    public Vector getAccelerationValues() {
        return new Vector(adjustedSensorValues[0], adjustedSensorValues[1],
                adjustedSensorValues[2]);
    }

    // /************************************************************************
    // * AB HIER WIRDS DRECKIG *
    // ************************************************************************/
    //
    // @Override
    // public void keyPressed(KeyEvent event) { }
    //
    // @Override
    // public void keyReleased(KeyEvent event) {
    // if (event.getKeyCode() == KeyEvent.KEYCODE_B) {
    // DodgeItActivity.RENDER_BOUNDS = !DodgeItActivity.RENDER_BOUNDS;
    // }
    // }
    //
    // private float ACC_MAX_VAL = 0.1f;
    //
    // @Override
    // public void keyHeld(KeyEvent event) {
    // if (event.getKeyCode() == KeyEvent.KEYCODE_S || event.getKeyCode() ==
    // KeyEvent.KEYCODE_DPAD_DOWN) {
    // if (adjustedSensorValues[2] < ACC_MAX_VAL)
    // adjustedSensorValues[2] += 0.01;
    // }
    // else if (event.getKeyCode() == KeyEvent.KEYCODE_W || event.getKeyCode()
    // == KeyEvent.KEYCODE_DPAD_UP) {
    // if (adjustedSensorValues[2] > -ACC_MAX_VAL)
    // adjustedSensorValues[2] -= 0.01;
    // }
    //
    // if (event.getKeyCode() == KeyEvent.KEYCODE_A || event.getKeyCode() ==
    // KeyEvent.KEYCODE_DPAD_LEFT) {
    // if (adjustedSensorValues[1] < ACC_MAX_VAL)
    // adjustedSensorValues[1] += 0.01;
    // }
    // else if (event.getKeyCode() == KeyEvent.KEYCODE_D || event.getKeyCode()
    // == KeyEvent.KEYCODE_DPAD_RIGHT) {
    // if (adjustedSensorValues[1] > -ACC_MAX_VAL)
    // adjustedSensorValues[1] -= 0.01;
    // }
    // }
}
