/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.profile.ui

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.flowintent.core.util.Resource
import com.flowintent.navigation.nav.ProfileNavigation
import com.flowintent.profile.R
import com.flowintent.profile.ui.vm.ProfileViewModel
import com.flowintent.uikit.anim.shimmerEffect
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_24
import com.flowintent.uikit.util.VAL_32
import com.flowintent.uikit.util.VAL_4
import com.flowintent.uikit.util.VAL_8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by profileViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        profileViewModel.reloadProfileImageIfNull()
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileViewModel.uploadImage(it) }
    }

    LaunchedEffect(uiState.uploadState) {
        when (uiState.uploadState) {
            is Resource.Success -> {
                profileViewModel.clearUploadState()
            }
            is Resource.Error -> {
                println("Upload Err: ${(uiState.uploadState as Resource.Error).message}")
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.profile_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { profileViewModel.onBackClicked() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.back_desc),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = VAL_16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileLargeHeader(
                username = uiState.userProfile?.name,
                email = uiState.userProfile?.email,
                profileImageUrl = uiState.userProfile?.profileImageUrl,
                localBitmap = uiState.profileBitmap,
                onImageClick = { imagePickerLauncher.launch("image/*") },
                isLoading = uiState.uploadState is Resource.Loading,
                isDataLoading = uiState.isProfileLoading
            )

            Spacer(modifier = Modifier.height(VAL_20.dp))

            StatsSection(onPendingClick = {
                profileViewModel.onNavigateTo(ProfileNavigation.PENDING_TASKS)
            })

            if (uiState.isProfileLoading) {
                ProfileInfoShimmer()
            } else {
                ProfileInfoSection(
                    username = uiState.userProfile?.name ?: stringResource(R.string.default_user),
                    email = uiState.userProfile?.email ?: stringResource(R.string.default_email)
                )
            }

            ActionSection(onActionClick = { destination ->
                profileViewModel.onNavigateTo(destination)
            })

            Spacer(modifier = Modifier.height(VAL_32.dp))
        }
    }
}

@Composable
private fun ProfileLargeHeader(
    username: String?,
    email: String?,
    profileImageUrl: String?,
    localBitmap: Bitmap?,
    onImageClick: () -> Unit,
    isLoading: Boolean,
    isDataLoading: Boolean
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = VAL_24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .then(
                    if (isDataLoading) Modifier.shimmerEffect()
                    else Modifier.background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)))
                )
                .clickable(enabled = !isLoading && !isDataLoading) { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (localBitmap != null) {
                Image(
                    bitmap = localBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (!profileImageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profileImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else if (!isDataLoading) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(64.dp)
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp), color = Color.White, strokeWidth = 3.dp)
                }
            }
        }

        Spacer(modifier = Modifier.height(VAL_16.dp))

        if (isDataLoading) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(28.dp)
                    .clip(RoundedCornerShape(VAL_8.dp))
                    .shimmerEffect()
            )
        } else {
            Text(text = username ?: stringResource(R.string.default_user), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(VAL_8.dp))

        if (isDataLoading) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(VAL_4.dp))
                    .shimmerEffect()
            )
        } else {
            Text(text = email ?: stringResource(R.string.default_email), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun StatsSection(onPendingClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VAL_8.dp)
    ) {
        StatCard(Modifier.weight(1f), stringResource(R.string.pending_label), "5", Icons.AutoMirrored.Filled.List, onClick = onPendingClick)
        StatCard(Modifier.weight(1f), stringResource(R.string.done_label), "12", Icons.Default.CheckCircle)
        StatCard(Modifier.weight(1f), stringResource(R.string.success_label), "70%", Icons.Default.Speed)
    }
}

@Composable
private fun StatCard(modifier: Modifier, title: String, value: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(VAL_16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = VAL_16.dp, horizontal = VAL_8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(VAL_8.dp))
            Text(
                text = value,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProfileInfoSection(username: String, email: String) {
    ProfileContentSection(title = stringResource(R.string.account_details_label)) {
        InfoRow(label = stringResource(R.string.username_label), value = username, icon = Icons.Default.Person)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
        InfoRow(label = stringResource(R.string.email_label), value = email, icon = Icons.Default.Email)
    }
}

@Composable
private fun ProfileInfoShimmer() {
    ProfileContentSection(title = stringResource(R.string.account_details_label)) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(20.dp).clip(CircleShape).shimmerEffect())
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Box(modifier = Modifier.width(80.dp).height(12.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.width(150.dp).height(18.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(20.dp).clip(CircleShape).shimmerEffect())
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Box(modifier = Modifier.width(60.dp).height(12.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(modifier = Modifier.width(200.dp).height(18.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
                }
            }
        }
    }
}

@Composable
private fun ActionSection(onActionClick: (ProfileNavigation) -> Unit) {
    ProfileContentSection(title = stringResource(R.string.security_label)) {
        ActionRow(
            label = stringResource(R.string.change_password_label),
            icon = Icons.Default.Lock,
            onClick = { onActionClick(ProfileNavigation.CHANGE_PASSWORD) }
        )
        ActionRow(
            label = stringResource(R.string.two_factor_auth_label),
            icon = Icons.Default.VerifiedUser,
            onClick = { onActionClick(ProfileNavigation.TWO_FACTOR_AUTH) }
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun ActionRow(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun ProfileContentSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(VAL_16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = VAL_8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        Column(modifier = Modifier.padding(VAL_16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(VAL_12.dp))
            content()
        }
    }
}
