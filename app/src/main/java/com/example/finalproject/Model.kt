package com.example.finalproject

import android.os.Handler
import android.os.Looper
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.util.Scanner

object Model {
    private lateinit var department: String
    private lateinit var course: String
    private var section = "0101"
    private lateinit var building: String
    private lateinit var name: String

    private var maxSeats = 0
    private var openSeats = 0
    private var lat = 0.0
    private var lng = 0.0

    private lateinit var courseString: String
    private lateinit var sectionString: String
    private lateinit var buildingString: String

    fun isOffered(callback: (Boolean) -> Unit) {
        Thread(Runnable {
            try {
                val url = URL("https://api.umd.io/v1/courses/$department$course/sections?semester=202401")
                val inputStream = url.openStream()
                val scanner = Scanner(inputStream)

                var result = ""
                while (scanner.hasNext()) {
                    result += scanner.nextLine()
                }
                courseString = result

                Handler(Looper.getMainLooper()).post {
                    callback(true)
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    callback(false)
                }
            }
        }).start()
    }

    fun initSectionInfo() {
        val thread = Thread(Runnable {
            try {

                val url =
                    URL("https://api.umd.io/v1/courses/$department$course/sections/$section?semester=202401")
                val inputStream = url.openStream()
                val scanner = Scanner(inputStream)

                var result = ""
                while (scanner.hasNext()) {
                    result += scanner.nextLine()
                }
                sectionString = result
            } catch (e: Exception) {}
        })
        thread.start()
        thread.join()

        val jsonArray = JSONArray(sectionString)
        val jsonObject = jsonArray.getJSONObject(0)

        maxSeats = jsonObject.getString("seats").toInt()
        openSeats = jsonObject.getString("open_seats").toInt()
        building = jsonObject.getJSONArray("meetings").getJSONObject(0).getString("building")

        if (building == "IRB") {
            building = "432"
        }
    }

    fun initLocation() {
        val thread = Thread(Runnable {
            try {
                val url = URL("https://api.umd.io/v1/map/buildings/$building")
                val inputStream = url.openStream()
                val scanner = Scanner(inputStream)

                var result = ""
                while (scanner.hasNext()) {
                    result += scanner.nextLine()
                }
                buildingString = result
            } catch (e: Exception) {}
        })
        thread.start()
        thread.join()

        val jsonObject = JSONObject(buildingString)
        val data = jsonObject.getJSONArray("data").getJSONObject(0)

        lat = data.getDouble("lat")
        lng = data.getDouble("long")
        name = data.getString("name")
    }

    fun getSections(): MutableList<String> {
        val jsonArray = JSONArray(courseString)
        val sections = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            sections.add(jsonObject.getString("number"))
        }

        return sections
    }

    fun setDepartment(newDepartment: String) {
        department = newDepartment
    }

    fun setCourse(newCourse: String) {
        course = newCourse
    }

    fun setSection(newSection: String) {
        section = newSection
    }

    fun getOpenSeats(): Int {
        return openSeats
    }

    fun getMaxSeats(): Int {
        return maxSeats
    }

    fun getLat(): Double {
        return lat
    }

    fun getLng(): Double {
        return lng
    }

    fun getName(): String {
        return name
    }
}