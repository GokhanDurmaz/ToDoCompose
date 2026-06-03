/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.home.GetHomeCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen, handling category retrieval.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeCategoriesUseCase: GetHomeCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getHomeCategoriesUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .collect { categories ->
                    _uiState.update { it.copy(categories = categories, isLoading = false) }
                }
        }
    }
}
