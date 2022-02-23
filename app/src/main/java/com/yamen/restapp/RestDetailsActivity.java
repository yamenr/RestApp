package com.yamen.restapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

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
        tvDescription.setText(rest.getDescription());
        tvAddress.setText(rest.getAddress());
        tvCategory.setText(rest.getCategory().toString());
        tvPhone.setText(rest.getPhone());
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