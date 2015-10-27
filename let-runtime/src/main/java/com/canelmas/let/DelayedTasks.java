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

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by can on 27/10/15.
 */
public final class DelayedTasks {

    private static Map<Integer, Task> tasks = Collections.synchronizedMap(new HashMap<Integer, Task>());

    static Task get(final int requestCode) {
        return tasks.get(requestCode);
    }

    static void remove(Task task) {
        tasks.remove(task.requestCode);
    }

    static void add(Task task) {
        tasks.put(task.requestCode, task);
    }

    static class Task implements Callable {

        final int requestCode;
        final List<String> permissionList;
        final ProceedingJoinPoint joinPoint;

        Task(List<String> permissionList, int requestCode, ProceedingJoinPoint joinPoint) {
            this.permissionList = permissionList;
            this.requestCode = requestCode;
            this.joinPoint = joinPoint;
        }

        public void execute() throws Exception {
            call();
        }

        @Override
        public Object call() throws Exception {

            try {
                return joinPoint.proceed();
            } catch (Throwable t) {
                throw new LetException("Future Task execution failed!", t);
            }
        }


        @Override
        public String toString() {
            return "Task{" +
                    "requestCode=" + requestCode +
                    ", permissionList=" + permissionList +
                    ", joinPoint=" + joinPoint +
                    '}';
        }
    }


}
