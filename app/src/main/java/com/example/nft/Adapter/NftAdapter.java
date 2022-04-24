package com.example.nft.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nft.Model.User;
import com.example.nft.R;
import com.example.nft.mainmenu;

import java.util.List;

public class NftAdapter extends RecyclerView.Adapter<NftAdapter.MyViewHolder>{
    private Context context;
    private List<User> list;
    private Dialog dialog;
    private int Args;



    public interface Dialog{
        void onClick(int pos);
    }
    public void setDialog(Dialog dial) {
        this.dialog = (Dialog) dial;
    }
//    public void setDialog(Dialog dialog) {
//        this.dialog = dialog;
//    }

    public NftAdapter(Context context, List<User> list ){
        this.context= context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_nft, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.name.setText(list.get(position).getName());
    //holder.price.setText("Price : " +list.get(position).getPrice().toString());
    //holder.owner.setText("Owner : " +list.get(position).getOwner().toString());
    Glide.with(context).load(list.get(position).getAvatar()).into(holder.avatar);
    Args = position;
        Log.d("punten", "awww ");

    }

    public void pind(){
       // Log.d("punten", "awww " + Args);
     //Intent intent = new Intent(this, com.example.nft.detail.class);
     //intent.putExtra(namew,list.get)
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
    TextView name, price, owner;
    ImageView avatar, back;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            //price = itemView.findViewById(R.id.email);
            //owner = itemView.findViewById(R.id.owner);
            avatar = itemView.findViewById(R.id.avatar);
            back = itemView.findViewById(R.id.back);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dialog!=null){
                        dialog.onClick(getLayoutPosition());


                    }
                }
            });
        }
    }

}
