package com.mitsuki.armory.base.ext

import kotlinx.coroutines.sync.Mutex

/**
 *
 */
fun Mutex.tryUnlock(owner: Any? = null) {
    try {
        unlock(owner)
    } catch (ignore: Exception) {
    }
}

/**
 * 阻塞一下？
 */
suspend fun Mutex.justLock(owner: Any? = null){
    lock(owner)
    tryUnlock(owner)
}