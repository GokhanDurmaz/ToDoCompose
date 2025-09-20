#!/bin/bash

# ==============================================================================
# Android App Install and Launch Script
# Compatible with macOS, Linux, and Windows (via Git Bash/WSL).
# ==============================================================================

# --- Constants ---
PACKAGE_NAME="com.flowintent.workspace"
MAIN_ACTIVITY=".ui.activity.MainActivity"

# --- Auto-detect ANDROID_HOME ---
if [ -z "$ANDROID_HOME" ]; then
  echo "ANDROID_HOME is not set. Trying to auto-detect..."
  case "$(uname -s)" in
    Darwin) ANDROID_HOME="$HOME/Library/Android/sdk" ;;
    Linux) ANDROID_HOME="$HOME/Android/sdk" ;;
    MINGW*|CYGWIN*|MSYS*) ANDROID_HOME="$USERPROFILE/AppData/Local/Android/Sdk" ;;
    *)
      echo "Unsupported operating system. Please set ANDROID_HOME manually."
      exit 0 # do not fail build
      ;;
  esac
fi

# --- Validate ANDROID_HOME ---
if [ ! -d "$ANDROID_HOME" ]; then
  echo "Invalid ANDROID_HOME path: $ANDROID_HOME"
  echo "Skipping deployment."
  exit 0
fi

echo "ANDROID_HOME detected: $ANDROID_HOME"

# --- PATH Configuration ---
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

# --- Check Connected Device(s) ---
echo "Checking connected devices..."
DEVICES=$(adb devices | awk 'NR>1 && $2=="device" {print $1}')
DEVICE_COUNT=$(echo "$DEVICES" | grep -c .)

if [ "$DEVICE_COUNT" -eq 0 ]; then
  echo "No connected Android device or emulator found. Skipping deployment."
  exit 0
fi

# --- Select Preferred Device ---
if [ "$DEVICE_COUNT" -eq 1 ]; then
  SELECTED_DEVICE="$DEVICES"
else
  REAL_DEVICE=$(echo "$DEVICES" | grep -v "^emulator-" | head -n1)
  if [ -n "$REAL_DEVICE" ]; then
    SELECTED_DEVICE="$REAL_DEVICE"
    echo "Multiple devices detected. Prioritizing physical device: $SELECTED_DEVICE"
  else
    SELECTED_DEVICE=$(echo "$DEVICES" | head -n1)
    echo "Multiple emulators detected. Using emulator: $SELECTED_DEVICE"
  fi
fi

if [ -z "$SELECTED_DEVICE" ]; then
  echo "Failed to select a device. Skipping deployment."
  exit 0
fi

echo "Selected device: $SELECTED_DEVICE"

ADB="adb -s $SELECTED_DEVICE"

# --- Build Variant and APK File ---
VARIANT=${1:-debug}
echo "Build variant: $VARIANT"

APK_DIR="app/build/outputs/apk/$VARIANT"
APK_FILE=$(find "$APK_DIR" -name "*.apk" 2>/dev/null | sort -n -r | head -n1)

if [ ! -f "$APK_FILE" ]; then
  echo "APK file not found in: $APK_DIR"
  echo "Skipping deployment."
  exit 0
fi

# --- Uninstall Existing Application ---
echo "Checking if package is installed: $PACKAGE_NAME"
if $ADB shell pm list packages | grep -q "$PACKAGE_NAME"; then
  echo "Uninstalling existing package: $PACKAGE_NAME"
  $ADB uninstall "$PACKAGE_NAME"
else
  echo "Package $PACKAGE_NAME not found. Skipping uninstall."
fi

# --- Install APK ---
echo "Installing APK: $APK_FILE"
$ADB install -r -t "$APK_FILE"

finish() {
  # Check if the 'adb install' command was successful.
  # '$?' holds the exit code of the previous command. 0 means success.
	if [ $? -ne 0 ]; then
    echo "APK installation failed."
    exit 1
  fi
}

trap finish EXIT

# --- Launch Application ---
echo "Launching application..."
$ADB shell am start -n "$PACKAGE_NAME/$MAIN_ACTIVITY" || echo "Failed to launch the app, but installation succeeded."

echo "Deployment finished!"