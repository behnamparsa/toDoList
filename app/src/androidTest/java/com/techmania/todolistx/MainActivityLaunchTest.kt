package com.techmania.todolistx

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.lifecycle.Lifecycle
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Simple instrumentation test to verify that MainActivity launches successfully.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityLaunchTest {

    @Test
    fun mainActivity_launchesSuccessfully() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // If we reach here, the Activity launched without crashing.
            // Optionally also assert it's at least in RESUMED state.
            assertTrue(
                "MainActivity should be at least RESUMED",
                scenario.state.isAtLeast(Lifecycle.State.RESUMED)
            )
        }
    }
}
