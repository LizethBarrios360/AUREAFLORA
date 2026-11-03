package com.compose.trykeeper.di

import com.compose.trykeeper.data.HomeRepoImpl
import com.compose.trykeeper.data.LoginRepoImpl
import com.compose.trykeeper.data.RegisterRepoImpl
import com.compose.trykeeper.db.AureaFloraDatabase
import com.compose.trykeeper.domain.HomeRepository
import com.compose.trykeeper.domain.LoginRepository
import com.compose.trykeeper.domain.RegisterRepository
import com.compose.trykeeper.presentacion.home.HomeViewModel
import com.compose.trykeeper.presentacion.login.LoginViewModel
import com.compose.trykeeper.presentacion.register.RegisterViewModel

import org.koin.dsl.module

fun appModule(aureaFloraDatabase: AureaFloraDatabase) = module {
    single<LoginRepository> { LoginRepoImpl(aureaFloraDatabase) }
    factory { LoginViewModel(get()) }
    single<RegisterRepository> { RegisterRepoImpl(aureaFloraDatabase) }
    factory { RegisterViewModel(get()) }
    single<HomeRepository> { HomeRepoImpl(aureaFloraDatabase) }
    factory { HomeViewModel(get()) }
}
