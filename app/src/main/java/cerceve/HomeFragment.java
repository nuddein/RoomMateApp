package cerceve;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nuddein.evarkadasiprojesi.EslesmeActivity;
import com.nuddein.evarkadasiprojesi.MapsActivity;
import com.nuddein.evarkadasiprojesi.R;

public class HomeFragment extends Fragment {



   public Button buttonMap;
   public Button eslesme;




    public HomeFragment() {
        // Required empty public constructor
    }



    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_home, container, false);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);




        //buttonMap = view.findViewById(R.id.button_Map);
       // buttonMap = view.findViewById(R.id.buttonMap);
            buttonMap = view.findViewById(R.id.button_buttonMap);

            eslesme = view.findViewById(R.id.button_eslesme);


        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MapsActivity.class));
            }
        });

        eslesme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EslesmeActivity.class));
            }
        });



       return view;
    }





}