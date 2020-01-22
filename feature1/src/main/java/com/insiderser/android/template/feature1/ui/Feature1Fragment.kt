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
package com.insiderser.android.template.feature1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.insiderser.android.template.common.ui.dagger.DaggerFragmentWithViewBinding
import com.insiderser.android.template.core.dagger.AppComponentProvider
import com.insiderser.android.template.feature1.dagger.DaggerFeature1Component
import com.insiderser.android.template.feature1.dagger.Feature1Component
import com.insiderser.android.template.feature1.databinding.Feature1FragmentBinding

/**
 * Sample [Fragment] that does nothing, except injecting into itself.
 */
class Feature1Fragment : DaggerFragmentWithViewBinding<Feature1FragmentBinding>() {

    override fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): Feature1FragmentBinding = Feature1FragmentBinding.inflate(inflater, container, false)

    override fun provideInjector(): Feature1Component {
        val appComponentProvider = requireActivity().application as AppComponentProvider
        val appComponent = appComponentProvider.appComponent
        return DaggerFeature1Component.factory().create(appComponent)
    }
}
