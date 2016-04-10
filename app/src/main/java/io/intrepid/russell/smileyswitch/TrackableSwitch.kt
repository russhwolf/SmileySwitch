package io.intrepid.russell.smileyswitch

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.intrepid.russell.smileyswitch.support.SwitchCompat
import org.jetbrains.anko.custom.ankoView

/**
 * This is an extension of the SwitchCompat widget which exposes a listener to track the thumb position.
 */
class TrackableSwitch : SwitchCompat {
    private var thumbPositionListener: ThumbPositionListener? = null

    /**
     * If the last integer thumb position was 0, this is false.
     * If it was 1, this is true
     */
    private var wasChecked = false

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    // This override is impossible using an unmodified version of SwitchCompat
    override fun setThumbPosition(position: Float) {
        super.setThumbPosition(position)
        if (position <= 0) {
            wasChecked = false
        }
        if (position >= 1) {
            wasChecked = true
        }
        thumbPositionListener?.onThumbPositionChanged(this, position, wasChecked)
    }

    fun setThumbPositionListener(listener: ThumbPositionListener) {
        thumbPositionListener = listener
    }

    fun setThumbPositionListener(listener: (View, Float, Boolean) -> Unit) {
        thumbPositionListener = object : ThumbPositionListener {
            override fun onThumbPositionChanged(view: TrackableSwitch, position: Float, wasChecked: Boolean)
                    = listener.invoke(view, position, wasChecked)
        }
    }

    interface ThumbPositionListener {
        fun onThumbPositionChanged(view: TrackableSwitch, position: Float, wasChecked: Boolean)
    }
}

// For Anko use
inline fun android.view.ViewManager.trackableSwitch() = trackableSwitch {}
inline fun android.view.ViewManager.trackableSwitch(init: io.intrepid.russell.smileyswitch.TrackableSwitch.() -> Unit) = ankoView({ io.intrepid.russell.smileyswitch.TrackableSwitch(it) }, init)