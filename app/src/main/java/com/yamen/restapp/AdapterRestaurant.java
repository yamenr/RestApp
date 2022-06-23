package com.yamen.restapp;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdapterRestaurant extends RecyclerView.Adapter<AdapterRestaurant.ViewHolder> {

    private List<Restaurant> mData;
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseServices fbs;

    private final AdapterRestaurant.ItemClickListener mClickListener = new ItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Restaurant rest = mData.get(position);
            Intent i = new Intent(context, RestDetailsActivity.class);
            i.putExtra("rest", rest);

            context.startActivity(i);
        }
    };

    // data is passed into the constructor
    AdapterRestaurant(Context context, List<Restaurant> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.fbs = FirebaseServices.getInstance();
    }

    // inflates the row layout from xml when needed
    @Override
    public AdapterRestaurant.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_rest, parent, false);
        return new AdapterRestaurant.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(AdapterRestaurant.ViewHolder holder, int position) {
        Restaurant rest = mData.get(position);
        holder.tvName.setText(rest.getName());
        fbs.getStorage().getReference().child(rest.getPhoto()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(rest.getPhoto()).into(holder.ivPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrol    led off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameRestRow);
            ivPhoto = itemView.findViewById(R.id.ivPhotoRestRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Restaurant getItem(int id) {
        return mData.get(id);
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
