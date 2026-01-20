package com.flowintent.workspace.ui.swipe

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.flowintent.workspace.util.COLOR_0XFF42A5F5
import com.flowintent.workspace.util.COLOR_0XFFEF5350
import com.flowintent.workspace.util.VAL_0_0
import com.flowintent.workspace.util.VAL_12
import com.flowintent.workspace.util.VAL_1_0
import com.flowintent.workspace.util.VAL_200
import com.flowintent.workspace.util.VAL_300
import com.flowintent.workspace.util.VAL_4
import com.flowintent.workspace.util.VAL_50
import com.flowintent.workspace.util.VAL_8
import kotlinx.coroutines.launch

@Composable
fun SwipeActions(
    modifier: Modifier = Modifier,
    stateHolder: SwipeStateHolder,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val fraction = (-stateHolder.offsetX.value / stateHolder.maxSwipe)
        .coerceIn(VAL_0_0, VAL_1_0)

    Row(
        modifier = modifier
            .fillMaxHeight()
            .width(VAL_200.dp)
            .clip(RoundedCornerShape(topEnd = VAL_12.dp, bottomEnd = VAL_12.dp))
            .background(Color.Transparent)
            .padding(end = VAL_8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {
                onDelete()
                stateHolder.scope.launch {
                    stateHolder.offsetX.animateTo(VAL_0_0, tween(VAL_300))
                }
            },
            shape = RoundedCornerShape(VAL_50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(COLOR_0XFFEF5350),
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = VAL_12.dp, vertical = VAL_8.dp),
            modifier = Modifier.alpha(fraction)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
            Text("Delete", Modifier.padding(start = VAL_4.dp))
        }

        Button(
            onClick = { onEdit() },
            shape = RoundedCornerShape(VAL_50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(COLOR_0XFF42A5F5),
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = VAL_12.dp, vertical = VAL_8.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
            Text("Edit", Modifier.padding(start = VAL_4.dp))
        }
    }
}

data class SwipeActionCallbacks(
    val onDelete: () -> Unit,
    val onEdit: () -> Unit,
    val onHeightChange: (Dp) -> Unit
)
