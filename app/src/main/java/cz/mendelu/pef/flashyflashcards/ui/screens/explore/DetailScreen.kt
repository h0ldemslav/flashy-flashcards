package cz.mendelu.pef.flashyflashcards.ui.screens.explore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.ramcosta.composedestinations.annotation.Destination
import cz.mendelu.pef.flashyflashcards.R
import cz.mendelu.pef.flashyflashcards.model.UiState
import cz.mendelu.pef.flashyflashcards.extensions.isLocationPermissionGranted
import cz.mendelu.pef.flashyflashcards.model.Business
import cz.mendelu.pef.flashyflashcards.model.DataSourceType
import cz.mendelu.pef.flashyflashcards.navigation.graphs.ExploreNavGraph
import cz.mendelu.pef.flashyflashcards.ui.elements.BasicScaffold
import cz.mendelu.pef.flashyflashcards.ui.elements.HyperlinkText
import cz.mendelu.pef.flashyflashcards.ui.elements.PermissionDialog
import cz.mendelu.pef.flashyflashcards.ui.elements.PlaceholderElement
import cz.mendelu.pef.flashyflashcards.ui.screens.ScreenErrors
import cz.mendelu.pef.flashyflashcards.ui.theme.PinkPrimaryLight
import cz.mendelu.pef.flashyflashcards.ui.theme.basicMargin
import cz.mendelu.pef.flashyflashcards.utils.GpsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val TestTagDetailMap = "TestTagDetailMap"

@ExploreNavGraph
@Destination
@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    dataSourceType: DataSourceType
) {
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var userLocation by remember {
        mutableStateOf<LatLng?>(null)
    }

    LaunchedEffect(Unit) {
        viewModel.getBusiness(dataSourceType)
    }

    PermissionDialog(permissions = permissions)

    BasicScaffold(
        topAppBarTitle = stringResource(id = R.string.detail),
        showLoading = viewModel.uiState.loading,
        onBackClick = { navController.popBackStack() },
        actions = {
            if (viewModel.uiState.data?.whenAdded != null) {
                IconButton(onClick = {
                    viewModel.deleteBusiness()

                    if (dataSourceType is DataSourceType.Local) {
                        navController.popBackStack()
                    }

                    Toast.makeText(
                        context,
                        context.getString(R.string.removed_from_bookmarks),
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.delete_label)
                    )
                }
            }

            RouteButton(
                context = context,
                onLocationChange = { location ->
                    if (location != null) {
                        userLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            )
        }
    ) { paddingValues ->
        DetailScreenContent(
            paddingValues = paddingValues,
            uiState = viewModel.uiState,
            userLocation = userLocation,
            actions = viewModel,
            context = context
        )
    }
}

@Composable
fun DetailScreenContent(
    paddingValues: PaddingValues,
    uiState: UiState<Business?, ScreenErrors>,
    userLocation: LatLng?,
    actions: DetailScreenActions,
    context: Context
) {
    if (uiState.data != null) {
        val uriHandler = LocalUriHandler.current

        Column(
            verticalArrangement = Arrangement.spacedBy(basicMargin()),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = basicMargin())
        ) {
            DetailScreenGoogleMap(
                businessLocation = LatLng(uiState.data!!.latitude, uiState.data!!.longitude),
                userLocation = userLocation,
                modifier = Modifier.testTag(TestTagDetailMap)
            )

            Text(
                text = uiState.data!!.name,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = uiState.data!!.displayAddress.joinToString(", ")
            )

            HyperlinkText(
                text = stringResource(id = R.string.yelp_link_text),
                uri = uiState.data!!.businessUrl,
                uriHandler = uriHandler
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (uiState.data!!.whenAdded == null) {
                    Button(
                        onClick = {
                            actions.saveBusiness()

                            Toast.makeText(
                                context,
                                context.getString(R.string.saved_to_bookmarks),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.padding(top = basicMargin())
                    ) {
                        Text(text = stringResource(id = R.string.bookmark_label))
                    }
                }
            }
        }
    }

    if (uiState.errors != null) {
        if (uiState.errors!!.messageRes == R.string.failed_to_refresh_business_try_again) {
            Toast.makeText(
                context,
                stringResource(id = uiState.errors!!.messageRes),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            PlaceholderElement(
                imageRes = uiState.errors!!.imageRes,
                textRes = uiState.errors!!.messageRes,
                paddingValues = paddingValues,
                fillMaxSize = true
            )
        }
    }
}

@Composable
fun DetailScreenGoogleMap(
    modifier: Modifier = Modifier,
    businessLocation: LatLng,
    userLocation: LatLng?,
    cameraZoom: Float = 10f
) {
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                compassEnabled = false,
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
            )
        )
    }

    val mapProperties = MapProperties(isMyLocationEnabled = userLocation != null)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            businessLocation,
            cameraZoom
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(top = 25.dp)
            .padding(bottom = 5.dp)
            .then(modifier)
    ) {
        GoogleMap(
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            modifier = Modifier
                .fillMaxSize()
                .border(BorderStroke(1.dp, PinkPrimaryLight))
        ) {
            Marker(state = MarkerState(position = businessLocation))

            if (userLocation != null) {
                Polyline(points = listOf(businessLocation, userLocation))
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun RouteButton(
    context: Context,
    onLocationChange: (Location?) -> Unit
) {
    val scope = rememberCoroutineScope()
    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    val settingResultRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == RESULT_OK) {
            scope.launch(Dispatchers.IO) {
                // Wait for GPS activation
                delay(2000)

                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    if (it != null) {
                        onLocationChange(it)
                    } else {
                        // Sometimes (even if with granted permission, enabled GPS and delay) location is null
                        // Thus below toast is provided to the user
                        val toastText = context.getString(R.string.failed_to_get_position_please_try_again)

                        Toast.makeText(
                            context,
                            toastText,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            val toastText = context.getString(R.string.failed_to_resolve_position_turned_off_gps)

            Toast.makeText(
                context,
                toastText,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    IconButton(
        onClick = {
            if (context.isLocationPermissionGranted()) {
                GpsUtils.initRequest(
                    context,
                    onEnabled = {
                        scope.launch(Dispatchers.IO) {
                            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                                onLocationChange(it)
                            }
                        }
                    },
                    onDisabled = {
                        settingResultRequest.launch(it)
                    }
                )
            } else {
                val toastText = context.getString(R.string.failed_to_resolve_position_location_permission_denied)

                Toast.makeText(
                    context,
                    toastText,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }) {
        Icon(
            imageVector = Icons.Default.Route,
            contentDescription = stringResource(id = R.string.route_fallback)
        )
    }
}