package com.roy.myshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.roy.myshop.databinding.ActivityParkingBinding
import org.jetbrains.anko.*
import java.net.URL

class ParkingActivity : AppCompatActivity(), AnkoLogger {
    private val testjson = "https://jsonplaceholder.typicode.com/posts"
    private val parking =
        "https://data.tycg.gov.tw/api/v1/rest/datastore/0daad6e6-0632-44f5-bd25-5e1de1e9146f?format=json"
    private lateinit var binding: ActivityParkingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        doAsync {
            val url = URL(parking)
            val json = url.readText()
//            info("test  $json ")
            uiThread {
                toast("Got it")
                binding.info.text = json
                alert("Got it", "Alert") {
                    okButton {
                        parseGson(json)
                    }
                }.show()

            }
        }
    }

    private fun parseGson(json: String) {
        val parking = Gson().fromJson<Parking>(json, Parking::class.java)
        info(parking.result.records.size)
        parking.result.records.forEach {
            info("${it.areaId} ${it.areaName} ${it.parkName}")
        }
    }
}

data class Parking(
    val result: Result,
    val success: Boolean
)

data class Result(
    val fields: List<Field>,
    val include_total: Boolean,
    val limit: Int,
    val offset: Int,
    val records: List<Record>,
    val records_format: String,
    val resource_id: String,
    val total: Int
)

data class Field(
    val id: String,
    val type: String
)

data class Record(
    val _id: Int,
    val address: String,
    val areaId: String,
    val areaName: String,
    val introduction: String,
    val parkId: String,
    val parkName: String,
    val payGuide: String,
    val surplusSpace: String,
    val totalSpace: Int,
    val wgsX: Double,
    val wgsY: Double
)