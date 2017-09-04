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

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public final class Let {

    /**
     * Check permission results and execute the {@link com.canelmas.let.AskPermission} annotated,
     * runtime permission required method afterwards if each permission required is granted.
     *
     * @param source
     * @param requestCode The request code received in {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}
     *                    or {@link android.app.Fragment#onRequestPermissionsResult(int, String[], int[])}
     * @param permissions Permission list received in {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}
     *                    or {@link android.app.Fragment#onRequestPermissionsResult(int, String[], int[])}
     * @param grantResults Result list received in {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])}
     *                     or {@link android.app.Fragment#onRequestPermissionsResult(int, String[], int[])}
     */
    public static void handle(Object source, int requestCode, String[] permissions, int[] grantResults) {

        final DelayedTasks.Task delayedTask = DelayedTasks.get(requestCode);

        if (null != delayedTask) {

            final List<DeniedPermission> deniedPermissions = new ArrayList<>();

            final LetContext letContext = new LetContext(source);

            for (int k = 0; k < permissions.length; k++) {

                boolean denied = grantResults[k]== PackageManager.PERMISSION_DENIED;

                if (denied) {

                    boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(letContext.getActivity(), permissions[k]);
                    boolean neverAskAgain = !shouldShowRationale;

                    deniedPermissions.add(new DeniedPermission(permissions[k], neverAskAgain));

                }

            }

            if (deniedPermissions.isEmpty()) {

                try {
                    delayedTask.execute();
                } catch (Exception e) {
                    throw new LetException("Delayed Execution Failed!", e);
                }

            } else {

                RuntimePermissionListener listener = RuntimePermissionListener.class.isInstance(source) ? (RuntimePermissionListener) source : null;

                if (null != listener) {
                    listener.onPermissionDenied(deniedPermissions);
                }

            }

        }

    }

}
