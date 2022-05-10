package com.turquoise.hotelbookrecomendation.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.turquoise.hotelbookrecomendation.Activities.HotelInfo;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private HotelViewHolder hotelViewHolder;
    private List<Hotel> hotels;
    private HotelResult hotelResult = new HotelResult();

    public HotelAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setHotels(List<Hotel> lists) {
        this.hotels = lists;
        hotelResult.setHotels(hotels);
        HotelResult hotelResult = new HotelResult();
        hotelResult.setHotels(hotels);
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.hotelcard, parent, false);
        hotelViewHolder = new HotelViewHolder(view);

        return hotelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Picasso
                .with(context)
                .load(Uri.parse(hotels.get(position).getImageUrl()))
                .into(holder.hotelImage);


        holder.hotelName.setText(hotels.get(position).getName());

        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHotels(hotels);
                Intent i = new Intent(context, HotelInfo.class);
                i.putExtra("hotels", hotelResult);
                i.putExtra("pos", position);
                i.putExtra("data", hotels.get(position));
                context.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return hotels.size();
    }

    class HotelViewHolder extends RecyclerView.ViewHolder {

        ImageView hotelImage;
        TextView hotelRatings, hotelName, hotelViews;

        TextView tags;
        Button bookButton;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.hotelImage);

            bookButton = itemView.findViewById(R.id.hotelBookButton);

            hotelName = itemView.findViewById(R.id.hotelName);


        }
    }


}
