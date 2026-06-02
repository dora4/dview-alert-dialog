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
import android.widget.EditText
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

    private var onPositive: (() -> Unit)? = null
    private var onNegative: (() -> Unit)? = null
    private var onDismiss: (() -> Unit)? = null

    private var positiveClickListener: View.OnClickListener? = null
    private var negativeClickListener: View.OnClickListener? = null
    private var dismissListener: DialogInterface.OnDismissListener? = null
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
    private var positiveLabel = ""
    private var negativeLabel = ""
    private lateinit var positiveButton: Button
    private lateinit var negativeButton: Button
    private lateinit var titleTextView: TextView
    private lateinit var messageTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        positiveButton = Button(context)
        negativeButton = Button(context)
        titleTextView = TextView(context)
        messageTextView = TextView(context)
        init()
    }

    fun width(width: Int): DoraAlertDialog {
        this.width = width
        return this
    }

    fun height(height: Int): DoraAlertDialog {
        this.height = height
        return this
    }

    fun hideBottomButtons(): DoraAlertDialog {
        this.buttonVisible = false
        canCancel(true)
        return this
    }

    fun size(width: Int, height: Int): DoraAlertDialog {
        width(width)
        height(height)
        return this
    }

    fun contentView(contentView: View): DoraAlertDialog {
        this.view = contentView
        return this
    }

    fun title(title: String): DoraAlertDialog {
        this.title = title
        titleTextView.text = title
        return this
    }

    fun message(message: String): DoraAlertDialog {
        this.message = message
        messageTextView.text = message
        return this
    }

    fun themeColor(@ColorInt color: Int): DoraAlertDialog {
        this.themeColor = color
        return this
    }

    fun themeColorResId(@ColorRes colorResId: Int): DoraAlertDialog {
        this.themeColor = ContextCompat.getColor(context, colorResId)
        return this
    }

    fun titleTextSize(textSize: Float): DoraAlertDialog {
        this.titleTextSize = textSize
        return this
    }

    fun messageTextColor(@ColorInt textColor: Int): DoraAlertDialog {
        this.messageTextColor = textColor
        return this
    }

    fun positiveButton(positiveLabel: String): DoraAlertDialog {
        this.positiveLabel = positiveLabel
        positiveButton.text = positiveLabel
        return this
    }

    fun negativeButton(negativeLabel: String): DoraAlertDialog {
        this.negativeLabel = negativeLabel
        negativeButton.text = negativeLabel
        return this
    }

    fun positiveListener(block: () -> Unit): DoraAlertDialog {
        this.onPositive = block
        return this
    }

    fun negativeListener(block: () -> Unit): DoraAlertDialog {
        this.onNegative = block
        return this
    }

    fun dismissListener(block: () -> Unit): DoraAlertDialog {
        this.onDismiss = block
        return this
    }

    fun positiveListener(listener: View.OnClickListener): DoraAlertDialog {
        this.positiveClickListener = listener
        return this
    }

    fun negativeListener(listener: View.OnClickListener): DoraAlertDialog {
        this.negativeClickListener = listener
        return this
    }

    fun dismissListener(listener: DialogInterface.OnDismissListener): DoraAlertDialog {
        this.dismissListener = listener
        return this
    }

    fun fullScreen(isFullScreen: Boolean): DoraAlertDialog {
        this.isFullScreen = isFullScreen
        return this
    }

    fun canCancel(isCancel: Boolean): DoraAlertDialog {
        this.isCancel = isCancel
        return this
    }

    private fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    fun <T : View> getView(viewId: Int): T? {
        return view?.findViewById<T>(viewId)
    }

    @Deprecated(
        message = "Use showCustom() instead.",
        replaceWith = ReplaceWith("showCustom()")
    )
    fun show(
        @LayoutRes layoutId: Int,
        build: (DoraAlertDialog.(View) -> Unit)? = null
    ): DoraAlertDialog {
        val contentView = LayoutInflater.from(context).inflate(layoutId, null)
        this.view = contentView
        build?.invoke(this, contentView)
        create()
        show()
        return this
    }

    @Deprecated(
        message = "Use showCustom() instead.",
        replaceWith = ReplaceWith("showCustom()")
    )
    fun show(contentView: View, build: (DoraAlertDialog.(View) -> Unit)? = null): DoraAlertDialog {
        this.view = contentView
        build?.invoke(this, contentView)
        create()
        show()
        return this
    }

    @Deprecated(
        message = "Use showMessage() instead.",
        replaceWith = ReplaceWith("showMessage()")
    )
    fun show(message: String, build: (DoraAlertDialog.() -> Unit)? = null): DoraAlertDialog {
        this.message = message
        build?.invoke(this)
        create()
        show()
        return this
    }
    
    fun interface OnBuildListener {
        fun onBuild(dialog: DoraAlertDialog, contentView: View)
    }

    fun showCustom(
        @LayoutRes layoutId: Int,
        listener: OnBuildListener? = null
    ): MyAlertDialog {
        val contentView = LayoutInflater.from(context).inflate(layoutId, null)
        this.contentView = contentView
        listener?.onBuild(this, contentView)
        create()
        show()
        return this
    }
    
    @JvmSynthetic
    fun showCustom(@LayoutRes layoutId: Int, build: (DoraAlertDialog.(View) -> Unit)? = null) : DoraAlertDialog {
        return show(layoutId, build)
    }

    fun showCustom(
        contentView: View,
        listener: OnBuildListener?
    ): DoraAlertDialog {
        this.contentView = contentView
        listener?.onBuild(this, contentView)
        create()
        show()
        return this
    }
    
    @JvmSynthetic
    fun showCustom(contentView: View, build: (DoraAlertDialog.(View) -> Unit)? = null) : DoraAlertDialog {
        return show(contentView, build)
    }

    /**
     * 创建简单信息对话框。
     *
     * 示例：
     *
     * DoraAlertDialog.create(this)
     *     .info(
     *         title = "提示",
     *         message = "兑换成功"
     *     )
     */
    fun info(
        message: String,
        title: String = "",
        positiveText: String = context.getString(R.string.confirm),
        callback: (() -> Unit)? = null
    ): DoraAlertDialog {
        this.title(title)
        this.message(message)
        negativeButton("")
        positiveButton(positiveText)
        positiveListener {
            callback?.invoke()
        }
        create()
        show()
        return this
    }

    /**
     * 创建简单输入弹窗。
     *
     * 示例：
     *
     * DoraAlertDialog.create(this)
     *     .title("请输入昵称")
     *     .input(
     *         hint = "昵称"
     *     ) {
     *         nickname ->
     *         updateNickname(nickname)
     *     }
     *     .show()
     */
    fun input(
        hint: String = "",
        defaultValue: String = "",
        positive: ((String) -> Unit)? = null
    ): DoraAlertDialog {
        val editText = EditText(context).apply {
            id = ID_INPUT_ONE
            setHint(hint)
            setText(defaultValue)
            setSelection(text.length)
        }
        contentView(editText)
        positiveListener {
            positive?.invoke(editText.text.toString())
        }
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
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
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
            onPositive?.invoke()
            positiveClickListener?.onClick(it)
            dismiss()
        }
        negativeButton.layoutParams =
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        negativeButton.setBackgroundResource(R.drawable.selector_dview_alert_dialog_bottom_button)
        negativeButton.setOnClickListener {
            onNegative?.invoke()
            negativeClickListener?.onClick(it)
            dismiss()
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
            onDismiss?.invoke()
            dismissListener?.onDismiss(it)
        }
    }

    companion object {

        val ID_INPUT_ONE = R.id.et_dview_input
        val ID_INPUT_TWO = R.id.et_dview_input2
        val ID_INPUT_THREE = R.id.et_dview_input3
        val ID_INPUT_FOUR = R.id.et_dview_input4
        val ID_INPUT_FIVE = R.id.et_dview_input5
        val ID_INPUT_SIX = R.id.et_dview_input6

        /**
         * 创建对话框实例。
         *
         * 示例：
         *
         * DoraAlertDialog.create(this)
         *     .title("提示")
         *     .message("保存成功")
         *     .show()
         */
        @JvmStatic
        fun create(context: Context): DoraAlertDialog {
            return DoraAlertDialog(context)
        }

        /**
         * 显示简单消息弹窗。
         *
         * 示例：
         *
         * DoraAlertDialog.showMessage(
         *     context = this,
         *     message = "保存成功"
         * )
         */
        @JvmStatic
        fun showMessage(
            context: Context,
            message: String,
            title: String = ""
        ): DoraAlertDialog {
            return create(context)
                .title(title)
                .message(message)
                .negativeButton("")
                .apply {
                    create()
                    show()
                }
        }

        /**
         * 显示简单输入弹窗。
         *
         * 示例：
         *
         * DoraAlertDialog.showInput(
         *     context = this,
         *     title = "请输入兑换码",
         *     hint = "兑换码"
         * ) { code ->
         *     redeem(code)
         * }
         */
        @JvmStatic
        fun showInput(
            context: Context,
            title: String,
            hint: String = "",
            defaultValue: String = "",
            positiveText: String = context.getString(R.string.confirm),
            callback: (String) -> Unit
        ): DoraAlertDialog {
            val editText = EditText(context).apply {
                id = ID_INPUT_ONE
                setHint(hint)
                setText(defaultValue)
                setSelection(text.length)
            }
            return create(context)
                .title(title)
                .contentView(editText)
                .positiveButton(positiveText)
                .positiveListener {
                    callback(editText.text.toString())
                }
                .apply {
                    create()
                    show()
                }
        }
    }
}
