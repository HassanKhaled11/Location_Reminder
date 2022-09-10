package com.udacity.project4.locationremiders.reminderlist

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.databinding.library.baseAdapters.R
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationremiders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragment
import com.udacity.project4.locationreminders.reminderslist.ReminderListFragmentDirections
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class ReminderListFragmentTest {

    private lateinit var dataSource: FakeDataSource
    private lateinit var viewModel: RemindersListViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupTest() {
        dataSource = FakeDataSource()
        viewModel =
            RemindersListViewModel(
                ApplicationProvider.getApplicationContext(),
                dataSource
            )
        stopKoin()
        val myModule = module {
            single {
                viewModel
            }
        }
        startKoin {
            modules(listOf(myModule))
        }
    }


    @After
    fun teardown(){
      stopKoin()
    }


    //testing the navigation of the fragments.

    @Test
    fun navigateTest() = runBlockingTest {
        val fragmentScenario =
            launchFragmentInContainer<ReminderListFragment>(Bundle(), com.udacity.project4.R.style.AppTheme)
        val navController = Mockito.mock(NavController::class.java)
        fragmentScenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(com.udacity.project4.R.id.addReminderFAB)).perform(click())

        Mockito.verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
    }


// testing displayig data
    @Test
    fun displayDataTest() = runBlockingTest {
        val reminder1 = ReminderDTO("title1", "description1", "dhaka1", 37.819927, -122.478256)

        dataSource.saveReminder(reminder1)
        launchFragmentInContainer<ReminderListFragment>(Bundle.EMPTY, com.udacity.project4.R.style.AppTheme)
        onView(withId(com.udacity.project4.R.id.noDataTextView)).check(
            ViewAssertions.matches(
                CoreMatchers.not(ViewMatchers.isDisplayed())
            )
        )
        onView(ViewMatchers.withText(reminder1.title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(reminder1.description)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
        onView(ViewMatchers.withText(reminder1.location)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }


// testing for the error messages.
    @Test
    fun errorTest() = runBlockingTest {
        dataSource.deleteAllReminders()

        launchFragmentInContainer<ReminderListFragment>(Bundle(), com.udacity.project4.R.style.AppTheme)
        onView(withId(com.udacity.project4.R.id.noDataTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}