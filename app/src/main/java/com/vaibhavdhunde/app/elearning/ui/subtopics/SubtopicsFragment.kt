package com.vaibhavdhunde.app.elearning.ui.subtopics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentSubtopicsBinding
import com.vaibhavdhunde.app.elearning.util.EventObserver
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import com.vaibhavdhunde.app.elearning.util.snackbar
import kotlinx.android.synthetic.main.fragment_subtopics.*
import kotlinx.android.synthetic.main.fragment_topics.refresh
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SubtopicsFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var subtopicsViewModel: SubtopicsViewModel

    private lateinit var binding: FragmentSubtopicsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subtopics, container, false)

        subtopicsViewModel = obtainViewModel(SubtopicsViewModel::class.java, factory)

        binding = FragmentSubtopicsBinding.bind(view).apply {
            viewmodel = subtopicsViewModel
            adapter = SubtopicsListAdapter(subtopicsViewModel)
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRefresh()
        setupEvents()
        setupNavigation()
        loadSubtopics()
    }

    private fun setupRefresh() {
        refresh.setOnRefreshListener {
            refresh.isRefreshing = false
            loadSubtopics()
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            fragment_subtopics.snackbar(message)
        })
    }

    private fun setupNavigation() {
        binding.viewmodel?.subtopicEvent?.observe(viewLifecycleOwner, EventObserver { topicId ->
            navigateToSubtopicFragment(topicId)
        })
    }

    private fun loadSubtopics() {
        binding.viewmodel?.loadSubtopics(getTopicId())
    }

    private fun navigateToSubtopicFragment(subtopicId: Long) {
        val action = SubtopicsFragmentDirections.actionOpenSubtopic(subtopicId)
        findNavController().navigate(action)
    }

    private fun getTopicId(): Long {
        return arguments!!.let {
            SubtopicsFragmentArgs.fromBundle(it).topicId
        }
    }

}
