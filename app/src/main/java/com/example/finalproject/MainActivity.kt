package com.example.finalproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val department: EditText = findViewById(R.id.department)
        val course: EditText = findViewById(R.id.course)
        val submit: Button = findViewById(R.id.submit)

        submit.setOnClickListener {
            if (department.text.isNotEmpty() && course.text.isNotEmpty()) {
                Model.setDepartment(department.text.toString())
                Model.setCourse(course.text.toString())

                Model.isOffered { offered ->
                    if (offered) {
                        val intent = Intent(this, CourseActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_left, 0)
                    } else {
                        Toast.makeText(this,"Invalid course! Please try again!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this,"Please input a course!", Toast.LENGTH_SHORT).show()
            }

            save(department.text.toString(), course.text.toString())
        }

        load()
        createAd()
    }

    private fun createAd() {
        val adView = AdView(this)
        val adSize = AdSize( AdSize.FULL_WIDTH, AdSize.AUTO_HEIGHT )
        adView.setAdSize(adSize)

        val adUnitId = "ca-app-pub-3940256099942544/6300978111"
        adView.adUnitId = adUnitId

        val builder: AdRequest.Builder = AdRequest.Builder()
        val request: AdRequest = builder.build()
        adView.loadAd(request)

        val layout: LinearLayout = findViewById(R.id.ad_view_main)
        layout.addView(adView)
    }

    private fun save(department: String, course: String) {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("DEPARTMENT", department)
            putString("COURSE", course)
            apply()
        }
    }

    private fun load() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val savedDepartment = sharedPreferences.getString("DEPARTMENT", "")
        val savedCourse = sharedPreferences.getString("COURSE", "")

        val department: EditText = findViewById(R.id.department)
        val course: EditText = findViewById(R.id.course)

        department.setText(savedDepartment)
        course.setText(savedCourse)
    }
}