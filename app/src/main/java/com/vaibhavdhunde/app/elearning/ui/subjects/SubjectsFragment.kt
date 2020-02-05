package com.vaibhavdhunde.app.elearning.ui.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentSubjectsBinding
import com.vaibhavdhunde.app.elearning.util.EventObserver
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import com.vaibhavdhunde.app.elearning.util.snackbar
import kotlinx.android.synthetic.main.fragment_subjects.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SubjectsFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var subjectsViewModel: SubjectsViewModel

    private lateinit var binding: FragmentSubjectsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subjects, container, false)

        subjectsViewModel = obtainViewModel(SubjectsViewModel::class.java, factory)

        binding = FragmentSubjectsBinding.bind(view).apply {
            viewmodel = subjectsViewModel
            adapter = SubjectsListAdapter(subjectsViewModel)
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setupNavigation()
        loadSubjects()
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            fragment_subjects.snackbar(message)
        })
    }

    private fun setupNavigation() {
        binding.viewmodel?.topicsEvent?.observe(viewLifecycleOwner, EventObserver { subjectId ->
            navigateToTopicsFragment(subjectId)
        })
    }

    private fun loadSubjects() {
        binding.viewmodel?.loadSubjects()
    }

    private fun navigateToTopicsFragment(subjectId: Long) {
        val action = SubjectsFragmentDirections.actionOpenTopics(subjectId)
        findNavController().navigate(action)
    }

}
