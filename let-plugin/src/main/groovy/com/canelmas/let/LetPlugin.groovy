/*
 * Copyright (C) 2018 Can Elmas
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

package com.canelmas.let

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.FeaturePlugin
import com.android.build.gradle.InstantAppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class LetPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        makeSureProjectIsCompatible(project)

        project.android.registerTransform(new LetTransform(project))

        project.dependencies {
            implementation 'org.aspectj:aspectjrt:1.8.9'
            implementation 'com.canelmas.let:let-runtime:1.0.0-beta2'
            implementation 'com.canelmas.let:let-annotations:1.0.0-beta2'
        }
    }

    private void makeSureProjectIsCompatible(Project project) {
        def hasAppPlugin = project.plugins.withType(AppPlugin)
        def hasLibPlugin = project.plugins.withType(LibraryPlugin)
        def hasFeaturePlugin = project.plugins.withType(FeaturePlugin)
        def hasInstantAppPlugin = project.plugins.withType(InstantAppPlugin)

        if (!hasAppPlugin && !hasLibPlugin && !hasFeaturePlugin && !hasInstantAppPlugin) {
            throw new IllegalStateException("Project must have one the following plugins applied " +
                    ": 'android', 'android-library', 'feature', 'instantapp'")
        }
    }

}