package com.flowintent.build_logic.secret

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface AndroidSecretExtension {
    val libName: Property<String>
    val cppFileName: Property<String>
    val cmakeVersion: Property<String>
    val cmakePath: Property<String>
    val secretKeys: ListProperty<String>
    val linkedLibraries: ListProperty<String>
}
