package org.tues.sponti.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base60
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Caption1
import org.tues.sponti.ui.theme.Caption2
import org.tues.sponti.ui.theme.Error
import org.tues.sponti.ui.theme.Paragraph1
import org.tues.sponti.ui.theme.Primary1
import org.tues.sponti.ui.theme.UnderlinedCaption1

enum class InputState {
    Default, Active, Filled, Error
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    inputState: InputState,
    modifier: Modifier = Modifier,
    maxLength: Int = 0,
    showIcon: Boolean = false,
    icon: @Composable (() -> Unit)? = null,
    errorMessage: String = "",
    forgotPassword: Boolean = false,
    onForgotPasswordClick: (() -> Unit)? = null,
    onIconClick: (() -> Unit)? = null,
    focusOnIconClick: Boolean = false,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    readOnly: Boolean = false,
    onFocusChange: (Boolean) -> Unit
) {
    val borderColor = when (inputState) {
        InputState.Default -> Base80
        InputState.Active -> Primary1
        InputState.Filled -> Base100
        InputState.Error -> Error
    }

    val labelColor = when (inputState) {
        InputState.Default -> null
        InputState.Active -> Primary1
        InputState.Filled -> Base100
        InputState.Error -> Error
    }

    val shape = RoundedCornerShape(16.dp)

    var isMultiline by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .height(16.dp)
                .padding(horizontal = 12.dp)
        ) {
            if (label.isNotEmpty() && labelColor != null) {
                Text(
                    text = label, style = Caption1, color = labelColor
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .border(BorderStroke(1.dp, borderColor), shape)
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = if (isMultiline) Alignment.Bottom else Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    BasicTextField(
                        value = value,
                        onValueChange = {
                            if (it.length <= maxLength || maxLength <= 0) {
                                onValueChange(it)
                            }
                        },
                        textStyle = Paragraph1.copy(color = Base100),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { onFocusChange.invoke(it.isFocused) },
                        decorationBox = { innerTextField ->
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder, style = Paragraph1, color = Base60
                                )
                            }
                            innerTextField()
                        },
                        singleLine = maxLength <= 0,
                        cursorBrush = SolidColor(Base100),
                        onTextLayout = { layout -> isMultiline = layout.lineCount > 1 },
                        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                        readOnly = readOnly
                    )
                }

                Spacer(Modifier.width(8.dp))

                if (showIcon && icon != null) {
                    Box(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (focusOnIconClick) {
                                focusRequester.requestFocus()
                            }
                            onIconClick?.invoke()
                        }) { icon() }
                } else if (maxLength > 0) {
                    Text(
                        text = "${value.length}/$maxLength", style = Caption1.copy(color = Base80)
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (inputState == InputState.Error && errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage, style = Caption2.copy(color = Error)
                        )
                    }
                    if (forgotPassword && onForgotPasswordClick != null) {
                        Spacer(Modifier.weight(1f))
                        Text(
                            "Forgot password?",
                            style = UnderlinedCaption1.copy(color = Primary1),
                            modifier = Modifier.clickable { onForgotPasswordClick.invoke() })
                    }
                }
            }
        }
    }
}
