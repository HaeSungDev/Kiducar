package com.kiducar.kiducation.kiducar.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class BluetoothHandler {

    // 블루투스 권한, 디바이스 목록 등의 기능을 수행하는 어댑터
    private BluetoothAdapter m_bluetoothAdapter;
    // 블루투스 통신 소켓
    private BluetoothSocket m_bluetoothSock;

    boolean initBluetooth()
    {
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(m_bluetoothAdapter == null){
            return false;
        }
        if(m_bluetoothAdapter.isEnabled()){
            return true;
        }
        else{
            //Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(intent. REQUEST_ENABLE_BT);
        }

        return true;
    }
}
