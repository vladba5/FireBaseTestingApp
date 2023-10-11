package com.example.firebasetestingapp.core

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.firebasetestingapp.core.news.NewsRepository
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ExampleInstrumentedTest {

    @Inject
    lateinit var newsRepository: NewsRepository

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val options = FirebaseOptions.Builder()
            .setApplicationId("com.example.news_pulse.testing")
            .setApiKey("AIzaSyDqaONmx1Au3KPtrUxEnQBcT6p8ACK4jEI")
            .setProjectId("news-pulse-3b672")
            .setDatabaseUrl("https://com.example.news_pulse.firebaseio.com")
            .setStorageBucket("news-pulse-3b672.appspot.com")
            .build()

        FirebaseApp.initializeApp(appContext, options)

        hiltRule.inject()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.firebasetestingapp.core.test", appContext.packageName)
    }

    @Test
    fun test() = runBlocking {
        newsRepository.observeUserNews().collect{
            Log.d("ptt", "vlad $it")
            assertNotNull(it)
            it.forEach {  article ->
                Log.d("ptt", article.toString())
            }
            assertTrue(it.isNotEmpty())
        }
    }
}