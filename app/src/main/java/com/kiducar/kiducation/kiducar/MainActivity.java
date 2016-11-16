package com.kiducar.kiducation.kiducar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kiducar.kiducation.kiducar.blockcoding.BluetoothHandler;

import static com.kiducar.kiducation.kiducar.blockcoding.BluetoothHandler.REQUEST_ENABLE_BT;

public class MainActivity extends AppCompatActivity {

    BluetoothHandler btHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btHandler = new BluetoothHandler(this);
        btHandler.enableBluetooth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT :
                if(resultCode == RESULT_OK){
                    //블루투스가 활성 상태로 변경
                    btHandler.selectDevice();
                }
                else if(resultCode == RESULT_CANCELED){
                    //블루투스가 비활성 상태일 경우
                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        btHandler.destroy();
        super.onDestroy();
    }
}
