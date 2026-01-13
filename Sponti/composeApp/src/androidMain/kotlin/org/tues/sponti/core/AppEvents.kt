package org.tues.sponti.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEvents {
    private val _challengeCompleted = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val challengeCompleted = _challengeCompleted.asSharedFlow()

    suspend fun notifyChallengeCompleted() {
        _challengeCompleted.emit(Unit)
    }
}