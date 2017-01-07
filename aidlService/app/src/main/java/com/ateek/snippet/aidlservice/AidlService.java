package com.ateek.snippet.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import ateek.IMyAidlCallback;
import ateek.IMyAidlInterface;
import ateek.INileConnectObserver;
import rx.Observable;
import rx.Subscriber;


/**
 * Created by ahmed-ateek on 21/12/2016.
 */

public class AidlService extends Service {
    public static final String TAG = AidlService.class.getSimpleName();

    private final RemoteCallbackList<IMyAidlCallback> mCallbackList = new RemoteCallbackList<>();
    private final RemoteCallbackList<INileConnectObserver> mConnectCallbackList = new RemoteCallbackList<>();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mInterface;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    IMyAidlInterface.Stub mInterface = new IMyAidlInterface.Stub() {
        @Override
        public void connectMiraCast(final String name,INileConnectObserver connectObserver) throws RemoteException {
            Log.d(TAG, "connectMiraCast: " + name);
            Log.d(TAG, "connectMiraCast: " + connectObserver);
            mConnectCallbackList.register(connectObserver);
            Observable.timer(5, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.d(TAG, "onError: " + throwable.getLocalizedMessage());
                }

                @Override
                public void onNext(Long aLong) {
                    onConnectCallBack(name, 1);
                }
            });

        }

        @Override
        public void disConnectMiraCast() throws RemoteException {
            Log.d(TAG, "disConnectMiraCast: ");

            Observable.timer(5, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable throwable) {
                    Log.d(TAG, "onError: " + throwable.getLocalizedMessage());
                }

                @Override
                public void onNext(Long aLong) {
                    onDisConnectCallBack();
                }
            });
        }

        @Override
        public void registerCallback(IMyAidlCallback callback) throws RemoteException {
            if (callback != null) {
                mCallbackList.register(callback);
            }
        }

        @Override
        public void unregisterCallback(IMyAidlCallback callback) throws RemoteException {
            if (callback != null) {
                mCallbackList.unregister(callback);
            }
        }
    };

    public void onConnectCallBack(String name, int result) {
        int N = mConnectCallbackList.beginBroadcast();
        try {
            for (int i = 0; i < N; i++) {
                mConnectCallbackList.getBroadcastItem(i).onConnected(name, result);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mConnectCallbackList.finishBroadcast();
    }

    public void onDisConnectCallBack() {
        int N = mCallbackList.beginBroadcast();
        try {
            for (int i = 0; i < N; i++) {
                mCallbackList.getBroadcastItem(i).onDiconnect();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mCallbackList.finishBroadcast();
    }

}
