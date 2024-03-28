package com.octagon_technologies.trecipe.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.octagon_technologies.trecipe.R
import com.octagon_technologies.trecipe.domain.State
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException


fun LiveData<State>.setUpSnackBars(fragment: Fragment) {
    observe(fragment.viewLifecycleOwner) {
        if (it == State.NoNetworkError)
            fragment.showShortSnackBar(R.string.no_network_available)
        else if (it == State.ApiError)
            fragment.showShortSnackBar(R.string.api_limit_exceeded)
    }
}
suspend fun MutableLiveData<State>.tryCatching(performTask: suspend () -> Unit) {
    try {
        value = State.Loading
        performTask()
        value = State.Done
//        value = State.Done   // Prevents us from setting State.Empty for failed tasks
    } catch (throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                Timber.i(throwable)
                value = State.ApiError
            }

            is UnknownHostException -> {
                value = State.NoNetworkError
            }

            else -> {
                Timber.e(throwable)
            }
        }
    }
}