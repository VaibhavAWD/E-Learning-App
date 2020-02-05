package com.vaibhavdhunde.app.elearning.ui.subtopic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentSubtopicBinding
import com.vaibhavdhunde.app.elearning.util.EventObserver
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import com.vaibhavdhunde.app.elearning.util.snackbar
import kotlinx.android.synthetic.main.fragment_subtopic.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SubtopicFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var subtopicViewModel: SubtopicViewModel

    private lateinit var binding: FragmentSubtopicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subtopic, container, false)

        subtopicViewModel = obtainViewModel(SubtopicViewModel::class.java, factory)

        binding = FragmentSubtopicBinding.bind(view).apply {
            viewmodel = subtopicViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        loadSubtopic()
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            fragment_subtopic.snackbar(message)
        })
    }

    private fun loadSubtopic() {
        binding.viewmodel?.loadSubtopic(getSubtopicId())
    }

    private fun getSubtopicId(): Long {
        return arguments!!.let {
            SubtopicFragmentArgs.fromBundle(it).subtopicId
        }
    }

}
