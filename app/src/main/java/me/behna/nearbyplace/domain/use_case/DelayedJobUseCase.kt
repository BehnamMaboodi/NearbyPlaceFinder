package me.behna.nearbyplace.domain.use_case

import kotlinx.coroutines.*
import me.behna.nearbyplace.utilities.Constants
import kotlin.coroutines.CoroutineContext

class DelayedJobUseCase(
    private val coroutineScope: CoroutineScope,
    private val coroutineContext: CoroutineContext,
    private var delay: Long = Constants.DEFAULT_JOB_DELAY
) : UseCase {
    private var delayedJob: Job? = null

    operator fun invoke(delay: Long? = Constants.DEFAULT_JOB_DELAY, listener: () -> Unit) {
        delay?.let { this.delay = it }
        delayedJob?.cancel()
        delayedJob = coroutineScope.launch(coroutineContext) {
            delay(this@DelayedJobUseCase.delay)
            if (isActive) listener()
        }
    }

}