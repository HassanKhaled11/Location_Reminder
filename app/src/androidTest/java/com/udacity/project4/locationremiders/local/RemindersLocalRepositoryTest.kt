package com.udacity.project4.locationremiders.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.udacity.project4.MainCoroutineRule
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RemindersLocalRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var remindersLocalRepository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @Before
    fun satUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()

        remindersLocalRepository = RemindersLocalRepository(
            database.reminderDao(), Dispatchers.Unconfined
        )
    }

    @After
    fun closeDataBase(){
        database.close()
    }

    @Test
    fun getRemindersTest_withEmptyList_returnTrue() = runBlocking {

        val result = remindersLocalRepository.getReminders()

        assertTrue { result is Result.Success }
        result as Result.Success
        assertTrue(emptyList<ReminderDTO>() == result.data)
    }


    @Test
    fun getRemindersTest_notEmptyList_returnList() = runBlocking {
        val reminder = ReminderDTO("title", "description", "dhaka", 37.819927, -122.478256)
        remindersLocalRepository.saveReminder(reminder)

        val result = remindersLocalRepository.getReminders()

        assertTrue (result is Result.Success)

        result as Result.Success
        assertTrue(result.data.size == 1 )
    }


    @Test
    fun saveReminderAndRetrievesReminderTest() = runBlocking {
        val reminder = ReminderDTO("title", "description", "location", 37.819927, -122.478256)
        remindersLocalRepository.saveReminder(reminder)

        val result = remindersLocalRepository.getReminder(reminder.id)

        assertTrue (result is Result.Success)
        result as Result.Success
        assertEquals("title",result.data.title )
        assertEquals("description",result.data.description )
        assertEquals("location",result.data.location)
    }


    @Test
    fun deleteAllRemindersTest() = runBlocking {
        val reminder = ReminderDTO("title", "description", "dhaka", 37.819927, -122.478256)
        remindersLocalRepository.saveReminder(reminder)
        remindersLocalRepository.deleteAllReminders()

        val result = remindersLocalRepository.getReminder(reminder.id)
        assertTrue (result is Result.Error)
        result as Result.Error
        assertTrue("Reminder not found!" == result.message)
    }

}