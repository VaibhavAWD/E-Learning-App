package com.vaibhavdhunde.app.elearning.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentFeedbackBinding
import com.vaibhavdhunde.app.elearning.util.*
import kotlinx.android.synthetic.main.fragment_feedback.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class FeedbackFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var feedbackViewModel: FeedbackViewModel

    private lateinit var binding: FragmentFeedbackBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)

        feedbackViewModel = obtainViewModel(FeedbackViewModel::class.java, factory)

        binding = FragmentFeedbackBinding.bind(view).apply {
            viewmodel = feedbackViewModel
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
                sendFeedback()
                true
            } else {
                false
            }
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            if (message is Int) {
                fragment_feedback.snackbar(message)
            } else if (message is String) {
                fragment_feedback.snackbar(message)
            }
        })

        binding.viewmodel?.closeSoftKeyboardEvent?.observe(viewLifecycleOwner, EventObserver {
            closeSoftKeyboard()
        })
    }

    private fun sendFeedback() {
        binding.viewmodel?.sendFeedback()
    }


}
