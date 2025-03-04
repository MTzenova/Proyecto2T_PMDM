package com.example.proyecto2t_pmdm.viewmodels

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel()
{
    //Observa ttodo el rato: Cuando los datos cambian, la vista se actualiza a partir del cambio en esos datos.
    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    private val _registerEnabled = MutableLiveData<Boolean>()
    val registerEnabled: LiveData<Boolean> get() = _registerEnabled

    private val _date = MutableLiveData<String>()
    val date: LiveData<String> get() = _date

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> get() = _passwordError

    private val _dateError = MutableLiveData<String>()
    val dateError: LiveData<String> get() = _dateError

    private val _usernameError = MutableLiveData<String>()
    val usernameError: LiveData<String> get() = _usernameError


    fun setEmail(email: String)
    {
        _email.value = email
    }

    fun setPassword(password: String)
    {
        _password.value = password
    }

    fun setDate(date: String)
    {
        _date.value = date

    }

    fun setUsername(username: String)
    {
        _username.value = username
    }

    fun onRegisterEnableChanged()
    {
        val isEmailValid = isValidEmail()
        val isPasswordValid = isValidPassword()
        val isDateValid = isValidDate()
        _registerEnabled.value = isEmailValid && isPasswordValid && isDateValid && isValidUser()
    }

    fun isValidUser():Boolean
    {
        val username = _username.value.orEmpty()
        if(username.isBlank())
        {
            _usernameError.value = "El campo 'Usuario' no puede estar vacío"
            return false
        }
        _usernameError.value = username
        return true
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

    fun isValidDate(): Boolean
    {
        val date = _date.value.orEmpty()
        if (date.isBlank()) {
            _dateError.value = "El campo 'Date' no puede estar vacío"
            return false
        }
        _dateError.value = date
        return true
    }

}