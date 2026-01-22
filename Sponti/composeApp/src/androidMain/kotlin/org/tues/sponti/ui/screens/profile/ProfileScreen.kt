package org.tues.sponti.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.sponti.R
import org.tues.sponti.navigation.Routes
import org.tues.sponti.ui.components.ChallengeCard
import org.tues.sponti.ui.components.MemoryCard
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Base80
import org.tues.sponti.ui.theme.Heading2
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Heading5
import org.tues.sponti.ui.theme.Heading6
import org.tues.sponti.ui.theme.Paragraph1
import org.tues.sponti.ui.theme.Primary1

@Composable
fun ProfileScreen(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: ProfileViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current

    val snackBarHostState = remember { SnackbarHostState() }
    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearGlobalError()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .background(Base0)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            if (state.isLoading && state.userData == null) {
                item {
                    Text(
                        text = "Loading...", style = Heading4, color = Primary1
                    )
                }
            } else {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Hi, ${state.userData?.username}!",
                            style = Heading5,
                            color = Primary1
                        )
                        Text(
                            text = stringResource(R.string.profileScreenDescription),
                            style = Paragraph1,
                            color = Base100
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Base80
                    )
                    Spacer(Modifier.height(32.dp))
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.profileScreenActiveChallenges),
                            style = Heading6,
                            color = Base100
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                }
                items(items = state.activeChallenge) {
                    when {
                        state.isLoading -> Text(
                            text = "Loading...", style = Heading4, color = Primary1
                        )

                        state.activeChallenge.isEmpty() -> Text(
                            text = stringResource(R.string.profileScreenNoActiveChallenges),
                            style = Heading4,
                            color = Base100
                        )

                        else -> {
                            Spacer(Modifier.height(24.dp))
                            ChallengeCard(it) {
                                navController.navigate("${Routes.CHALLENGE}/${it.id}")
                            }
                        }
                    }
                }
                item {
                    Spacer(Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.profileScreenCompletedChallenges),
                            style = Heading6,
                            color = Base100
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = state.completedCount.toString(),
                            style = Heading2,
                            color = Primary1
                        )
                    }
                    Spacer(Modifier.height(32.dp))
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.profileScreenMemories),
                            style = Heading6,
                            color = Base100
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    when {
                        state.isLoading -> Text(
                            text = "Loading...", style = Heading4, color = Primary1
                        )

                        state.memories.isEmpty() -> Text(
                            text = stringResource(R.string.profileScreenNoMemories),
                            style = Heading4,
                            color = Base100
                        )

                        else -> {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                LazyRow(
                                    modifier = Modifier.wrapContentWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(state.memories) { MemoryCard(it) }
                                }
                            }
                            Spacer(Modifier.height(20.dp))
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
    }
}