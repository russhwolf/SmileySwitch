package io.intrepid.russell.smileyswitch

import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewManager
import org.jetbrains.anko.backgroundResource
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
                backgroundResource = R.drawable.hack_bugfix_background
                thumbDrawable = getDrawable(R.drawable.thumb)
                setThumbResource(R.drawable.thumb)
                setTrackResource(R.drawable.track)
                onCheckedChange { compoundButton, isChecked ->
                    val thumbBg = (((thumbDrawable as LayerDrawable).getDrawable(0)) as InsetDrawable).drawable as TransitionDrawable
                    val track = (trackDrawable) as TransitionDrawable
                    val duration = resources.getInteger(R.integer.checked_animation_duration_ms)
                    if (isChecked) {
                        thumbBg.startTransition(duration)
                        track.startTransition(duration)
                    } else {
                        thumbBg.reverseTransition(duration)
                        track.reverseTransition(duration)
                    }
                }
                setThumbPositionListener { view, position, wasChecked ->
                    val offset = { progress: Float, threshold: Float, scale: Float ->
                        (scale *
                                (if (progress < threshold) {
                                    progress / threshold
                                } else if (progress > 1 - threshold) {
                                    (1 - progress) / threshold
                                } else {
                                    1f
                                })).toInt()
                    }

                    val progress = if (wasChecked) 1 - position else position
                    val turnThreshold = 0.1f
                    val turnRange = resources.getDimension(R.dimen.turn_range)
                    val turnOffset = offset(progress, turnThreshold, turnRange)
                    val squishThreshold = 0.2f
                    val squishRange = resources.getDimension(R.dimen.squish_range)
                    val squishOffset = offset(progress, squishThreshold, squishRange)

                    val thumbLayers = thumbDrawable as LayerDrawable
                    thumbLayers.setLayerInset(0, -squishOffset, squishOffset, -squishOffset, squishOffset);
                    if (wasChecked) {
                        thumbLayers.setLayerInset(1, -turnOffset, 0, turnOffset, 0)
                        thumbLayers.setLayerInset(2, -turnOffset, 0, turnOffset, 0)
                    } else {
                        thumbLayers.setLayerInset(1, turnOffset, 0, -turnOffset, 0)
                        thumbLayers.setLayerInset(2, turnOffset, 0, -turnOffset, 0)
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