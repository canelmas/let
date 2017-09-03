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
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class RuntimePermissionRequest {

    private static final AtomicInteger PERMISSIONS_REQUEST_CODE = new AtomicInteger();

    final Object source;
    final ProceedingJoinPoint joinPoint;

    RuntimePermissionRequest(ProceedingJoinPoint joinPoint, Object source) {
        this.source = source;
        this.joinPoint = joinPoint;
    }

    public void retry() {
        proceed(true);
    }

    Object proceed() {
        return proceed(false);
    }

    private Object proceed(final boolean retry) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method method = signature.getMethod();

        final String[] permissionList = method.getAnnotation(AskPermission.class).value();

        //  Permissions to ask and to show rationales
        final List<String> permissionsToAsk = new ArrayList<>();
        final List<String> permissionsToExplain = new ArrayList<>();

        final LetContext letContext = new LetContext(source);

        for (String permission : permissionList) {

            if (!isPermissionValid(permission)) {
                throw new LetException("Permission name not valid!");
            } else {

                final int permissionResult = ContextCompat.checkSelfPermission(letContext.getActivity(), permission);

                if (permissionResult != PackageManager.PERMISSION_GRANTED) {

                    final boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(letContext.getActivity(), permission);

                    if (showRationale && !retry) {
                        permissionsToExplain.add(permission);
                    } else {
                        permissionsToAsk.add(permission);
                    }

                }

            }

        }

        /**
         *  -Show rationales if necessary and return, before making any permission request
         *  -Trigger necessary permission request and return
         *  -Permissions needed already granted, no need to make request; proceed with the actual method
         */
        RuntimePermissionListener listener = RuntimePermissionListener.class.isInstance(source) ?
                (RuntimePermissionListener) source : null;

        if (!permissionsToExplain.isEmpty()) {

            if (null != listener) {
                listener.onShowPermissionRationale(permissionsToExplain, new RuntimePermissionRequest(joinPoint, source));
            } else {
                throw new LetException(source + " or its parent should implement RuntimePermissionListener");
            }

            return null;

        } else if (!permissionsToAsk.isEmpty()) {

            final int requestCode = PERMISSIONS_REQUEST_CODE.getAndIncrement() & 0xff;

            DelayedTasks.add(new DelayedTasks.Task(permissionsToAsk, requestCode, joinPoint));

            letContext.requestPermissions(permissionsToAsk.toArray(new String[]{}), requestCode);

            return null;

        } else {

            try {
                return joinPoint.proceed();
            } catch (Throwable t) {
                throw new LetException("Proceeding with the annotated method failed!", t);
            }

        }

    }

    private boolean isPermissionValid(final String permissionName) {
        // TODO
        return !TextUtils.isEmpty(permissionName);
    }

}
