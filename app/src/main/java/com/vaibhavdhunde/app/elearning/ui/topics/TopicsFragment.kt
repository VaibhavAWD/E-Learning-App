package com.vaibhavdhunde.app.elearning.ui.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentTopicsBinding
import com.vaibhavdhunde.app.elearning.util.EventObserver
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import com.vaibhavdhunde.app.elearning.util.snackbar
import kotlinx.android.synthetic.main.fragment_topics.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class TopicsFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var topicsViewModel: TopicsViewModel

    private lateinit var binding: FragmentTopicsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_topics, container, false)

        topicsViewModel = obtainViewModel(TopicsViewModel::class.java, factory)

        binding = FragmentTopicsBinding.bind(view).apply {
            viewmodel = topicsViewModel
            adapter = TopicsListAdapter(topicsViewModel)
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRefresh()
        setupEvents()
        setupNavigation()
        loadTopics()
    }

    private fun setupRefresh() {
        refresh.setOnRefreshListener {
            refresh.isRefreshing = false
            loadTopics()
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            fragment_topics.snackbar(message)
        })
    }

    private fun setupNavigation() {
        binding.viewmodel?.subtopicsEvent?.observe(viewLifecycleOwner, EventObserver { topicId ->
            navigateToSubtopicsFragment(topicId)
        })
    }

    private fun loadTopics() {
        binding.viewmodel?.loadTopics(getSubjectId())
    }

    private fun navigateToSubtopicsFragment(topicId: Long) {
        val action = TopicsFragmentDirections.actionOpenSubtopics(topicId)
        findNavController().navigate(action)
    }

    private fun getSubjectId(): Long {
        return arguments!!.let {
            TopicsFragmentArgs.fromBundle(it).subjectId
        }
    }

}
