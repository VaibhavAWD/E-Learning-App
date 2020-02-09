package com.vaibhavdhunde.app.elearning.application

import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.util.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class TestElearningApplication : ElearningApplication(), KodeinAware {
    override val kodein = Kodein.lazy {
        bind<FakeElearningRepository>() with singleton { FakeElearningRepository() }
        bind() from provider { ViewModelFactory(instance()) }
    }
}