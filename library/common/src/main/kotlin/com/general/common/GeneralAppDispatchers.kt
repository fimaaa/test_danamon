package com.general.common

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class GeneralDefaultDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class GeneralIODispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class GeneralMainDispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class GeneralMainImmediateDispatcher