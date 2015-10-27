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

                    /**
                     * TODO: 23/10/15
                     * come up with a way to distinguish 'Never Ask Again' state here,
                     * without second advice and  without iterating permissions again
                     *
                     * only reason for second advice is that we can't access context here
                     * and the listener
                     */

                    DelayedTasks.remove(delayedTask);

                    return;
                }

            }

            // required permissions are granted so proceed
            try {
                Logger.log("<<< Required permissions granted");
                delayedTask.execute();

            } catch (Exception e) {
                throw new LetException("Delayed Execution Failed!", e);
            }

        }

    }

}
