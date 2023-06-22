package com.nejj.workoutorganizerapp.database

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest

class SupabaseUniversalClient {


    private val supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://zjahjfspjkkjokisiquf.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpqYWhqZnNwamtram9raXNpcXVmIiwicm9sZSI6ImFub24iLCJpYXQiOjE2ODY3MjQxOTgsImV4cCI6MjAwMjMwMDE5OH0.ou8yxc2QbgU9gq8bkQOlCsXPZ_1SzM-7QG4Qej-N4lQ",
    ) {
        install(Postgrest)
        install(GoTrue)
    }

    fun getClient() : SupabaseClient {
        return supabaseClient
    }

    companion object {
        @Volatile
        private var instance: SupabaseUniversalClient? = null
        private val LOCK = Any()

        operator fun invoke() = instance ?: synchronized(LOCK) {
            instance ?: createClient().also { instance = it}
        }

        private fun createClient(): SupabaseUniversalClient {
            return SupabaseUniversalClient()
        }
    }
}