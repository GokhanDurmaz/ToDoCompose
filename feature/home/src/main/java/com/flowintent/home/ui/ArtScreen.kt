/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.flowintent.home.R
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_16
import com.flowintent.uikit.util.VAL_20
import com.flowintent.uikit.util.VAL_8
import androidx.compose.ui.graphics.Brush as GBrush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtScreen(onBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.art_gallery_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = stringResource(R.string.back_desc), modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = VAL_16.dp)
        ) {
            ArtFeaturedCard()
            Spacer(modifier = Modifier.height(VAL_20.dp))
            Text(stringResource(R.string.your_creations_label), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(VAL_12.dp))
            
            val artItems = listOf(
                ArtItem(stringResource(R.string.sketching), Icons.Default.Brush, Color(0xFFFF5722)),
                ArtItem(stringResource(R.string.oil_painting), Icons.Default.Palette, Color(0xFFE91E63)),
                ArtItem(stringResource(R.string.coloring), Icons.Default.ColorLens, Color(0xFF2196F3)),
                ArtItem(stringResource(R.string.digital_art), Icons.Default.Palette, Color(0xFF9C27B0))
            )
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(VAL_12.dp),
                verticalArrangement = Arrangement.spacedBy(VAL_12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(artItems) { item ->
                    ArtGalleryItem(item)
                }
            }
        }
    }
}

@Composable
private fun ArtFeaturedCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(VAL_20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    GBrush.linearGradient(
                        listOf(Color(0xFF6200EE), Color(0xFF03DAC6))
                    )
                )
                .padding(VAL_20.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                Text(stringResource(R.string.daily_inspiration_label), color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(stringResource(R.string.inspiration_subtitle), color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                Icons.Default.Palette,
                contentDescription = null,
                modifier = Modifier.size(64.dp).align(Alignment.TopEnd),
                tint = Color.White.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
private fun ArtGalleryItem(item: ArtItem) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f),
        shape = RoundedCornerShape(VAL_16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.height(VAL_8.dp))
                Text(item.title, fontWeight = FontWeight.Medium, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

private data class ArtItem(val title: String, val icon: ImageVector, val color: Color)
