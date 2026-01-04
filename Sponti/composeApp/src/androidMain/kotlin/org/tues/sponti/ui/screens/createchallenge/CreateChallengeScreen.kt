package org.tues.sponti.ui.screens.createchallenge

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.tues.sponti.R
import org.tues.sponti.data.chal.PlaceType
import org.tues.sponti.data.chal.VehicleType
import org.tues.sponti.navigation.Routes
import org.tues.sponti.ui.components.ButtonSize
import org.tues.sponti.ui.components.ButtonState
import org.tues.sponti.ui.components.DropdownInputField
import org.tues.sponti.ui.components.IconInputField
import org.tues.sponti.ui.components.ImagePickerBox
import org.tues.sponti.ui.components.InputField
import org.tues.sponti.ui.components.InputState
import org.tues.sponti.ui.components.PickerDialog
import org.tues.sponti.ui.components.PrimaryButton
import org.tues.sponti.ui.screens.common.FieldType
import org.tues.sponti.ui.screens.common.createTempImageFile
import org.tues.sponti.ui.screens.common.toUiText
import org.tues.sponti.ui.screens.common.uriToFile
import org.tues.sponti.ui.theme.Base0
import org.tues.sponti.ui.theme.Base100
import org.tues.sponti.ui.theme.Caption1
import org.tues.sponti.ui.theme.Caption2
import org.tues.sponti.ui.theme.Error
import java.io.File

