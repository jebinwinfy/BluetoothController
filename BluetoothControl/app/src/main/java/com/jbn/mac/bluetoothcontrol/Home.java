package com.jbn.mac.bluetoothcontrol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Matrix;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.lang.Thread.sleep;

public class Home extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "bluetooth1";

//    Button buttonConnect;

    Handler handler;
    MediaPlayer mp;

    LinearLayout linearLayoutForward;
    LinearLayout linearLayoutBackward;
    LinearLayout linearLayoutLeft;
    LinearLayout linearLayoutRight;
    LinearLayout linearLayoutConnect;
    LinearLayout linearLayoutHorn;

    ImageView navigationImage;
    ImageView imageViedwBluetooth;

    TextView textViewBluetoothStatus;
    TextView textViewIndicator;

    SeekBar seekBar;
    SeekBar seekBarIndicator;

    Switch headLight;
    Switch headLight2;
    Switch hazardLight;
    Switch fogLight;
    Switch interiorLight;
    Switch parkLight;
    Switch switchAuto;

    boolean isForwardPressed = false;
    boolean isBackwardPressed = false;
    boolean isLeftPressed = false;
    boolean isRightPressed = false;
    boolean isIndicatorOn = false;
    boolean isAutoModeOn = false;

    private OutputStream outStream = null;
    private ProgressDialog dialog;

    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    GradientDrawable gradientDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        buttonConnect = (Button) findViewById(R.id.buttonConnect);

        handler = new Handler();
        mp = MediaPlayer.create(this, R.raw.sound);


        linearLayoutBackward = (LinearLayout) findViewById(R.id.linearLayoutBackward);
        linearLayoutForward = (LinearLayout) findViewById(R.id.linearLayoutForward);
        linearLayoutLeft = (LinearLayout) findViewById(R.id.linearLayoutLeft);
        linearLayoutRight = (LinearLayout) findViewById(R.id.linearLayoutRight);
        linearLayoutConnect = (LinearLayout) findViewById(R.id.linearLayoutConnect);
        linearLayoutHorn = (LinearLayout) findViewById(R.id.linearLayoutHorn);

        linearLayoutForward.setOnTouchListener(this);
        linearLayoutBackward.setOnTouchListener(this);
        linearLayoutLeft.setOnTouchListener(this);
        linearLayoutRight.setOnTouchListener(this);
        linearLayoutConnect.setOnTouchListener(this);
        linearLayoutHorn.setOnTouchListener(this);

        headLight = (Switch) findViewById(R.id.switchHeadLight);
        headLight2 = (Switch) findViewById(R.id.switchHeadLight2);
        hazardLight = (Switch) findViewById(R.id.switchHazardLight);
        fogLight = (Switch) findViewById(R.id.switchFogLight);
        interiorLight = (Switch) findViewById(R.id.switchInteriorLight);
        parkLight = (Switch) findViewById(R.id.switchParkLight);
        switchAuto = (Switch) findViewById(R.id.switchAuto);

        seekBar=(SeekBar)findViewById(R.id.seekBar);
        seekBar.setMax(10);
        seekBar.setProgress(5);

        seekBarIndicator = (SeekBar) findViewById(R.id.seekBarIndicator);
        seekBarIndicator = (SeekBar) findViewById(R.id.seekBarIndicator);
        seekBarIndicator.setMax(2);
        seekBarIndicator.setProgress(1);

//        seekBarIndicator.getProgressDrawable().setColorFilter(Color.parseColor("#FF0000"), PorterDuff.Mode.MULTIPLY);
        seekBarIndicator.getThumb().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);
        Log.i("getThumb", seekBarIndicator.getThumb()+"");

        gradientDrawable = (GradientDrawable) seekBarIndicator.getThumb();
        gradientDrawable.setColor(Color.parseColor("#ff000000"));

//        Drawable thumb = getResources().getDrawable( R.drawable.custom_seek_bar_thumb2 );
//        seekBarIndicator.setThumb(thumb);

        textViewBluetoothStatus = (TextView) findViewById(R.id.textViewBluetoothStatus);
        textViewBluetoothStatus.setText("Connect Device");
        textViewBluetoothStatus.setTextColor(Color.parseColor("#cc0000"));

        textViewIndicator = (TextView) findViewById(R.id.textViewIndicator);

        navigationImage = (ImageView) findViewById(R.id.navigationImage);
        imageViedwBluetooth = (ImageView) findViewById(R.id.imageViedwBluetooth);

//        buttonConnect.setBackgroundColor(Color.parseColor("#ff4000"));
        navigationImage.setBackgroundResource(R.drawable.empty);

