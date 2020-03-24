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

package com.insiderser.android.template.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.ref.WeakReference

@RunWith(AndroidJUnit4::class)
class ViewScopedDelegateTest {

    private lateinit var ref: WeakReference<Any>

    private lateinit var fragment: FakeFragmentWithViewBinding

    @Before
    fun setUp() {
        val view = View(mockk(relaxed = true))

        ref = WeakReference(view)
        fragment = FakeFragmentWithViewBinding(view)
    }

    @Test
    fun whenViewIsNotCreated_getValue_throwsIllegalStateException() {
        assertThrows(IllegalStateException::class.java) {
            fragment.targetProperty
        }
    }

    @Test
    @Suppress("UNUSED_VARIABLE")
    fun whenViewIsCreated_getValue_returnsValue() {
        val scenario = launchFragment { fragment }

        assertThat(fragment.targetProperty).isSameInstanceAs(ref.get())
        assertThat(ref.get()).isNotNull()
    }

    @Test
    fun whenViewIsDestroyed_value_isGCed_and_getValue_throwsIllegalStateException() {
        val scenario = launchFragment { fragment }

        scenario.moveToState(DESTROYED)
        assertThrows(IllegalStateException::class.java) {
            fragment.targetProperty
        }

        System.gc()
        // Check view is garbage collected
        assertThat(ref.get()).isNull()
    }

    class FakeFragmentWithViewBinding(mockView: View) : Fragment() {

        private var mockView: View? = mockView

        var targetProperty: View by viewLifecycleScoped()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            targetProperty = mockView!!
            mockView = null
            return targetProperty
        }
    }
}