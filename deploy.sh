#!/bin/bash
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH

echo "Current dir: $(pwd)"
echo "PATH=$PATH"

PACKAGE_NAME="com.flowintent.workspace"
MAIN_ACTIVITY=".ui.activity.MainActivity"

VARIANT=${1:-debug}
APK_FILE=$(ls -t app/build/outputs/apk/$VARIANT/*.apk | head -n1)


if [ ! -f "$APK_FILE" ]; then
  echo "Failed to obtain APK file!"
  exit 1
fi

echo "Installing APK file: $APK_FILE"
if ! adb install -r "$APK_FILE"; then
  echo "adb install failed"
fi

echo "Starting application..."
adb shell am start -n "$PACKAGE_NAME/$MAIN_ACTIVITY" || echo "failed to start app"

echo "Completed!"
