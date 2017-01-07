// INileDisConnectObserver.aidl
package ateek;

// Declare any non-default types here with import statements

interface INileDisConnectObserver {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      void onDisconnected(int returnCode);
}
