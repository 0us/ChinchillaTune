package com.example.chinchillatuner.di

import com.example.base.audio.audio.AudioService
import com.example.base.audio.audio.DefaultAudioService
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