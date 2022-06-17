package study.heltoe.movieapp.utils

sealed class StateData<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : StateData<T>(data)
    class Error<T>(message: String, data: T? = null) : StateData<T>(data, message)
    class Loading<T> : StateData<T>()
    class Default<T>(data: T) : StateData<T>(data)
}
