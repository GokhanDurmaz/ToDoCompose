/*
 * Copyright (c) 2026 FlowIntent. All rights reserved.
 */

package com.flowintent.test.scenarios

import com.flowintent.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object UseCaseScenarios {
    fun <T> success(data: T): Flow<Resource<T>> = flowOf(Resource.Success(data))
    
    fun <T> error(message: String): Flow<Resource<T>> = flowOf(Resource.Error(message))
    
    fun <T> loading(): Flow<Resource<T>> = flowOf(Resource.Loading)
}
