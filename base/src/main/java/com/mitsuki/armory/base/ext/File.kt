package com.mitsuki.armory.base.ext

import java.io.File


/**
 * 强制保证文件夹路径存在
 * 注：当该路径是文件时会删除文件并创建文件夹
 */
fun File.ensureDir() {
    if (exists() && isFile) {
        delete()
        mkdirs()
    } else {
        mkdirs()
    }
}

/**
 * 清除文件夹以及其子文件
 * 也可用于直接删除文件
 */
fun File.clear() {
    if (exists()) {
        if (isDirectory) {
            listFiles()?.forEach { it.clear() }
        }
        delete()
    }
}

/**
 * 去除后缀
 */
fun File.ignoreSuffixName(): String {
    val dotIndex = name.lastIndexOf(".")
    return if (dotIndex > -1) name.substring(0, dotIndex) else name
}

/**
 * 列出所有文件列表
 */
fun File.allFiles(): List<File> {
    return listFiles()?.toList()?.flatMap {
        if (it.isFile) {
            arrayListOf(it)
        } else {
            it.allFiles()
        }
    } ?: emptyList()
}