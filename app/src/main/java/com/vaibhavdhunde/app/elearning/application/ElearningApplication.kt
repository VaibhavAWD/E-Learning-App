package com.vaibhavdhunde.app.elearning.application

import android.app.Application
import com.vaibhavdhunde.app.elearning.api.ElearningApi
import com.vaibhavdhunde.app.elearning.api.NetworkInterceptor
import com.vaibhavdhunde.app.elearning.data.*
import com.vaibhavdhunde.app.elearning.data.source.local.ElearningDatabase
import com.vaibhavdhunde.app.elearning.data.source.local.IUsersLocalDataSource
import com.vaibhavdhunde.app.elearning.data.source.remote.ISubjectsRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.source.remote.IUsersRemoteDataSource
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
        bind<ElearningRepository>() with singleton {
            DefaultElearningRepository(instance(), instance(), instance())
        }
        bind() from provider { ViewModelFactory(instance()) }
    }
}