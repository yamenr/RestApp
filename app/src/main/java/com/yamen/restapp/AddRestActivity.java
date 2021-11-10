package com.yamen.restapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AddRestActivity extends AppCompatActivity {

    private static final String TAG = "AddRestActivity";
    private EditText etName, etDesc, etAddress, etPhone;
    private Spinner spCat;
    private ImageView ivPhoto;
    private FirebaseServices fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rest);

        getSupportActionBar().hide();
        connectComponents();
    }

    private void connectComponents() {
        etName = findViewById(R.id.etNameAddRest);
        etDesc = findViewById(R.id.etDescriptionAddRest);
        etAddress = findViewById(R.id.etAddressAddRest);
        etPhone = findViewById(R.id.etPhoneAddRest);
        spCat = findViewById(R.id.spRestCatAddRest);
        ivPhoto = findViewById(R.id.ivPhotoAddRest);
        fbs = FirebaseServices.getInstance();
        spCat.setAdapter(new ArrayAdapter<RestCat>(this, android.R.layout.simple_list_item_1, RestCat.values()));
    }

    public void add(View view) {
        // check if any field is empty
        String name, description, address, phone, category, photo;
        name = etName.getText().toString();
        description = etDesc.getText().toString();
        address = etAddress.getText().toString();
        phone = etPhone.getText().toString();
        category = spCat.getSelectedItem().toString();
        if (ivPhoto.getDrawable() == null)
            photo = "no_image";
        else photo = ivPhoto.getDrawable().toString();

        if (name.trim().isEmpty() || description.trim().isEmpty() || address.trim().isEmpty() ||
            phone.trim().isEmpty() || category.trim().isEmpty() || photo.trim().isEmpty())
        {
            Toast.makeText(this, R.string.err_fields_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        Restaurant rest = new Restaurant(name, description, address, RestCat.valueOf(category), photo, phone);
        fbs.getFire().collection("restaurants")
                .add(rest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void selectPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),40);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 40) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        ivPhoto.setBackground(null);
                        ivPhoto.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}