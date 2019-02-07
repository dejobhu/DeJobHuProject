package com.dejobhu.skhu.dejobhu.Fragment

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.async



class SNSViewModal(app: Application) : AndroidViewModel(app) {
    val emails: LiveData<State>
        get() = emailLiveData

    val emailLiveData = MutableLiveData<State>()
    val emailData by lazy { (1..17).map { "Email $it" }.toList() }

    fun getQustions() {
        async {
            emailLiveData.postValue(State.InProgress)
            Thread.sleep(2000)
            emailLiveData.postValue(State.Success(emailData))
        }
    }
}

sealed class State {
    object InProgress : State()
    data class Success(val data: List<String>) : State()
}