package com.imt3673.project.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.imt3673.project.main.R;
import com.imt3673.project.utils.Utils;

/**
 * Sensor Listener Manager
 */
public class SensorListenerManager {

    private final Context       context;
    private final SensorManager sensorManager;

    /**
     *
     * @param context
     */
    public SensorListenerManager(final Context context) {
        this.context       = context;
        this.sensorManager = (SensorManager)this.context.getSystemService(Context.SENSOR_SERVICE);
    }

    /**
     *
     * @param sensorListener
     * @param sensor
     */
    public void addListener(final SensorEventListener sensorListener, final Sensor sensor) {
        // Tell the user if the device does not support the specified sensor
        if (sensor == null)
            Utils.alertMessage(this.context.getString(R.string.error_no_accel), this.context);
        else
            this.sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     *
     * @param sensorListener
     */
    public void removeListener(final SensorEventListener sensorListener) {
        this.sensorManager.unregisterListener(sensorListener);
    }

    /**
     *
     * @param type
     * @return
     */
    public Sensor getSensor(final int type) {
        return this.sensorManager.getDefaultSensor(type);
    }

}
