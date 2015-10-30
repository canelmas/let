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

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by can on 31/08/15.
 */
@Aspect
public final class LetAspect {

    private LetAspect() {
    }

    @Around("execution(@com.canelmas.let.AskPermission * *(..)) && this(source)")
    public Object adviceForAskPermissionAnnotatedMethods(final ProceedingJoinPoint joinPoint, Object source) throws Throwable {

        //  proceed as it is for devices running prior to M
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return joinPoint.proceed();
        }

        return new RuntimePermissionRequest(joinPoint, getActivity(source)).proceed();

    }

    /**
     *
     * This advice is needed for distinguishing the 'Never Ask Again' and 'Thank you, but not now!'
     * states.
     *
     * State is 'Never Ask Agan' only if permission requests after an initial one, keep getting
     * denied and shouldShowRequestPermissionRationale returns false.
     *
     *
     * @param joinPoint
     * @param tar
     * @param source
     * @throws Throwable
     */
    @Around("execution(* *.onRequestPermissionsResult(..)) && target(tar) && this(source)")
    public void adviceForOnRequestPermissionsResult(final ProceedingJoinPoint joinPoint, Object tar, Object source) throws Throwable {

        // TODO: 27/10/15 avoid unnecessary calls to this aspect

        //  make sure onRequestPermissionsResult() is executed
        joinPoint.proceed();

        String[] permissions = (String[]) joinPoint.getArgs()[1];
        int[] results = (int[]) joinPoint.getArgs()[2];

        final List<DeniedPermissionRequest> requestResults = new ArrayList<>();

        final Activity activity = getActivity(source);

        for (int k= 0; k <permissions.length; k++) {

            String permission = permissions[k];
            int result = results[k];

            boolean denied = result == PackageManager.PERMISSION_DENIED;

            if (denied) {

                boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                boolean neverAskAgain = !shouldShowRationale;

                requestResults.add(new DeniedPermissionRequest(permission, neverAskAgain));

                Logger.log("<<< " + permissions[k] + " denied" + (neverAskAgain ? " with Never Ask Again checked." : ""));

            }

        }

        RuntimePermissionListener listener = RuntimePermissionListener.class.isInstance(source) ? (RuntimePermissionListener) source : null;

        if (null != listener && !requestResults.isEmpty()) {
            listener.onPermissionDenied(requestResults);
        }


    }

    private Activity getActivity(Object source) {

        if (source instanceof Activity) {

            return (Activity) source;

        } else if (source instanceof Fragment) {

            return ((Fragment) source).getActivity();

        } else if (source instanceof android.app.Fragment) {

            return ((android.app.Fragment) source).getActivity();

        }

        throw new LetException("Source type not supported yet!");
    }



}
