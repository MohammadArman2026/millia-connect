package com.reyaz.core.common.utils

import android.util.Log


inline fun <T> safeCall(
    tag: String = "SAFE_CALL",
    operation: String = "unknown",
    block: () -> T
): Result<T> = try {
    Result.success(block())
} catch (e: Exception) {
    Log.d(tag, "Error during $operation: ${e.message}", e)
    Result.failure(e)
}


suspend inline fun <T> safeSuspendCall(
    tag: String = "SAFE_SUSPEND_CALL",
    operation: String = "unknown",
    crossinline block: suspend () -> T
): Result<T> = try {
    Result.success(block())
} catch (e: Exception) {
    Log.d(tag, "Error during $operation: ${e.message}", e)
    Result.failure(e)
}

