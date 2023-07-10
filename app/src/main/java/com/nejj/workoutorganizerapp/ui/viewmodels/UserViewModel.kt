package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.models.*
import com.nejj.workoutorganizerapp.repositories.FirestoreRepository
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.synchronizers.FirestoreSynchronizationManager
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserViewModel(
    val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val _isLoggingInFirstTime = MutableLiveData<Boolean>()
    val isLoggingInFirstTime: LiveData<Boolean>
        get() = _isLoggingInFirstTime

    private val firestoreRepository = FirestoreRepository("users")

    fun upsertUser(userUID: String) = viewModelScope.launch {
        var user = workoutRepository.getUserByUID(userUID)

        if(user == null) {
            user = User(null, userUID, Firebase.auth.currentUser?.email.toString(), 0.0, LocalDateTime.now(), LocalDateTime.now())
        } else {
            user.userLastLogin = LocalDateTime.now()
        }

        workoutRepository.upsertUser(user)
    }

    fun upsertEntityFirebaseLogin(userUID: String, email: String) = viewModelScope.launch {
        val userEntity = firestoreRepository.getEntityById("userUID", userUID)

        val map = mutableMapOf<String, Any>()
        map["userUID"] = userUID
        map["email"] = email

        if(userEntity.isNotEmpty()) {
            for (document in userEntity) {
                map["userLastLogin"] = LocalDateTime.now().toString()
                firestoreRepository.updateEntity(document.id, map)
            }
        } else {
            map["userCreated"] = LocalDateTime.now().toString()
            map["userLastLogin"] = LocalDateTime.now().toString()
            firestoreRepository.insertEntity(map)
        }
    }

    fun updateUserBodyweight(userUID: String, bodyweight: Double) = viewModelScope.launch {
        val userEntity = firestoreRepository.getEntityById("userUID", userUID)

        val map = mutableMapOf<String, Any>()
        map["bodyweight"] = bodyweight

        if(userEntity.isNotEmpty()) {
            for (document in userEntity) {
                firestoreRepository.updateEntity(document.id, map)
            }
        }
    }

    fun updateAllEntitiesUserUID(userUID: String) = viewModelScope.launch {
        val lastLoggedInUser = workoutRepository.getMostRecentUser()
        val firestoreSynchronizationManager = FirestoreSynchronizationManager(workoutRepository)
        if(lastLoggedInUser == null) {
            workoutRepository.batchUpdateUserUIDs(userUID)

            val userEntity = firestoreRepository.getEntityById("userUID", userUID)
            if(userEntity.isNotEmpty()) {
                syncCurrentUserLocalDataWIthFirebase(firestoreSynchronizationManager, userUID)
            }
        } else {
            if(lastLoggedInUser.userUID == userUID) {
                workoutRepository.batchUpdateUserUIDs(userUID)
            } else {
                syncCurrentUserLocalDataWIthFirebase(firestoreSynchronizationManager, userUID)
            }
        }
    }

    private suspend fun syncCurrentUserLocalDataWIthFirebase(
        firestoreSynchronizationManager: FirestoreSynchronizationManager,
        userUID: String
    ) {
        workoutRepository.nukeDatabase()
        firestoreSynchronizationManager.saveFirebaseDataToLocalDB(userUID)
    }
}