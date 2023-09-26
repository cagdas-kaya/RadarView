package app.uraniumcode.radarview

import android.view.MotionEvent
import android.view.View
import kotlin.math.pow
import kotlin.math.sqrt

class IconClickHandler(
    private val listener: OnRadarIconClickListener,
    private val iconPositions: List<Pair<Float, Float>>
) : View.OnTouchListener {


    private  val clickThreshold = 70


    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = it.x
                    val y = it.y
                    val index = findClickedIconIndex(x, y)

                    if (index != -1) {
                        listener.onRadarIconClick(index)
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun findClickedIconIndex(x: Float, y: Float): Int {
        for (i in iconPositions.indices) {
            val imageX = iconPositions[i].first
            val imageY = iconPositions[i].second
            val distance = calculateDistance(x, y, imageX, imageY)
            if (distance < clickThreshold) {

                return i
            }
        }
        return -1
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1).pow(2) + (y2 - y1).pow(2))
    }
}
