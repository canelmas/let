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

import android.os.Build;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by can on 31/08/15.
 */
@Aspect
public final class LetAspect {

    private LetAspect() {
    }

    /**
     * Advice that triggers the runtime permission request,
     * executed around {@link AskPermission} annotated methods.
     *
     * #ProceedingJoinPoint is proceeded if the runtime is below {@link android.os.Build.VERSION_CODES#M}
     *
     * @see {@link LetContext}
     * @see {@link AskPermission}
     *
     * @param joinPoint Annotated method
     * @param source Source of the annotated method i.e {@link android.app.Fragment} or
     * {@link android.app.Activity}
     * @return #joinPoint.proceed if annotated method should be executed; {@link RuntimePermissionRequest#proceed()}
     * otherwise
     * @throws Throwable
     */
    @Around("execution(@com.canelmas.let.AskPermission * *(..)) && this(source)")
    public Object annotatedMethods(final ProceedingJoinPoint joinPoint, Object source) throws Throwable {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return joinPoint.proceed();
        }

        return new RuntimePermissionRequest(joinPoint, source).proceed();

    }

}
