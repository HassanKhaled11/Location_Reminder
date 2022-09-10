package com.udacity.project4.locationremiders.data

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

class FakeDataSource(var datalist : MutableList<ReminderDTO> = mutableListOf() ):ReminderDataSource {

    override suspend fun getReminders(): Result<List<ReminderDTO>> {

        if(datalist == null){
            return Result.Error("No data Found")
        }
        else{
         return Result.Success(datalist)
        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
            datalist.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if(datalist == null){
            return Result.Error("Reminder is not Found")
        }
        else{
            val reminder = datalist.find { it.id == id}
            if (reminder != null)
            return Result.Success(reminder)

            else
                return Result.Error("Reminder is not Found")
        }

     }

    override suspend fun deleteAllReminders() {
         datalist.clear()
    }


}