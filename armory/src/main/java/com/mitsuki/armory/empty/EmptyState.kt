package com.mitsuki.armory.empty

sealed class EmptyState(val isEmpty: Boolean) {

    class Normal(isEmpty: Boolean) : EmptyState(isEmpty) {
        override fun equals(other: Any?): Boolean {
            return other is Normal && isEmpty == other.isEmpty
        }

        override fun hashCode(): Int {
            return isEmpty.hashCode()
        }
    }

    class Error(val error: Throwable) : EmptyState(true) {
        override fun equals(other: Any?): Boolean {
            return other is Error &&
                    isEmpty == other.isEmpty &&
                    error == other.error
        }

        override fun hashCode(): Int {
            return isEmpty.hashCode() + error.hashCode()
        }
    }
}