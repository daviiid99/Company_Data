package com.daviiid99.company_data
import android.os.Environment
import com.github.kittinunf.fuel.httpDownload
import java.io.File
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.FileReader
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate.parse
import java.util.Date

class showData {

    var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "data.txt")
    var jsonArray : JsonArray = JsonArray()
    var full_names = mutableListOf<String>()
    var list_of_dates  = mutableListOf<Date>()
    val url = "https://19h0vwjnce.execute-api.eu-west-3.amazonaws.com/MobileInternshipAPI/data"
    var path = "sdcard/download/data.txt"
    var local_file: File = File(path)
    val formatOfDate = SimpleDateFormat("dd/MM/yyyy") // Spanish format

    var json = ""

    fun main(action: String) : MutableList<String>{
        // define variables for url and local file path

        if (action.contains("full")){
            // User requested full names sort alphabetically
            setup()
            customerFullName()
        } else if (action.contains("jobs")){
            setup()
            companiesJobs()
        } else if (action.contains("latest")){
            setup()
            customerCheck(action)
        } else if (action.contains("earliest")){
            setup()
            customerCheck(action)
        }
        return full_names
    }

    fun setup()  {
        // Required methods for downloading data and save it
        local_file.createNewFile()
        decodeJsonFile()
    }

    fun sortAlphabetically(list :  MutableList<String>) :  List<String>{
        // This will receive a list and will return it in alphabetical order
        var tempList  = list.toList() // transform to list
        tempList = tempList.sortedDescending().reversed() // sort list alphabetically
        return tempList
    }

    fun showListOnDisplay(list : List<String>){
        // Print all contents from a list into display

        while(!file.exists()){
            println("Awaiting for file download...")
        }

        if (jsonArray.size() > 0){
            for (name: String in list){
                println("Nombre :  ${name}")

            }
        }
    }



    fun customerFullName(){
        // Retrieve a list of customer full names sort alphabetically
        for (jsonElement in jsonArray){
            var json = jsonElement.asJsonObject
            var first_name : String? = if (json.get("First Name").isJsonNull) null else json.get("First Name")?.asString
            var last_name : String? = if (json.get("Last Name").isJsonNull) null else json.get("Last Name")?.asString
            var full_name = "$first_name $last_name"

            if (!full_names.contains(full_name) && !full_name.contains("null")){
                // Add names into list only if it isn't already on it
                full_names.add(full_name)
            }
        }

        // Sort the final list in alphabetical order
        full_names = sortAlphabetically(full_names).toMutableList()
        showListOnDisplay(full_names)
    }

    fun customerCheck(action : String){
        // Retrieve the customer with latest check-in

        fun addValuesToList(){
            // First, we create a list full of dates
            for (jsonElement in jsonArray){

                var json = jsonElement.asJsonObject
                var date : String? = if (json.get("Last Check-In Date").isJsonNull) null else json.get("Last Check-In Date")?.asString

                if (date != null && date != "" && !list_of_dates.contains(formatOfDate.parse(date))){
                    // Add dates into list
                        list_of_dates.add(formatOfDate.parse(date))
                    }
            }
        }

        fun compareDates(action : String): Date{
            // We compare all dates available
            // Determine latest date
            var date1 : Date = Date()
            var date2 : Date = Date()
            var max : Int = list_of_dates.size
            var minDate : Date = list_of_dates[0]
            var maxDate : Date = list_of_dates[0]
            var finalDate : Date = list_of_dates[0]

            for (date in list_of_dates){
                if (action == "latest"){
                    if (maxDate < date){
                        // Always save highest date into variable
                        maxDate = date
                        finalDate = maxDate
                    }
                }

                if (action == "earliest"){
                    if (date < minDate){
                        // Always save earliest date into variable
                        minDate = date
                        finalDate = minDate
                    }
                }
            }

            return finalDate


        }

        fun getCustomerWithlatestDate(max : Date) : String{
            // First, create a full list of all customers
            customerFullName()
            var customer_latest_check : String = ""

            for (jsonElement in jsonArray){
                var json = jsonElement.asJsonObject
                var date : String? = if (json.get("Last Check-In Date").isJsonNull) null else json.get("Last Check-In Date")?.asString

                if (date != null && date != "") {
                    if (formatOfDate.parse(date) == max){
                        var customer_name : String? = if (json.get("First Name").isJsonNull) null else json.get("First Name")?.asString
                        var customer_surname : String? = if (json.get("Last Name").isJsonNull) null else json.get("Last Name")?.asString
                        customer_latest_check = "$customer_name  $customer_surname"
                    }
                }
            }

            print(customer_latest_check)

            return customer_latest_check
        }

        // First step
        addValuesToList()

        // Seconds step
        var date : Date = compareDates(action)

        // Third step
        // Retrieve customer with latest check
        full_names = mutableListOf<String>(getCustomerWithlatestDate(date))
    }

    fun customerEarliestCheck(){
        // Retrieve the customer with the earliest check-in

    }

    fun companiesJobs(){
        // Retrieve a list of companies user's jobs
        for(jsonElement in jsonArray){
            var json = jsonElement.asJsonObject
            var job : String? = if (json.get("Job").isJsonNull) null else json.get("Job")?.asString

            if (!full_names.contains(job)){
                // Add jobs into list only if doesn't exists
                // and not null
                if (job != null) {
                        full_names.add(job.toString())
                }
            }
        }

        full_names = sortAlphabetically(full_names).toMutableList()
        showListOnDisplay(full_names)
    }

    fun readFileContent()   {
        // A method to read all content from a json file
        // Saves text into a variable
        val bufferedReader = BufferedReader(FileReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.forEachLine { stringBuilder.append(it) }
        val gson = Gson()
        jsonArray = JsonParser.parseString(stringBuilder.toString()).asJsonArray
    }

    fun decodeJsonFile() {

        // A method to read a file from local path
        // Save all his content into a temp variable

        // Check if file exists
        // If exists save all his content
        if (file.exists()){
            readFileContent()
        }
    }

    fun downloadJsonFile() {

        // A method to download a file from the net
        // Stores the file under device external storage
        // sdcard/download/data.txt

        if (local_file.exists()){
            // Remove it before try to download a new version
            local_file.deleteOnExit()
        }

        url.httpDownload().destination { _, _ -> local_file }.response { result ->
            when (result) {
                is com.github.kittinunf.result.Result.Success -> {
                    // File downloaded successfully
                    val fileContent = local_file.readText()
                    decodeJsonFile()
                }
                is com.github.kittinunf.result.Result.Failure -> {
                    // Handle failure
                    val exception = result.getException()
                    println("Failed to download file: $exception")
                }
            }
        }
    }
}