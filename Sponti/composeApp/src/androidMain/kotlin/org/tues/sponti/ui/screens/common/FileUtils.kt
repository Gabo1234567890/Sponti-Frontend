package org.tues.sponti.ui.screens.common

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun Context.createTempImageFile(): File {
    val dir = File(cacheDir, "images").apply { mkdirs() }
    return File.createTempFile("challenge_thumbnail_", ".jpg", dir)
}

fun Uri.toTempFile(context: Context): File? {
    val inputStream = context.contentResolver.openInputStream(this) ?: return null

    val dir = File(context.cacheDir, "images").apply { mkdirs() }
    val tempFile = File.createTempFile("challenge_thumbnail_", ".jpg", dir)

    FileOutputStream(tempFile).use { outputStream ->
        inputStream.copyTo(outputStream)
    }

    return tempFile
}