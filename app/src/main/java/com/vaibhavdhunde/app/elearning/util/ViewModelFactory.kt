package com.vaibhavdhunde.app.elearning.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.ui.blogdetails.BlogDetailsViewModel
import com.vaibhavdhunde.app.elearning.ui.blogs.BlogsViewModel
import com.vaibhavdhunde.app.elearning.ui.changepassword.ChangePasswordViewModel
import com.vaibhavdhunde.app.elearning.ui.feedback.FeedbackViewModel
import com.vaibhavdhunde.app.elearning.ui.login.LoginViewModel
import com.vaibhavdhunde.app.elearning.ui.main.MainViewModel
import com.vaibhavdhunde.app.elearning.ui.profile.ProfileViewModel
import com.vaibhavdhunde.app.elearning.ui.register.RegisterViewModel
import com.vaibhavdhunde.app.elearning.ui.report.ReportViewModel
import com.vaibhavdhunde.app.elearning.ui.splash.SplashViewModel
import com.vaibhavdhunde.app.elearning.ui.subjects.SubjectsViewModel
import com.vaibhavdhunde.app.elearning.ui.subtopic.SubtopicViewModel
import com.vaibhavdhunde.app.elearning.ui.subtopics.SubtopicsViewModel
import com.vaibhavdhunde.app.elearning.ui.topics.TopicsViewModel

class ViewModelFactory(private val repository: ElearningRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(SplashViewModel::class.java) ->
                    SplashViewModel(repository)
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)
                isAssignableFrom(RegisterViewModel::class.java) ->
                    RegisterViewModel(repository)
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)
                isAssignableFrom(ProfileViewModel::class.java) ->
                    ProfileViewModel(repository)
                isAssignableFrom(ChangePasswordViewModel::class.java) ->
                    ChangePasswordViewModel(repository)
                isAssignableFrom(SubjectsViewModel::class.java) ->
                    SubjectsViewModel(repository)
                isAssignableFrom(TopicsViewModel::class.java) ->
                    TopicsViewModel(repository)
                isAssignableFrom(SubtopicsViewModel::class.java) ->
                    SubtopicsViewModel(repository)
                isAssignableFrom(SubtopicViewModel::class.java) ->
                    SubtopicViewModel(repository)
                isAssignableFrom(FeedbackViewModel::class.java) ->
                    FeedbackViewModel(repository)
                isAssignableFrom(ReportViewModel::class.java) ->
                    ReportViewModel(repository)
                isAssignableFrom(BlogsViewModel::class.java) ->
                    BlogsViewModel(repository)
                isAssignableFrom(BlogDetailsViewModel::class.java) ->
                    BlogDetailsViewModel(repository)
                else -> throw IllegalArgumentException("Unknown model class: $modelClass")
            }
        } as T
    }
}