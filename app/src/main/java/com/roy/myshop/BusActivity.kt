package com.roy.myshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roy.myshop.databinding.ActivityBusBinding
import com.roy.myshop.databinding.ActivityParkingBinding
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class BusActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var binding: ActivityBusBinding
    var busRep: Bus? = null
    var busRetrofit = Retrofit
        .Builder()
        .baseUrl("https://data.tycg.gov.tw/api/v1/rest/datastore/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        info("ttt")
        doAsync {
            val bus = busRetrofit.create(BusService::class.java)
            busRep = bus.listBuses()
                .execute()
                .body()
            info("start")
            busRep?.result?.records?.forEach {
                info("BusID:${it?.BusID} RouteID:${it?.RouteID} Speed:${it?.Speed}")
            }
            uiThread {
                val recycler = binding.busRecycler
                recycler.layoutManager = LinearLayoutManager(it)
                recycler.setHasFixedSize(true)
                recycler.adapter = busAdapter()
            }

        }

    }

    inner class busAdapter : RecyclerView.Adapter<BusHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_bus, parent, false)
            val holder = BusHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: BusHolder, position: Int) {
            holder.bindBus(busRep!!.result?.records.get(position))
        }

        override fun getItemCount(): Int {
            if (busRep == null) {
                return 0
            }
            return busRep!!.result?.records?.size
        }
    }

    class BusHolder(view: View) : RecyclerView.ViewHolder(view) {
        val busID = itemView.findViewById<TextView>(R.id.busID)
        val busRouteID = itemView.findViewById<TextView>(R.id.busRouteID)
        val busSpeed = itemView.findViewById<TextView>(R.id.busSpeed)
        fun bindBus(busInfo: BusRecord) {
            busID.text = busInfo.BusID
            busRouteID.text = busInfo.RouteID
            busSpeed.text = busInfo.Speed
        }
    }
}

data class Bus(
    val result: BusResult,
    val success: Boolean
)

data class BusResult(
    val fields: List<BusField>,
    val include_total: Boolean,
    val limit: Int,
    val offset: Int,
    val records: List<BusRecord>,
    val records_format: String,
    val resource_id: String,
    val total: Int
)

data class BusField(
    val id: String,
    val type: String
)

data class BusRecord(
    val Azimuth: String,
    val BusID: String,
    val BusStatus: String,
    val DataTime: String,
    val DutyStatus: String,
    val GoBack: String,
    val Latitude: String,
    val Longitude: String,
    val ProviderID: String,
    val RouteID: String,
    val Speed: String,
    val _id: Int,
    val ledstate: String,
    val sections: String
)

interface BusService {
    @GET("bf55b21a-2b7c-4ede-8048-f75420344aed")
    fun listBuses(): Call<Bus>
}



