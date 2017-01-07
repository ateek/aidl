// INileConnectObserver.aidl
package ateek;

// Declare any non-default types here with import statements

interface INileConnectObserver {
       void onConnected(String deviceName, int returnCode);
}