//        buttonConnect.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
//                if(btAdapter == null) {
//                    Toast.makeText(getBaseContext(), "Device have no Bluetooth", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if(!btAdapter.enable()) {
//
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBtIntent, 1);
//                    Toast.makeText(getBaseContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent myIntent = new Intent(Home.this, BluetoothList.class);
////                myIntent.putExtra("key", value); //Optional parameters
////                    startActivity(myIntent);
//                    startActivityForResult(myIntent, 1);
//                }
//            }
//        });


        headLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    sendDataToBluetoothDevice("W");//On
                } else {
                    sendDataToBluetoothDevice("w");//Off
                }
            }
        });

        headLight2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    sendDataToBluetoothDevice("U");//On
                } else {
                    sendDataToBluetoothDevice("u");//Off
                }
            }
        });

        hazardLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    sendDataToBluetoothDevice("X");//On
                } else {
                    sendDataToBluetoothDevice("x");//Off
                }
            }
        });

        fogLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    sendDataToBluetoothDevice("Z");//On
                } else {
                    sendDataToBluetoothDevice("z");//Off
                }
            }
        });

        interiorLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    sendDataToBluetoothDevice("A");//On
                } else {
                    sendDataToBluetoothDevice("a");//Off
                }
            }
        });

        parkLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    sendDataToBluetoothDevice("C");//On
                    Toast.makeText(getBaseContext(),"Park light on", Toast.LENGTH_SHORT).show();
                } else {
                    sendDataToBluetoothDevice("c");//Off
                    Toast.makeText(getBaseContext(),"Park light off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        switchAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getBaseContext(),"Auto Steering mode On", Toast.LENGTH_SHORT).show();
                    isAutoModeOn = true;
                    sendDataToBluetoothDevice("T");
                } else {
                    Toast.makeText(getBaseContext(),"Auto Steering mode Off", Toast.LENGTH_SHORT).show();
                    isAutoModeOn = false;
                    sendDataToBluetoothDevice("t");
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if(progressChangedValue == 10) {
                    sendDataToBluetoothDevice("q");
                    Toast.makeText(getBaseContext(),"MAX SPEED!!", Toast.LENGTH_SHORT).show();
                } else {
                    int val = progressChangedValue + 48;
                    sendDataToBluetoothDevice(Integer.toString(val));
                }
            }
        });

        seekBarIndicator.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                switch (progressChangedValue) {
                    case 0:
                        //Left indicator
                        handler.removeCallbacks(runnableCode);
                        handler.post(runnableCode);
                        sendDataToBluetoothDevice("M");
                        break;
                    case 1:
                        //Indicator off
                        handler.removeCallbacks(runnableCode);
                        gradientDrawable.setColor(Color.parseColor("#ff000000"));
                        sendDataToBluetoothDevice("p");
                        break;
                    case 2:
                        //Right indicator
                        handler.removeCallbacks(runnableCode);
                        handler.post(runnableCode);
                        sendDataToBluetoothDevice("N");
                        break;
                }
            }
        });

    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            if(isIndicatorOn) {
                isIndicatorOn = false;
//                textViewIndicator.setTextColor(Color.parseColor("#009d00"));
                gradientDrawable.setColor(Color.parseColor("#ffd88c00"));
            } else {
                isIndicatorOn = true;
//                textViewIndicator.setTextColor(Color.parseColor("#ffbf00"));
                gradientDrawable.setColor(Color.parseColor("#ffffe500"));
            }
            playSound();
            handler.postDelayed(this, 800);
        }
    };

    void playSound() {
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(this, R.raw.sound);
            } mp.start();
        } catch(Exception e) { e.printStackTrace(); }
    }

    void sendCommand() {

        if(isForwardPressed && isLeftPressed) {
            //Move forward left
            sendDataToBluetoothDevice("G");
            navigationImage.setBackgroundResource(R.drawable.fl);
            repeatCommand(true);
        } else if(isForwardPressed && isRightPressed) {
            //Move forward right
            sendDataToBluetoothDevice("I");
            navigationImage.setBackgroundResource(R.drawable.fr);
            repeatCommand(false);
        } else if(isBackwardPressed && isLeftPressed) {
            //Move backward left
            sendDataToBluetoothDevice("H");
            navigationImage.setBackgroundResource(R.drawable.bl);
            repeatCommand(true);
        } else if(isBackwardPressed && isRightPressed) {
            //Move backward right
            sendDataToBluetoothDevice("J");
            navigationImage.setBackgroundResource(R.drawable.br);
            repeatCommand(false);
        } else if(isForwardPressed && !isBackwardPressed) {
            //Move forward
            sendDataToBluetoothDevice("F");
            navigationImage.setBackgroundResource(R.drawable.f);
        } else if(isBackwardPressed && !isForwardPressed) {
            //Move Backward
            sendDataToBluetoothDevice("B");
            navigationImage.setBackgroundResource(R.drawable.b);
        } else if(isLeftPressed && !isRightPressed) {
            //Move left
            sendDataToBluetoothDevice("L");
            navigationImage.setBackgroundResource(R.drawable.l);
            repeatCommand(true);
        } else if(isRightPressed && !isLeftPressed) {
            //Move Right
            sendDataToBluetoothDevice("R");
            navigationImage.setBackgroundResource(R.drawable.r);
            repeatCommand(false);
//            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
////            rotation.setRepeatCount(Animation.);
//            navigationImage.startAnimation(rotation);
        } else {
            //Error
            sendDataToBluetoothDevice("S");
            navigationImage.setBackgroundResource(R.drawable.empty);


//            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
////            rotation.setRepeatCount(Animation.);
//            navigationImage.startAnimation(rotation);
        }
    }

    void sendDataToBluetoothDevice(String data) {
        if (btSocket!=null) {
            try {
                btSocket.getOutputStream().write(data.toString().getBytes());
            } catch (IOException e) {
                imageViedwBluetooth.setBackgroundResource(R.drawable.bluetooth_disconnected);
                textViewBluetoothStatus.setText("Connect Device");
                textViewBluetoothStatus.setTextColor(Color.parseColor("#cc0000"));
                Toast.makeText(getBaseContext(), "ERROR to transmit!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Repeating 'send command'
    void repeatCommand(final Boolean isLeft) {
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            private long time = 0;

            @Override
            public void run() {
                if(isLeft && isLeftPressed){
                    sendCommand();
                } else if(!isLeft && isRightPressed) {
                    sendCommand();
                }
            }
        }, 30); //(takes millis)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                HashMap<String, BluetoothDevice> hashMap = (HashMap<String, BluetoothDevice>)data.getSerializableExtra("Data");
                BluetoothDevice bt = hashMap.get("btObj");


                this.address = bt.getAddress();

                dialog = new ProgressDialog(this);
                dialog.setMessage("Connecting...Please wait!!!");
                dialog.show();

                try {
                    if (btSocket == null || !isBtConnected) {
                        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect();//start connection

//                        buttonConnect.setBackgroundColor(Color.parseColor("#61a51a"));
                        imageViedwBluetooth.setBackgroundResource(R.drawable.bluetooth_connected);
                        textViewBluetoothStatus.setText("Connected");
                        textViewBluetoothStatus.setTextColor(Color.parseColor("#00b300"));

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
                catch (IOException e) {
                    Toast.makeText(getBaseContext(),"Error To Communicate!!! Try again", Toast.LENGTH_SHORT).show();
//                    buttonConnect.setBackgroundColor(Color.parseColor("#ff4000"));
                    imageViedwBluetooth.setBackgroundResource(R.drawable.bluetooth_disconnected);
                    textViewBluetoothStatus.setText("Connect Device");
                    textViewBluetoothStatus.setTextColor(Color.parseColor("#cc0000"));
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.linearLayoutForward:
                if(event.getAction() == MotionEvent.ACTION_UP){
                    isForwardPressed = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isForwardPressed = true;
                }
                sendCommand();
                return true;
            case R.id.linearLayoutBackward:
                if(event.getAction() == MotionEvent.ACTION_UP){
                    isBackwardPressed = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isBackwardPressed = true;
                }
                sendCommand();
                return true;
            case R.id.linearLayoutLeft:
                if(event.getAction() == MotionEvent.ACTION_UP){
                    isLeftPressed = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isLeftPressed = true;
                }
                sendCommand();
                return true;
            case R.id.linearLayoutRight:
                if(event.getAction() == MotionEvent.ACTION_UP){
                    isRightPressed = false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isRightPressed = true;
                }
                sendCommand();
                return true;
            case R.id.linearLayoutHorn:
                if(event.getAction() == MotionEvent.ACTION_UP){
                    sendDataToBluetoothDevice("v");
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    sendDataToBluetoothDevice("V");
//                    Toast.makeText(getBaseContext(), "Horn turned on", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.linearLayoutConnect:
                if(event.getAction() == MotionEvent.ACTION_UP){

                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                    if(btAdapter == null) {
                        Toast.makeText(getBaseContext(), "Device have no Bluetooth", Toast.LENGTH_SHORT).show();
                    }
                    if(!btAdapter.enable()) {

                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                        Toast.makeText(getBaseContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent myIntent = new Intent(Home.this, BluetoothList.class);
                        startActivityForResult(myIntent, 1);
                    }
                }

                return true;

        }
        return false;
    }
}
