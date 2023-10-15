package com.example.firebasetestingapp.core.news.data_sources

import android.util.Log
import com.example.firebasetestingapp.core.news.News
import com.example.firebasetestingapp.core.news.News.Companion.mapToNews
import com.example.firebasetestingapp.core.news.NewsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NewsLocalDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore,
) {
    private val userUId get() = firebaseAuth.uid

    fun getUserNews(): Flow<List<News>> = callbackFlow {
        val docRef = firebaseFireStore
            .collection(NewsRepository.newsTable)
            .document("aWfBSvqRxiZcDGZPrLWukZItdl32")

        val listenerRegistration =
            docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val newsTmpList = mutableListOf<News>()
                    snapshot.data?.forEach {
                        mapToNews(it).let { article ->
                            newsTmpList.add(article)
                        }
                    }
                    trySend(newsTmpList)
                } else {
                    trySend(emptyList())
                }
            }
        // This block will be executed when the flow is closed
        awaitClose {
            listenerRegistration.remove() // Remove the Firestore snapshot listener
        }
    }

    fun getDetailedArticle(articleId: String): Flow<News?> = callbackFlow {
        userUId?.let { uid ->
            val docRef = firebaseFireStore
                .collection(NewsRepository.newsTable)
                .document(uid)

            val listenerRegistration =
                docRef.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("Error", "Error listening to document: $error")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        snapshot.data?.forEach {
                            mapToNews(it).let { article ->
                                if (article.id == articleId) {
                                    trySend(article).isSuccess
                                }
                            }
                        }
                    } else {
                        trySend(null).isSuccess
                    }
                }

            // This block will be executed when the flow is closed
            awaitClose {
                listenerRegistration.remove()
            }
        } ?: awaitClose { null }
    }


}