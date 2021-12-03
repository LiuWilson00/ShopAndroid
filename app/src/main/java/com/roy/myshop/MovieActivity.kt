package com.roy.myshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.roy.myshop.databinding.ActivityMainBinding
import com.roy.myshop.databinding.ActivityMovieBinding
import com.roy.myshop.databinding.ActivityParkingBinding
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL

class MovieActivity : AppCompatActivity(), AnkoLogger {
    //    private val movieUrl = "KnnIJL"
    private lateinit var binding: ActivityMovieBinding
    var movieRetrofit = Retrofit.Builder()
        .baseUrl("https://api.jsonserve.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private var movies: List<Movie>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        doAsync {
            val movieService = movieRetrofit.create(MovieService::class.java)
//            val url = URL(movieUrl)
//            val json = url.readText()
//            parseGson(json)
            movies = movieService.listMovies()
                .execute()
                .body()
            uiThread {
                val recycler = binding.movieRecyler
                recycler.layoutManager = LinearLayoutManager(it)
                recycler.setHasFixedSize(true)
                recycler.adapter = movieAdapter()

            }
        }

    }

//    private fun parseGson(json: String) {
//        movies = Gson().fromJson<List<Movie>>(json, object : TypeToken<List<Movie>>() {}.type)
//    }

    inner class movieAdapter : RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_movie, parent, false)
            val holder = MovieHolder(view)
            return holder
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            holder.bindMovie(movies?.get(position))
        }

        override fun getItemCount(): Int {
            if (movies == null) {
                return 0
            }
            return movies!!.size
        }
    }

    inner class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        var movieTitle: TextView = itemView.findViewById<TextView>(R.id.movieTitle)
        var director: TextView = itemView.findViewById<TextView>(R.id.director)
        var imdbVotes: TextView = itemView.findViewById<TextView>(R.id.imdbVotes)
        var movieImage: ImageView = itemView.findViewById<ImageView>(R.id.movieImage)
        fun bindMovie(target: Movie?) {
            movieTitle.text = target?.Title
            director.text = target?.Director
            imdbVotes.text = target?.imdbVotes
            Glide.with(this@MovieActivity)
                .load(target!!.Images[0])
                .override(300)
                .into(movieImage)
        }
    }
}

data class Movie(
    val Actors: String,
    val Awards: String,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String
)

interface MovieService {
    @GET("KnnIJL")
    fun listMovies(): Call<List<Movie>>
}