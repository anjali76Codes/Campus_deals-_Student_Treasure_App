package com.example.campus;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecyclerAdapter extends FirebaseRecyclerAdapter<model, RecyclerAdapter.ViewHolder> {

    public RecyclerAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull model product) {
        holder.bind(product);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView productImageView;
        private final TextView productNameTextView;
        private final TextView productPriceTextView;
        private final TextView productDescriptionTextView;
        private final Button buyButton;
        private final Button chatButton; // Add a reference to the chat button

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.text_product_name);
            productPriceTextView = itemView.findViewById(R.id.text_price);
            productDescriptionTextView = itemView.findViewById(R.id.text_product_content);
            productImageView = itemView.findViewById(R.id.image_product);
            buyButton = itemView.findViewById(R.id.buybtn);
            chatButton = itemView.findViewById(R.id.chatbtn); // Initialize the chat button
        }

        @SuppressLint("SetTextI18n")
        public void bind(model product) {
            productNameTextView.setText(product.getName());
            productPriceTextView.setText("Rs." + product.getPrice());
            productDescriptionTextView.setText(product.getDescription());
            Glide.with(itemView.getContext())
                    .load(product.getImageUrl())
                    .into(productImageView);

            // Set OnClickListener on buy button
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle buy button click
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, OnlinePayment.class);
                    // Pass necessary data to the OnlinePaymentActivity
                    intent.putExtra("productId", product.getId());
                    context.startActivity(intent);
                }
            });

            // Set OnClickListener on chat button
            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Handle chat button click
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, Chat.class);
                    // Pass necessary data to the ChatActivity if needed
                    context.startActivity(intent);
                }
            });
        }
    }

}


