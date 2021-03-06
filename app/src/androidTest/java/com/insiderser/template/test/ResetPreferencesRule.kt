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

package com.insiderser.template.test

import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.insiderser.template.core.data.prefs.AppPreferencesStorage
import com.insiderser.template.core.data.prefs.AppPreferencesStorageImpl
import kotlinx.coroutines.Dispatchers
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A simple test rule that resets app preferences to default.
 * For example, we might want not to show boarding screen.
 */
class ResetPreferencesRule : TestWatcher() {

    val storage: AppPreferencesStorage by lazy {
        AppPreferencesStorageImpl(
            getApplicationContext(),
            Dispatchers.IO
        )
    }

    override fun starting(description: Description?) {
        resetPreferences()
    }

    fun resetPreferences() = with(storage) {
        selectedTheme = null
    }
}
