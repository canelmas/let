Let
====

Annotation based simple API flavored with AOP to handle new Android Runtime Permission model

Simply Add`@AskPermission` to your methods that require permissions to be granted at runtime.
 
```java
@AskPermission(ACCESS_FINE_LOCATION)
private void getUserLocationAndDoSomething() {
    Toast.makeText(SampleActivity.this, "Now that I have the permission, I can get your location!", Toast.LENGTH_SHORT).show();
}
``` 

Let will execute the method's body if permissions are already granted or when the permissions requested
are granted by the user at runtime.
  
Let will tell whether the permissions asked are denied (with or without 'Never Ask Again' checked) or 
whether the rationales should be shown to the user before requesting them.

Ask for multiple permissions 

```java
@AskPermission({
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SEND_SMS
})
private void makePhoneCall() {

    Toast.makeText(SampleActivity.this, "Calling..", Toast.LENGTH_SHORT).show();

    final Intent intent = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:00123124234234"));
    startActivity(intent);
}
```

Override the `onRequestPermissionsResult` in your Activity or Fragment and let Let handle the rest.

```
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    Let.handle(requestCode, permissions, grantResults);
}
```

To get notified about denied permissions and rationales to be shown, make sure your Activity or Fragment 
implements `RuntimePermissionListener`

```
public class SampleActivity extends AppCompatActivity implements RuntimePermissionListener {
....
@Override
public void onShowPermissionRationale(List<String> permissions, final RuntimePermissionRequest request) {
    // show permission rationales in a dialog. 
    // Retry the permission request when user confirms by calling request.retry()
    ....
}   

@Override
public void onPermissionDenied(List<DeniedPermissionRequest> results) {
    // Update UI and prompt a dialog to tell user to go to the app settings page in order to
    // grant again the permission denied with 'Never Ask Again'
}
```

Add it to your project today!

```groovy
buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath 'com.canelmas.let:let-plugin:0.1.1'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'let'
```

Why?
====

If you check the Google's Samples about this new permission model, you'll see the second permission request is
triggered at the same time the rationale is shown, without waiting user i.e. simple toast message is
shown and then the permission request is retried.

Well, this is obviously not a perfect UX. So if you want to handle this rationale step by yourself,
pause for a while, wait for a user confirmation and try again with few lines of code, Let is what
you're looking for.

Another reason is that you might want to distinguish Not-Granted state from 'Never Ask Again' state if
you want to update your UI or want to dispatch user to the Application Settings page where only the permission
can be enabled again.

If you want to concentrate on more important things, let Let handle this flows with masking the whole 
boilerplate code.
  

License
--------

    Copyright 2015 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
