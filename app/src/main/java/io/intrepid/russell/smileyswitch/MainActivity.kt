package io.intrepid.russell.smileyswitch

import android.content.Context
import android.graphics.Canvas
import android.graphics.Movie
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import org.jetbrains.anko.appcompat.v7.switchCompat
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.gravity
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            gravity = Gravity.CENTER
            switchCompat()
                    .lparams {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT
                        height = ViewGroup.LayoutParams.WRAP_CONTENT
                    }
            gifView()
        }
    }
}

class GifView(context: Context) : View(context) {
    val movie: Movie
    val start = System.currentTimeMillis()

    init {
        // Otherwise we freeze or crash when trying to display animated gif
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val inputStream = context.resources.openRawResource(R.raw.normal_fun);
        movie = Movie.decodeStream(inputStream)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(movie.width(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(movie.height(), MeasureSpec.EXACTLY))
    }

    override fun onDraw(canvas: Canvas) {
        val now = System.currentTimeMillis()
        val time = (now - start).toInt() % movie.duration()
        movie.setTime(time)
        movie.draw(canvas, (canvas.width - movie.width()).toFloat() / 2, (canvas.height - movie.height()).toFloat() / 2)
        postInvalidateDelayed(15)
    }
}

inline fun ViewManager.gifView() = gifView {}
inline fun ViewManager.gifView(init: GifView.() -> Unit) = ankoView({ GifView(it) }, init)