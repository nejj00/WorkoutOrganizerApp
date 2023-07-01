package com.nejj.workoutorganizerapp.util

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessActivities
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.SessionInsertRequest
import com.nejj.workoutorganizerapp.ui.MainActivity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class GoogleFitTester(val apiContext: Context, val activity: Activity) {

    val fitnessOptions: FitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_WRITE)
        .addDataType(DataType.TYPE_WORKOUT_EXERCISE, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.TYPE_WORKOUT_EXERCISE, FitnessOptions.ACCESS_WRITE)
        .build()

    private val TAG = "GoogleFitTester"

    fun record() {
        val account = GoogleSignIn.getAccountForExtension(apiContext, fitnessOptions)
        if (GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            println("Has permissions")
        } else {
            GoogleSignIn.requestPermissions(
                activity,
                1001,
                account,
                fitnessOptions
            );
        }

        Fitness.getRecordingClient(
            activity,
            GoogleSignIn.getAccountForExtension(apiContext, fitnessOptions)
        )
            .subscribe(DataType.TYPE_WEIGHT)
            .addOnSuccessListener {
                Log.i(MainActivity.TAG, "Successfully subscribed!")
            }
            .addOnFailureListener { e ->
                Log.w(MainActivity.TAG, "There was a problem subscribing.", e)
            }
    }

    fun readRequest() {
        var endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        var startTime = endTime.minusWeeks(1)
        Log.i(MainActivity.TAG, "Range Start: $startTime")
        Log.i(MainActivity.TAG, "Range End: $endTime")

        val readRequest =
            DataReadRequest.Builder()
                // The data request can specify multiple data types to return,
                // effectively combining multiple data queries into one call.
                // This example demonstrates aggregating only one data type.
                .read(DataType.TYPE_WEIGHT)
                // Analogous to a "Group By" in SQL, defines how data should be
                // aggregated.
                // bucketByTime allows for a time span, whereas bucketBySession allows
                // bucketing by <a href="/fit/android/using-sessions">sessions</a>.
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build()

        Fitness.getHistoryClient(activity, GoogleSignIn.getAccountForExtension(apiContext, fitnessOptions))
            .readData(readRequest)
            .addOnSuccessListener { response ->
                // The aggregate query puts datasets into buckets, so flatten into a
                // single list of datasets
                for (dataSet in response.buckets.flatMap { it.dataSets }) {
                    dumpDataSet(dataSet)
                }
            }
            .addOnFailureListener { e ->
                Log.w(MainActivity.TAG,"There was an error reading data from Google Fit", e)
            }
    }

    private fun dumpDataSet(dataSet: DataSet) {
        Log.i(MainActivity.TAG, "Data returned for Data type: ${dataSet.dataType.name}")
        for (dp in dataSet.dataPoints) {
            Log.i(MainActivity.TAG,"Data point:")
            Log.i(MainActivity.TAG,"\tType: ${dp.dataType.name}")
            Log.i(MainActivity.TAG,"\tStart: ${dp.getStartTimeString()}")
            Log.i(MainActivity.TAG,"\tEnd: ${dp.getEndTimeString()}")
            for (field in dp.dataType.fields) {
                Log.i(MainActivity.TAG,"\tField: ${field.name.toString()} Value: ${dp.getValue(field)}")
            }
        }
    }
    fun DataPoint.getStartTimeString() = Instant.ofEpochSecond(this.getStartTime(TimeUnit.SECONDS))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().toString()

    fun DataPoint.getEndTimeString() = Instant.ofEpochSecond(this.getEndTime(TimeUnit.SECONDS))
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime().toString()


    fun insertHistoryData() {
        // Declare that the data being inserted was collected during the past hour.
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusHours(1)

        // Create a data source
        val dataSource = DataSource.Builder()
            .setAppPackageName(apiContext)
            .setDataType(DataType.TYPE_WORKOUT_EXERCISE)
            .setStreamName(MainActivity.TAG)
            .setType(DataSource.TYPE_RAW)
            .build()

        // For each data point, specify a start time, end time, and the
        // data value -- in this case, 950 new steps.
        val stepCountDelta = 950
        val dataPoint =
            DataPoint.builder(dataSource)
                .setField(Field.FIELD_EXERCISE, FitnessActivities.WEIGHTLIFTING)
                .setTimeInterval(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
                .build()

        val dataSet = DataSet.builder(dataSource)
            .add(dataPoint)
            .build()

        Fitness.getHistoryClient(activity, GoogleSignIn.getAccountForExtension(apiContext, fitnessOptions))
            .insertData(dataSet)
            .addOnSuccessListener {
                Log.i(MainActivity.TAG, "DataSet added successfully!")
            }
            .addOnFailureListener { e ->
                Log.w(MainActivity.TAG, "There was an error adding the DataSet", e)
            }

    }

    fun sessionInsertActivity(workoutName: String) {
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusHours(1)

        // Create a session with metadata about the activity.
        val session = Session.Builder()
            .setName("Weightlifting session")
            .setIdentifier("UniqueIdentifierHere")
            .setDescription(workoutName)

            .setActivity(FitnessActivities.WEIGHTLIFTING)
            .setStartTime(startTime.toEpochSecond(), TimeUnit.MILLISECONDS)
            .setEndTime(endTime.toEpochSecond(), TimeUnit.MILLISECONDS)
            .build()

        // Build a session insert request
        val insertRequest = SessionInsertRequest.Builder()
            .setSession(session)
            // Optionally add DataSets for this session.
            //.addDataSet(dataset)
            .build()

        Fitness.getSessionsClient(activity, GoogleSignIn.getAccountForExtension(apiContext, fitnessOptions))
            .insertSession(insertRequest)
            .addOnSuccessListener {
                Log.i(TAG, "Session insert was successful!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was a problem inserting the session: ", e)
            }
    }

    fun activitySegmentTest() {
        val endTime = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val startTime = endTime.minusMinutes(30)
        val startWalkTime = startTime.plusMinutes(10)
        val endWalkTime = startWalkTime.plusMinutes(10)


        // Create a DataSet of ActivitySegments to indicate the runner walked for
        // 10 minutes in the middle of a run.
        val activitySegmentDataSource = DataSource.Builder()
            .setAppPackageName(activity.packageName)
            .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
            .setStreamName("Running session-activity segments")
            .setType(DataSource.TYPE_RAW)
            .build()

        val firstRunningDp = DataPoint.builder(activitySegmentDataSource)
            .setActivityField(Field.FIELD_ACTIVITY, FitnessActivities.RUNNING)
            .setTimeInterval(startTime.toEpochSecond(), startWalkTime.toEpochSecond(), TimeUnit.MILLISECONDS)
            .build()

        val walkingDp = DataPoint.builder(activitySegmentDataSource)
            .setActivityField(Field.FIELD_ACTIVITY, FitnessActivities.WALKING)
            .setTimeInterval(startWalkTime.toEpochSecond(), endWalkTime.toEpochSecond(), TimeUnit.MILLISECONDS)
            .build()

        val secondRunningDp = DataPoint.builder(activitySegmentDataSource)
            .setActivityField(Field.FIELD_ACTIVITY, FitnessActivities.RUNNING)
            .setTimeInterval(endWalkTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.MILLISECONDS)
            .build()

        val activitySegments = DataSet.builder(activitySegmentDataSource)
            .addAll(listOf(firstRunningDp, walkingDp, secondRunningDp))
            .build()

        // Create a session with metadata about the activity.
        val session = Session.Builder()
            .setName("Running session")
            .setDescription("Long run around Shoreline Park")
            .setIdentifier("UniqueIdentifierHere")
            .setActivity(FitnessActivities.RUNNING)
            .setStartTime(startTime.toEpochSecond(), TimeUnit.MILLISECONDS)
            .setEndTime(endTime.toEpochSecond(), TimeUnit.MILLISECONDS)
            .build()

        // Build a session insert request
        val insertRequest = SessionInsertRequest.Builder()
            .setSession(session)
            .addDataSet(activitySegments)
            .build()

        Fitness.getSessionsClient(activity, GoogleSignIn.getAccountForExtension(apiContext, fitnessOptions))
            .insertSession(insertRequest)
            .addOnSuccessListener {
                Log.i(TAG, "Session insert was successful!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "There was a problem inserting the session: ", e)
            }
    }
}