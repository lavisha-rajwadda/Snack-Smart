package com.lavisha.firstapplication

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

data class Substitute(
    val id: String = "",
    val name: String = "",
    val benefits: String = "",
    val calories: Int = 0,
    val protein: Int = 0,
    var isSaved: Boolean = false // Mutable for toggling
) {
    companion object {
        // Convert Firestore document to Substitute object
        fun fromDocument(doc: DocumentSnapshot): Substitute {
            return Substitute(
                id = doc.id, // Use document ID as unique identifier
                name = doc.getString("name") ?: "",
                benefits = doc.getString("benefits") ?: "",
                calories = doc.getLong("calories")?.toInt() ?: 0,
                protein = doc.getLong("protein")?.toInt() ?: 0,
                isSaved = false // Will be set later
            )
        }

        // Check saved status for a list of substitutes
        fun checkSavedStatus(
            userId: String,
            substitutes: List<Substitute>,
            onComplete: (List<Substitute>) -> Unit,
            onError: (Exception) -> Unit
        ) {
            if (userId.isEmpty()) {
                onComplete(substitutes)
                return
            }

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("saved_items")
                .get()
                .addOnSuccessListener { savedDocs ->
                    val savedIds = savedDocs.documents.map { it.id }
                    val updatedList = substitutes.map { sub ->
                        sub.copy(isSaved = savedIds.contains(sub.id))
                    }
                    onComplete(updatedList)
                }
                .addOnFailureListener { onError(it) }
        }

        // Toggle save/unsave for a substitute
        fun toggleSave(
            userId: String,
            substitute: Substitute,
            onSuccess: (Boolean) -> Unit, // true = saved, false = unsaved
            onError: (Exception) -> Unit
        ) {
            if (userId.isEmpty()) {
                onError(Exception("User not authenticated"))
                return
            }

            val docRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("saved_items")
                .document(substitute.id)

            if (substitute.isSaved) {
                // Unsave item
                docRef.delete()
                    .addOnSuccessListener { onSuccess(false) }
                    .addOnFailureListener { onError(it) }
            } else {
                // Save item
                docRef.set(substitute.toMap())
                    .addOnSuccessListener { onSuccess(true) }
                    .addOnFailureListener { onError(it) }
            }
        }
    }

    // Convert object to map for Firestore
    fun toMap(): Map<String, Any> = mapOf(
        "name" to name,
        "benefits" to benefits,
        "calories" to calories,
        "protein" to protein
    )
}
