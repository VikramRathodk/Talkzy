package com.devvikram.talkzy.ui.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class HomeViewmodel  @Inject constructor(

):ViewModel(){


    // isHomeToolbar visible flow variable
    private val _isHomeToolbarVisible = MutableStateFlow<Boolean>(true)
    val isHomeToolbarVisibleFlow : StateFlow<Boolean> = _isHomeToolbarVisible.asStateFlow()


    fun updateHomeToolbarState(value: Boolean) {
        _isHomeToolbarVisible.value = value
    }

}