package org.tues.sponti.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.tues.sponti.R

data class FlowTexts(
    val title: String,
    val inboxMessage: String,
    val successTitle: String,
    val successMessage: String,
    val failureTitle: String,
    val failureMessage: String
)

@Composable
fun flowTexts(type: FlowType) = when (type) {
    FlowType.VERIFY_EMAIL -> FlowTexts(
        title = stringResource(R.string.verificationTitle),
        inboxMessage = stringResource(R.string.verificationInboxMessage),
        successTitle = stringResource(R.string.verificationSuccessTitle),
        successMessage = stringResource(R.string.verificationSuccessMessage),
        failureTitle = stringResource(R.string.verificationFailureTitle),
        failureMessage = stringResource(R.string.verificationFailureMessage)
    )

    FlowType.FORGOT_PASSWORD -> FlowTexts(
        title = stringResource(R.string.forgotPassTitle),
        inboxMessage = stringResource(R.string.forgotPassInboxMessage),
        successTitle = stringResource(R.string.forgotPassSuccessTitle),
        successMessage = stringResource(R.string.forgotPassSuccessMessage),
        failureTitle = stringResource(R.string.forgotPassFailureTitle),
        failureMessage = stringResource(R.string.forgotPassFailureMessage)
    )
}