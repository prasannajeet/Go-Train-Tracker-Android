package com.p2.apps.mygotraintracker.presentation.utils

/**
 * Lets the UI act on a controlled bound of states that can be defined here
 * @author Prasan
 * @since 1.0
 */
sealed class ViewState<out T> {

    /**
     * Represents UI state where the UI should be showing a loading UX to the user
     */
    object Loading : ViewState<Nothing>()

    /**
     * Represents the UI state where the operation requested by the UI has been completed
     */
    data class RenderSuccess<T>(val data: T) : ViewState<T>()

    /**
     * Represents the UI state where the operation requested by the UI has failed to complete
     * either due to a IO issue or a service exception and the same is conveyed back to the UI
     * to be shown the user
     * @param throwable [Throwable] instance containing the root cause of the failure in a [String]
     */
    data class RenderFailure(val throwable: Throwable) : ViewState<Nothing>()
}