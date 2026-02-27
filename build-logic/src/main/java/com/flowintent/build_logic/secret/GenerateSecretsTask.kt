package com.flowintent.build_logic.secret

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class GenerateSecretsTask : DefaultTask() {

    @get:Input
    abstract val secrets: MapProperty<String, String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun generate() {
        val allSecrets = secrets.get()
        val key = allSecrets["XOR_KEY"] ?: "default_salt"

        val sb = StringBuilder()

        sb.appendLine("#ifndef SECRETS_H")
        sb.appendLine("#define SECRETS_H")
        sb.appendLine("#include <stdint.h>")
        sb.appendLine("#include <stddef.h>")
        sb.appendLine("")
        sb.appendLine("static const char* XOR_KEY = \"$key\";")
        sb.appendLine("")

        allSecrets.filter { it.key != "XOR_KEY" }.forEach { (name, value) ->
            val bytes = value.toByteArray(Charsets.UTF_8)
            val xorBytes = key.toByteArray(Charsets.UTF_8)

            val encrypted = bytes.mapIndexed { index, b ->
                (b.toInt() xor xorBytes[index % xorBytes.size].toInt()) and 0xFF
            }

            val hexString = encrypted.joinToString(", ") { "0x${Integer.toHexString(it)}" }

            sb.appendLine("static const uint8_t ${name}_ENC[] = { $hexString };")
            sb.appendLine("static const size_t ${name}_LEN = ${bytes.size};")
            sb.appendLine("")
        }

        sb.appendLine("#endif")

        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(sb.toString())
    }
}
