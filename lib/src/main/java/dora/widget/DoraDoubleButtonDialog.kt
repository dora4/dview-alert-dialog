package dora.widget

import android.app.Activity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import dora.widget.alertdialog.R

/**
 * 带有确认和取消两个按钮的弹窗，能够满足大部分提示信息弹窗需求。
 */
class DoraDoubleButtonDialog @JvmOverloads constructor(activity: Activity,
                                                       private var listener: DialogListener? = null,
                                                       canceledOnTouchOutside: Boolean? = null)
                                                        : BaseTipsDialog(activity),
                                                            View.OnClickListener {

    private lateinit var tvContent: TextView
    private lateinit var tvCancel: TextView
    private lateinit var tvConfirm: TextView
    private lateinit var tvTitle: TextView
    private lateinit var rlContainer: RelativeLayout

    /**
     * 不同业务使用不同的事件类型，便于复用。
     */
    private lateinit var eventType: String

    init {
        initViews()
        setCanceledOnTouchOutside(canceledOnTouchOutside?:true)
    }

    override fun initViews() {
        setContentView(R.layout.dialog_dview_double_button)
        tvContent = findViewById(R.id.tvContent)
        tvTitle = findViewById(R.id.tvTitle)
        tvCancel = findViewById(R.id.tvCancel)
        tvConfirm = findViewById(R.id.tvConfirm)
        rlContainer = findViewById(R.id.rlContainer)
        tvCancel.setOnClickListener(this)
        rlContainer.setOnClickListener(this)
        tvConfirm.setOnClickListener(this)
    }

    /**
     * 显示对话框。
     */
    @JvmOverloads
    fun show(eventType: String,
             message: String,
             title: String? = null,
             positiveLabel: String? = null,
             negativeLabel: String? = null) {
        this.eventType = eventType
        tvContent.text = message
        title?.let {
            tvTitle.text = it
        }
        positiveLabel?.let {
            tvConfirm.text = positiveLabel
        }
        negativeLabel?.let {
            tvCancel.text = negativeLabel
        }
        show()
    }

    /**
     * 补充一个传入资源id的。
     */
    fun show(eventType: String,
             message: String,
             title: String? = null,
             @StringRes positiveResId: Int,
             @StringRes negativeResId: Int? = null) {
        this.eventType = eventType
        tvContent.text = message
        title?.let {
            tvTitle.text = it
        }
        tvConfirm.text = ContextCompat.getString(context, positiveResId)
        negativeResId?.let {
            tvCancel.text = ContextCompat.getString(context, it)
        }
        show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvConfirm -> {
                listener?.onConfirm(eventType)
                dismiss()
            }
            R.id.tvCancel -> {
                listener?.onCancel(eventType)
                dismiss()
            }
        }
    }

    interface DialogListener {
        fun onConfirm(eventType: String)
        fun onCancel(eventType: String)
    }
}
