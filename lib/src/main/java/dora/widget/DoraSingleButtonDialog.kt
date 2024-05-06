package dora.widget

import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dora.widget.alertdialog.R

class DoraSingleButtonDialog(activity: Activity, private var listener: DialogListener?) :
    BaseTipsDialog(activity), View.OnClickListener {

    private var tvContent: TextView? = null
    private var tvConfirm: TextView? = null
    private var llContainer: LinearLayout? = null
    private lateinit var buttonType: String

    init {
        initViews()
        setCanceledOnTouchOutside(true)
    }

    override fun initViews() {
        setContentView(R.layout.dialog_dview_single_button)
        tvContent = findViewById(R.id.tvContent)
        tvConfirm = findViewById(R.id.tvConfirm)
        llContainer = findViewById(R.id.llContainer)
        llContainer!!.setOnClickListener(this)
        tvConfirm!!.setOnClickListener(this)
    }

    fun show(buttonType: String, message: String) {
        this.buttonType = buttonType
        tvContent!!.text = message
        show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvConfirm -> if (listener != null) {
                listener!!.onButtonClick(buttonType)
                dismiss()
            }
        }
    }

    interface DialogListener {
        fun onButtonClick(buttonType: String)
    }
}