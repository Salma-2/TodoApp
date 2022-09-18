package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource

object ServiceLocator {
    private var database: ToDoDatabase? = null

    //it could get used by multiple threads
    @Volatile
    var tasksRepository: TasksRepository? = null

    fun provideTasksRepository(context: Context): TasksRepository {
// to prevent creating the repo twice.
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository {
//        DefaultTasksRepository.getRepository(app)
        val newRepo =
            DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TasksDataSource {
        val database = database ?: createDatabase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDatabase(context: Context): ToDoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            "Task.db"
        ).build()
        database = result
        return result
    }
}