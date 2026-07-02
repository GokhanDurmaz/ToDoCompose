/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.home.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowintent.core.db.auth.GetNameUseCase
import com.flowintent.core.db.home.GetHomeCategoriesUseCase
import com.flowintent.core.util.AppEventTracker
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
    private val getNameUseCase: GetNameUseCase,
    private val eventTracker: AppEventTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val userName: StateFlow<String?> = getNameUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        eventTracker.logMessage("HomeViewModel initialized")
        loadCategories()
    }

    fun onCategoryClicked(categoryName: String) {
        val params = mapOf("category_name" to categoryName)
        eventTracker.logEvent("home_category_clicked", params)
    }

    fun onSearchClicked() {
        eventTracker.logEvent("home_search_clicked")
    }

    fun onNotificationsClicked() {
        eventTracker.logEvent("home_notifications_clicked")
    }

    fun onProfileClicked() {
        eventTracker.logEvent("home_profile_clicked")
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getHomeCategoriesUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        eventTracker.logEvent("categories_loaded_success")
                        _uiState.update { it.copy(categories = resource.data, isLoading = false) }
                    }
                    is Resource.Error -> {
                        eventTracker.logMessage("Error loading categories: ${resource.message}")
                        eventTracker.logEvent("categories_load_error")
                        _uiState.update { it.copy(isLoading = false, error = resource.message) }
                    }
                }
            }
        }
    }
}
