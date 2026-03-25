package com.flowintent.core.util

/**
 * This annotation indicates that a dependency is injected specifically to trigger
 * its initialization or background side effects (e.g, App Check Debug Token generation)
 * even if it is not directly called within the class methods.
 * DO NOT REMOVE
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class RequiredForInit(val reason: String = "")
