package cz.mendelu.pef.flashyflashcards.ui.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialog(
    permissions: List<String>,
    onPermissionsResult: (Map<String, Boolean>) -> Unit = {}
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions) {
        onPermissionsResult(it)
    }

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }
}