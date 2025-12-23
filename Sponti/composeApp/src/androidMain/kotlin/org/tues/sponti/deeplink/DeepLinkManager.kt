package org.tues.sponti.deeplink

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class DeepLinkEvent {
    data class ResetPassword(val token: String?, val email: String?): DeepLinkEvent()
}

object DeepLinkManager {
    private val _events = MutableStateFlow<DeepLinkEvent?>(null)
    val events = _events.asStateFlow()

    fun handle(event: DeepLinkEvent) {
        _events.value = event
    }

    fun consume() {
        _events.value = null
    }
}