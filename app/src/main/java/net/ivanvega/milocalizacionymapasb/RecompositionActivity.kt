package net.ivanvega.milocalizacionymapasb


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import net.ivanvega.milocalizacionymapasb.ui.theme.MapsComposeSampleTheme
import kotlin.random.Random

private const val TAG = "RecompositionActivity"

/**
 * This is a sample activity showcasing how the recomposition works. The location is changed
 * every time we click on the button, and the marker gets updated (removed and added in a new
 * location)
 */
class RecompositionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val cameraPositionState = rememberCameraPositionState {
                position = defaultCameraPosition
            }
            Box(Modifier.fillMaxSize()) {
                MapsComposeSampleTheme {
                    GoogleMapView(
                        modifier = Modifier.matchParentSize(),
                        cameraPositionState = cameraPositionState
                    )
                }
            }
        }
    }

    @Composable
    fun GoogleMapView(
        modifier: Modifier = Modifier,
        cameraPositionState: CameraPositionState = rememberCameraPositionState(),
        content: @Composable () -> Unit = {},
    ) {
        val markerState = rememberMarkerState(position = singapore)

        val uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
        val mapProperties by remember {
            mutableStateOf(MapProperties(mapType = MapType.NORMAL))
        }

        val mapVisible by remember { mutableStateOf(true) }
        if (mapVisible) {
            GoogleMap(
                modifier = modifier,
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
                onPOIClick = {
                    Log.d(TAG, "POI clicked: ${it.name}")
                }
            ) {

                val markerClick: (Marker) -> Boolean = {
                    Log.d(TAG, "${it.title} was clicked")
                    cameraPositionState.projection?.let { projection ->
                        Log.d(TAG, "The current projection is: $projection")
                    }
                    false
                }

                Marker(
                    state = markerState,
                    title = "Marker in Singapore",
                    onClick = markerClick
                )

                content()
            }
            Column {
                Button(onClick = {
                    val randomValue = Random.nextInt(3)
                    markerState.position = when (randomValue) {
                        0 -> singapore
                        1 -> singapore2
                        2 -> singapore3
                        else -> singapore
                    }
                }) {
                    Text("Change Location")
                }
            }
        }
    }
}