package com.example.firebasetestingapp.core

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.firebasetestingapp.core.news.NewsRepository
import com.example.firebasetestingapp.core.news.data_sources.NewsLocalDataSource
import com.example.firebasetestingapp.core.news.data_sources.NewsRemoteDataSource
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.firebasetestingapp.core.test", appContext.packageName)
    }

    @Test
    fun test() = runBlocking {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val options = FirebaseOptions.Builder()
            .setApplicationId("com.example.news_pulse.testing")
            .setApiKey("AIzaSyDqaONmx1Au3KPtrUxEnQBcT6p8ACK4jEI")
            .setProjectId("news-pulse-3b672")
            .setDatabaseUrl("https://com.example.news_pulse.firebaseio.com")
            .setStorageBucket("news-pulse-3b672.appspot.com")
            .build()

        FirebaseApp.initializeApp(appContext, options)

        val remote = NewsRemoteDataSource(
            firebaseAuth = FirebaseAuth.getInstance(),
            firebaseFireStore = FirebaseFirestore.getInstance()
        )

        val local = NewsLocalDataSource(
            firebaseAuth = FirebaseAuth.getInstance(),
            firebaseFireStore = FirebaseFirestore.getInstance()
        )

        val repo  = NewsRepository(
            remote = remote,
            local = local
        )

        repo.observeUserNews().collect{
            assertNotNull(it)
            assertTrue(it.isNotEmpty())
        }
    }
}