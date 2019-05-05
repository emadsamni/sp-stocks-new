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

import com.mg.spstocks.models.gold;
import com.mg.spstocks.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class Goldadapter extends RecyclerView.Adapter<Goldadapter.MyViewHolder>{
    @NonNull
    private LayoutInflater inflater;
    List<gold> data = Collections.emptyList();
    Context context;

    public Goldadapter(List<gold> data, Context context) {
        inflater= LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override

    public Goldadapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= inflater.inflate(R.layout.gold_item,viewGroup ,false);
        Goldadapter.MyViewHolder holder= new Goldadapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Goldadapter.MyViewHolder myViewHolder, int i) {
        gold current= data.get(i);
        if (current.getLog().size()!=0) {
            myViewHolder.goldName.setText(current.getGold_name());
            myViewHolder.goldPrice.setText(current.getLog().get(0).getGold_price() + " " + current.getGold_type().getName() + " ");
            if (current.getLog().size() > 1 ) {
                if (current.getLog().get(0).getGold_price() > current.getLog().get(1).getGold_price()) {
                    myViewHolder.goldChange.setImageResource(R.drawable.trendingup);
                } else {
                    if (current.getLog().get(0).getGold_price() < current.getLog().get(1).getGold_price()) {
                        myViewHolder.goldChange.setImageResource(R.drawable.trendingdown);
                    } else {
                        myViewHolder.goldChange.setImageResource(R.drawable.trendingup);
                    }

                }
            }
            else {
                myViewHolder.goldChange.setImageResource(R.drawable.trendingup);
            }
        }


        String path= "https://api.spstocks.com/public/storage/"+current.getImage();
        path = path.replaceAll("\\\\", "/");
        Picasso.with(context).load(path).into(myViewHolder.goldIcon);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends  RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView goldIcon;
        ImageView goldChange;
        TextView goldPrice;
        TextView goldName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout2);
            goldIcon= (ImageView) itemView.findViewById(R.id.imageViewGoldIcon);
            goldChange= (ImageView) itemView.findViewById(R.id.imageViewGoldChange);
            goldPrice =(TextView) itemView.findViewById(R.id.textViewGoldValue);
            goldName =(TextView)itemView.findViewById(R.id.textViewGoldName);

        }
    }
}
