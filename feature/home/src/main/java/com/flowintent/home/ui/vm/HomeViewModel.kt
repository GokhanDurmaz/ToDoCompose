/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.GetNameUseCase
import com.flowintent.core.db.home.GetHomeCategoriesUseCase
import com.flowintent.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen, handling category retrieval and user info.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeCategoriesUseCase: GetHomeCategoriesUseCase,
    private val getNameUseCase: GetNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val userName: StateFlow<String?> = getNameUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getHomeCategoriesUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(categories = resource.data, isLoading = false) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = resource.message) }
                    }
                }
            }
        }
    }
}
