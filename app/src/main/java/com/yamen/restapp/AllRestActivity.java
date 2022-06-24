package com.yamen.restapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
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

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("  RestApp");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.miAddRest:
                gotoAddRest();
                return true;


            case R.id.miSignout:
                showDialog1();
                fbs.getAuth().signOut();
                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    private void showDialog1() {
        new AlertDialog.Builder(this)
                .setTitle("Alert!")
                .setMessage("Are you sure you want to logout!?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void gotoAddRest() {
        Intent i = new Intent(this, AddRestActivity.class);
        startActivity(i);
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

            // TODO: Added sorting
            Collections.sort(rests, new RestaurantComparator());
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}