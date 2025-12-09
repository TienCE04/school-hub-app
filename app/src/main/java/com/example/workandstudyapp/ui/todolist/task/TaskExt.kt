package com.example.workandstudyapp.ui.todolist.task

import java.time.DayOfWeek

val dayMap = mapOf(
    DayOfWeek.MONDAY to "Mon",
    DayOfWeek.TUESDAY to "Tue",
    DayOfWeek.WEDNESDAY to "Wed",
    DayOfWeek.THURSDAY to "Thu",
    DayOfWeek.FRIDAY to "Fri",
    DayOfWeek.SATURDAY to "Sat",
    DayOfWeek.SUNDAY to "Sun"
)
fun checkInputAddTask(title:String,content:String,time:String): Boolean{
    return !(title.isEmpty() && content.isEmpty() && time.isEmpty())
}

fun checkTimeTask(time:String): Boolean{
    if(time.isEmpty() || time.length>5){
        return false
    }
    var hour=""
    var minute=""
    val parts=time.split(":")
    if(parts.size!=2){
        return false
    }
    hour=parts[0]
    minute=parts[1]
    val hourReal=hour.toIntOrNull()
    val minuteReal=minute.toIntOrNull()
    if(hourReal==null || minuteReal==null){
        return false
    }

    if(hourReal !in 0..<24){
        return false
    }
    if(minuteReal !in 0..59){
        return false
    }
    return true
}

fun convertTime(time:String):String{
    val parts=time.split(":")
    val hour=parts[0]
    val minute=parts[1]
    if(hour.length==1 && minute.length==1){
        return "0$hour:0$minute"
    }

    if(hour.length==2 && minute.length==1){
        return "$hour:0$minute"
    }

    if(hour.length==1 && minute.length==2){
        return "0$hour:$minute"
    }

    return time
}