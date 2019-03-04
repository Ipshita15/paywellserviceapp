package com.cloudwell.paywell.services.libaray

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.support.annotation.StringRes
import android.support.annotation.StyleRes
import android.support.v4.widget.TextViewCompat
import android.util.AttributeSet
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import com.cloudwell.paywell.services.R
import kotlinx.android.synthetic.main.sequence_step.view.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/3/19.
 */
public class SequenceStep(context: Context?, attrs: AttributeSet?)
    : TableRow(context, attrs) {

    public constructor(context: Context) : this(context, null)

    private var isActive: Boolean = false

    init {
        View.inflate(getContext(), R.layout.sequence_step, this)

        clipToPadding = false
        clipChildren = false

        val attributes = getContext().theme.obtainStyledAttributes(
                attrs,
                R.styleable.SequenceStep,
                0,
                R.style.SequenceStep)

//        setupAnchor(attributes)
//        setupAnchorTextAppearance(attributes)
//        setupTitle(attributes)
//        setupTitleTextAppearance(attributes)
//        setupSubtitle(attributes)
//        setupSubtitleTextAppearance(attributes)
//        setupActive(attributes)

        attributes.recycle()
    }

    /**
     * Sets the anchor label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchor
     */
    public fun setDate(anchor: CharSequence?) {
        this.date.text = anchor
        this.date.visibility = View.VISIBLE
        this.date.minWidth = resources.getDimensionPixelSize(R.dimen.sequence_anchor_min_width)
    }

    public fun setTime(anchor: CharSequence?) {
        this.time.text = anchor
        this.time.visibility = View.VISIBLE
        this.time.minWidth = resources.getDimensionPixelSize(R.dimen.sequence_anchor_min_width)
    }

    fun setDateColor(s: String) {
        this.date.setTextColor(Color.parseColor(s))
    }


    /**
     * Sets the anchor text appearance
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_anchorTextAppearance
     */
    public fun setAnchorTextAppearance(@StyleRes resourceId: Int) {
        TextViewCompat.setTextAppearance(date, resourceId)
        verticallyCenter(date, title)
    }

    /**
     * Sets the title label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_title
     */
    public fun setTitle(title: CharSequence?) {
        this.title.text = title
        this.title.visibility = View.VISIBLE
    }

    /**
     * Sets the title label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_title
     */
    public fun setTitle(@StringRes resId: Int) {
        setTitle(context.getString(resId))
    }

    /**
     * Sets the anchor text appearance
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_titleTextAppearance
     */
    public fun setTitleTextAppearance(@StyleRes resourceId: Int) {
        TextViewCompat.setTextAppearance(title, resourceId)
        verticallyCenter(date, title)
    }

    /**
     * Sets the subtitle label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_subtitle
     */
    public fun setSubtitle(subtitle: CharSequence?) {
        this.subtitle.text = subtitle
        this.subtitle.visibility = View.VISIBLE
    }

    /**
     * Sets the subtitle label
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_subtitle
     */
    public fun setSubtitle(@StringRes resId: Int) {
        setSubtitle(context.getString(resId))
    }

    /**
     * Sets the subtitle text appearance
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_subtitleTextAppearance
     */
    public fun setSubtitleTextAppearance(@StyleRes resourceId: Int) {
        TextViewCompat.setTextAppearance(subtitle, resourceId)
    }

    /**
     * Returns whether step is active step
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_active
     */
    public fun isActive(): Boolean {
        return isActive
    }

    /**
     * Sets whether step is active step
     *
     * @attr ref com.transferwise.sequencelayout.R.styleable#SequenceStep_active
     */
    public fun setActive(isActive: Boolean) {
        this.isActive = isActive
    }

    fun getDotOffset(): Int =
            (Math.max(getViewHeight(date), getViewHeight(title)) - 8.toPx()) / 2 //TODO dynamic dot height

    private fun setupAnchor(attributes: TypedArray) {
        if (!attributes.hasValue(R.styleable.SequenceStep_anchor)) {
            date.visibility = View.INVISIBLE
        } else {
            setDate(attributes.getString(R.styleable.SequenceStep_anchor))
        }
    }

    private fun setupSubtitle(attributes: TypedArray) {
        if (!attributes.hasValue(R.styleable.SequenceStep_subtitle)) {
            subtitle.visibility = View.GONE
        } else {
            setSubtitle(attributes.getString(R.styleable.SequenceStep_subtitle))
        }
    }

    private fun setupTitle(attributes: TypedArray) {
        if (!attributes.hasValue(R.styleable.SequenceStep_title)) {
            title.visibility = View.GONE
        } else {
            setTitle(attributes.getString(R.styleable.SequenceStep_title))
        }
    }

    private fun setupTitleTextAppearance(attributes: TypedArray) {
        if (attributes.hasValue(R.styleable.SequenceStep_titleTextAppearance)) {
            setTitleTextAppearance(attributes.getResourceId(R.styleable.SequenceStep_titleTextAppearance, 0))
        }
    }

    private fun setupSubtitleTextAppearance(attributes: TypedArray) {
        if (attributes.hasValue(R.styleable.SequenceStep_subtitleTextAppearance)) {
            setSubtitleTextAppearance(attributes.getResourceId(R.styleable.SequenceStep_subtitleTextAppearance, 0))
        }
    }

    private fun setupAnchorTextAppearance(attributes: TypedArray) {
        if (attributes.hasValue(R.styleable.SequenceStep_anchorTextAppearance)) {
            setAnchorTextAppearance(attributes.getResourceId(R.styleable.SequenceStep_anchorTextAppearance, 0))
        }
    }

    private fun setupActive(attributes: TypedArray) {
        setActive(attributes.getBoolean(R.styleable.SequenceStep_active, false))
    }

    private fun verticallyCenter(vararg views: View) {
        var maxHeight = 0
        for (view in views) {
            val height = getViewHeight(view)
            maxHeight = Math.max(maxHeight, height)
        }
        for (view in views) {
            val height = getViewHeight(view)
            (view.layoutParams as MarginLayoutParams).topMargin = (maxHeight - height) / 2
            view.requestLayout()
        }
    }

    private fun getViewHeight(view: View) =
            if (view is TextView) {
                ((view.lineHeight - view.lineSpacingExtra) / view.lineSpacingMultiplier).toInt()
            } else {
                view.measuredHeight
            }

    public fun hiddenDate() {
        this.date.visibility = View.GONE
    }

    public fun hiddenTime() {
        this.time.visibility = View.GONE
    }


    public fun hiddentext() {
        this.title.visibility = View.GONE
    }

    fun hiddensubtitle() {
        this.subtitle.visibility = View.GONE
    }

}