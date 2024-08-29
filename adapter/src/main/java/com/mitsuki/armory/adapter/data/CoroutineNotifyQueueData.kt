package com.mitsuki.armory.adapter.data

import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext


class CoroutineNotifyQueueData<T>(
    coroutineScope: CoroutineScope, diffCallback: DiffUtil.ItemCallback<T>
) : NotifyQueueData<T>(diffCallback) {
    private val mDataChangeSharedFlow: MutableSharedFlow<NotifyData<T>> = MutableSharedFlow()

    private val mutex = Mutex()
    private val mJob: Job = coroutineScope.launch(Dispatchers.Default) {
        mDataChangeSharedFlow.collect {
            mutex.withLock { handleNotifyData(it) }
        }
    }

    private suspend fun handleNotifyData(nd: NotifyData<T>) {
        withContext(Dispatchers.Default) {
            requireAdapter()?.also { adapter ->
                nd.applyChange(data, diffCallback, true)
                withContext(Dispatchers.Main) {
                    nd.dispatchChange(adapter)
                }
            } ?: run {
                nd.applyChange(data, diffCallback, false)
            }
        }
    }

    override fun postUpdate(nd: NotifyData<T>) {
        mDataChangeSharedFlow.tryEmit(nd)
    }

    suspend fun postUpdateSuspend(nd: NotifyData<T>) {
        mDataChangeSharedFlow.emit(nd)
    }

    override fun release() {
        if (!mJob.isCancelled) {
            mJob.cancel()
        }
    }
}