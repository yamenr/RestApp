package com.yamen.restapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class RestDetailsActivity extends AppCompatActivity {

    private TextView tvName, tvDescription, tvAddress, tvCategory, tvPhone;
    private ImageView ivPhoto;

    /*
        private String address;
    private RestCat category;
    private String photo;
    private String phone;
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_details);

        connectComponents();
        Intent i = this.getIntent();
        Restaurant rest = (Restaurant) i.getSerializableExtra("rest");

        tvName.setText(rest.getName());
        tvDescription.setText(rest.getName());
        tvAddress.setText(rest.getName());
        tvCategory.setText(rest.getName());
        tvPhone.setText(rest.getName());
        Picasso.get().load(rest.getPhoto()).into(ivPhoto);
    }

    private void connectComponents() {
        tvName = findViewById(R.id.tvNameRestDetails);
        tvDescription = findViewById(R.id.tvDescriptionRestDetails);
        tvAddress = findViewById(R.id.tvAddressRestDetails);
        tvCategory = findViewById(R.id.tvCategoryRestDetails);
        tvPhone = findViewById(R.id.tvPhoneRestDetails);
        ivPhoto = findViewById(R.id.ivPhotoRestDetails);
    }
}