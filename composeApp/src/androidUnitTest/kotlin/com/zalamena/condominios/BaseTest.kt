package com.zalamena.condominios

import androidx.test.platform.app.InstrumentationRegistry
import com.zalamena.condominios.app.database.AppDatabase
import com.zalamena.condominios.moradores.app.di.testModule
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository
import org.junit.After
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

abstract class BaseTest: KoinTest {
    protected val moradoresRepository: MoradoresRepository by inject()
    protected val database: AppDatabase by inject()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        startKoin {
            androidContext(context)
            modules(testModule)
        }
    }

    @After
    open fun tearDown() {
        database.close()
        stopKoin()
    }
}