package io.intrepid.russell.smileyswitch

import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.gravity
import org.jetbrains.anko.onCheckedChange
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            gravity = Gravity.CENTER
            trackableSwitch {
                setThumbResource(R.drawable.thumb)
                setTrackResource(R.drawable.track)
                onCheckedChange { compoundButton, isChecked ->
                    val thumbBg = ((thumbDrawable as LayerDrawable).getDrawable(0)) as TransitionDrawable
                    val track = (trackDrawable) as TransitionDrawable
                    val duration = resources.getInteger(R.integer.animation_duration_ms)
                    if (isChecked) {
                        thumbBg.startTransition(duration)
                        track.startTransition(duration)
                    } else {
                        thumbBg.reverseTransition(duration)
                        track.reverseTransition(duration)
                    }
                }
                setThumbPositionListener { view, position, wasChecked ->
                    val thumbLayers = thumbDrawable as LayerDrawable
                    val progress = if (wasChecked) 1 - position else position
                    val threshold = 0.1f
                    val offset = if (progress < threshold) progress / threshold else if (progress > 1 - threshold) (1 - progress) / threshold else 1f
                    val range = resources.getDimension(R.dimen.thumb_head_turn_offset)
                    val scaledOffset = (offset * range).toInt()
                    if (wasChecked) {
                        thumbLayers.setLayerInset(1, -scaledOffset, 0, scaledOffset, 0)
                        thumbLayers.setLayerInset(2, -scaledOffset, 0, scaledOffset, 0)
                    } else {
                        thumbLayers.setLayerInset(1, scaledOffset, 0, -scaledOffset, 0)
                        thumbLayers.setLayerInset(2, scaledOffset, 0, -scaledOffset, 0)
                    }
                }
            }.lparams {
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
            }
        }
    }
}

inline fun ViewManager.trackableSwitch() = trackableSwitch {}
inline fun ViewManager.trackableSwitch(init: TrackableSwitch.() -> Unit) = ankoView({ TrackableSwitch(it) }, init)