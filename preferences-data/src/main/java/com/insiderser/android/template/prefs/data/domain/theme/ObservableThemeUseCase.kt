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
package com.insiderser.android.template.prefs.data.domain.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.insiderser.android.template.core.domain.ObservableUseCase
import com.insiderser.android.template.core.result.Result
import com.insiderser.android.template.model.Theme
import com.insiderser.android.template.prefs.data.AppPreferencesStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

/**
 * Use case for getting [LiveData] of user-selected theme settings from app preferences.
 * The [Result] is always [Result.Success].
 */
class ObservableThemeUseCase @Inject constructor(
    private val preferencesStorage: AppPreferencesStorage
) : ObservableUseCase<Unit, Theme>() {

    private val executed = AtomicBoolean(false)

    override suspend fun execute(param: Unit) {
        if (executed.getAndSet(true)) {
            // We're already observing changes in app preferences. No need to do anything again.
            return
        }

        val themeLiveData = withContext(Dispatchers.IO) {
            preferencesStorage.selectedThemeObservable.map { storageKey: String? ->
                storageKey?.let { Theme.fromStorageKey(storageKey) }
                    ?: DEFAULT_THEME
            }.asLiveData()
        }

        withContext(Dispatchers.Main) {
            result.addSource(themeLiveData) { themeResult ->
                result.value = themeResult
            }
        }
    }
}