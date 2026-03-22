package com.flowintent.profile.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.flowintent.core.util.Resource
import com.flowintent.profile.ui.vm.ProfileViewModel
import com.flowintent.uikit.util.VAL_24
import com.flowintent.uikit.util.VAL_32

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileImageScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            viewModel.uploadImage(it)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile Picture", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClicked() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(VAL_24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    val profileUrl = userProfile?.profileImageUrl
                    val displayImage: Any? = selectedImageUri ?: profileUrl

                    if (displayImage != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(displayImage)
                                .allowHardware(false)
                                .diskCachePolicy(coil.request.CachePolicy.DISABLED)
                                .memoryCachePolicy(coil.request.CachePolicy.DISABLED)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onSuccess = { println("Coil: Upload the image file successfully.") },
                            onError = { error ->
                                println("Coil Err: ${error.result.throwable.message}")
                            }
                        )
                    } else {
                        Icon(
                            Icons.Default.Person,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }

                    if (uploadState is Resource.Loading) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                }

                SmallFloatingActionButton(
                    onClick = { launcher.launch("image/*") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Edit", modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(VAL_32.dp))

            Text(
                "Pick a photo that represents you in FlowIntent.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(VAL_24.dp))

            if (uploadState is Resource.Error) {
                Text(
                    text = (uploadState as Resource.Error).message ?: "Error uploading image",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
