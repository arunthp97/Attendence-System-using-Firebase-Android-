package com.example.arun.arrow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    View view;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
    }
    public void setDetails(final Context context, String name ,String roll_no)
    {
        TextView textView1,textView2;
        CardView cardView;
        cardView=itemView.findViewById(R.id.cardview);
        ImageView imageView;
        imageView=itemView.findViewById(R.id.image);
        textView1=itemView.findViewById(R.id.Name);
        textView2=itemView.findViewById(R.id.Enroll);
        textView1.setText(name);
        textView2.setText(roll_no);
    }


}
