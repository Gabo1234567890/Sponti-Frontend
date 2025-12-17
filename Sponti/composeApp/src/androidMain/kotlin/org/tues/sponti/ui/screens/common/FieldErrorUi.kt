package org.tues.sponti.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.tues.sponti.R

@Composable
fun FieldError.toUiText(field: FieldType): String = when (this) {
    FieldError.Empty -> when (field) {
        FieldType.USERNAME -> stringResource(R.string.emptyUsername)
        else -> stringResource(R.string.unknownError)
    }

    FieldError.InvalidFormat -> when (field) {
        FieldType.EMAIL -> stringResource(R.string.invalidEmail)
        FieldType.LOGIN -> stringResource(R.string.invalidLoginResponse)
        else -> stringResource(R.string.unknownError)
    }

    FieldError.Short -> when (field) {
        FieldType.PASSWORD -> stringResource(R.string.shortPassword)
        else -> stringResource(R.string.unknownError)
    }

    FieldError.Weak -> when (field) {
        FieldType.PASSWORD -> stringResource(R.string.weakPassword)
        else -> stringResource(R.string.unknownError)
    }

    FieldError.Unknown -> when (field) {
        FieldType.USERNAME -> stringResource(R.string.unknownUsernameError)
        FieldType.EMAIL -> stringResource(R.string.unknownEmailError)
        FieldType.PASSWORD -> stringResource(R.string.unknownPasswordError)
        FieldType.LOGIN -> stringResource(R.string.unknownLoginError)
        FieldType.GLOBAL -> stringResource(R.string.unknownError)
    }

    FieldError.Network -> stringResource(R.string.networkError)

    is FieldError.Server -> message
}