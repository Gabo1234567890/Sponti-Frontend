package org.tues.sponti.ui.screens.challenge

import org.tues.sponti.data.part.PublicCompletionImage
import org.tues.sponti.ui.screens.common.ChallengeType
import org.tues.sponti.ui.screens.common.FieldError
import org.tues.sponti.ui.screens.common.FinishChallengeFlowStep
import java.io.File

data class ChallengeState(
    val challengeData: ChallengeType? = null,
    val completedCount: Int = 0,
    val publicCompletionImages: List<PublicCompletionImage> = emptyList(),
    val isActive: Boolean = false,
    val finishStep: FinishChallengeFlowStep = FinishChallengeFlowStep.NONE,
    val completionImages: List<File> = emptyList(),
    val isSubmittingFinish: Boolean = false,
    val isLoading: Boolean = false,
    val globalError: FieldError? = null
)