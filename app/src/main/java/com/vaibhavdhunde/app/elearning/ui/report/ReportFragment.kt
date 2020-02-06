package com.vaibhavdhunde.app.elearning.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentReportBinding
import com.vaibhavdhunde.app.elearning.util.*
import kotlinx.android.synthetic.main.fragment_report.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ReportFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var reportViewModel: ReportViewModel

    private lateinit var binding: FragmentReportBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report, container, false)

        reportViewModel = obtainViewModel(ReportViewModel::class.java, factory)

        binding = FragmentReportBinding.bind(view).apply {
            viewmodel = reportViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupInputField()
        setupEvents()
    }

    private fun setupInputField() {
        input_message.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendReport()
                true
            } else {
                false
            }
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            if (message is Int) {
                fragment_report.snackbar(message)
            } else if (message is String) {
                fragment_report.snackbar(message)
            }
        })

        binding.viewmodel?.closeSoftKeyboardEvent?.observe(viewLifecycleOwner, EventObserver {
            closeSoftKeyboard()
        })
    }

    private fun sendReport() {
        binding.viewmodel?.sendReport()
    }


}
