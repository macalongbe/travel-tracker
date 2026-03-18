# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keepclassmembers class * {
    @javax.inject.* <fields>;
    @javax.inject.* <methods>;
}