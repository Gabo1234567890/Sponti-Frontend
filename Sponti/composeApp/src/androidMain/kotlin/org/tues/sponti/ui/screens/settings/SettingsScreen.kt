package org.tues.sponti.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.tues.sponti.R
import org.tues.sponti.data.user.Role
import org.tues.sponti.navigation.Routes
import org.tues.sponti.ui.components.SettingElement
import org.tues.sponti.ui.components.Switch
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.theme.Base40
import org.tues.sponti.ui.theme.Error
import org.tues.sponti.ui.theme.Heading4
import org.tues.sponti.ui.theme.Primary1

@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    val state by viewModel.state.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearGlobalError()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                Text(
                    text = "Loading...", style = Heading4, color = Primary1
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        SettingElement(title = "Account Details") {
                            viewModel.onViewAccountDetailsChange(
                                true
                            )
                        }
                        HorizontalDivider(thickness = 1.dp, color = Base40)
                        SettingElement(
                            title = "Share Images", action = {
                                Switch(checked = state.allowPublicImages) {
                                    viewModel.patchUserData(allowPublicImages = it)
                                }
                            }) {
                            viewModel.patchUserData(
                                allowPublicImages = !state.allowPublicImages
                            )
                        }
                        if (state.role == Role.ADMIN) {
                            HorizontalDivider(thickness = 1.dp, color = Base40)
                            SettingElement(title = "Admin Panel") {
                                navController.navigate(Routes.ADMIN)
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = Base40)
                        SettingElement(title = "Reset Password") {
                            viewModel.logout {
                                navController.navigate(
                                    Routes.FORGOT_PASSWORD
                                ) { popUpTo(0) }
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = Base40)
                        SettingElement(
                            title = "Logout", titleColor = Error
                        ) { viewModel.logout { navController.navigate(Routes.LOGIN) { popUpTo(0) } } }
                        HorizontalDivider(thickness = 1.dp, color = Base40)
                        SettingElement(
                            title = "Delete Account", titleColor = Error
                        ) {
                            viewModel.deleteAccount {
                                navController.navigate(Routes.CREATE_ACCOUNT) { popUpTo(0) }
                            }
                        }
                        HorizontalDivider(thickness = 1.dp, color = Base40)
                    }
                    Column {
                        SettingElement(
                            title = "Terms & Conditions",
                            action = {
                                Image(
                                    painter = painterResource(R.drawable.back_arrow),
                                    contentDescription = null,
                                    modifier = Modifier.rotate(180f)
                                )
                            }
                        ) { }
                        SettingElement(
                            title = "Privacy Policy",
                            action = {
                                Image(
                                    painter = painterResource(R.drawable.back_arrow),
                                    contentDescription = null,
                                    modifier = Modifier.rotate(180f)
                                )
                            }
                        ) { }
                    }
                }
            }
        }
    }
}