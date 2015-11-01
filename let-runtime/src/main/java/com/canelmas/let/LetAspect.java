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
import android.os.Build;
import android.support.v4.app.ActivityCompat;

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

        return new RuntimePermissionRequest(joinPoint, source).proceed();

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

        //  make sure onRequestPermissionsResult() is executed
        joinPoint.proceed();

        String[] permissions = (String[]) joinPoint.getArgs()[1];
        int[] results = (int[]) joinPoint.getArgs()[2];

        final List<DeniedPermission> requestResults = new ArrayList<>();

        final LetContext letContext = new LetContext(source);

        for (int k= 0; k <permissions.length; k++) {

            String permission = permissions[k];
            int result = results[k];

            boolean denied = result == PackageManager.PERMISSION_DENIED;

            if (denied) {

                boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(letContext.getActivity(), permission);
                boolean neverAskAgain = !shouldShowRationale;

                requestResults.add(new DeniedPermission(permission, neverAskAgain));

                Logger.log("\t" + permissions[k] + " denied" + (neverAskAgain ? " with Never Ask Again checked." : ""));

            }

        }

        RuntimePermissionListener listener = RuntimePermissionListener.class.isInstance(source) ? (RuntimePermissionListener) source : null;

        if (null != listener && !requestResults.isEmpty()) {
            Logger.log("<<< should handle denied permissions");
            listener.onPermissionDenied(requestResults);
        }

    }

}
