package com.example.sharran.github.dialogFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import com.example.sharran.github.R
import com.example.sharran.github.services.FilterListener
import com.example.sharran.github.utils.AppContext
import com.example.sharran.github.utils.hideKeyboard
import kotlinx.android.synthetic.main.filter_bottom_sheet.*
import java.util.*

class FilterDialog : BottomSheetDialogFragment(){
    val searchActivity = AppContext.searchActivity
    lateinit var filterListener: FilterListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.setOnShowListener {
            val  d = dialog as BottomSheetDialog
            val  bottomSheetInternal = d.findViewById<View>(android.support.design.R.id.design_bottom_sheet)!!
            BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return inflater.inflate(R.layout.filter_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard(language)
        initializeDatePicker()
        initializeClearDate()
        apply.setOnClickListener {
            val filterData = FilterData(
                language = language.text.toString(),
                createdFrom = created_from.text.toString(),
                createdTo = created_to.text.toString(),
                pushedFrom = pushed_from.text.toString(),
                pushedTo = pushed_to.text.toString()
            )
            dismiss()
            filterListener.onFilterClicked(filterData)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initializeDatePicker() {
        created_from.setOnClickListener {
            showDatePicker(created_from , created_from_clear)
        }

        created_to.setOnClickListener {
            showDatePicker(created_to , created_to_clear)
        }

        pushed_from.setOnClickListener {
            showDatePicker(pushed_from , pushed_from_clear)
        }

        pushed_to.setOnClickListener {
            showDatePicker(pushed_to , pushed_to_clear)
        }
    }

    private fun initializeClearDate() {
        created_from_clear.setOnClickListener {
            created_from.text = ""
            showClearIcon(created_from_clear, false)
        }
        created_to_clear.setOnClickListener {
            created_to.text = ""
            showClearIcon(created_to_clear, false)
        }
        pushed_from_clear.setOnClickListener {
            pushed_from.text = ""
            showClearIcon(pushed_from_clear, false)
        }
        pushed_to_clear.setOnClickListener {
            pushed_to.text = ""
            showClearIcon(pushed_to_clear, false)
        }
    }

    private fun showDatePicker(dateView: TextView, clearButton: ImageButton) {
        DatePickerDialog(
            searchActivity,
            DatePickerDialog.OnDateSetListener { _, year, mm, dayOfMonth ->
                val month = mm + 1
                val monthOfYear = if(month > 9) "$month" else "0$month"
                val day = if (dayOfMonth > 9) "$dayOfMonth" else "0$dayOfMonth"
                dateView.text = "$year-$monthOfYear-$day"
                showClearIcon(clearButton,true)
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showClearIcon(clearView : ImageButton, show : Boolean) {
        if (show)
            clearView.visibility = View.VISIBLE
        else
            clearView.visibility = View.GONE
    }


}

data class FilterData(val language: String,
                      val createdFrom : String,
                      val createdTo : String,
                      val pushedFrom : String,
                      val pushedTo: String
)