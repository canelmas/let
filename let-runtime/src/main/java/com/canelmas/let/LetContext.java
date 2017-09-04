/*
 * Copyright (C) 2015 Can Elmas
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

package com.canelmas.let;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;

public final class LetContext {

    private final Object source;

    public LetContext(final Object source) {
        this.source = source;
    }

    @SuppressLint("NewApi")
    void requestPermissions(String[] permissions, int requestCode) {

        if (source instanceof Activity) {

            ActivityCompat.requestPermissions((Activity) source, permissions, requestCode);

        } else if (source instanceof android.support.v4.app.Fragment) {

            ((android.support.v4.app.Fragment) source).requestPermissions(permissions, requestCode);

        } else if (source instanceof Fragment) {

            ((Fragment) source).requestPermissions(permissions, requestCode);

        } else throw new LetException("Source type not supported yet!");

    }

    Activity getActivity() {

        if (source instanceof Activity) {

            return (Activity) source;

        } else if (source instanceof android.support.v4.app.Fragment) {

            return ((android.support.v4.app.Fragment) source).getActivity();

        } else if (source instanceof Fragment) {

            return ((Fragment) source).getActivity();

        } else throw new LetException("Source type not supported yet!");

    }
}
