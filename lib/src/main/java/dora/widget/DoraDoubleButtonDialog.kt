package dora.widget

import android.app.Activity
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import dora.widget.alertdialog.R

class DoraDoubleButtonDialog(activity: Activity, private var listener: DialogListener) : BaseTipsDialog(activity),
    View.OnClickListener {

    private var tvContent: TextView? = null
    private var tvCancel: TextView? = null
    private var tvConfirm: TextView? = null
    private var tvTitle: TextView? = null
    private var rlContainer: RelativeLayout? = null
    private lateinit var buttonType: String

    init {
        init()
        setCanceledOnTouchOutside(true)
    }

    override fun init() {
        setContentView(R.layout.dialog_dview_double_button)
        tvContent = findViewById(R.id.tvContent)
        tvTitle = findViewById(R.id.tvTitle)
        tvCancel = findViewById(R.id.tvCancel)
        tvConfirm = findViewById(R.id.tvConfirm)
        rlContainer = findViewById(R.id.rlContainer)
        tvCancel!!.setOnClickListener(this)
        rlContainer!!.setOnClickListener(this)
        tvConfirm!!.setOnClickListener(this)
    }

    @JvmOverloads
    fun show(buttonType: String, message: String, title: String? = null) {
        this.buttonType = buttonType
        tvContent!!.text = message
        title?.let {
            tvTitle!!.text = it
        }
        show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvCancel -> dismiss()
            R.id.tvConfirm -> {
                listener.onButtonClick(buttonType)
                dismiss()
            }
        }
    }

    interface DialogListener {
        fun onButtonClick(buttonType: String)
    }
}