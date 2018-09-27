package com.jbn.mac.bluetoothcontrol;

import android.bluetooth.BluetoothDevice;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Callback callback;
    private List<String> listOfNames;
    List<BluetoothDevice> listOfDevices;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView address;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            address = view.findViewById(R.id.address);
        }
    }

    public MyAdapter(Set<BluetoothDevice> listOfNames, Callback callback) {
        listOfDevices = new ArrayList<BluetoothDevice>();
        List<String> lists = new ArrayList<String>();
        for(BluetoothDevice bt : listOfNames) {
            if(bt != null) {
                listOfDevices.add(bt);
                lists.add(bt.getName());
            }
        }

        this.listOfNames = lists;
        this.callback = callback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.title.setText(listOfNames.get(position));

        BluetoothDevice device = listOfDevices.get(position);
        holder.address.setText(device.getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.invoke(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfNames.size();
    }

}
