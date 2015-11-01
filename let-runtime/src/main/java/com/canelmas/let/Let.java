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

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by can on 31/08/15.
 */
public final class Let {

    @TargetApi(Build.VERSION_CODES.M)
    public static void handle(int requestCode, String[] permissions, int[] grantResults) {

        final DelayedTasks.Task delayedTask = DelayedTasks.get(requestCode);

        if (null != delayedTask) {

            for (int k = 0; k < permissions.length; k++) {
                
                if (grantResults[k] == PackageManager.PERMISSION_DENIED) {

                    DelayedTasks.remove(delayedTask);

                    return;
                }

            }

            try {
                Logger.log("<<< Required permissions granted");
                delayedTask.execute();

            } catch (Exception e) {
                throw new LetException("Delayed Execution Failed!", e);
            }

        }

    }

}
