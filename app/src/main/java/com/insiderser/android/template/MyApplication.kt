/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.insiderser.android.template

import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.insiderser.android.template.core.dagger.AppComponent
import com.insiderser.android.template.core.dagger.AppComponentProvider
import com.insiderser.android.template.core.util.toAppCompatNightMode
import com.insiderser.android.template.dagger.AppComponentImpl
import com.insiderser.android.template.dagger.DaggerAppComponentImpl
import com.insiderser.android.template.model.Theme
import com.insiderser.android.template.prefs.data.dagger.PreferencesStorageComponent
import com.insiderser.android.template.prefs.data.dagger.PreferencesStorageComponentProvider
import com.insiderser.android.template.prefs.data.domain.theme.ObserveThemeUseCase
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

/**
 * This is an entry point of the whole application.
 *
 * This class executes basic app configuration, such as building a
 * Dagger graph, initializing libraries that need to be initialized on startup, etc.
 */
class MyApplication : DaggerApplication(), AppComponentProvider,
    PreferencesStorageComponentProvider {

    private val appComponentImpl: AppComponentImpl by lazy {
        DaggerAppComponentImpl.factory().create(this)
    }

    @Inject
    internal lateinit var observeThemeUseCase: ObserveThemeUseCase

    override fun onCreate() {
        // Enable strict mode before Dagger builds graph
        if (BuildConfig.DEBUG) {
            enableStrictMode()
        }

        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // TODO: plant Crashlytics tree
        }

        observeThemeUseCase().observeForever(::updateAppTheme)
    }

    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }

    private fun updateAppTheme(selectedTheme: Theme) {
        Timber.d("Setting theme $selectedTheme")
        AppCompatDelegate.setDefaultNightMode(selectedTheme.toAppCompatNightMode())
    }

    /**
     * Returns app-level [Dagger component][dagger.Component], that is used
     * throughout the app.
     */
    override fun applicationInjector() = appComponentImpl

    override val appComponent: AppComponent get() = appComponentImpl
    override val preferencesStorageComponent: PreferencesStorageComponent get() = appComponentImpl
}
