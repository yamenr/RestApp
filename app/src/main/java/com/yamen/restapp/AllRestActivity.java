package com.yamen.restapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllRestActivity extends AppCompatActivity {

    private RecyclerView rvAllRest;
    AdapterRestaurant adapter;
    FirebaseServices fbs;
    ArrayList<Restaurant> rests;
    MyCallback myCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_rest);

        fbs = FirebaseServices.getInstance();
        rests = new ArrayList<Restaurant>();
        readData();
        myCallback = new MyCallback() {
            @Override
            public void onCallback(List<Restaurant> restsList) {
                RecyclerView recyclerView = findViewById(R.id.rvRestsAllRest);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new AdapterRestaurant(getApplicationContext(), rests);
                recyclerView.setAdapter(adapter);
            }
        };


        // set up the RecyclerView
        /*
        RecyclerView recyclerView = findViewById(R.id.rvRestsAllRest);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterRestaurant(this, rests);
        recyclerView.setAdapter(adapter);*/
    }

    private void readData() {
        try {

            fbs.getFire().collection("restaurants")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    rests.add(document.toObject(Restaurant.class));
                                }

                                myCallback.onCallback(rests);
                            } else {
                                Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}