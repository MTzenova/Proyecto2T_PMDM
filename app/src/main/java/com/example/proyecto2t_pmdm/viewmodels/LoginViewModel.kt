package com.example.proyecto2t_pmdm.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel()
{
    //Observa ttodo el rato: Cuando los datos cambian, la vista se actualiza a partir del cambio en esos datos.
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> get() = _loginEnable

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> get() = _passwordError


    fun onEmailChanged(email: String)
    {
        _email.value = email
    }

    fun onPasswordChanged(password: String)
    {
        _password.value = password
    }

    fun onLoginEnableChanged()
    {
        val isEmailValid = isValidEmail()
        val isPasswordValid = isValidPassword()
        _loginEnable.value = isEmailValid && isPasswordValid
    }

    fun isValidEmail(): Boolean
    {
        val email = _email.value.orEmpty()
        if(email.isBlank())
        {
            _emailError.value ="El campo 'Correo electrónico' no puede estar vacío"
            return false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false
        }

        _emailError.value = email //esto al igual me da error
        return true
    }

    fun isValidPassword(): Boolean
    {
        val password = _password.value.orEmpty()
        if (password.isBlank()) {
            _passwordError.value = "El campo 'Contraseña' no puede estar vacío"
            return false
        } else if (password.length <= 8) {
            _passwordError.value = "La contraseña debe tener más de 8 caracteres"
            return false
        }

        _passwordError.value = password //esto al igual me da error
        return true

    }


}