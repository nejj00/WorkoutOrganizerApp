package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.*
import com.nejj.workoutorganizerapp.repositories.FirestoreRepository
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.synchronizers.FirestoreSynchronizationManager
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    private val _isLoggingInFirstTime = MutableLiveData<Boolean>()
    val isLoggingInFirstTime: LiveData<Boolean>
        get() = _isLoggingInFirstTime

    private val firestoreRepository = FirestoreRepository("users")

    suspend fun getLastLoggedInUserFirebase(): User? {
        val userSnapshot = firestoreRepository.getLastLoggedInUser()

        if(userSnapshot.isNotEmpty()) {
            val document = userSnapshot[0]

            val userUID = document.data?.get("userUID") as String
            val email = document.data?.get("email") as String
            val bodyweight = document.data?.get("bodyweight") as Double
            val userCreated = LocalDateTime.parse(document.data?.get("userCreated") as String, DateTimeFormatter.ISO_LOCAL_DATE)
            val userLastLogin = LocalDateTime.parse(document.data?.get("userLastLogin") as String, DateTimeFormatter.ISO_LOCAL_DATE)

            return User(userUID, email, bodyweight, userCreated, userLastLogin)
        }

        return null
    }
    fun upsertEntity(entity: LastLoggedInUser) = viewModelScope.launch {
        val entites = getLastLoggedInUserList()
        entites.forEach{ deleteEntity(it) }

        workoutRepository.upsertLastLoggedInUser(entity)
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
        val lastLoggedInUser = getLastLoggedInUserFirebase()
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

    fun deleteEntity(entity: LastLoggedInUser) = viewModelScope.launch {
        workoutRepository.deleteLastLoggedInUser(entity)
    }

    fun getLastLoggedInUser() = workoutRepository.getLastLoggedInUser()

    suspend fun getLastLoggedInUserList() = workoutRepository.getLastLoggedInUserList()
}