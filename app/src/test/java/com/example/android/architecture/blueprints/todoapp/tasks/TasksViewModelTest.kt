package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {
    @Test
    fun addNewTask_setNewTaskEvent() {
        // GIVEN a fresh ViewModel
        val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        // WHEN adding a new task

        // THEN the new task event is triggered
    }
}