package com.chinchillateam.chinchillatune.di

import com.chinchillateam.base.audio.AudioService
import com.chinchillateam.base.audio.DefaultAudioService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AudioModule {

    @Binds
    abstract fun bindAudioService(service: DefaultAudioService): AudioService
}