package dora.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import dora.widget.alertdialog.R

open class BaseTipsDialog(context: Activity, @StyleRes themeResId: Int) : AppCompatDialog(context, themeResId) {

    private var linearLayoutRoot: LinearLayout? = null
    @ColorInt
    protected var themeColor: Int = 0

    protected var contentView: View? = null
        private set

    constructor(activity: Activity) : this(activity, R.style.DoraView_Theme_Widget_TipsDialog)

    init {
        try {
            themeColor = ContextCompat.getColor(context, R.color.colorPrimary)
            setOwnerActivity(context)
            superInitViews()
        } catch (ignore: Exception) {
        }
    }

    @ColorInt fun getThemeColor() : Int {
        return themeColor
    }

    private fun superInitViews() {
        linearLayoutRoot = LinearLayout(context)
        linearLayoutRoot!!.setBackgroundColor(Color.parseColor("#00000000"))
        linearLayoutRoot!!.gravity = Gravity.CENTER
        setCanceledOnTouchOutside(false)
    }

    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        if (cancel) {
            linearLayoutRoot!!.setOnClickListener { dismiss() }
        }
    }

    fun setGravity(gravity: Int): BaseTipsDialog {
        val window = window
        window?.setGravity(gravity)
        return this
    }

    fun showTop() {
        setGravity(Gravity.TOP)
        show()
    }

    fun showBottom() {
        setGravity(Gravity.BOTTOM)
        show()
    }

    override fun show() {
        try {
            if (ownerActivity != null && !ownerActivity!!.isFinishing) {
                super.show()
            }
        } catch (ignore: Exception) {
        }
    }

    /**
     * 显示在指定控件的下方位置。
     */
    fun showViewBottom(v: View?) {
        if (v == null) {
            show()
            return
        }
        val height = v.measuredHeight
        val location = IntArray(2)
        v.getLocationInWindow(location) // 若是普通activity，则y坐标为可见的状态栏高度+可见的标题栏高度+view左上角到标题栏底部的距离。
        // 减去状态栏高度
        var statusBarHeight = 0
        // 获取status_bar_height资源的ID
        val resources = v.context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            // 根据资源ID获取响应的尺寸值
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        val isSamSung = false
        val showTopPadding: Int = if (isSamSung) {
            location[1] + height
        } else {
            location[1] - statusBarHeight + height
        }
        paddingTop(showTopPadding)
        showTop()
    }

    /**
     * 显示在指定控件的上方位置。
     */
    fun showViewTop(v: View?) {
        if (v == null) {
            show()
            return
        }
        val height = v.measuredHeight
        val location = IntArray(2)
        v.getLocationInWindow(location)
        // 若是普通activity，则y坐标为可见的状态栏高度+可见的标题栏高度+view左上角到标题栏底部的距离。
        // 减去状态栏高度
        var statusBarHeight = 0
        // 获取status_bar_height资源的ID
        val resources = v.context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            // 根据资源ID获取响应的尺寸值
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        val isSamsung = false // 三星s8适配 18.5:9屏幕
        val showTopPadding: Int = if (isSamsung) {
            location[1] - height
        } else {
            location[1] - statusBarHeight - height
        }
        paddingTop(showTopPadding)
        showTop()
    }

    /**
     * 设置窗口的显示和隐藏动画。
     */
    fun setWindowAnimations(@StyleRes resId: Int) {
        window!!.setWindowAnimations(resId)
    }

    fun paddingTop(top: Int): BaseTipsDialog {
        linearLayoutRoot!!.setPadding(
            linearLayoutRoot!!.paddingLeft, top,
            linearLayoutRoot!!.paddingRight, linearLayoutRoot!!.paddingBottom
        )
        return this
    }

    fun paddingBottom(bottom: Int): BaseTipsDialog {
        linearLayoutRoot!!.setPadding(
            linearLayoutRoot!!.paddingLeft, linearLayoutRoot!!.paddingTop,
            linearLayoutRoot!!.paddingRight, bottom
        )
        return this
    }

    fun paddingLeft(left: Int): BaseTipsDialog {
        linearLayoutRoot!!.setPadding(
            left, linearLayoutRoot!!.paddingTop,
            linearLayoutRoot!!.paddingRight, linearLayoutRoot!!.paddingBottom
        )
        return this
    }

    fun paddingRight(right: Int): BaseTipsDialog {
        linearLayoutRoot!!.setPadding(
            linearLayoutRoot!!.paddingLeft, linearLayoutRoot!!.paddingTop,
            right, linearLayoutRoot!!.paddingBottom
        )
        return this
    }

    fun paddings(paddings: Int): BaseTipsDialog {
        linearLayoutRoot!!.setPadding(paddings, paddings, paddings, paddings)
        return this
    }

    /**
     * 设置窗口上下左右的边距。
     */
    fun padding(left: Int, top: Int, right: Int, bottom: Int): BaseTipsDialog {
        linearLayoutRoot!!.setPadding(left, top, right, bottom)
        return this
    }

    fun setDialogView(view: View?, params: ViewGroup.LayoutParams?): BaseTipsDialog {
        var params = params
        contentView = view
        wrapView(contentView)
        if (params == null) {
            params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        super.setContentView(linearLayoutRoot!!, params)
        // 设置全屏生效
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return this
    }

    fun wrapView(view: View?) {
        linearLayoutRoot!!.removeAllViews()
        linearLayoutRoot!!.addView(
            view,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun setContentView(@LayoutRes layoutId: Int) {
        val view = LayoutInflater.from(context).inflate(layoutId, null)
        this.setContentView(view, null)
    }

    fun setContentView(@LayoutRes layoutId: Int, params: ViewGroup.LayoutParams?) {
        val view = LayoutInflater.from(context).inflate(layoutId, null)
        this.setContentView(view, params)
    }

    override fun setContentView(view: View) {
        this.setContentView(view, null)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        setDialogView(view, params)
    }

    override fun dismiss() {
        try {
            super.dismiss()
        } catch (ignore: Exception) {
        }
    }

    /**
     * 判定当前是否需要隐藏
     */
    fun isHideKeyboard(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(ev.x > left && ev.x < right && ev.y > top && ev.y < bottom)
        }
        return false
    }

    /**
     * 隐藏软键盘.
     */
    fun hideSoftInput(token: IBinder?) {
        if (token != null) {
            val manager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (isHideKeyboard(view, event)) {
                hideSoftInput(view!!.windowToken)
            }
        }
        return super.dispatchTouchEvent(event)
    }

    protected open fun initViews() {}
}