package com.example.chatzapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import static com.example.chatzapp.R.id.textView2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends AppCompatActivity {
    Button buttonOn, buttonOff,  visible;
    TextView textView2;
    private Context context=this;

    BluetoothAdapter myBluetoothAdapter;
    Intent btEnablingIntent;
    int requestCodeForEnable;
    Button button;
    ListView listView;
    public static final int MESSAGE_STATE_CHANGED=0;
    public static final int MESSAGE_READ=1;
    public static final int MESSAGE_WRITE=2;
    public static final int MESSAGE_DEVICE_NAME=3;
    public static final int MESSAGE_TOAST=4;

    public static final String DEVICE_NAME="deviceName";
    public static final String TOAST="toast";
    private String connectedDevice;

    private void setState(CharSequence subTitle){
        getSupportActionBar().setSubtitle(subTitle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        textView2=findViewById(R.id.textView2);
        Intent myintent=getIntent() ;
        String userNameText=myintent.getStringExtra("EXTRA_NAME");
        textView2.setText("Welcome "+userNameText+"!");


        buttonOn = findViewById(R.id.btOn);
        buttonOff = findViewById(R.id.btOff);

        button = findViewById(R.id.button3);
        listView = findViewById(R.id.listView);
        visible = findViewById(R.id.button5);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        requestCodeForEnable = 1;
        visible.setOnClickListener(view -> {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                startActivity(intent);
            }

        });
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        bluetoothOnMethod();



        buttonOff.setOnClickListener(view -> bluetoothOffMethod());
        exeButton();
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o=listView.getItemAtPosition(i);
                String value= o.toString();
                Intent in=new Intent(MainActivity.this,Chat.class);
                in.putExtra(DEVICE_NAME,value);
                startActivity(in);
            }
        });}
        private void exeButton() {
            button.setOnClickListener(view -> {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
                    String[] devices=new String[bt.size()];
                    int index=0;
                    if(bt.size()>0){
                        for(BluetoothDevice device:bt){
                            devices[index]= device.getName();
                            index++;
                        }

                        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,devices);
                        listView.setAdapter(arrayAdapter);
                    }

                }




            });
        }


        private void bluetoothOffMethod() {
            if (myBluetoothAdapter.isEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    myBluetoothAdapter.disable();
                }

            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == requestCodeForEnable) {
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Bluetooth is enable!", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Bluetooth enabling cancelled!", Toast.LENGTH_SHORT).show();
                }
            }

        }

        private void bluetoothOnMethod() {
            buttonOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(myBluetoothAdapter==null){
                        Toast.makeText(MainActivity.this, "Bluetooth not supported!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(!myBluetoothAdapter.isEnabled()){
                            //write some code for enabling the bluetooth
                            //for that we need two objects-intent,request code
                            startActivityForResult(btEnablingIntent,requestCodeForEnable);
                        }
                    }
                }
            });
        }



    }


