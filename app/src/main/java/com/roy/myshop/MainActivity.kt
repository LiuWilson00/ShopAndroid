package com.roy.myshop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.roy.myshop.databinding.ActivityMainBinding
import com.roy.myshop.databinding.RowFunctionBinding

class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var rowBinding: RowFunctionBinding
    private val TAG = "Main"
    var signup = false
    var nickname: String = ""
    val auth = FirebaseAuth.getInstance()
    val functions = listOf<String>(
        "Camera",
        "Invite friend",
        "Parking",
        "Movie",
        "Bus",
        "Download coupons",
        "News",
        "News",
        "News",
        "News",
        "News",
        "News",
        "News",
        "News",
        "News",
        "News",
        "Maps"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
        setSupportActionBar(binding.toolbar)
//
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

        auth.addAuthStateListener { auth ->
            authChanged(auth)
        }
        binding.fab.setOnClickListener { view ->

        }
        //Spinner
        var spinner = binding.contentMain.spinner
        val colors = arrayOf("Red", "Green", "Blue")
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colors)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d(TAG, "onItemSelected: ${colors[position]}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        //RecyclerView
        var recycler = binding.contentMain.recycler

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)
        recycler.adapter = FunctionAdapter()


    }

    inner class FunctionAdapter : RecyclerView.Adapter<FunctionHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FunctionHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_function, parent, false)
            val holder = FunctionHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: FunctionHolder, position: Int) {
            holder.nameText.text = functions.get(position)
            holder.itemView.setOnClickListener { view ->
                functionClicked(holder, position)
            }
        }

        override fun getItemCount(): Int {
            return functions.size
        }

    }

    private fun functionClicked(holder: MainActivity.FunctionHolder, position: Int) {
        when (position) {
            1 -> startActivity(Intent(this, ContactActivity::class.java))
            2 -> startActivity(Intent(this, ParkingActivity::class.java))
            3 -> startActivity(Intent(this, MovieActivity::class.java))
            4 -> startActivity(Intent(this, BusActivity::class.java))
        }

    }

    class FunctionHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameText: TextView = itemView.findViewById<TextView>(R.id.functionName)
    }

    private fun authChanged(auth: FirebaseAuth) {
        if (auth.currentUser == null) {
            val intent = Intent(this, SignUpActivity::class.java)
            signupResultLauncher.launch(intent)
        } else {

            Log.d(TAG, "authChanged: ${auth.currentUser?.uid}")
        }
    }

    private val signupResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = Intent(this, NewUserInfo::class.java)
                newUserLauncher.launch(intent)
            }
        }

    private val newUserLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        }

    override fun onResume() {
        super.onResume()
        getNickname { nick ->
            binding.contentMain.mainNickname.setText("Hello! $nick")
        }

    }


//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        return navController.navigateUp(appBarConfiguration)
//                || super.onSupportNavigateUp()
//    }
}