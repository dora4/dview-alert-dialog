package dora.widget

import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dora.widget.alertdialog.R

/**
 * 简版提示信息弹窗，不怎么会用到。
 */
class DoraSingleButtonDialog @JvmOverloads constructor(activity: Activity,
                                                       private var listener: DialogListener? = null,
                                                       canceledOnTouchOutside: Boolean? = null) :
                                                    BaseTipsDialog(activity), View.OnClickListener {

    private lateinit var tvContent: TextView
    private lateinit var tvConfirm: TextView
    private lateinit var llContainer: LinearLayout

    /**
     * 不同业务使用不同的事件类型，便于复用。
     */
    private lateinit var eventType: String

    init {
        initViews()
        setCanceledOnTouchOutside(canceledOnTouchOutside ?: true)
    }

    override fun initViews() {
        setContentView(R.layout.dialog_dview_single_button)
        tvContent = findViewById<TextView>(R.id.tvContent)!!
        tvConfirm = findViewById<TextView>(R.id.tvConfirm)!!
        llContainer = findViewById<LinearLayout>(R.id.llContainer)!!
        llContainer.setOnClickListener(this)
        tvConfirm.setOnClickListener(this)
    }

    /**
     * 显示对话框。
     */
    fun show(eventType: String, message: String, block: (DoraSingleButtonDialog.() -> Unit)? = null) : DoraSingleButtonDialog {
        this.eventType = eventType
        tvContent.text = message
        block?.invoke(this)
        tvConfirm.setTextColor(themeColor)
        show()
        return this
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvConfirm ->  {
                listener?.onButtonClick(eventType)
                dismiss()
            }
        }
    }

    interface DialogListener {
        fun onButtonClick(eventType: String)
    }
}
