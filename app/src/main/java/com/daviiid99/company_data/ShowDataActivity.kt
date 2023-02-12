package com.daviiid99.company_data
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ShowDataActivity(val action : String = "") : AppCompatActivity() {
    var list = listOf<String>()

    val todo: String? by lazy {
        intent.getStringExtra("action")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)

        // Declare UI elements
        var title = findViewById<TextView>(R.id.title_text)

        // Initialize list
        var list = listOf<String>()

        // A method to get a list oncreate
        var data = showData()
        var temp_list = retrieveList(todo.toString(), data)
        updateList(temp_list)
        updateTitle(todo.toString(), title)
        fullScreenMode()

    }

    fun updateTitle(action : String, title : TextView){
        // This method read the input action string
        // Determines the title for the screen

        if (action.contains("full")){
            title.text = "Customer's full names"
        }

        else if (action.contains("jobs")){
            title.text = "Companies user's jobs"
        }

        else if (action.contains("latest")){
            title.text = "Customer latest check"
        }

        else if (action.contains("earliest")){
            title.text = "Customer earliest check"
        }

    }

    fun updateList(tempList : List<String>){
        // A method to add values into a list
        print("mi lista $tempList")

        var listView = findViewById<ListView>(R.id.list_items)
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tempList)
        listView.adapter = adapter

        // A method to notify adapter about changes
        adapter.notifyDataSetInvalidated()
    }

    fun retrieveList(action : String, data : showData) : List<String>{
        var list = data.main(action)
        return list
    }

    fun fullScreenMode(){
        // Full screen mode support
        // This is just cosmetic
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}