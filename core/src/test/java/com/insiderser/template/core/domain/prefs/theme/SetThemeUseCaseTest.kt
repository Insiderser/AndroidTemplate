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

package com.insiderser.template.core.domain.prefs.theme

import com.google.common.truth.Truth.assertThat
import com.insiderser.template.core.fakes.FakeAppPreferencesStorage
import com.insiderser.template.core.domain.prefs.theme.SetThemeUseCase
import com.insiderser.template.core.domain.prefs.theme.Theme
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class SetThemeUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val preferencesStorage = FakeAppPreferencesStorage()

    private val useCase = SetThemeUseCase(
        preferencesStorage,
        testDispatcher
    )

    @Test
    fun givenTheme_useCase_updatesPreferencesStorage() = testDispatcher.runBlockingTest {
        Theme.values().forEach { theme ->
            useCase(theme)
            assertThat(preferencesStorage.selectedTheme).isEqualTo(theme.storageKey)
        }
    }
}
