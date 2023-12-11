package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class CourseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        val spinner: Spinner = findViewById(R.id.spinner)
        val submit: Button = findViewById(R.id.submit)
        val back: Button = findViewById(R.id.back)

        val adapter = ArrayAdapter(this, R.layout.spinner_item, Model.getSections())
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val section = parent.getItemAtPosition(position).toString()
                Model.setSection(section)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        submit.setOnClickListener {
            val intent = Intent(this, SectionActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_left,0)
        }

        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_right,0)
        }
    }
}