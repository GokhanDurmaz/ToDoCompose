#!/bin/bash

# ==============================================================================
# Android App Install and Launch Script
# Made compatible with macOS, Linux, and Windows (via Git Bash/WSL).
# ==============================================================================

# Define constants at the top for easy management.
PACKAGE_NAME="com.flowintent.workspace"
MAIN_ACTIVITY=".ui.activity.MainActivity"

# --- Auto-detect ANDROID_HOME ---
# 1. If ANDROID_HOME is already set in the environment, use it.
# 2. If not, try to find the default location based on the OS.

if [ -z "$ANDROID_HOME" ]; then
  echo "ANDROID_HOME environment variable not set. Attempting to auto-detect..."
  case "$(uname -s)" in
    Darwin) # macOS
      ANDROID_HOME="$HOME/Library/Android/sdk"
      ;;
    Linux) # Linux
      ANDROID_HOME="$HOME/Android/sdk"
      ;;
    MINGW*|CYGWIN*|MSYS*) # Windows (Git Bash, Cygwin, etc.)
      # On Windows, it's usually in the AppData folder.
      ANDROID_HOME="$USERPROFILE/AppData/Local/Android/Sdk"
      ;;
    *)
      echo "Unsupported operating system. Please set the ANDROID_HOME variable manually."
      exit 1
      ;;
  esac
fi

# Validate that the detected or manually set ANDROID_HOME path is a valid directory.
if [ ! -d "$ANDROID_HOME" ]; then
  echo "ANDROID_HOME path is not valid: $ANDROID_HOME"
  echo "Please ensure the Android SDK is installed correctly or set the ANDROID_HOME variable manually."
  exit 1
fi

echo "ANDROID_HOME set to: $ANDROID_HOME"

# --- PATH Configuration ---
# Add SDK tools to the PATH to ensure commands like 'adb' are available.
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools:$PATH"

# --- Build Variant and APK File ---
# Default to 'debug' if no argument is provided. (e.g., ./install.sh release)
VARIANT=${1:-debug}
echo "Using build variant: $VARIANT"

# Find the APK file. 'ls -t' lists the most recently modified file first.
APK_DIR="app/build/outputs/apk/$VARIANT"
APK_FILE=$(find "$APK_DIR" -name "*.apk" 2>/dev/null | sort -n -r | head -n1 | sed 's/^[^ ]* //')

# Always quote variables to handle potential spaces in file paths.
if [ ! -f "$APK_FILE" ]; then
  echo "APK file not found! Looked in directory: $APK_DIR"
  echo "Please make sure the project has been built successfully."
  exit 1
fi

# --- Uninstall, Install, and Launch ---
echo "Uninstalling existing application: $PACKAGE_NAME"
# The '-k' flag attempts to uninstall while keeping data, which is not ideal for resolving debug-release signature conflicts.
# We omit the '-k' flag to ensure a clean uninstallation.
adb uninstall "$PACKAGE_NAME"

# --- Installation and Launch ---
echo "Installing APK: $APK_FILE"
adb install -r -t "$APK_FILE"

finish() {
  # Check if the 'adb install' command was successful.
  # '$?' holds the exit code of the previous command. 0 means success.
	if [ $? -ne 0 ]; then
    echo "APK installation failed."
    exit 1
  fi
}

trap finish EXIT

echo "Launching the application..."
adb shell am start -n "$PACKAGE_NAME/$MAIN_ACTIVITY" || echo "Failed to launch the app, but the installation was successful."

echo "Operation completed!"