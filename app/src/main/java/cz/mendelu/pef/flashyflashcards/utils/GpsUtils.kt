package cz.mendelu.pef.flashyflashcards.utils

import android.content.Context
import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

object GpsUtils {

    fun initRequest(
        context: Context,
        onEnabled: () -> Unit,
        onDisabled: (IntentSenderRequest) -> Unit
    ) {
        val locationRequest = LocationRequest
            .Builder(1000)
            .setMinUpdateIntervalMillis(1000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
        val client = LocationServices.getSettingsClient(context)
        val builder = LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)
            .build()
        val gpsSettingsTask = client.checkLocationSettings(builder)

        gpsSettingsTask.addOnSuccessListener {
            onEnabled()
        }

        gpsSettingsTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    val intentSenderRequest = IntentSenderRequest
                        .Builder(exception.resolution)
                        .build()

                    onDisabled(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }
}