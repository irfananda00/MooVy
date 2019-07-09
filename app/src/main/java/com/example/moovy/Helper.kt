package com.example.moovy

import java.text.ParseException
import java.text.SimpleDateFormat

class Helper {
    companion object{

        fun dateFormatter(dateString: String): String {
            val fmt = SimpleDateFormat("yyyy-MM-dd")
            try {
                val date = fmt.parse(dateString)
                val fmtOut = SimpleDateFormat("dd MMMM yyyy")
                return fmtOut.format(date)
            }catch (e: ParseException){
                e.printStackTrace()
            }
            return ""
        }

    }
}