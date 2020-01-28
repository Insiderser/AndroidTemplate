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
package com.insiderser.android.template.settings.ui.theme

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.insiderser.android.template.model.Theme
import com.insiderser.android.template.prefs.data.dagger.PreferencesStorageComponentProvider
import com.insiderser.android.template.settings.ui.R
import com.insiderser.android.template.settings.ui.dagger.DaggerSettingsComponent
import javax.inject.Inject

/**
 * Dialog fragment for selecting app theme preference. Should only be used from
 * [SettingsFragment][com.insiderser.android.template.settings.ui.SettingsFragment].
 */
// Don't use viewLifecycleOwner here since DialogFragment doesn't create any layouts.
@SuppressLint("FragmentLiveDataObserve")
internal class ThemeSettingDialogFragment : AppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ThemeSettingViewModel by viewModels { viewModelFactory }

    private val adapter: ArrayAdapter<ThemeHolder> by lazy {
        ArrayAdapter<ThemeHolder>(
            requireContext(),
            android.R.layout.simple_list_item_single_choice
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.settings_theme_dialog_title)
            .setSingleChoiceItems(adapter, -1) { dialog, which ->
                adapter.getItem(which)?.theme?.let { theme ->
                    viewModel.setSelectedTheme(theme)
                }
                dialog.dismiss()
            }
            .create()

    // Make sure we start observing everything after the dialog was created.
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.availableThemes.observe(this, Observer(::setAvailableThemes))
        viewModel.selectedTheme.observe(this, Observer(::setSelectedTheme))
    }

    private fun setAvailableThemes(themes: List<Theme>) {
        adapter.clear()
        adapter.addAll(
            themes.map { ThemeHolder(it, findTitleForTheme(it)) }
        )
        setSelectedTheme(viewModel.selectedTheme.value)
    }

    private fun setSelectedTheme(theme: Theme?) {
        val selectedIndex = (0 until adapter.count).indexOfFirst { index ->
            adapter.getItem(index)?.theme == theme
        }
        (dialog as AlertDialog).listView.setItemChecked(selectedIndex, true)
    }

    private fun findTitleForTheme(theme: Theme) = getString(
        when (theme) {
            Theme.LIGHT -> R.string.settings_theme_light
            Theme.DARK -> R.string.settings_theme_dark
            Theme.FOLLOW_SYSTEM -> R.string.settings_theme_follow_system
            Theme.AUTO_BATTERY -> R.string.settings_theme_auto_battery
        }
    )

    override fun onAttach(context: Context) {
        injectItself()
        super.onAttach(context)
    }

    private fun injectItself() {
        val preferencesProvider =
            requireActivity().application as PreferencesStorageComponentProvider
        val preferencesDataComponent = preferencesProvider.preferencesStorageComponent
        val settingsComponent = DaggerSettingsComponent.factory().create(preferencesDataComponent)
        settingsComponent.inject(this)
    }

    /**
     * Show this dialog with [ThemeSettingDialogFragment]'s [TAG].
     * @see AppCompatDialogFragment.show
     */
    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    companion object {

        @JvmField
        val TAG = ThemeSettingDialogFragment::class.java.name
    }
}

private data class ThemeHolder(val theme: Theme, val title: String) {
    override fun toString(): String = title
}
