package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nuddein.evarkadasiprojesi.R;

import java.awt.font.TextAttribute;
import java.util.List;

import Model.Kullanici;
import cerceve.ProfilFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.jvm.internal.Lambda;

public class KullaniciAdapter extends RecyclerView.Adapter<KullaniciAdapter.viewHolder>{

    private Context mContext;
    private List<Kullanici> mKullanicilar;

    private FirebaseUser firebaseKullanici;

    public KullaniciAdapter(Context mContext, List<Kullanici> mKullanicilar) {
        this.mContext = mContext;
        this.mKullanicilar = mKullanicilar;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.kullanici_ogesi, parent, false);


        return new KullaniciAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        firebaseKullanici = FirebaseAuth.getInstance().getCurrentUser();

        final Kullanici kullanici = mKullanicilar.get(position);


       holder.kullaniciAdi.setText(kullanici.getKullaniciadi());
       holder.ad.setText(kullanici.getAd());
        Glide.with(mContext).load(kullanici.getResimurl()).into(holder.profil_resmi);








        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileid", kullanici.getId());
                editor.apply();



                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,
                        new ProfilFragment()).commit();
            }
        });



    }

    @Override
    public int getItemCount() {
        return mKullanicilar.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        public TextView kullaniciAdi;
        public TextView ad;
        public CircleImageView profil_resmi;


        public viewHolder(@NonNull View itemView) {

            super(itemView);

            kullaniciAdi = itemView.findViewById(R.id.txt_kullanici_adi_Oge);
            ad = itemView.findViewById(R.id.txt_ad_Oge);
            profil_resmi = itemView.findViewById(R.id.profil_resmi_oge);


        }
    }

}
