package com.p2.apps.mygotraintracker.presentation.utils

import android.util.Log
import com.p2.apps.mygotraintracker.contract.ILogger

class AppLogger: ILogger {
    private val TAG = "MyGoTrainTracker"
    override fun logDebug(message: String) {
        Log.d(TAG, message)
    }

    override fun logWarning(message: String) {
        Log.w(TAG, message)
    }

    override fun logError(message: String) {
        Log.e(TAG, message)
    }

    override fun logInfo(message: String) {
        Log.i(TAG, message)
    }
}