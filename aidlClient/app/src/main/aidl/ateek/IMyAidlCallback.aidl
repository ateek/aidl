// IMyAidlCallback.aidl
package ateek;

// Declare any non-default types here with import statements

interface IMyAidlCallback {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onConnect();
    void onDiconnect();
}
