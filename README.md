[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Let-green.svg?style=true)](https://android-arsenal.com/details/1/2843) [![](https://img.shields.io/badge/AndroidWeekly-%23182-red.svg)](http://androidweekly.net/issues/issue-182)

Let
====

Annotation based simple API flavoured with AOP to handle new Android runtime permission model.

If you check [Google's Samples] (https://github.com/googlesamples/android-RuntimePermissions/blob/master/Application/src/main/java/com/example/android/system/runtimepermissions/MainActivity.java) 
about the new permission model, you'll see a lot of boiler plate code for requesting, handling
and retrying the request for required permissions.

Let will minimize the boiler plate code you have to write for requesting and handling permissions and hence 
help you keep your code more readable.  
  
Let let Handle
====

Annotate your methods requiring permissions with `@AskPermission` and let Let handle the rest.
 
```java
@AskPermission(ACCESS_FINE_LOCATION)
private void getUserLocationAndDoSomething() {
    Toast.makeText(
        SampleActivity.this, 
        "Now that I have the permission I need, I'll get your location and do something with it", 
        Toast.LENGTH_SHORT
    ).show();
    ...
}
```

```java
@AskPermission({
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA
})
private void skipTutorial() {
    // permissions needed for best the app experience are granted; let's go to the app's home screen
    startActivity(new Intent(this, HomeActivity.class));
}
```

Let will check these annotated methods and execute them unless the permissions required are granted;
otherwise Let will put on hold the method execution and request these permissions at runtime. After examining 
the permission request result, Let will execute the method already put on hold only if the permissions are granted by user.
  
Let will also inform about the rationales before making any permission request
and tell about denied permissions; whether they're simply denied or with 'Never Ask Again' checked.   
 
Just make sure to override the `onRequestPermissionsResult` in your Activity or Fragment, where your
`@AskPermission` annotated methods are located:

```java
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    Let.handle(this, requestCode, permissions, grantResults);
}
```

And make sure your Activity or Fragment implements `RuntimePermissionListener` in order to get notified 
about denied permissions and rationales:

```java
public class SampleActivity extends AppCompatActivity implements RuntimePermissionListener {
    
    // ....
    
    @Override
    public void onShowPermissionRationale(List<String> permissions, final RuntimePermissionRequest request) {
        /**
        * show permission rationales in a dialog, wait for user confirmation and retry the permission 
        * request by calling request.retry()    
        */               
    }
  
    @Override
    public void onPermissionDenied(List<DeniedPermission> deniedPermissionList) {
        /**
        * Do whatever you need to do about denied permissions:
        *   - update UI
        *   - if permission is denied with 'Never Ask Again', prompt a dialog to tell user
        *   to go to the app settings screen in order to grant again the permission denied 
        */              
    }
    
    //  ...
}
```

Usage
====

Add it to your project today!

```groovy

buildscript {
    repositories {                    
        jcenter()        
    }

    dependencies {        
        classpath 'com.canelmas.let:let-plugin:0.1.9'
    }
}


apply plugin: 'com.android.application'
apply plugin: 'let'

repositories {        
    jcenter()
}
```

Proguard
====

Make sure your proguard rule set includes following lines: 

    -keep class com.canelmas.let.** { *; }
    -keepnames class * implements com.canelmas.let.RuntimePermissionListener

    -keepclassmembers class * implements com.canelmas.let.RuntimePermissionListener {
        public void onRequestPermissionsResult(***);
    }

    -keepclasseswithmembernames class * {
        @com.canelmas.let.* <methods>;
    }

License
====

    Copyright 2015 Can Elmas

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
