package com.kiducar.kiducation.kiducar.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Size;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothHandler {

    private AppCompatActivity mActivity;

    // 블루투스 작업을 처리해주기 위한 어댑터
    private BluetoothAdapter mBluetoothAdapter;
    // 페어링된 블루투스 집합
    private Set<BluetoothDevice> mPairedDevices;
    // 페어링된 블루투스 장치 개수
    private int mPairedDevicesCount;
    // 연결 할 블루투스 장치
    private BluetoothDevice mRemoteDevice;
    // 연결 소켓
    private BluetoothSocket mSocket;
    // 소켓 입력 스트림
    InputStream mInputStream;
    // 소켓 출력 스트림
    OutputStream mOutputStream;

    public static final int REQUEST_ENABLE_BT = 3;

    public BluetoothHandler(AppCompatActivity ac) {
        mActivity = ac;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mPairedDevices = null;
        mPairedDevicesCount = 0;
        mRemoteDevice = null;
        mSocket = null;
        mInputStream = null;
        mOutputStream = null;
    }

    // 블루투스 권한
    public void enableBluetooth() {
        // 장치를 지원하지 않는 경우, Activity 종료
        if (mBluetoothAdapter == null) {
            mActivity.finish();
        }
        //지원하는 경우, 블루투스 권한 획득
        else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
            }
            else{
                selectDevice();
            }
        }
    }

    public void selectDevice() {
        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        mPairedDevicesCount = mPairedDevices.size();

        if (mPairedDevicesCount > 0) {
            //페어링된 장치가 있는 경우
            if (mPairedDevicesCount == 0) {
                mActivity.finish();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
            builder.setTitle("블루투스 장치 선택");

            //페어링된 블루투스 장치의 이름 목록을 작성
            List<String> listItems = new ArrayList<String>();
            for (BluetoothDevice device : mPairedDevices) {
                listItems.add(device.getName());
            }
            listItems.add("취소");

            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (item == mPairedDevicesCount) {
                        //연결할 장치를 선택하지 않고 취소를 누른경우
                        mActivity.finish();
                    } else {
                        //연결할 장치를 선택한 경우
                        //선택한 장치와 연결을 시도
                        connectToSelectDevice(items[item].toString());
                    }
                }
            });

            builder.setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            //페어링된 장치가 없는경우 어플리케이션 종료...
            Toast.makeText(mActivity.getApplicationContext(), "cannot find paired device", Toast.LENGTH_SHORT).show();
            mActivity.finish();
        }

    }

    // 선택한 장치를 가져옴
    BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;

        for (BluetoothDevice device : mPairedDevices) {
            if (name.equals(device.getName())) {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }

    // 장치에 데이터를 전송함.
    public void sendData(byte[] data) {
        try{
            // 데이터 크기를 앞에 붙여서 전송
            byte[] extendedData = new byte[data.length+4];
            int len = data.length;

            // int형 길이를 byte에 나눠 넣음(자바는 빅 엔디안 방식으로 저장해서 역순으로 저장)
            extendedData[3] = (byte)(len >> 24);
            extendedData[2] = (byte)(len >> 16);
            extendedData[1] = (byte)(len >> 8);
            extendedData[0] = (byte)(len);

            mOutputStream.write(extendedData);
        }
        catch(IOException ie){
            Toast.makeText(mActivity.getApplicationContext(), "send fail!!!", Toast.LENGTH_SHORT).show();
            mActivity.finish();
        }
    }

    // 페어링된 장치와 연결
    public void connectToSelectDevice(String selectedDeviceName){
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
        // 시리얼 통신 서비스 사용
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try{
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);

            mSocket.connect();

            // 클라이언트에서 전송만하므로 inputStream을 열지 않음.
            mOutputStream = mSocket.getOutputStream();
        }
        catch(IOException ie){
            Toast.makeText(mActivity.getApplicationContext(), "connect fail!!", Toast.LENGTH_SHORT).show();
            mActivity.finish();
        }
    }

    // 블루투스 제거
    public void destroy(){
        try{
            if(mInputStream != null)
                mInputStream.close();
            if(mOutputStream != null)
                mOutputStream.close();
            if(mSocket != null)
                mSocket.close();
        }
        catch(Exception e){}
    }
}
