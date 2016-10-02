package fr.frazew.virtualgyroscope;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import de.robv.android.xposed.XposedHelpers;

public class VirtualSensorListener implements SensorEventListener {
    private SensorEventListener realListener = null;
    private Sensor registeredSensor = null;
    public Sensor sensorRef = null;
    public boolean isDummyGyroListener = false;

    public VirtualSensorListener(SensorEventListener realListener, Sensor registeredSensor) {
        this.realListener = realListener;
        this.registeredSensor = registeredSensor;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (this.sensorRef != null) {
            SensorEvent newEvent = (SensorEvent) XposedHelpers.newInstance(XposedMod.SENSOR_EVENT_CLASS, event.values.length);
            newEvent.timestamp = event.timestamp;
            newEvent.sensor = this.sensorRef;
            newEvent.accuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;
            System.arraycopy(event.values, 0, newEvent.values, 0, event.values.length);
            realListener.onSensorChanged(newEvent);
        }
    }

    public Sensor getSensor() {
        return this.registeredSensor;
    }

    public SensorEventListener getRealListener() {
        return this.realListener;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
