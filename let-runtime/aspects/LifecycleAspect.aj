import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

public aspect LifecycleAspect {

    // Pointcuts for Activity and Fragment onSomething() methods
    pointcut activityMethods() : execution(* Activity+.on*(..)) ;
    pointcut fragmentMethods() : execution(* Fragment+.on*(..)) ;

    // Advice that gets executed "around" the Pointcuts
    Object around() : (activityMethods() || fragmentMethods()) {
        String method = thisJoinPoint.getSignature().toShortString();
        Log.d("Lifecycle", "Enter " + method);
        Object result = proceed();
        Log.d("Lifecycle", "Exit  " + method);
        return result;
    }
}