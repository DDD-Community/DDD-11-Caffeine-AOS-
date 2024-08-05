package com.taltal.poison.ui.onboard

import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    private val _nickname = MutableStateFlow<String>("")
    val nickname: StateFlow<String> = _nickname

    private val _isNicknameValid = MutableStateFlow<Boolean>(false)
    val isNicknameValid: StateFlow<Boolean> = _isNicknameValid

    private val _errorText = MutableStateFlow<String>("")
    val errorText: StateFlow<String> = _errorText

    private val _gender = MutableStateFlow<String>("")
    val gender: StateFlow<String> = _gender

    private val _birth = MutableStateFlow<String>("")
    private val _weight = MutableStateFlow<String>("")
    private val _height = MutableStateFlow<String>("")
    val isProfileFullFilled = combine(
        listOf(_gender, _birth, _weight, _height)
    ) {
        it.all { value -> value.isNotEmpty() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _chosenPurpose = MutableStateFlow<String>("")
    val chosenPurpose: StateFlow<String> = _chosenPurpose

    private val _chosenGoal = MutableStateFlow<String>("")
    val chosenGoal: StateFlow<String> = _chosenGoal

    val pagerState = PagerState(0) {
        5
    }

    fun updateNickname(newNickname: String) {
        viewModelScope.launch {
            _nickname.value = newNickname
            _isNicknameValid.value = checkNickNameValidation(newNickname)
        }
    }

    fun moveToNextPage() {
        viewModelScope.launch {
            val nextPage = pagerState.currentPage + 1
            if (nextPage < pagerState.pageCount) {
                pagerState.scrollToPage(nextPage)
            }
        }
    }

    fun delayAndMoveToNextPage() {
        viewModelScope.launch {
            delay(4000)
            moveToNextPage()
        }
    }

    fun moveToPreviousPage() {
        viewModelScope.launch {
            val previousPage = pagerState.currentPage - 1
            if (previousPage >= 0) {
                pagerState.scrollToPage(previousPage)
            }
        }
    }

    fun checkNickNameValidation(nickname: String): Boolean {
        val regex = "^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ0-9]{1,8}$".toRegex()
        return regex.matches(nickname)
    }

    fun updateGenderOption(gender: String) {
        viewModelScope.launch {
            _gender.value = gender
        }
    }

    fun updateBirth(birth: String) {
        viewModelScope.launch {
            _birth.value = birth
        }
    }

    fun updateWeight(weight: String) {
        viewModelScope.launch {
            _weight.value = weight
        }
    }

    fun updateHeight(height: String) {
        viewModelScope.launch {
            _height.value = height
        }
    }

    fun updateTargetPurpose(purpose: String) {
        viewModelScope.launch {
            _chosenPurpose.value = purpose
        }
    }

    fun updateGoal(goal: String) {
        viewModelScope.launch {
            _chosenGoal.value = goal
        }
    }

    fun uploadUserData() {
        // upload user data to server

    }
}