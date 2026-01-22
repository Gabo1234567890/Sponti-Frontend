package org.tues.sponti.ui.screens.common

sealed class FieldError {
    object Empty: FieldError()
    object InvalidFormat: FieldError()
    object Short: FieldError()
    object Weak: FieldError()
    object NoMatch: FieldError()
    object Unknown: FieldError()
    object Network: FieldError()
    data class Server(val message: String): FieldError()
}