package com.example.weatherapplication.ui.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.weatherapplication.R
import com.example.weatherapplication.data.entity.dailyweather.DailyWeatherResponse
import com.example.weatherapplication.data.entity.dailyweather.Weather
import com.example.weatherapplication.databinding.FragmentWeatherBinding
import com.example.weatherapplication.ui.adapters.DailyWeatherAdapter
import com.example.weatherapplication.ui.viewmodels.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale


class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private lateinit var viewModel: WeatherViewModel
    private val ANIMATION_DURATION = 5000L // Animasyonun süresi (milisaniye cinsinden)
    private lateinit var flpc: FusedLocationProviderClient
    private lateinit var locationTask: Task<Location>
    private lateinit var city:String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWeatherBinding.inflate(LayoutInflater.from(context), container, false)


        binding.recycller.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)


        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        viewModel.getWeather("Samsun")
        viewModel.getDailyWeather("Samsun")

        binding.imageView.setOnClickListener {
            city=binding.editTextText.text.toString()
            viewModel.getWeather(city)
            viewModel.getDailyWeather(city)
        }


        viewModel.dailyResponse.observe(viewLifecycleOwner) {
            val weatherAdapter = DailyWeatherAdapter(requireContext(), it)
            binding.recycller.adapter = weatherAdapter
            weatherAdapter.submitList(it)//verileri güncelledik adapter için.

        }
        val listView = ArrayList<View>()
        listView.add(binding.textViewMain)
        listView.add(binding.weatheranimaiton)
        listView.add(binding.textView4)
        listView.add(binding.textViewHumidity)
        listView.add(binding.textViewTemp)
        listView.add(binding.textViewWindSpeed)
        listView.add(binding.textViewFelt)
        listView.add(binding.imageView)
        listView.add(binding.imageViewIcon)
        listView.add(binding.editTextText)
        listView.add(binding.sunrise)
        listView.add(binding.sunset)
        listView.add(binding.recycller)
        listView.add(binding.textViewDay)

        startWelcomeAnimation(listView)


        viewModel.response.observe(viewLifecycleOwner, Observer {

            val response = it
            val localTime1 = localTimeCalc(response.sys.sunrise)
            val localTime2 = localTimeCalc(response.sys.sunset)
            val localDate = LocalDate.now()
            val dayOfWeek2 = localDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val currentTime = LocalTime.now()
            if(currentTime in localTime1 .. localTime2){
                binding.weatheranimaiton.setBackgroundResource(R.color.grey)
                binding.weatheranimaiton.setAnimation(R.raw.animationweather2)
                binding.imageView.setBackgroundResource(R.color.grey)
                binding.recycller.setBackgroundResource(R.color.grey)
            }else{
                binding.weatheranimaiton.setBackgroundResource(R.color.black)
                binding.weatheranimaiton.setAnimation(R.raw.animationweather)
                binding.imageView.setBackgroundResource(R.color.black)
                binding.recycller.setBackgroundResource(R.color.black)
            }



            val iconId = response.weather[0].icon
            val imgUrl = "https://api.openweathermap.org/img/w/$iconId.png"
            Glide.with(this@WeatherFragment).load(imgUrl).into(binding.imageViewIcon)
            binding.textViewMain.text = " ${(response.weather.get(0).description).toUpperCase()}"
            binding.textViewTemp.text = "Temperature : ${(response.main.temp - 273).toInt()} °C "
            binding.textViewFelt.text =
                "Felt Temperature : ${(response.main.feels_like - 273).toInt()} °C "
            binding.textViewHumidity.text = "Humidity : % ${response.main.humidity}"
            binding.textViewWindSpeed.text = "Wind Speed : ${response.wind.speed} m/s"
            binding.sunset.text = "Sunset : $localTime1"
            binding.sunrise.text = "Sunrise : $localTime2"
            binding.editTextText.setText(response.name.toUpperCase())
            binding.textViewDay.text = dayOfWeek2.toUpperCase()

        })

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun localTimeCalc(a: Int): LocalTime {
        val utcTimeInSeconds = a.toLong()
        val instant = Instant.ofEpochSecond(utcTimeInSeconds)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Istanbul"))
        val localTime = localDateTime.toLocalTime()

        return localTime
    }

    private fun visibilityGone(view: List<View>) {
        for (i in view) {
            i.visibility = View.GONE
        }
    }

    private fun visibilityVisible(view: List<View>) {
        for (i in view) {
            i.visibility = View.VISIBLE
        }
    }

    private fun startWelcomeAnimation(listView: List<View>) {

        val lottieAnimationView: LottieAnimationView = binding.weatheranimaiton2
        lottieAnimationView.setAnimation(R.raw.animationwelcome)
        lottieAnimationView.playAnimation()
        if (lottieAnimationView.visibility == View.VISIBLE) {
            visibilityGone(listView)
        }

        Handler().postDelayed({
            lottieAnimationView.cancelAnimation()
            lottieAnimationView.visibility = View.GONE
            visibilityVisible(listView)
        }, ANIMATION_DURATION)

    }

}








