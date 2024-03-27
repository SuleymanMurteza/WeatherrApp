package com.example.weatherapplication.ui.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.data.entity.dailyweather.DailyWeatherData
import com.example.weatherapplication.data.entity.dailyweather.DailyWeatherResponse
import com.example.weatherapplication.data.entity.dailyweather.Weather
import com.example.weatherapplication.databinding.CardDesignBinding
import com.squareup.picasso.Picasso
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DailyWeatherAdapter(var mContext:Context,var weather:List<DailyWeatherData>)
    : ListAdapter<DailyWeatherData, DailyWeatherAdapter.CardDesignHolder>(DiffCallback()) {


    inner class CardDesignHolder(var binding:CardDesignBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDesignHolder {
        val binding=CardDesignBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return CardDesignHolder(binding)
    }

    override fun getItemCount(): Int {
        return weather.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CardDesignHolder, position: Int) {
        val weatherr= weather[position]
        val t = holder.binding
        t.textViewDescrp.text=weatherr.weather[0].description.toUpperCase()
        val day= day(weatherr.dt.toLong())
        t.textView2.text=day
        val dateTime= time(weatherr.dt_txt)
        t.textViewClock.text=dateTime.toString()
        val imageIcon=weatherr.weather[0].icon
        val imageUrl="https://api.openweathermap.org/img/w/$imageIcon.png"
        Glide.with(mContext).load(imageUrl).into(t.imageView2)

    }
}
@RequiresApi(Build.VERSION_CODES.O)
private fun day(s:Long):String {
    val localDate =
        Instant.ofEpochSecond(s).atZone(ZoneId.systemDefault()).toLocalDate();
    val formatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
    val dayName = localDate.format(formatter)
    return dayName
}
@RequiresApi(Build.VERSION_CODES.O)
private fun time(a:String):LocalDateTime{
    val localtime=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime=LocalDateTime.parse(a,localtime)
    return dateTime

}
class DiffCallback : DiffUtil.ItemCallback<DailyWeatherData>() {
    override fun areItemsTheSame(oldItem: DailyWeatherData, newItem: DailyWeatherData): Boolean {
        return oldItem.dt == newItem.dt // Örnek olarak, benzersiz bir öğe kimliği kontrol ediliyor
    }

    override fun areContentsTheSame(oldItem: DailyWeatherData, newItem: DailyWeatherData): Boolean {
        return oldItem == newItem // İçeriklerin aynı olup olmadığını kontrol ediyoruz
    }
}
