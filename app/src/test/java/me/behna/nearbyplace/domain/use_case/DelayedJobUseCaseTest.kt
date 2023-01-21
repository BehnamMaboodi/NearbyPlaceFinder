package me.behna.nearbyplace.domain.use_case

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.behna.nearbyplace.utilities.Constants
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class DelayedJobUseCaseTest {
    @Test
    fun `test invoke with default delay`() = runBlocking {
        val coroutineScope = CoroutineScope(Dispatchers.Unconfined)
        val coroutineContext = Dispatchers.Unconfined
        val useCase = DelayedJobUseCase(coroutineScope, coroutineContext)
        var listenerInvoked = false
        useCase { listenerInvoked = true }
        assertFalse(listenerInvoked)
        delay(Constants.DEFAULT_JOB_DELAY + 100)
        assertTrue(listenerInvoked)
    }

    @Test
    fun `test invoke with custom delay`() = runBlocking {
        val coroutineScope = CoroutineScope(Dispatchers.Unconfined)
        val coroutineContext = Dispatchers.Unconfined
        val useCase = DelayedJobUseCase(coroutineScope, coroutineContext)
        var listenerInvoked = false
        val customDelay = Constants.DEFAULT_JOB_DELAY + 1000
        useCase(customDelay) { listenerInvoked = true }
        assertFalse(listenerInvoked)
        delay(customDelay + 100)
        assertTrue(listenerInvoked)
    }

    @Test
    fun `test invoke cancel previous job`() = runBlocking {
        val coroutineScope = CoroutineScope(Dispatchers.Unconfined)
        val coroutineContext = Dispatchers.Unconfined
        val useCase = DelayedJobUseCase(coroutineScope, coroutineContext)
        var listenerInvoked = false
        useCase { listenerInvoked = true }
        assertFalse(listenerInvoked)
        useCase { listenerInvoked = false }
        delay(Constants.DEFAULT_JOB_DELAY + 100)
        assertFalse(listenerInvoked)
    }
}