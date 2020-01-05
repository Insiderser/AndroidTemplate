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
package com.insiderser.android.template.dagger

import com.insiderser.android.core.dagger.ViewModelModule
import com.insiderser.android.template.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            MainModule::class,
            ViewModelModule::class,
            ActivityBindingModule::class
        ])
interface AppComponent : AndroidInjector<MyApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<MyApplication> {

        override fun create(@BindsInstance application: MyApplication): AppComponent
    }
}
