Let
====

Annotation based simple API flavored with AOP to handle new Android Runtime Permission model


Simply Add`@AskPermission` to your methods where you'll need the permission to be granted at runtime 
in order to execute method's body. Let will handle the whole flow for you and inform when the 
permissions asked are denied (with or without 'Never Ask Again') or whether the rationales should be 
shown to the user.

```java
@AskPermission(ACCESS_FINE_LOCATION)
private void getUserLocationAndDoSomething() {
    Toast.makeText(SampleActivity.this, "Now that I have the permission, I can get your location!", Toast.LENGTH_SHORT).show();
}
```

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
