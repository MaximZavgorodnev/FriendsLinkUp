package ru.maxpek.friendslinkup.error

import android.database.SQLException
import ru.maxpek.friendslinkup.R
import java.io.IOException

sealed class AppError(var code: String): RuntimeException() {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetworkError
            else -> UnknownError
        }
    }
}
class ApiError(val status: Int, code: String): AppError(code)
object NetworkError : AppError((R.string.error_network).toString())
object UnknownError: AppError((R.string.error_unknown).toString())
object DbError : AppError((R.string.error_db).toString())