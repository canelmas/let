# add following lines to your proguard config

-keep class com.canelmas.let.** { *; }
-keepnames class * implements com.canelmas.let.RuntimePermissionListener

-keepclassmembers class * implements com.canelmas.let.RuntimePermissionListener {
    public void onRequestPermissionsResult(***);
}

-keepclasseswithmembernames class * {
    @com.canelmas.let.* <methods>;
}