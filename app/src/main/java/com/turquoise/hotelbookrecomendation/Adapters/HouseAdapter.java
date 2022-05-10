package com.turquoise.hotelbookrecomendation.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.turquoise.hotelbookrecomendation.Activities.HotelUserInfo;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.HotelResult;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private HouseViewHolder houseViewHolder;
    private List<Hotel> hotels;
    private HotelResult hotelResult = new HotelResult();
   public String dt0;
   public String dt1;
    public HouseAdapter(Context context) {
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
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.hotelcard, parent, false);
        houseViewHolder = new HouseViewHolder(view);

        return houseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Picasso
                .with(context)
                .load(Uri.parse(hotels.get(position).getImageUrl()))
                .into(holder.hotelImage);


        holder.hotelName.setText(hotels.get(position).getName());
        if(hotels.get(position).getPrice()>0)
        holder.hotelprice.setText("￥"+hotels.get(position).getPrice());
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setHotels(hotels);
               Intent i = new Intent(context, HotelUserInfo.class);
                i.putExtra("hotels", hotelResult);
                i.putExtra("pos", position);
               i.putExtra("data", hotels.get(position));
                i.putExtra("dt0",dt0);
                i.putExtra("dt1",dt1);
               context.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return hotels.size();
    }

    class HouseViewHolder extends RecyclerView.ViewHolder {

        ImageView hotelImage;
        TextView hotelRatings, hotelName, hotelprice;

        TextView tags;
        Button bookButton;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.hotelImage);

            bookButton = itemView.findViewById(R.id.hotelBookButton);

            hotelName = itemView.findViewById(R.id.hotelName);

            hotelprice=itemView.findViewById(R.id.priceView);


        }
    }


}
