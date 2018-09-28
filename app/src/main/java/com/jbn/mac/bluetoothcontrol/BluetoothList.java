package com.jbn.mac.bluetoothcontrol;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BluetoothList extends AppCompatActivity {

    private BluetoothAdapter btAdapter = null;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Set<BluetoothDevice> pairedDevices;

    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_list);

        backButton = (Button) findViewById(R.id.backButton);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevices = btAdapter.getBondedDevices();

        final List<BluetoothDevice> listOfNames = new ArrayList<BluetoothDevice>();
        for(BluetoothDevice bt : pairedDevices)
            listOfNames.add(bt);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(pairedDevices, new Callback() {
            @Override
            public void invoke(int index) {
                Log.i("name",String.valueOf(index));

                Intent returnIntent = new Intent();
                HashMap<String,BluetoothDevice> hashMap = new HashMap<>();
                hashMap.put("btObj",listOfNames.get(index));
                returnIntent.putExtra("Data",hashMap);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

                onBackPressed();
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
