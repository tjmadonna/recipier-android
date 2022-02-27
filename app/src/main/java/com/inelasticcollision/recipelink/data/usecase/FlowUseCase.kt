package com.inelasticcollision.recipelink.data.usecase

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class UseCaseDispatchers(
    val executionDispatcher: CoroutineDispatcher,
    val postExecutionDispatcher: CoroutineDispatcher
)

interface UseCaseObserver<T> {
    fun onSuccess(value: T)
    fun onError(error: Throwable)
}

abstract class FlowUseCase<T, in Params> constructor(
    private val dispatchers: UseCaseDispatchers
) {

    private val supervisor = SupervisorJob()

    private val scope = CoroutineScope(dispatchers.postExecutionDispatcher + supervisor)

    private var observer: UseCaseObserver<T>? = null

    abstract fun buildUseCaseFlow(params: Params? = null): Flow<T>

    open fun execute(observer: UseCaseObserver<T>, params: Params? = null) {
        this.observer = observer

        buildUseCaseFlow(params)
            .flowOn(dispatchers.executionDispatcher)
            .onEach { value -> observer.onSuccess(value) }
            .catch { error -> observer.onError(error) }
            .launchIn(scope)
    }

    fun cancel() {
        observer = null
        supervisor.cancelChildren()
    }

    fun dispose() {
        cancel()
        scope.cancel()
    }
}