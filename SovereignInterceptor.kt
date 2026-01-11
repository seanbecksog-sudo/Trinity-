package com.trinity.protocol

import android.net.VpnService
import android.content.Intent
import android.os.ParcelFileDescriptor

class SovereignInterceptor : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Establishing the "Coating" layer
        val builder = Builder()
        
        try {
            vpnInterface = builder.setSession("Trinity Sovereign Protocol")
                .addAddress("10.8.0.1", 24) 
                .addDnsServer("8.8.8.8")
                .addRoute("0.0.0.0", 0) 
                .establish()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        vpnInterface?.close()
    }
}
