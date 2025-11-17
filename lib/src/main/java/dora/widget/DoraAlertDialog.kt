package dora.widget

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.TruncateAt
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import dora.widget.alertdialog.R

/**
 * 号称很强大的kotlin版本提示信息框，可以自定义布局内容。
 */
class DoraAlertDialog private constructor(context: Context) :
    AppCompatDialog(context, R.style.DoraView_Theme_Widget_AlertDialog) {

    private var onPositive: View.OnClickListener? = null
    private var onNegative: View.OnClickListener? = null
    private var onDismiss: DialogInterface.OnDismissListener? = null
    private var view: View? = null
    @ColorInt
    private var themeColor: Int = Color.BLACK
    private var isFullScreen = false
    private var isCancel = false
    private var width = 0
    private var height = 0
    private var titleTextSize = 16f
    private var messageTextColor = Color.GRAY
    private var messageTextSize = 15f
    private var title: String = ""
    private var message: String = ""
    private var buttonVisible = true
    private lateinit var positiveLabel: String
    private lateinit var negativeLabel: String

    private val positiveButton: Button by lazy {
        Button(context)
    }

    private val negativeButton: Button by lazy {
        Button(context)
    }

    private val titleTextView: TextView by lazy {
        TextView(context)
    }

    private val messageTextView: TextView by lazy {
        TextView(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    fun width(width: Int) : DoraAlertDialog {
        this.width = width
        return this
    }

    fun height(height: Int) : DoraAlertDialog {
        this.height = height
        return this
    }

    fun hideBottomButtons() : DoraAlertDialog {
        this.buttonVisible = false
        canCancel(true)
        return this
    }

    fun size(width: Int, height: Int) : DoraAlertDialog {
        width(width)
        height(height)
        return this
    }

    fun contentView(contentView: View) : DoraAlertDialog {
        this.view = contentView
        return this
    }

    fun title(title: String) : DoraAlertDialog {
        this.title = title
        titleTextView.text = title
        return this
    }

    fun message(message: String) : DoraAlertDialog {
        this.message = message
        messageTextView.text = message
        return this
    }

    fun themeColor(@ColorInt color: Int) : DoraAlertDialog {
        this.themeColor = color
        return this
    }

    fun themeColorResId(@ColorRes colorResId: Int) : DoraAlertDialog {
        this.themeColor = ContextCompat.getColor(context, colorResId)
        return this
    }

    fun titleTextSize(textSize: Float) : DoraAlertDialog {
        this.titleTextSize = textSize
        return this
    }

    fun messageTextColor(@ColorInt textColor: Int) : DoraAlertDialog {
        this.messageTextColor = textColor
        return this
    }

    fun positiveButton(positiveLabel: String) : DoraAlertDialog {
        this.positiveLabel = positiveLabel
        positiveButton.text = positiveLabel
        return this
    }

    fun negativeButton(negativeLabel: String) : DoraAlertDialog {
        this.negativeLabel = negativeLabel
        negativeButton.text = negativeLabel
        return this
    }

    fun positiveListener(listener: View.OnClickListener) : DoraAlertDialog {
        this.onPositive = listener
        return this
    }

    fun negativeListener(listener: View.OnClickListener) : DoraAlertDialog {
        this.onNegative = listener
        return this
    }

    fun dismissListener(listener: DialogInterface.OnDismissListener) : DoraAlertDialog {
        this.onDismiss = listener
        return this
    }

    fun fullScreen(isFullScreen: Boolean) : DoraAlertDialog {
        this.isFullScreen = isFullScreen
        return this
    }

    fun canCancel(isCancel: Boolean) : DoraAlertDialog {
        this.isCancel = isCancel
        return this
    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    fun <T : View> getView(viewId: Int) : T? {
        return view?.findViewById<T>(viewId)
    }

    fun show(@LayoutRes layoutId: Int, build: (DoraAlertDialog.(View) -> Unit)? = null) : DoraAlertDialog {
        val contentView = LayoutInflater.from(context).inflate(layoutId, null)
        this.view = contentView
        build?.invoke(this, contentView)
        create()
        show()
        return this
    }

    fun show(contentView: View, build: (DoraAlertDialog.(View) -> Unit)? = null) : DoraAlertDialog {
        this.view = contentView
        build?.invoke(this, contentView)
        create()
        show()
        return this
    }

    fun show(message: String, build: (DoraAlertDialog.() -> Unit)? = null) : DoraAlertDialog {
        this.message = message
        build?.invoke(this)
        create()
        show()
        return this
    }

    private fun init() {
        positiveLabel = context.getString(R.string.confirm)
        negativeLabel = context.getString(R.string.cancel)
        val dialogLayout = LinearLayout(context)
        dialogLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogLayout.orientation = LinearLayout.VERTICAL
        dialogLayout.minimumWidth = dp2px(context, 200f)
        dialogLayout.setBackgroundColor(Color.WHITE)
        val topLayout = LinearLayout(context)
        topLayout.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        topLayout.gravity = Gravity.CENTER_VERTICAL
        titleTextView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        titleTextView.text = title
        titleTextView.setTextColor(themeColor)
        titleTextView.textSize = titleTextSize
        titleTextView.ellipsize = TruncateAt.END
        val dp10 = dp2px(context, 10f)
        val dp20 = dp2px(context, 20f)
        val dp1 = dp2px(context, 1f)
        titleTextView.setPadding(dp10, dp10, dp10, dp10)
        topLayout.addView(titleTextView)
        dialogLayout.addView(topLayout)
        val view = View(context)
        view.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp1)
        view.setBackgroundColor(-0x353536)
        if (!TextUtils.isEmpty(title)) {
            topLayout.visibility = View.VISIBLE
            view.visibility = View.VISIBLE
        } else {
            topLayout.visibility = View.GONE
            view.visibility = View.GONE
        }
        val layoutContainer = RelativeLayout(context)
        layoutContainer.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (this.view != null) {
            val params = RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            layoutContainer.addView(this.view, params)
        } else {
            messageTextView.text = message
            messageTextView.textSize = messageTextSize
            messageTextView.setTextColor(messageTextColor)
            val params = RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.leftMargin = dp10
            params.topMargin = dp20
            params.rightMargin = dp10
            params.bottomMargin = dp20
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            layoutContainer.addView(messageTextView, params)
        }
        val bottomLayout = LinearLayout(context)
        bottomLayout.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 40f))
        bottomLayout.orientation = LinearLayout.HORIZONTAL
        positiveButton.layoutParams =
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        positiveButton.setBackgroundResource(R.drawable.selector_dview_alert_dialog_bottom_button)
        positiveButton.setOnClickListener {
            dismiss()
            onPositive?.onClick(it)
        }
        negativeButton.layoutParams =
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        negativeButton.setBackgroundResource(R.drawable.selector_dview_alert_dialog_bottom_button)
        negativeButton.setOnClickListener {
            dismiss()
            onNegative?.onClick(it)
        }
        if (positiveLabel.isNotEmpty()) {
            positiveButton.text = positiveLabel
            positiveButton.setTextColor(themeColor)
        } else {
            positiveButton.visibility = View.GONE
        }
        if (negativeLabel.isNotEmpty()) {
            negativeButton.text = negativeLabel
        } else {
            negativeButton.visibility = View.GONE
        }
        val horizontalDivider = View(context)
        horizontalDivider.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp1)
        horizontalDivider.setBackgroundColor(-0x353536)
        dialogLayout.addView(view)
        val verticalDivider = View(context)
        verticalDivider.layoutParams =
            LinearLayout.LayoutParams(dp1, LinearLayout.LayoutParams.MATCH_PARENT)
        verticalDivider.setBackgroundColor(-0x353536)
        dialogLayout.addView(layoutContainer)
        if (buttonVisible) {
            dialogLayout.addView(horizontalDivider)
        }
        bottomLayout.addView(negativeButton)
        bottomLayout.addView(verticalDivider)
        bottomLayout.addView(positiveButton)
        if (buttonVisible) {
            dialogLayout.addView(bottomLayout)
        }
        val params = window!!.attributes
        if (isFullScreen) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            if (width > 0) {
                params.width = width
            } else {
                params.width = WindowManager.LayoutParams.WRAP_CONTENT
            }
            if (height > 0) {
                params.height = height
            } else {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }
        if (isCancel) {
            setCanceledOnTouchOutside(true)
            setCancelable(true)
        } else {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
        }
        window!!.attributes = params
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(dialogLayout)
        setOnDismissListener {
            onDismiss?.onDismiss(it)
        }
    }

    companion object {

        val ID_INPUT_ONE = R.id.et_dview_input
        val ID_INPUT_TWO = R.id.et_dview_input2
        val ID_INPUT_THREE = R.id.et_dview_input3
        val ID_INPUT_FOUR = R.id.et_dview_input4
        val ID_INPUT_FIVE = R.id.et_dview_input5
        val ID_INPUT_SIX = R.id.et_dview_input6

        @Volatile
        private var instance: DoraAlertDialog? = null

        @JvmStatic
        fun create(context: Context): DoraAlertDialog {
            return instance ?: synchronized(this) {
                instance ?: DoraAlertDialog(context).also { instance = it }
            }
        }
    }
}