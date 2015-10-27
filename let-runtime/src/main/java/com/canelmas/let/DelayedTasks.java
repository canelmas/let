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
