package com.example.campus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    MainActivity mainActivity;
    ArrayList<Users> usersArrayList;
    LayoutInflater layoutInflater;
    public UserAdapter(MainActivity mainActivity, ArrayList<Users> usersArrayList) {
        this.mainActivity=mainActivity;
        this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.from(mainActivity).inflate(R.layout.user,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {

        Users users=usersArrayList.get(position);
        holder.userName.setText(users.userName);
        holder.userStatus.setText(users.status);
        Picasso.get().load(users.profilePic).into(holder.userImg);

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userImg;
        TextView userName,userStatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userImg=itemView.findViewById(R.id.userImg);
            userName=itemView.findViewById(R.id.userName);
            userStatus=itemView.findViewById(R.id.userStatus);
        }
    }
}
