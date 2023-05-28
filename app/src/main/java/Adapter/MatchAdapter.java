package Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nuddein.evarkadasiprojesi.R;

import java.util.List;

import Model.Kullanici;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    public interface MatchAdapterListener {
        void onDataChanged();
    }

    private Context context;
    private List<String> matchList;
    private MatchAdapterListener listener;

    public MatchAdapter(Context context, List<String> matchList, MatchAdapterListener listener) {
        this.context = context;
        this.matchList = matchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String matchId = matchList.get(position);
        Log.d("MatchAdapterDebug", "Binding data for matchId: " + matchId);  // Log the matchId to check if it's correct

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(matchId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanici kullanici = snapshot.getValue(Kullanici.class);
                if (kullanici != null) {
                    holder.username.setText(kullanici.getKullaniciadi());
                    Log.d("MatchAdapterDebug", "Username set: " + kullanici.getKullaniciadi());  // Log the username to check if it's correctly set

                } else {
                    Log.e("MatchAdapterDebug", "User data is null for matchId: " + matchId);  // Log an error message if the user data is null
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MatchAdapterDebug", "Error reading user data: " + error.getMessage());  // Log the error message if there's a problem reading the user data
            }
        });




        holder.acceptButton.setOnClickListener(v -> {
            // Handle the accept action
            DatabaseReference matchReference = FirebaseDatabase.getInstance().getReference("matchRequests")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(matchId);
            matchReference.setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // remove match request after it's accepted
                    FirebaseDatabase.getInstance().getReference("matchRequests")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(matchId)
                            .removeValue();
                    listener.onDataChanged(); // notify data change
                }
            });
        });

        holder.declineButton.setOnClickListener(v -> {
            // Handle the decline action
            DatabaseReference matchReference = FirebaseDatabase.getInstance().getReference("matchRequests")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(matchId);
            matchReference.setValue(false).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // remove match request after it's declined
                    FirebaseDatabase.getInstance().getReference("matchRequests")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(matchId)
                            .removeValue();
                    listener.onDataChanged(); // notify data change
                }
            });
        });

    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView username;
        public Button acceptButton;
        public Button declineButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_match_item);
            username = itemView.findViewById(R.id.text_view_username_match_item);
            acceptButton = itemView.findViewById(R.id.button_accept_match_item);
            declineButton = itemView.findViewById(R.id.button_decline_match_item);
        }
    }
}

