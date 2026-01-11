package com.trinity.protocol

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trinity.protocol.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var gyroscope: Sensor? = null

    // Load the C++ Kernel you just committed
    companion object {
        init {
            System.loadLibrary("protocol")
        }
    }

    // Link to C++ Functions
    external fun verifyBioPulse(): String
    external fun checkAcousticBuffer(buffer: ShortArray): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Hardware Sensors
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        // Display the Kernel Status from C++
        binding.sampleText.text = verifyBioPulse()
    }

    override fun onSensorChanged(event: SensorEvent) {
        // BIO-PULSE LOGIC: Detecting micro-tremors
        val tremor = Math.abs(event.values[0]) + Math.abs(event.values[1])
        if (tremor > 0.05) {
            binding.sampleText.text = "BIO-PULSE: VERIFIED"
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
