package com.udacity.project4.locationreminders.reminderslist

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.MainCoroutineRule
import com.udacity.project4.getOrAwaitValue
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import org.koin.core.context.stopKoin
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var viewModel: RemindersListViewModel
    private lateinit var dataSource: FakeDataSource


    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        dataSource = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),dataSource)
    }

   @After
   fun teardown(){
       stopKoin()
   }


    @Test
    fun loadRemindersTest_withEmptyList_returnTrue(){

      viewModel.loadReminders()

        assertEquals(emptyList<ReminderDataItem>(),viewModel.remindersList.getOrAwaitValue())

    }


    @Test
    fun loadRemindersTest_withThreeReminders_returnFalse(){

        val reminder1 = ReminderDTO("T1","hello 1","location 1",
            30.254191579122224, 31.455338343915514)


        val reminder2 = ReminderDTO("T2","hello 2","location 2",
            30.256518719233206, 31.50189038390654)



        val reminder3 = ReminderDTO("T3","hello 3","location 3",
            30.17187552186255, 31.492371551805117 )


        val list = mutableListOf<ReminderDTO>(reminder1,reminder2,reminder3)
        dataSource = FakeDataSource(list)
         viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),dataSource)
        viewModel.loadReminders()

        assertFalse(emptyList<ReminderDataItem>() == viewModel.remindersList.getOrAwaitValue())

    }


    @Test
    fun checkLoading() {
        mainCoroutineRule.pauseDispatcher()
        viewModel.loadReminders()
        assertTrue(viewModel.showLoading.getOrAwaitValue())

        mainCoroutineRule.resumeDispatcher()
        assertFalse(viewModel.showLoading.getOrAwaitValue())
    }


    @Test
    fun checkError() {
        dataSource.setEnableError(true)

        viewModel.loadReminders()
        assertEquals("data is not found",viewModel.showSnackBar.getOrAwaitValue())
    }



}