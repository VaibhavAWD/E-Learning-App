package com.vaibhavdhunde.app.elearning.ui.blogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentBlogsBinding
import com.vaibhavdhunde.app.elearning.util.EventObserver
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import com.vaibhavdhunde.app.elearning.util.snackbar
import kotlinx.android.synthetic.main.fragment_blogs.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class BlogsFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var blogsViewModel: BlogsViewModel

    private lateinit var binding: FragmentBlogsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blogs, container, false)

        blogsViewModel = obtainViewModel(BlogsViewModel::class.java, factory)

        binding = FragmentBlogsBinding.bind(view).apply {
            viewmodel = blogsViewModel
            adapter = BlogsListAdapter(blogsViewModel)
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setupNavigation()
        loadBlogs()
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            fragment_blogs.snackbar(message)
        })
    }

    private fun setupNavigation() {
        binding.viewmodel?.blogEvent?.observe(viewLifecycleOwner, EventObserver { blogId ->
            navigateToBlogFragment(blogId)
        })
    }

    private fun loadBlogs() {
        binding.viewmodel?.loadBlogs()
    }

    private fun navigateToBlogFragment(blogId: Long) {
        val action = BlogsFragmentDirections.actionOpenBlogDetails(blogId)
        findNavController().navigate(action)
    }

}
