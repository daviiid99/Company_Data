package com.daviiid99.company_data

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fullScreenMode()

        //  Create an intent for a new activity
        val Intent = Intent(this, ShowDataActivity::class.java)

        // Create an instance of the data class
        val retrieveData =  showData()
        downloadFile(retrieveData)

        // Reference all buttons on main activity
        var customer_earliest_check_button = findViewById<Button>(R.id.button_customer_earliest)
        var customer_latest_check_button = findViewById<Button>(R.id.button_customer_latest)
        var customer_full_names_button = findViewById<Button>(R.id.button_customer_names_sort)
        var companies_users_job_button = findViewById<Button>(R.id.button_companies_users_jobs_sort)

        // Listen buttons

        customer_earliest_check_button.setOnClickListener {
            nextScreen("earliest", retrieveData, Intent)
        }

        customer_latest_check_button.setOnClickListener{
            nextScreen("latest", retrieveData, Intent)
        }

        customer_full_names_button.setOnClickListener{
            nextScreen("full", retrieveData, Intent)
        }

        companies_users_job_button.setOnClickListener {
            nextScreen("jobs", retrieveData, Intent)
        }

        requestStoragePermission()

    }

    fun nextScreen(action : String, retrieveData : showData, intent : Intent){
        // A method to navigate to next screen
        // It will receive an action name as input
        var details = ShowDataActivity(action = action)
        var intent = Intent(this, ShowDataActivity::class.java).apply {
            putExtra("action", action)
        }
        startActivity(intent)

    }

    fun requestStoragePermission(){
        val intent = Intent(
            ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
            Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        )

        val APP_STORAGE_ACCESS_REQUEST_CODE = 501 // Any value
        startActivityForResult(intent, APP_STORAGE_ACCESS_REQUEST_CODE)
    }


    fun fullScreenMode(){
        // Full screen mode support
        // This is just cosmetic
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun downloadFile(downloader : showData){
        // We need to download file before trying to access it
        downloader.downloadJsonFile()
        print("File downloaded!")
    }
}