package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
we don't need this anymore, because we're not passing an app to the viewModel
 */
//@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val task1 = Task("title1", "desc1")
    private val task2 = Task("title2", "desc2", true)
    private val task3 = Task("title3", "desc3", true)

    private lateinit var tasksRepository: FakeTestRepository

    // GIVEN a fresh ViewModel (subject under the test)
    private lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setupViewModel() {
        tasksRepository = FakeTestRepository()
        tasksRepository.addTasks(task1, task2, task3)

        tasksViewModel = TasksViewModel(tasksRepository)
    }

    @Test
    fun addNewTask_setNewTaskEvent() {
        // WHEN adding a new task
        tasksViewModel.addNewTask()
        // THEN the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled(), (not(nullValue())))
    }

    // check that if you've set your filter type to show all tasks, that the Add task button is visible.
    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        val value = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(value, `is`(true))
    }
}