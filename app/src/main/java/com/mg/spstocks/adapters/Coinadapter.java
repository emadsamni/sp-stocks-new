package com.mg.spstocks.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mg.spstocks.R;
import com.mg.spstocks.models.Coin;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import static com.mg.spstocks.R.*;

public class Coinadapter extends RecyclerView.Adapter<Coinadapter.MyViewHolder>{

      private LayoutInflater inflater;
      List<Coin> data = Collections.emptyList();
      Context context;

    public Coinadapter(List<Coin> data, Context context) {
        inflater= LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view= inflater.inflate(layout.coins_item,viewGroup ,false);
       MyViewHolder holder= new MyViewHolder(view);
       return holder;

    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Coin current= data.get(i);
        if (current.getLog().size()!=0) {
            myViewHolder.buyValue.setText(current.getLog().get(0).getSell() + " S.P");
            myViewHolder.sellValue.setText(current.getLog().get(0).getBuy() + " S.P");
            if (current.getLog().size() > 1 ) {
                if (current.getLog().get(0).getSell() > current.getLog().get(1).getSell()) {
                    myViewHolder.ChangeIcon.setImageResource(drawable.trendingup);
                } else {
                    if (current.getLog().get(0).getBuy() < current.getLog().get(1).getBuy()) {
                        myViewHolder.ChangeIcon.setImageResource(drawable.trendingdown);
                    } else {
                        myViewHolder.ChangeIcon.setImageResource(drawable.trendingup);
                    }

                }
            }
            else {
                myViewHolder.ChangeIcon.setImageResource(drawable.trendingup);
            }
        }


        String path= "https://api.spstocks.com/public/storage/"+ current.getImage();
        path = path.replaceAll("\\\\", "/");
        Picasso.with(context).load(path).into(myViewHolder.coinIcon);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView coinIcon;
        ImageView ChangeIcon;
        TextView sellValue;
        TextView buyValue;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
              linearLayout = (LinearLayout) itemView.findViewById(id.linearLayout1);
             coinIcon= (ImageView) itemView.findViewById(id.coin_id);
             ChangeIcon= (ImageView) itemView.findViewById(id.iconchange);
             sellValue= (TextView) itemView.findViewById(id.textViewSellValue);
             buyValue   =(TextView) itemView.findViewById(id.textViewBuyValue);
        }
    }
}
