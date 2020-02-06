package com.vaibhavdhunde.app.elearning.ui.blogdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.databinding.FragmentBlogDetailsBinding
import com.vaibhavdhunde.app.elearning.util.EventObserver
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import com.vaibhavdhunde.app.elearning.util.obtainViewModel
import com.vaibhavdhunde.app.elearning.util.snackbar
import kotlinx.android.synthetic.main.fragment_blog_details.*
import kotlinx.android.synthetic.main.fragment_subtopic.*
import kotlinx.android.synthetic.main.fragment_subtopic.btn_retry
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class BlogDetailsFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private val factory: ViewModelFactory by instance()

    private lateinit var blogDetailsViewModel: BlogDetailsViewModel

    private lateinit var binding: FragmentBlogDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blog_details, container, false)

        blogDetailsViewModel = obtainViewModel(BlogDetailsViewModel::class.java, factory)

        binding = FragmentBlogDetailsBinding.bind(view).apply {
            viewmodel = blogDetailsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRetry()
        setupEvents()
        loadBlog()
    }

    private fun setupRetry() {
        btn_retry.setOnClickListener {
            loadBlog()
        }
    }

    private fun setupEvents() {
        binding.viewmodel?.showMessageEvent?.observe(viewLifecycleOwner, EventObserver { message ->
            fragment_blog_details.snackbar(message)
        })
    }

    private fun loadBlog() {
        binding.viewmodel?.loadBlog(getBlogId())
    }

    private fun getBlogId(): Long {
        return arguments!!.let {
            BlogDetailsFragmentArgs.fromBundle(it).blogId
        }
    }

}
