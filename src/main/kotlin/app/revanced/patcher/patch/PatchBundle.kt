@file:Suppress("unused")

package app.revanced.patcher.patch

import java.io.File
import java.util.jar.Attributes
import java.util.jar.JarFile

data class PatchBundle(val patchesJar: String) {
    val patches by lazy {
        runCatching {
            loadPatchesFromJar(setOf(File(patchesJar)))
        }.getOrNull()
    }

    val manifestAttributes by lazy {
        runCatching {
            JarFile(patchesJar).use { jar ->
                jar.manifest?.mainAttributes?.let {
                    ManifestAttributes(
                        name = it.string("name"),
                        version = it.string("version"),
                        description = it.string("description"),
                        source = it.string("source"),
                        author = it.string("author"),
                        contact = it.string("contact"),
                        website = it.string("website"),
                        license = it.string("license")
                    )
                }
            }
        }.getOrNull()
    }

    data class ManifestAttributes(
        val name: String?,
        val version: String?,
        val description: String?,
        val source: String?,
        val author: String?,
        val contact: String?,
        val website: String?,
        val license: String?
    )
}

fun Attributes.string(name: String): String? =
    getValue(name)?.takeIf { it.isNotBlank() } // If empty, set it to null instead.
