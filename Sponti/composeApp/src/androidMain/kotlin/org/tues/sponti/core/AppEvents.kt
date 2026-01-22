package org.tues.sponti.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEvents {
    private val _challengeInteracted = MutableSharedFlow<Unit>(extraBufferCapacity = 1, replay = 1)
    val challengeInteracted = _challengeInteracted.asSharedFlow()

    suspend fun notifyChallengeInteracted() {
        _challengeInteracted.emit(Unit)
    }
}