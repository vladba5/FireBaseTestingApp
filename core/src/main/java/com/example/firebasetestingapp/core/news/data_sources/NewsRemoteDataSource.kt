package com.example.firebasetestingapp.core.news.data_sources

import android.util.Log
import com.example.firebasetestingapp.core.news.News
import com.example.firebasetestingapp.core.news.NewsRepository.Companion.newsTable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewsRemoteDataSource () {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val userUId get() = firebaseAuth.uid

    fun saveToUserArticles(article: News) {
        val userCollectionRef = userUId?.let {
            firebaseFireStore
                .collection(newsTable)
                .document(it)
        }

        userCollectionRef?.get()?.addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                userCollectionRef.update(mapOf(article.id to article))
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful || result.isComplete) {
                            Log.d("ptt","Article added to favorites: $article")
                        } else if (result.isCanceled) {
                            Log.d("ptt","error updating $article")
                        }
                    }
                    .addOnFailureListener {
                        Log.e("error", "Failed to add article to favorites: $it")
                    }
            } else {
                userCollectionRef.set(mapOf(article.id to article))
                    .addOnCompleteListener {
                        Log.d("ptt","First article added to favorites: $article")
                    }
                    .addOnFailureListener {
                        Log.e("ptt","Failed to add first article to favorites: $it")
                    }
            }
        }
    }
}
