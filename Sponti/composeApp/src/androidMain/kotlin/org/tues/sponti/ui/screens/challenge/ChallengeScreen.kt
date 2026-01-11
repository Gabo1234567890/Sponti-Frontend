package org.tues.sponti.ui.screens.challenge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import org.tues.sponti.R
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.ChallengeDetailChip
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.minutesToFormattedTimeString
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base20
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Heading5
import org.tues.sponti.ui.theme.Heading7
import org.tues.sponti.ui.theme.Paragraph1
import org.tues.sponti.ui.theme.Primary1

@Composable
fun ChallengeScreen(challengeId: String, modifier: Modifier = Modifier) {
    val viewModel: ChallengeViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ChallengeViewModel(
                challengeId = challengeId
            ) as T
        }
    })
    val state by viewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current

    val snackBarHostState = remember { SnackbarHostState() }
    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    Box(
        modifier = modifier.fillMaxSize().padding(bottom = 24.dp)
    ) {
        if (state.isLoading && state.challengeData == null) {
            Text(
                text = "Loading...", style = Heading4, color = Primary1
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                    .background(Base0),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = state.challengeData!!.title, style = Heading4, color = Base100
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.challengeScreenTimesCompleted),
                            style = Paragraph1,
                            color = Base100
                        )
                        Text(
                            text = state.completedCount.toString(),
                            style = Heading5,
                            color = Primary1
                        )
                    }
                }
                when {
                    state.isLoading -> Text(
                        text = "Loading...", style = Heading4, color = Primary1
                    )

                    state.publicCompletionImages.isEmpty() -> Text(
                        text = stringResource(R.string.challengeScreenNoCompletionImages),
                        style = Heading4,
                        color = Base100
                    )

                    else -> {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(state.publicCompletionImages) {
                                AsyncImage(
                                    model = it.url,
                                    contentDescription = "Completion image",
                                    modifier = Modifier
                                        .size(200.dp)
                                        .shadow(
                                            elevation = 16.dp,
                                            shape = RoundedCornerShape(16.dp),
                                            clip = false,
                                            spotColor = Color.Black.copy(alpha = 0.1f)
                                        )
                                        .clip(shape = RoundedCornerShape(20.dp)),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(R.drawable.image_placeholder),
                                    error = painterResource(R.drawable.image_placeholder)
                                )
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = state.challengeData!!.description,
                        style = Paragraph1,
                        color = Base100
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(16.dp),
                        clip = false,
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    )
                    .background(Base0, RoundedCornerShape(16.dp))
                    .border(BorderStroke(1.dp, Base20), RoundedCornerShape(16.dp))
                    .padding(top = 12.dp)
                    .zIndex(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.location),
                            contentDescription = "Location",
                            tint = Primary1,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = state.challengeData!!.place,
                            style = Heading7,
                            color = Primary1
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        ChallengeDetailChip(
                            iconId = R.drawable.price,
                            text = state.challengeData!!.price.toString(),
                            small = false,
                            modifier = Modifier.weight(1f)
                        )
                        ChallengeDetailChip(
                            iconId = R.drawable.time,
                            text = state.challengeData!!.duration.minutesToFormattedTimeString(),
                            small = false,
                            modifier = Modifier.weight(1f)
                        )
                        ChallengeDetailChip(
                            iconId = R.drawable.vehicle,
                            text = state.challengeData!!.vehicle.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            small = false,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (!state.isActive) {
                        PrimaryButton(
                            text = "Start challenge",
                            size = ButtonSize.Large,
                            state = ButtonState.Active,
                            modifier = Modifier.fillMaxWidth()
                        ) { viewModel.startChallenge(challengeId = challengeId, onSuccess = {}) }
                    } else {
                        PrimaryButton(
                            text = "Cancel",
                            size = ButtonSize.Large,
                            state = ButtonState.Active,
                            modifier = Modifier.background(Base80)
                        ) { viewModel.cancelChallenge(challengeId = challengeId, onSuccess = {}) }
                        PrimaryButton(
                            text = "Finish",
                            size = ButtonSize.Large,
                            state = ButtonState.Active,
                            modifier = Modifier.background(Base80)
                        ) { viewModel.completeChallenge(challengeId = challengeId, onSuccess = {}) }
                    }
                }
            }
        }
    }
}