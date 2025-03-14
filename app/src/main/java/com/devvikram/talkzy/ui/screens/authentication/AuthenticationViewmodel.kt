package com.devvikram.talkzy.ui.screens.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devvikram.talkzy.config.ModelMapper
import com.devvikram.talkzy.config.constants.LoginPreference
import com.devvikram.talkzy.config.enums.ContactType
import com.devvikram.talkzy.data.firebase.models.FirebaseContact
import com.devvikram.talkzy.data.firebase.repository.FirebaseContactRepository
import com.devvikram.talkzy.data.room.repository.ContactRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resumeWithException


@HiltViewModel
class AuthenticationViewmodel @Inject constructor(
    @ApplicationContext context: Context,
    private val firebaseAuth: FirebaseAuth,
    val loginPreference: LoginPreference,
    private val contactRepository: ContactRepository,
    private val firebaseContactRepository: FirebaseContactRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState = _signUpState.asStateFlow()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()



    sealed class SignUpState {
        data object Idle : SignUpState()
        data object Loading : SignUpState()
        class Success(
            val message: String
        ) : SignUpState()

        data class Error(val message: String) : SignUpState()

    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        class Success(
            val message: String
        ) : LoginState()

        data class Error(val message: String) : LoginState()
    }


    fun signUp(
        email: String,
        password: String,
        fullName: String,
        mobileNumber: String = "1234567890"
    ) {
        if (email.isEmpty()) {
            _signUpState.value = SignUpState.Error("Email is required")
            return
        }
        if (password.isEmpty()) {
            _signUpState.value = SignUpState.Error("Password is required")
            return
        }
        if (fullName.isEmpty()) {
            _signUpState.value = SignUpState.Error("Full name is required")
            return
        }

        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            try {
                val user = createUserWithEmailAndPassword(email, password)
                user?.let {
                    val contact = FirebaseContact(
                        email = it.email.toString(),
                        name = fullName,
                        userId = it.uid.toString(),
                        mobileNumber = mobileNumber,
                        contactType = ContactType.PERSONAL.name,
                        createdAt = System.currentTimeMillis(),
                        lastModifiedAt = System.currentTimeMillis(),
                    )

                    coroutineScope {
                        launch { firebaseContactRepository.insertContact(contact) }
                        launch { contactRepository.insertContact(ModelMapper.toRoomContact(contact)) }
                    }
                    _signUpState.value = SignUpState.Success("Sign up successful")
                } ?: run {
                    _signUpState.value = SignUpState.Error("User creation failed")
                }
            } catch (e: Exception) {
                _signUpState.value = SignUpState.Error("SignUp Exception: ${e.message}")
            }
        }
    }

    private suspend fun createUserWithEmailAndPassword(email: String, password: String): FirebaseUser? =
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result?.user, null)
                    } else {
                        continuation.resumeWithException(task.exception ?: Exception("Unknown error occurred"))
                    }
                }
        }


    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.Error("Email and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            loginPreference.setUserId(user?.uid.toString())
                            loginPreference.setLoggedIn(true)

                            _loginState.value = LoginState.Success("Login successful")

                        } else {
                            task.exception?.let {
                                _loginState.value = LoginState.Error(it.message.toString())
                            }
                        }
                    }
            } catch (e: Exception) {
                // Handle exception here
                _loginState.value = LoginState.Error("Login Exception : " + e.message.toString())
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                _loginState.value = LoginState.Success("Login successful")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message.toString())
            }
        }
    }

    fun signInWithFacebook() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                _loginState.value = LoginState.Success("Login successful")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message.toString())
            }
        }
    }

}