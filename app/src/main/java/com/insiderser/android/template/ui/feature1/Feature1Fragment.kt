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

package com.insiderser.android.template.ui.feature1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.insiderser.android.template.core.ui.NavigationHost
import com.insiderser.android.template.core.ui.viewLifecycleScoped
import com.insiderser.android.template.databinding.Feature1FragmentBinding
import dev.chrisbanes.insetter.Insetter

/**
 * Sample [Fragment].
 */
class Feature1Fragment : Fragment() {

    private var binding: Feature1FragmentBinding by viewLifecycleScoped()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        Feature1FragmentBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as NavigationHost).registerToolbarWithNavigation(binding.toolbar)

        Insetter.builder().setOnApplyInsetsListener { statusBar, insets, _ ->
            statusBar.updateLayoutParams<ViewGroup.LayoutParams> {
                height = insets.systemWindowInsetTop
            }
        }.applyToView(binding.statusBar)
    }
}
