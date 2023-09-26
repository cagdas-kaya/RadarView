package app.uraniumcode.radarsample

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.uraniumcode.radarview.OnRadarIconClickListener
import app.uraniumcode.radarview.RadarView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val radarIcons = listOf(
            BitmapFactory.decodeResource(resources, R.drawable.red_dot),
            BitmapFactory.decodeResource(resources, R.drawable.red_dot),
            BitmapFactory.decodeResource(resources, R.drawable.red_dot),
        )
        val positions = listOf(
            Pair(100f, 100f),
            Pair(300f, 200f),
            Pair(400f, 500f),
        )

        val radarView: RadarView = findViewById(R.id.radar)
        radarView.startAnimation()
        radarView.addImagesAndPositions(radarIcons, positions)

        radarView.setIconClickListener(object : OnRadarIconClickListener {
            override fun onRadarIconClick(index: Int) {
                Toast.makeText(applicationContext, "Click:$index", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
