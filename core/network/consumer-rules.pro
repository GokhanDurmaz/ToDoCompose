# Ktor uses ManagementFactory to detect the debugger on JVM.
# These classes do not exist on Android, so we tell R8 to ignore the missing references.
-dontwarn java.lang.management.**
-dontwarn javax.management.**