@Composable
fun CreateChallengeScreen(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel: CreateChallengeViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    var tempCameraFile by remember { mutableStateOf<File?>(null) }

    var titleState by remember { mutableStateOf(InputState.Default) }
    var descriptionState by remember { mutableStateOf(InputState.Default) }
    var priceState by remember { mutableStateOf(InputState.Default) }
    var durationState by remember { mutableStateOf(InputState.Default) }
    var placeState by remember { mutableStateOf(InputState.Default) }

    LaunchedEffect(state.titleError) {
        titleState =
            if (state.titleError != null) InputState.Error else if (state.title.isEmpty()) InputState.Default
            else InputState.Filled
    }

    LaunchedEffect(state.priceError) {
        priceState =
            if (state.priceError != null) InputState.Error else if (state.price.isEmpty()) InputState.Default
            else InputState.Filled
    }

    LaunchedEffect(state.durationError) {
        durationState =
            if (state.durationError != null) InputState.Error else if (state.duration.isEmpty()) InputState.Default
            else InputState.Filled
    }

    LaunchedEffect(state.placeError) {
        placeState =
            if (state.placeError != null) InputState.Error else if (state.place.isEmpty()) InputState.Default
            else InputState.Filled
    }

    val snackBarHostState = remember { SnackbarHostState() }

    var globalErrorText = state.globalError?.toUiText(FieldType.GLOBAL)

    LaunchedEffect(globalErrorText) {
        globalErrorText?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    var showPicker by remember { mutableStateOf(false) }

    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempCameraFile?.let { viewModel.onThumbnailChange(it) }
            }
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@rememberLauncherForActivityResult

            val file = uriToFile(context, uri)

            if (file != null) viewModel.onThumbnailChange(file) else globalErrorText =
                "Cannot open input stream from URI"
        }

    val errorText = when {
        state.titleError != null -> state.titleError!!.toUiText(FieldType.TITLE)
        state.priceError != null -> state.priceError!!.toUiText(FieldType.PRICE)
        state.durationError != null -> state.durationError!!.toUiText(FieldType.TIME)
        state.placeError != null -> state.placeError!!.toUiText(FieldType.PLACE)
        else -> ""
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .verticalScroll(rememberScrollState())
            .background(Base0)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InputField(
                    value = state.title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    label = "Title",
                    placeholder = "Title",
                    inputState = titleState,
                    onFocusChange = { focused ->
                        titleState =
                            if (focused) InputState.Active else if (state.title.isEmpty()) InputState.Default else InputState.Filled
                    },
                    maxLength = 25
                )
                InputField(
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    label = "Description",
                    placeholder = "Description",
                    inputState = descriptionState,
                    onFocusChange = { focused ->
                        descriptionState =
                            if (focused) InputState.Active else if (state.description.isEmpty()) InputState.Default else InputState.Filled
                    },
                    maxLength = 500
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ImagePickerBox(
                    imageFile = state.thumbnail, onClick = { showPicker = true })
                Text(
                    text = "Add an image that will serve as the thumbnail of the challenge.",
                    style = Caption1,
                    color = Base100
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        IconInputField(
                            iconId = R.drawable.price,
                            value = state.price,
                            onValueChange = { viewModel.onPriceChange(it) },
                            placeholder = "0",
                            inputState = priceState,
                            maxLength = 3,
                            onFocusChange = { focused ->
                                priceState =
                                    if (focused) InputState.Active else if (state.price.isEmpty()) InputState.Default else InputState.Filled
                            },
                            modifier = Modifier.width(76.dp)
                        )
                        IconInputField(
                            iconId = R.drawable.time,
                            value = state.duration,
                            onValueChange = { viewModel.onDurationChange(it) },
                            placeholder = "00:00",
                            inputState = durationState,
                            maxLength = 5,
                            onFocusChange = { focused ->
                                durationState =
                                    if (focused) InputState.Active else if (state.duration.isEmpty()) InputState.Default else InputState.Filled
                            },
                            modifier = Modifier.width(76.dp)
                        )
                        DropdownInputField(
                            iconId = R.drawable.vehicle,
                            options = VehicleType.entries.map { vehicleType ->
                                vehicleType.name.lowercase().replaceFirstChar { it.uppercase() }
                            },
                            selected = state.vehicle.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            onSelectedChange = { selected ->
                                viewModel.onVehicleChange(
                                    VehicleType.valueOf(
                                        selected.uppercase()
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    Text(
                        text = errorText,
                        style = Caption2,
                        color = Error
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconInputField(
                        iconId = R.drawable.location,
                        value = state.place,
                        onValueChange = { viewModel.onPlaceChange(it) },
                        placeholder = "Place",
                        inputState = placeState,
                        maxLength = 25,
                        onFocusChange = { focused ->
                            placeState =
                                if (focused) InputState.Active else if (state.place.isEmpty()) InputState.Default else InputState.Filled
                        },
                        modifier = Modifier
                            .weight(1f)
                            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                    )
                    DropdownInputField(
                        iconId = null,
                        options = PlaceType.entries.map { placeType ->
                            placeType.name.lowercase().replaceFirstChar { it.uppercase() }
                        },
                        selected = state.placeType.name.lowercase()
                            .replaceFirstChar { it.uppercase() },
                        onSelectedChange = { selected ->
                            viewModel.onPlaceTypeChange(
                                PlaceType.valueOf(
                                    selected.uppercase()
                                )
                            )
                        },
                        modifier = Modifier.width(100.dp)
                    )
                }
            }
        }
        PrimaryButton(
            text = "Submit",
            size = ButtonSize.Large,
            state =
                if (
                    state.title.isEmpty()
                    || state.price.isEmpty()
                    || state.duration.isEmpty()
                    || state.place.isEmpty()
                    || state.titleError != null
                    || state.priceError != null
                    || state.durationError != null
                    || state.placeError != null
                    || state.isSubmitting
                ) ButtonState.Disabled else ButtonState.Active
        ) {
            viewModel.submit(onSuccess = { navController.navigate(Routes.HOME) })
        }

        if (showPicker) {
            PickerDialog(
                onGallery = {
                    showPicker = false
                    galleryLauncher.launch("image/*")
                },
                onCamera = {
                    showPicker = false
                    val file = createTempImageFile(context)
                    tempCameraFile = file
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    cameraLauncher.launch(uri)
                },
                onDismiss = { showPicker = false }
            )
        }
    }
}