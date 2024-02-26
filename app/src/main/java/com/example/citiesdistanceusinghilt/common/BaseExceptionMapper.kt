package com.example.citiesdistanceusinghilt.common

import com.example.citiesdistanceusinghilt.R
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

class BaseExceptionMapper {

    companion object {
        fun map(throwable: Throwable): BaseException {
            if (throwable is HttpException) {
                try {
                    val errorJsonObject = JSONObject(throwable.response()?.errorBody()!!.string())
                    val errorMessage = errorJsonObject.getString("message")
                    return when (throwable.code()) {
                        401 -> BaseException(BaseException.Type.AUTH, serverMessage = errorMessage)

                        else -> BaseException(BaseException.Type.SIMPLE, R.string.unknown_server_error)
                    }
                } catch (exception: Exception) {
                    Timber.e(exception)
                }
            }

            return BaseException(BaseException.Type.SIMPLE, R.string.unknown_error)
        }
    }
}