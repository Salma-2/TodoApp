package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.succeeded
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TasksLocalDataSourceTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase
    private lateinit var localDataSource: TasksLocalDataSource

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        )
            // Room doesn't allow database queries to be run on the main thread
            .allowMainThreadQueries()
            .build()

        localDataSource = TasksLocalDataSource(
            database.taskDao(),
            Dispatchers.Main)
    }

    @After
    fun closeDb() = database.close()

    // runBlocking is used here because of https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    // TODO: Replace with runBlockingTest once issue is resolved
    @Test
    fun saveTask_retrievesTask() = runBlocking {
        // GIVEN - A new task saved in the database.
        val newTask = Task("title", "description", false)
        localDataSource.saveTask(newTask)

        // WHEN  - Task retrieved by ID.
        val result = localDataSource.getTask(newTask.id)

        // THEN - Same task is returned.
        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data.title, `is`("title"))
        assertThat(result.data.description, `is`("description"))
        assertThat(result.data.isCompleted, `is`(false))
    }

    @Test
    fun completeTask_retrievedTaskIsComplete() = runBlocking {
        // Save a new active task in the local data source
        val activeTask = Task("title", "description", false)
        localDataSource.saveTask(activeTask)

        // Mark it as complete.
        localDataSource.completeTask(activeTask)
        val result = localDataSource.getTask(activeTask.id)

        // Check that the task can be retrieved from the local data source and is complete.
        assertThat(result.succeeded, `is`(true))
        result as Success
        assertThat(result.data.title, `is`(activeTask.title))
        assertThat(result.data.isCompleted, `is`(true))

    }
}