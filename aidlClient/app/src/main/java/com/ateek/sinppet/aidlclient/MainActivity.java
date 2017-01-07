package com.ateek.sinppet.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ateek.IMyAidlCallback;
import ateek.IMyAidlInterface;
import ateek.INileConnectObserver;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private IMyAidlInterface mAidl;
    ServiceCallBack mServiceCallBack;
    ConnectCallback mConnectCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bindService();
        mServiceCallBack = new ServiceCallBack();
        mConnectCallback = new ConnectCallback();


        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mConnectCallback != null) {
                        mAidl.connectMiraCast("ahmed", mConnectCallback);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        findViewById(R.id.btn_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mAidl.disConnectMiraCast();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    public void bindService() {
        bindService(createExplicitFromImplicitIntent(this, new Intent("com.ateek.snippet.aidlservice")), mServiceConn, Context.BIND_AUTO_CREATE);
        Log.d(getClass().getSimpleName(), "bindService: " +
                ((bindService(createExplicitFromImplicitIntent(this, new Intent("com.ateek.snippet.aidlservice")), mServiceConn, Context.BIND_AUTO_CREATE)) ? "true" : "false"));
    }

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mAidl = IMyAidlInterface.Stub.asInterface(service);
            try {
                mAidl.registerCallback(mServiceCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mAidl = null;
        }
    };

    @Nullable
    private static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    private class ConnectCallback extends INileConnectObserver.Stub {
        @Override
        public void onConnected(String deviceName, int returnCode) throws RemoteException {
            Log.d(TAG, "onConnected: ");
            Log.d(TAG, "onConnected: ReturnCond " + returnCode);
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }


    /**
     * AIDL interface callbacks
     */
    private class ServiceCallBack extends IMyAidlCallback.Stub {

        @Override
        public void onConnect() throws RemoteException {

            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "connect", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onConnect: ");

                }
            });

        }

        @Override
        public void onDiconnect() throws RemoteException {
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Disconnect", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onDiconnect: ");
                }
            });
        }
    }

}
