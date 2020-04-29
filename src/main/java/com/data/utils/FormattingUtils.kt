package com.data.utils

object FormattingUtils {
    //Helper method for song that takes length in Milliseconds and outputs it in a more readable HH:MM:SS format
    fun msToMinSec(length: Long): String {
        val totSeconds = length.toInt() / 1000
        var seconds = ""
        var minutes = ""
        var hours = ""
        if (totSeconds % 60 < 10) seconds = "0" + totSeconds % 60 else seconds += totSeconds % 60
        if (totSeconds / 60 < 10) minutes = "0" + totSeconds / 60 else if (totSeconds / 60 > 59) minutes += totSeconds / 60 % 60 else minutes += totSeconds / 60
        if (totSeconds / 3600 < 10) hours = "0" + totSeconds / 60 / 60 else hours += totSeconds / 60 / 60
        return if ("00" == hours) "$minutes:$seconds" else {
            if (minutes.length == 1) minutes = "0$minutes"
            "$hours:$minutes:$seconds"
        }
    }
}