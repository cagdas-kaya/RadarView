# RadarView

RadarView is a custom view that supports you to place the icons you want on the radar screen and allows them to be clicked.

## Features 

You can assign a custom image to each icon in the radar and place it in the position you want. You can customize the number of radar circles, their colors and rotation speed.

### Prerequisites

You can implement this directly download this module from GitHub or you can add a dependency from jetpack like bellow 

First, add jitpack dependancy in your project level gradle file 
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

After that add bellow dependancy in your app level gradle 
```
   implementation 'com.github.cagdas-kaya:RadarView:1.0.0'

```
### Example

Firstly add view

```
  <app.uraniumcode.radarview.RadarView
        android:id="@+id/radar"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true"
        app:sweepSpeed="5"
        app:circleCount="4"
        app:circleColor="@color/green"
        app:sweepColor="@color/red"
        />
```

Code part

```
// add your icons

        val radarIcons = listOf(
            BitmapFactory.decodeResource(resources, R.drawable.red_dot),
            BitmapFactory.decodeResource(resources, R.drawable.red_dot),
            BitmapFactory.decodeResource(resources, R.drawable.red_dot),
        )

//add your icons locations

        val positions = listOf(
            Pair(100f, 100f),
            Pair(300f, 200f),
            Pair(400f, 500f),
        )

        val radarView: RadarView = findViewById(R.id.radar)

// start your radar animation

        radarView.startAnimation()

//set your icons to RadarView

        radarView.addImagesAndPositions(radarIcons, positions)

// you can listen click

        radarView.setIconClickListener(object : OnRadarIconClickListener {
            override fun onRadarIconClick(index: Int) {
                Toast.makeText(applicationContext, "Click:$index", Toast.LENGTH_SHORT).show()
            }
        })
```
![](https://s6.gifyu.com/images/S4DET.gif)
