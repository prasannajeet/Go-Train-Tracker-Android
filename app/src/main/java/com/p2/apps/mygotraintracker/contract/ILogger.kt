package com.p2.apps.mygotraintracker.contract

interface ILogger {
    fun logDebug(message: String)
    fun logWarning(message: String)
    fun logError(message: String)
    fun logInfo(message: String)
}