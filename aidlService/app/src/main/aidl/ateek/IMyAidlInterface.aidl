// IMyAidlInterface.aidl
package ateek;

import ateek.INileConnectObserver;
import ateek.INileDisConnectObserver;
import ateek.IMyAidlCallback;

// Declare any non-default types here with import statements

 interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
       void connectMiraCast(String name,INileConnectObserver callback );
//       void disConnectMiraCast(INileDisConnectObserver callback);

//       void connectMiraCast(String name);
       void disConnectMiraCast();
       void registerCallback(IMyAidlCallback callback);
       void unregisterCallback(IMyAidlCallback callback);



}
