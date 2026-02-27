package com.flowintent.build_logic.secret

data class SecretConfig(
    val libName: String = "native_secrets",
    val cppFileName: String = "native-lib.cpp",
    val cmakeVersion: String = "3.22.1",
    val cmakePath: String = "src/main/cpp/CMakeLists.txt",
    val linkedLibraries: List<String> = listOf("log"),
    val secretKeys: List<String> = emptyList()
)
