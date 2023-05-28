package cerceve;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nuddein.evarkadasiprojesi.BaslangicActivity;
import com.nuddein.evarkadasiprojesi.ProfilDuzenleActivity;
import com.nuddein.evarkadasiprojesi.R;

import java.util.HashMap;
import java.util.Map;

import Model.Kullanici;

public class ProfilFragment extends Fragment {
    ImageView resimLogout, profil_resmi ;

    ImageView gmail_icon;
    ImageView whatsapp_icon;
    TextView
             txt_ad, txt_kullaniciAdi,  txt_telNo, txt_egitimBilgisi, mail, sure, mesafe;
    Button btn_talep_gonder, btn_profil_duzenle;
    FirebaseUser mevcutKullanici;
    private FirebaseAuth auth;
    String profilId;
    private CheckBox checkBoxLisans, checkBoxYuksekLisans, checkBoxDoktora;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public ProfilFragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profil, container, false);

        mevcutKullanici = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        profilId = prefs.getString("profileid", "none");

        resimLogout = view.findViewById(R.id.resimLogout_profilCercevesi);
        profil_resmi = view.findViewById(R.id.profil_resmi_profilCercevesi);
        txt_kullaniciAdi = view.findViewById(R.id.txt_kullaniciadi_profilcerceve);
        txt_ad = view.findViewById(R.id.txt_ad_profilCercevesi);
        txt_egitimBilgisi = view.findViewById(R.id.txt_egitimBilgisi_profilCercevesi);
        mail = view.findViewById(R.id.txt_mail_profilCercevesi);
        auth = FirebaseAuth.getInstance();
        txt_telNo = view.findViewById(R.id.txt_telNo_profilCercevesi);

        btn_talep_gonder = view.findViewById(R.id.btn_talepGonder_profilCercevesi);
        btn_profil_duzenle = view.findViewById(R.id.btn_profilDuzenle_profilCercevesi);

        checkBoxLisans = view.findViewById(R.id.checkbox_lisans_profilFragment);
        checkBoxYuksekLisans = view.findViewById(R.id.checkbox_yuksek_lisans_profilFragment);
        checkBoxDoktora = view.findViewById(R.id.checkbox_doktora_profilFragment);

        sure = view.findViewById(R.id.text_sure_profilCercevesi);
        mesafe = view.findViewById(R.id.text_mesafe_profilCercevesi);


        gmail_icon = view.findViewById(R.id.gmail_icon);
        whatsapp_icon = view.findViewById(R.id.whatsapp_icon);






        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        //Metodlaricagiriyoruz
        kullaniciBilgisi();

        checkMatchStatus(mevcutKullanici.getUid(), profilId);

        if(profilId.equals(mevcutKullanici.getUid())) {
            // Eğer mevcut kullanıcı kendi profilini görüntülüyorsa

            btn_profil_duzenle.setVisibility(View.VISIBLE);
            btn_profil_duzenle.setText("Profil Duzenle");

            btn_talep_gonder.setVisibility(View.GONE);
        } else {
            // Eğer mevcut kullanıcı başka bir kullanıcının profilini görüntülüyorsa


            btn_talep_gonder.setText("Talep Gönder");
            btn_profil_duzenle.setVisibility(View.GONE);
            btn_talep_gonder.setVisibility(View.VISIBLE);
        }

            resimLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    auth.signOut();

                    Intent intentToBaslangic = new Intent(getContext(), BaslangicActivity.class);
                    startActivity(intentToBaslangic);

                }
            });

        btn_talep_gonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Eşleşme isteği oluştur
                createMatchRequest(mevcutKullanici.getUid(), profilId);
            }
        });

        btn_profil_duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfilDuzenleActivity.class));
            }
        });

        // WhatsApp'ı başlatmak için intent

// ImageView veya ImageButton'a bir click listener ayarlama
        whatsapp_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://wa.me/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
gmail_icon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Uri uri = Uri.parse("https://mail.google.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(webIntent);
    }
});





        return view;
    }

    private void kullaniciBilgisi() {
        DatabaseReference kullaniciYolu = FirebaseDatabase.getInstance().getReference("Kullanicilar").child(profilId);

        kullaniciYolu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) {
                    return;
                }

                Kullanici kullanici = snapshot.getValue(Kullanici.class);

                if (kullanici != null) {
                    Glide.with(getContext()).load(kullanici.getResimurl()).into(profil_resmi);
                    txt_kullaniciAdi.setText(kullanici.getKullaniciadi());
                    txt_ad.setText(kullanici.getAd());

                    txt_telNo.setText("telefon numarası "+kullanici.getTelNo());

                    mesafe.setText("İstenen mesafe:"+ kullanici.getMesafe()+ " km");
                    sure.setText("İstenen süre: "+ kullanici.getSure()+ "ay");

                    mail.setText("mail: "+ kullanici.getMail());

                    // Eğitim durumunu alın ve CheckBox'ları güncelleyin

                    Map<String, Boolean> egitimDurumu = (Map<String, Boolean>) kullanici.getEgitimDurumu();

                    if (egitimDurumu != null) {
                        checkBoxLisans.setChecked(egitimDurumu.get("lisans"));
                        checkBoxYuksekLisans.setChecked(egitimDurumu.get("yuksek_lisans"));
                        checkBoxDoktora.setChecked(egitimDurumu.get("doktora"));
                    } else {
                        checkBoxLisans.setChecked(false);
                        checkBoxYuksekLisans.setChecked(false);
                        checkBoxDoktora.setChecked(false);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        checkBoxLisans.setEnabled(false);
        checkBoxYuksekLisans.setEnabled(false);
        checkBoxDoktora.setEnabled(false);
    }
    public void createMatchRequest(String senderId, String receiverId) {
        String matchRequestId = mDatabase.child("matchRequests").push().getKey();
        Map<String, Object> matchRequest = new HashMap<>();
        matchRequest.put("senderId", senderId);
        matchRequest.put("receiverId", receiverId);
        matchRequest.put("status", "pending"); // başlangıçta durum "bekleyen" olacak

        // Veritabanına yeni bir eşleşme isteği ekler
        if (matchRequestId != null) {
            mDatabase.child("matchRequests").child(matchRequestId).setValue(matchRequest)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Talep gönderildi durumunu göstermek için buton metnini değiştir
                            btn_talep_gonder.setText("Talep Gönderildi");
                            btn_talep_gonder.setEnabled(false);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding match request", e);
                        }
                    });
        }
    }



    private void checkMatchStatus(String senderId, String receiverId) {
        DatabaseReference matchRequestsRef = mDatabase.child("matchRequests");

        matchRequestsRef.orderByChild("senderId").equalTo(senderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String receiverIdInDb = (String) snapshot.child("receiverId").getValue();
                    if (receiverId.equals(receiverIdInDb)) {
                        // Eşleşme talebi zaten gönderildi
                        btn_talep_gonder.setText("Talep Gönderildi");
                        btn_talep_gonder.setEnabled(false);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error reading match requests: " + databaseError.getMessage());
            }
        });
    }
}

