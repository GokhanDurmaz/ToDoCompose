/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.home.GetHomeCategoriesUseCase
import com.flowintent.core.db.repository.EncryptedProtoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
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
    private val repo: EncryptedProtoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val userName: StateFlow<String?> = repo.nameFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

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
