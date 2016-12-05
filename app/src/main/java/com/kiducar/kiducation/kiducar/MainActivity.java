package com.kiducar.kiducation.kiducar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kiducar.kiducation.kiducar.blockinterface.ExecuteModule;
import com.kiducar.kiducation.kiducar.bluetooth.BluetoothHandler;

import static com.kiducar.kiducation.kiducar.bluetooth.BluetoothHandler.REQUEST_ENABLE_BT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothHandler btHandler = new BluetoothHandler(this);
        ExecuteModule.getInstance().setBluetoothHandler(btHandler);
        btHandler.enableBluetooth();
    }

    @Override
    // 블루투스 권한 확인 결과를 받아옴
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT :
                if(resultCode == RESULT_OK){
                    //블루투스가 활성 상태로 변경
                    ExecuteModule.getInstance().getBluetoothHandler().selectDevice();
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
    // 액티비티가 종료될때 블루투스 제거
    protected void onDestroy() {
        if(ExecuteModule.getInstance().getBluetoothHandler() != null)
            ExecuteModule.getInstance().getBluetoothHandler().destroy();
        super.onDestroy();
    }

    public void onClickSend(View v){
        ExecuteModule.getInstance().setIsFirstPageOpen(false);
        Intent blockCodingIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
        blockCodingIntent.putExtra("page", ExecuteModule.getInstance().getMainPageBlock());
        startActivity(blockCodingIntent);
    }

    public void onClickExit(View v){
        ExecuteModule.getInstance().exitCar();
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}