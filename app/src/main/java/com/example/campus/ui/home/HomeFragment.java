package com.example.campus.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.campus.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnBuy = root.findViewById(R.id.btnBuy);
        Button btnSell = root.findViewById(R.id.btnSell);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to BuyFragment
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_buy);
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to SellFragment
                Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_sell);
            }
        });

        return root;
    }
}
