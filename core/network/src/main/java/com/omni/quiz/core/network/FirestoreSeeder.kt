package com.omni.quiz.core.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A utility class to seed the Firestore database with initial test data.
 */
@Singleton
class FirestoreSeeder @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {

    suspend fun seedDatabase() {
        Log.d("FirestoreSeeder", "🚀 Starting Database Seeding...")

        val quizzesCollection = firestore.collection("quizzes")

        val seedData = listOf(
            // 1. Vocabulary
            mapOf(
                "text" to "Samochód",
                "type" to "VOCABULARY",
                "options" to listOf("Fahrrad", "Auto", "Zug", "Flugzeug"),
                "correctOptionIndex" to 1,
                "translationHint" to "Brumm brumm! Es hat vier Räder.",
                "isEngagementOnly" to false
            ),
            // 2. Geography
            mapOf(
                "text" to "Welches Land sehen wir hier?",
                "type" to "GEOGRAPHY",
                "options" to listOf("Spanien", "Italien", "Griechenland", "Kroatien"),
                "correctOptionIndex" to 1,
                "imageUrl" to "https://images.unsplash.com/photo-1516483638261-f4dafaf00bc2?q=80&w=800&auto=format&fit=crop",
                "isEngagementOnly" to false
            ),
            // 3. Motivation
            mapOf(
                "text" to "Der einzige Weg, großartige Arbeit zu leisten, ist zu lieben, was man tut. – Steve Jobs",
                "type" to "MOTIVATION",
                "isEngagementOnly" to true,
                "options" to emptyList<String>(),
                "correctOptionIndex" to -1
            )
        )

        try {
            seedData.forEach { data ->
                val result = quizzesCollection.add(data).await()
                Log.d("FirestoreSeeder", "✅ Successfully added document with ID: ${result.id}")
            }
            Log.d("FirestoreSeeder", "🎉 Database Seeding Complete!")
            showToast("Seeding Success!")
        } catch (e: Exception) {
            Log.e("FirestoreSeeder", "❌ Error seeding database", e)
            showToast("Seeding Failed: ${e.message}")
            throw e
        }
    }

    private suspend fun showToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
