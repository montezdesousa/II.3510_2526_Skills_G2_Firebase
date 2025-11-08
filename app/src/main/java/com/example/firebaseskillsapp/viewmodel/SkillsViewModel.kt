package com.example.firebaseskillsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Skill(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

class SkillsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _skillsState = MutableStateFlow<SkillsState>(SkillsState.Idle)
    val skillsState: StateFlow<SkillsState> = _skillsState

    private val _skills = MutableStateFlow<List<Skill>>(emptyList())
    val skills: StateFlow<List<Skill>> = _skills

    init {
        loadSkills()
    }

    private fun loadSkills() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _skillsState.value = SkillsState.Error("User not authenticated")
            return
        }

        db.collection("skills")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    if (error.code == com.google.firebase.firestore.FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        _skillsState.value = SkillsState.Error("Permission denied. Please update Firestore security rules.")
                    } else {
                        _skillsState.value = SkillsState.Error(error.localizedMessage ?: "Failed to load skills")
                    }
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val skillsList = snapshot.documents.mapNotNull { doc ->
                        Skill(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            userId = doc.getString("userId") ?: "",
                            timestamp = doc.getLong("timestamp") ?: 0L
                        )
                    }
                    _skills.value = skillsList
                    if (skillsList.isEmpty()) {
                        _skillsState.value = SkillsState.Idle
                    }
                }
            }
    }

    fun addSkill(skillName: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            _skillsState.value = SkillsState.Error("User not authenticated")
            return
        }

        if (skillName.isBlank()) {
            _skillsState.value = SkillsState.Error("Skill name cannot be empty")
            return
        }

        _skillsState.value = SkillsState.Loading

        val skill = hashMapOf(
            "name" to skillName,
            "userId" to userId,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("skills")
            .add(skill)
            .addOnSuccessListener {
                _skillsState.value = SkillsState.Success("Skill added successfully!")
                // Reset state after 2 seconds
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    _skillsState.value = SkillsState.Idle
                }, 2000)
            }
            .addOnFailureListener { exception ->
                _skillsState.value = SkillsState.Error(exception.localizedMessage ?: "Failed to add skill")
            }
    }

    fun deleteSkill(skillId: String) {
        db.collection("skills")
            .document(skillId)
            .delete()
            .addOnSuccessListener {
                _skillsState.value = SkillsState.Success("Skill deleted!")
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    _skillsState.value = SkillsState.Idle
                }, 2000)
            }
            .addOnFailureListener { exception ->
                _skillsState.value = SkillsState.Error(exception.localizedMessage ?: "Failed to delete skill")
            }
    }

    fun setError(message: String) {
        _skillsState.value = SkillsState.Error(message)
    }
}

sealed class SkillsState {
    object Idle : SkillsState()
    object Loading : SkillsState()
    data class Success(val message: String) : SkillsState()
    data class Error(val message: String) : SkillsState()
}