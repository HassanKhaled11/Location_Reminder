package com.udacity.project4.locationremiders.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersDatabase
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RemidersDaoTest {
    private lateinit var dataBase : RemindersDatabase

    @get:Rule
    var instantExecuteRule = InstantTaskExecutorRule()



    @Before
    fun setDataBase(){
        dataBase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext()
            ,RemindersDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    fun closeDataBase(){
        dataBase.close()
    }

    @Test
    fun insertAndGetReminder_withRemiderId_ReturnTrue() = runBlocking{
        val reminder = ReminderDTO("title","describtion1","my location"
            ,30.178405155073524, 31.475205433869043)

        dataBase.reminderDao().saveReminder(reminder)

        val result  = dataBase.reminderDao().getReminderById(reminder.id)

        assertTrue(reminder.id == result?.id)
        assertTrue(reminder.title == result?.title)
        assertTrue(reminder.description == result?.description)
        assertTrue(reminder.location == result?.location)
    }



    @Test
    fun insertAndGetAllReminders_withTwoReminders_ReturnFalse()  = runBlocking {

         val reminder1 =  ReminderDTO("title","describtion1","my location"
             ,30.178405155073524, 31.475205433869043)


         val reminder2 = ReminderDTO("title","describtion1","my location"
            ,30.11611948787842, 31.400477859871092)

        dataBase.reminderDao().saveReminder(reminder1)
        dataBase.reminderDao().saveReminder(reminder2)

        val list = dataBase.reminderDao().getReminders()

        assertFalse(emptyList<ReminderDTO>() == list)


    }
}