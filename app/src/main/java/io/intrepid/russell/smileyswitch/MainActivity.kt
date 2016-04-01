package io.intrepid.russell.smileyswitch

import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.ViewGroup
import org.jetbrains.anko.appcompat.v7.switchCompat
import org.jetbrains.anko.gravity
import org.jetbrains.anko.onCheckedChange
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            gravity = Gravity.CENTER
            switchCompat {
                setThumbResource(R.drawable.thumb)
                onCheckedChange { compoundButton, b ->
                    val thumbBg = ((thumbDrawable as LayerDrawable).getDrawable(0)) as TransitionDrawable
                    val duration = resources.getInteger(R.integer.animation_duration_ms)
                    if (b) {
                        thumbBg.startTransition(duration)
                    } else {
                        thumbBg.reverseTransition(duration)
                    }
                }
            }.lparams {
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }
}