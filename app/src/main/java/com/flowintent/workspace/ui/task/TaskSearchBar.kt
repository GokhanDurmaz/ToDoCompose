package com.flowintent.workspace.ui.task

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.flowintent.uikit.util.VAL_12
import com.flowintent.uikit.util.VAL_50
import com.flowintent.workspace.ui.search.SearchBar

@Composable
fun TaskSearchBar(query: String, onQueryChange: (String) -> Unit) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = VAL_12.dp, end = VAL_12.dp, bottom = VAL_12.dp)
            .height(VAL_50.dp)
    )
}
