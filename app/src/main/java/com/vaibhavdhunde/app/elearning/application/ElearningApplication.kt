package com.vaibhavdhunde.app.elearning.application

import android.app.Application
import com.vaibhavdhunde.app.elearning.api.ElearningApi
import com.vaibhavdhunde.app.elearning.api.NetworkInterceptor
import com.vaibhavdhunde.app.elearning.data.*
import com.vaibhavdhunde.app.elearning.data.source.local.ElearningDatabase
import com.vaibhavdhunde.app.elearning.data.source.local.IUsersLocalDataSource
import com.vaibhavdhunde.app.elearning.data.source.remote.*
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ElearningApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@ElearningApplication))
        bind() from singleton { NetworkInterceptor(instance()) }
        bind() from singleton { ElearningApi(instance()) }
        bind() from singleton { ElearningDatabase.getInstance(instance()) }
        bind() from singleton { instance<ElearningDatabase>().usersDao() }
        bind<UsersLocalDataSource>() with singleton { IUsersLocalDataSource(instance()) }
        bind<UsersRemoteDataSource>() with singleton { IUsersRemoteDataSource(instance()) }
        bind<SubjectsRemoteDataSource>() with singleton { ISubjectsRemoteDataSource(instance()) }
        bind<TopicsRemoteDataSource>() with singleton { ITopicsRemoteDataSource(instance()) }
        bind<SubtopicsRemoteDataSource>() with singleton { ISubtopicsRemoteDataSource(instance()) }
        bind<FeedbacksRemoteDataSource>() with singleton { IFeedbacksRemoteDataSource(instance()) }
        bind<ReportsRemoteDataSource>() with singleton { IReportsRemoteDataSource(instance()) }
        bind<ElearningRepository>() with singleton {
            DefaultElearningRepository(
                instance(), instance(), instance(), instance(), instance(), instance(), instance()
            )
        }
        bind() from provider { ViewModelFactory(instance()) }
    }
}