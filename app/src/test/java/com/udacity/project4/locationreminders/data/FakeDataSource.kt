package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource(val datalist: MutableList<ReminderDTO> = mutableListOf()) : ReminderDataSource {


    var shouldReturError = false

   fun setEnableError(value:Boolean){
    shouldReturError = value
  }

    override suspend fun saveReminder(reminder: ReminderDTO) {

        datalist.add(reminder)
    }


    override suspend fun getReminders(): Result<List<ReminderDTO>> {
       if(shouldReturError){

           return Result.Error("data is not found")
       }
        else {
           return Result.Success(datalist)
       }
    }


    override suspend fun getReminder(id: String): Result<ReminderDTO> {

        if (shouldReturError){

        return Result.Error("reminder is not found ")
        }

        val reminder = datalist.find { it.id == id }
        if(reminder != null){
            return Result.Success(reminder)
        }
        else{
            return Result.Error("reminder is not found ")
        }
    }

    override suspend fun deleteAllReminders() {
        datalist.clear()
    }
}