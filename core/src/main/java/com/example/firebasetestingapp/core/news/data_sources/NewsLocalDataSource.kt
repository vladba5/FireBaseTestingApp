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

class NewsLocalDataSource (
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
){

//    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val firebaseFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val userUId get() = firebaseAuth.uid

    fun getUserNews(): Flow<List<News>> = callbackFlow {
        Log.e("ptt123","123")
        Log.e("ptt123","uid is $userUId")

//        userUId?.let { uid ->
//            Log.e("ptt123","444")

            val docRef = firebaseFireStore
                .collection(NewsRepository.newsTable)
                .document("aWfBSvqRxiZcDGZPrLWukZItdl32")
            Log.e("ptt123","555")

            val listenerRegistration =
                docRef.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("ptt123","Error listening to document: $error")
                        return@addSnapshotListener
                    }

                    Log.e("ptt123","333")

                    if (snapshot != null && snapshot.exists()) {
                        Log.e("ptt123","456")

                        val newsTmpList = mutableListOf<News>()
                        snapshot.data?.forEach {
                            mapToNews(it).let { article ->
                                newsTmpList.add(article)
                            }
                        }
                        Log.e("ptt123","778")

                        trySend(newsTmpList)
                    } else {
                        Log.e("ptt123","666")

                        trySend(emptyList())
                    }
                }
            // This block will be executed when the flow is closed
            awaitClose {
                listenerRegistration.remove() // Remove the Firestore snapshot listener
                emptyList<News>()
            }
//        } ?:
//        awaitClose { emptyList<News>() }
    }

    fun getDetailedArticle(articleId: String): Flow<News?> = callbackFlow {
        userUId?.let { uid ->
            val docRef = firebaseFireStore
                .collection(NewsRepository.newsTable)
                .document(uid)

            val listenerRegistration =
                docRef.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("Error","Error listening to document: $error")
                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        snapshot.data?.forEach {
                            mapToNews(it).let { article ->
                                if(article.id == articleId){
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