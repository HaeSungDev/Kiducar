package com.kiducar.kiducation.kiducar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.kiducar.kiducation.kiducar.blockinterface.ExecuteModule;
import com.kiducar.kiducation.kiducar.bluetooth.BluetoothHandler;

import static com.kiducar.kiducation.kiducar.bluetooth.BluetoothHandler.REQUEST_ENABLE_BT;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_LOADING = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loading = new Intent(this, Loading.class);
        startActivityForResult(loading, REQUEST_LOADING);
    }

    @Override
    // 블루투스 권한 확인 결과를 받아옴
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    //블루투스가 활성 상태로 변경
                    ExecuteModule.getInstance().getBluetoothHandler().selectDevice();
                } else if (resultCode == RESULT_CANCELED) {
                    //블루투스가 비활성 상태일 경우
                    finish();
                }
                break;

            case REQUEST_LOADING:
                BluetoothHandler btHandler = new BluetoothHandler(this);
                ExecuteModule.getInstance().setBluetoothHandler(btHandler);
                btHandler.enableBluetooth();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    // 액티비티가 종료될때 블루투스 제거
    protected void onDestroy() {
        if (ExecuteModule.getInstance().getBluetoothHandler() != null)
            ExecuteModule.getInstance().getBluetoothHandler().destroy();
        super.onDestroy();
    }

    public void onClickBlockCoding(View v) {

        if(ExecuteModule.getInstance().getMainPageBlock() != null) {
            // 반복블록 대화상자 생성
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("기존 블록 코드를 지우시겠습니까?");
            builder.setCancelable(false);
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ExecuteModule.getInstance().setIsFirstPageOpen(false);
                    Intent blockCodingIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
                    startActivity(blockCodingIntent);
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ExecuteModule.getInstance().setIsFirstPageOpen(false);
                    Intent blockCodingIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
                    blockCodingIntent.putExtra("page", ExecuteModule.getInstance().getMainPageBlock());
                    startActivity(blockCodingIntent);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            ExecuteModule.getInstance().setIsFirstPageOpen(false);
            Intent blockCodingIntent = new Intent(getApplicationContext(), BlockCodingActivity.class);
            startActivity(blockCodingIntent);
        }
    }

    public void onClickTurnOff(View v) {
        ExecuteModule.getInstance().exitCar();
        finish();
    }

    public void onClickHelp(View v) {
        // 도움말 레이아웃 뷰 생성
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_question, null);

        // 도움말 대화상자 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}