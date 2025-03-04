package com.p2.apps.mygotraintracker.utils

import android.content.Context
import android.net.ConnectivityManager as AndroidConnectivityManager
import android.net.NetworkCapabilities
import com.p2.apps.mygotraintracker.contract.ConnectivityManager

class ConnectivityManagerImpl(private val context: Context) : ConnectivityManager {

    override fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as AndroidConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
