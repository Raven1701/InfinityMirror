/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.Raven1701.infinitymirror;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ScrollView;

import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raven1701.infinitymirror.R;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class InfinityMirrorControl extends AppCompatActivity implements recyclerViewAdapter.ItemClickListener  {
    private final static String TAG = InfinityMirrorControl.class.getSimpleName();
    int intColor = Color.WHITE;
    int secondintColor = Color.BLACK;
    int firstintColor = intColor;
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private int[] RGBFrame = {0, 0, 0};
    private TextView mConnectionState;
    private CheckBox mAutoscrollCheckBox;
    private ScrollView mDataScroll;
    private TextView mDataField;
    private String mDeviceName;
    ArrayList<Item> items;
    private String mDeviceAddress;
    public String dataColor = "000000000";
    public String dataColor2 = "000000000";
    public String dataBrightness = "50";
    public String dataDelay = "002";
    public String dataMode = "100";
    public recyclerViewAdapter myRecyclerViewAdapter;
    //  private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;


    private BluetoothGattCharacteristic characteristicTX;
    private BluetoothGattCharacteristic characteristicRX;


    public final static UUID HM_RX_TX =
            UUID.fromString(SampleGattAttributes.HM_RX_TX);

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = this.getSharedPreferences("com.Raven1701.infinitymirror", MODE_PRIVATE);
        preferences.edit().putString("adress", "empty").apply();
        preferences.edit().putString("name", "empty").apply();
        Log.i("SharedPreferenced", "name, adress reset.");
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                Log.d(TAG, "RETRY CONNECTION");
                mBluetoothLeService.connect(mDeviceAddress); //retry conenction
                //clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());


            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(mBluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void clearUI() {
        // mDataField.setText(R.string.no_data);
        mDataField.setText("");
    }
    private void createItems(){
        Boolean T = true;
        Boolean F = false;
        items.add(new Item(F,T,F,T, 100, "Full Color"));
        items.add(new Item(T,T,F,T, 101, "Theater Chase"));
        items.add(new Item(T,F,F,T, 102, "Rainbow Cycle"));
        items.add(new Item(T,F,F,T, 103, "Rainbow Theater Chase"));
        items.add(new Item(F,F,F,T, 104, "Rainbow"));
        items.add(new Item(T,T,F,T,105,"Color Blink"));
        items.add(new Item(F,F,F,F, 106, "Random Color"));
        items.add(new Item(T,T,T,T, 107, "Comet between Colors"));
        items.add(new Item(T,T,T,T, 108, "Double Comet between Colors"));
        items.add(new Item(T, T,T,T,109, "changing colors"));
        items.add(new Item(T,T,T,T, 110, "changing rarndom colors"));
        items.add(new Item(T,T,T,T, (int)((Math.random()*20)+1), "Random Mode"));

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        items = new ArrayList<>();
        createItems();
        myRecyclerViewAdapter = new recyclerViewAdapter(this, items);
        myRecyclerViewAdapter.setClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myRecyclerViewAdapter);



        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.

       // ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        // is serial present?
        mAutoscrollCheckBox = (CheckBox) findViewById(R.id.autoscroll_checkBox);
        mDataScroll = (ScrollView) findViewById(R.id.data_scroll);
        uptadeMode();

        mDataField = (TextView) findViewById(R.id.data_value);


        //ref: http://codetheory.in/android-ontouchevent-ontouchlistener-motionevent-to-detect-common-gestures/
        // "Capturing Touch Events on a View"

        /*mDataField.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // have same code as onTouchEvent() (for the Activity) above

                int action = event.getActionMasked();

                Log.d(TAG, "DF Touch" +String.valueOf(action));

                return true;
            }
        });*/



        if (getActionBar() != null) { //if actionbar is there
            getActionBar().setTitle(mDeviceName);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }

        if (savedInstanceState != null) { //restore destroyed data
            mDataField.setText(savedInstanceState.getString("mDataField"));
            mDataScroll.post(new Runnable() {
                @Override
                public void run() {
                    mDataScroll.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        final TextView brightness = findViewById(R.id.textShowBrightness);
        SeekBar seekBarBritghness = findViewById(R.id.seekBarBrightness);
        seekBarBritghness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int tempBrightness;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                brightness.setText(String.valueOf(progress));
                tempBrightness = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                String bright = String.valueOf(String.valueOf((int)((tempBrightness)*2.55)));
                Log.i("Brightness", dataBrightness);
                dataBrightness = prepareData(dataBrightness);
                sendDataToBLE();
            }
        });
        final TextView delay = findViewById(R.id.textShowDelay);
        SeekBar seekBarDelay = findViewById(R.id.seekBarTime);
        seekBarDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int tempDelay;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                delay.setText(String.valueOf(i)+"[ms]");
                tempDelay = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                dataDelay = prepareData(String.valueOf((int)(tempDelay/10)));
                sendDataToBLE();
                Log.i("Delay:", dataDelay);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothLeService != null) {
            unbindService(mServiceConnection);
            mBluetoothLeService = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        menu.findItem(R.id.show_logs).setVisible(true); //hide own menu

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ConstraintLayout logs = findViewById(R.id.logsLayout);
        switch (item.getItemId()){
            case R.id.show_logs:
                if(logs.getVisibility()==View.VISIBLE){
                    logs.setVisibility(View.GONE);
                    item.setTitle(R.string.show_logs);
                }
                else{
                    logs.setVisibility(View.VISIBLE);
                    item.setTitle(R.string.hide_logs);
                }
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
  public void colorPanelClick2(View view){
          ColorPickerDialogBuilder
                  .with(this)
                  .setTitle("Choose color")
                  .initialColor(intColor)
                  .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                  .density(12)
                  .lightnessSliderOnly()
                  .setOnColorSelectedListener(new OnColorSelectedListener() {
                      @Override
                      public void onColorSelected(int color) {
                          ImageButton button = findViewById(R.id.colorPanel2);
                          button.setBackgroundColor(color);
                          intColor = color;
                          dataColor2 = prepareData(String.valueOf(Color.red(color)))+prepareData(String.valueOf(Color.green(color)))+prepareData(String.valueOf(Color.blue(color)));
                          sendDataToBLE();
                      }
                  })
                  .setPositiveButton("accept", new ColorPickerClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int color, Integer[] allColors) {
                          ImageButton button = findViewById(R.id.colorPanel2);
                          button.setBackgroundColor(color);
                          intColor = color;
                          secondintColor = intColor;
                          dataColor2 = prepareData(String.valueOf(Color.red(color)))+prepareData(String.valueOf(Color.green(color)))+prepareData(String.valueOf(Color.blue(color)));
                          sendDataToBLE();
                      }
                  })
                  .setNegativeButton("back", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          int color = secondintColor;
                          ImageButton button = findViewById(R.id.colorPanel2);
                          button.setBackgroundColor(color);
                          dataColor2 = prepareData(String.valueOf(Color.red(color)))+prepareData(String.valueOf(Color.green(color)))+prepareData(String.valueOf(Color.blue(color)));
                          sendDataToBLE();
                      }
                  })
                  .build()
                  .show();
      }

    public void colorPanelClick(View view){
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose color")
                .initialColor(intColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .lightnessSliderOnly()
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        ImageButton button = findViewById(R.id.colorPanel);
                        button.setBackgroundColor(color);
                        intColor = color;
                        dataColor = prepareData(String.valueOf(Color.red(color)))+prepareData(String.valueOf(Color.green(color)))+prepareData(String.valueOf(Color.blue(color)));
                        sendDataToBLE();
                    }
                })
                .setPositiveButton("accept", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int color, Integer[] allColors) {
                        ImageButton button = findViewById(R.id.colorPanel);
                        button.setBackgroundColor(color);
                        intColor = color;
                        firstintColor = intColor;
                        dataColor = prepareData(String.valueOf(Color.red(color)))+prepareData(String.valueOf(Color.green(color)))+prepareData(String.valueOf(Color.blue(color)));
                        sendDataToBLE();
                    }
                })
                .setNegativeButton("back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int color = firstintColor;
                        ImageButton button = findViewById(R.id.colorPanel);
                        button.setBackgroundColor(color);
                        dataColor = prepareData(String.valueOf(Color.red(color)))+prepareData(String.valueOf(Color.green(color)))+prepareData(String.valueOf(Color.blue(color)));
                        sendDataToBLE();
                    }
                })
                .build()
                .show();
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(getResources().getString(resourceId));
            }
        });
    }

    private void displayData(String data) {

        if (data != null) {
            // mDataField.setText(data);
            mDataField.append(data);
            //mDataScroll.fullScroll(ScrollView.FOCUS_DOWN);

            if (mAutoscrollCheckBox.isChecked()) {
                mDataScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        mDataScroll.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        }
    }


    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();


        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));

            // If the service exists for HM 10 Serial, say so.
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            // get characteristic when UUID matches RX/TX UUID
            characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
            characteristicRX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
        }

    }
    public void clickButton(View v){
             sendDataToBLE("00000025510150050");

    }
    void sendDataToBLE(String str) {
        Log.d(TAG, "Sending result=" + str);
        final byte[] tx = str.getBytes();
        if (mConnected) {
            characteristicTX.setValue(tx);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
        }
    }
    public void sendDataToBLE(){
        String str = dataColor+dataMode+dataBrightness+dataDelay+dataColor2;
        Log.d(TAG, "Sending result=" + str);
        final byte[] tx = str.getBytes();
        if (mConnected) {
            characteristicTX.setValue(tx);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    // on change of bars write char
    private void makeChange() {
        String str = RGBFrame[0] + "," + RGBFrame[1] + "," + RGBFrame[2] + "\n";
        Log.d(TAG, "Sending result=" + str);
        final byte[] tx = str.getBytes();
        if (mConnected) {
            characteristicTX.setValue(tx);
            mBluetoothLeService.writeCharacteristic(characteristicTX);
            mBluetoothLeService.setCharacteristicNotification(characteristicRX, true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mDataField", mDataField.getText().toString());
    }


    public String prepareData(String data){
        switch(data.length()){
            case 3:
                return data;
            case 2:
                return "0"+data;
            case 1:
                return "00"+data;

            default:
                return "000";
        }
    }
   public void colorWipe(View v){
        dataMode = "100";
        sendDataToBLE();
    }
   public void theaterChase(View v){
        dataMode = "101";
        sendDataToBLE();
    }
   public void rainbow(View v){
        dataMode = "102";
        sendDataToBLE();
    }
   public void theaterChaseRainbow(View v){
        dataMode = "103";
        sendDataToBLE();
    }
   public void rainbowCycle(View v){
        dataMode = "104";
        sendDataToBLE();
    }
    public void mode105(View v){
        dataMode = "105";
        sendDataToBLE();
    }
    public void mdoe106(View v){
        dataMode = "106";
        sendDataToBLE();
    }
    public void mode107(View v){
        dataMode = "107";
        sendDataToBLE();
    }
    public void uptadeMode(String string){
        TextView textViewMode = findViewById(R.id.textShowMode);
        textViewMode.setText(string);
    }
    public void uptadeMode(){
       switch(Integer.valueOf(dataMode)){
           case 100:
               uptadeMode("Full Color");
               break;
           case 101:
               uptadeMode("Theater Chase");
               break;
           case 102:
               uptadeMode("Rainbow Cycler");
               break;
           case 103:
               uptadeMode("Theater Chase Rainbow");
               break;
           case 104:
               uptadeMode("Rainbow Color");
               break;
           case 105:

               break;
       }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("mode: ", String.valueOf(items.get(position).mode));
        dataMode = String.valueOf(items.get(position).mode);
        sendDataToBLE();
    }
}