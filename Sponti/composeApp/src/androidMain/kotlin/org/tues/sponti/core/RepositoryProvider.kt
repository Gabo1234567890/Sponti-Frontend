package org.tues.sponti.core

import org.tues.sponti.data.chal.ChalRepository

object RepositoryProvider {
    val chalRepository by lazy { ChalRepository() }
}