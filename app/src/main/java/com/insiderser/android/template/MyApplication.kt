/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.insiderser.android.template

import android.os.StrictMode
import com.insiderser.android.template.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

/**
 * This is an entry point of the whole application.
 *
 * This class executes basic app configuration, such as building a
 * Dagger graph, initializing libraries that need to be initialized on startup, etc.
 */
class MyApplication : DaggerApplication() {

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

    /**
     * Returns app-level [Dagger component][dagger.Component], that is used
     * throughout the app.
     */
    override fun applicationInjector(): AndroidInjector<MyApplication> {
        return DaggerAppComponent.factory().create(this)
    }
}
