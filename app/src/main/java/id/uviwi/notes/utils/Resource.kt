package id.uviwi.notes.utils

data class Resource<out T>(val status: State, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = State.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = State.ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> =
            Resource(status = State.LOADING, data = data, message = null)
    }
}
