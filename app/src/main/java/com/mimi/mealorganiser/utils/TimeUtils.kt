package com.mimi.mealorganiser.utils

/**
 * Created by Mimi on 15/11/2017.
 * This class is used for converting time
 */
class TimeUtils{
    companion object {
        val MILLIS_PER_SECOND = 1000
        val SECONDS_PER_MINUTE = 60
        val MINUTES_PER_HOUR = 60
        val TYPE_SECONDS = 0
        val TYPE_MINUTS = 1
        val TYPE_HOURS = 2
    }
    /**
     * this function converts the input
     * from the user into milliseconds
     *
     * value = the inputed value of text
     * type = the type of the value inputted
     *          0 = sec, 1 = min, 2 = hours
     */
    fun convertToMillis(value:String, type:Int):Long{
        if(value.isEmpty())
            throw Exception("Null value: $value")
        val intValue = value.toInt()
        val millis = when(type){
            TYPE_SECONDS -> intValue * MILLIS_PER_SECOND
            TYPE_MINUTS -> intValue * MILLIS_PER_SECOND * SECONDS_PER_MINUTE
            TYPE_HOURS -> intValue * MILLIS_PER_SECOND * SECONDS_PER_MINUTE * MINUTES_PER_HOUR
            else -> throw Exception("Unknown type: $type")
        }
        return millis.toLong()
    }
}
