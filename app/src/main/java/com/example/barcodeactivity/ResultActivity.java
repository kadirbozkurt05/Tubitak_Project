package com.example.barcodeactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeactivity.databinding.ActivityResultBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity {
   private ActivityResultBinding binding;


        FirebaseFirestore firebaseFirestore;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityResultBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);

            binding.textView.setVisibility(View.GONE);
            binding.textView2.setVisibility(View.GONE);
            binding.textView3.setVisibility(View.GONE);
            binding.scrollView.setVisibility(View.GONE);



            firebaseFirestore = FirebaseFirestore.getInstance();
            binding.imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            loadDoc();

            Intent intent = getIntent();
            String barkod = intent.getStringExtra("barkod");
            binding.textView3.setMovementMethod(new ScrollingMovementMethod());

            DocumentReference documentReference = firebaseFirestore.collection("products").document(barkod);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    //BURAYA İSTENİLEN VERİLER EKLENECEK
                    String productName = (String) documentSnapshot.get("productName");
                    String ingreditens = (String) documentSnapshot.get("ingredients");
                    String exWords = (String) documentSnapshot.get("explanation");
                    if (documentSnapshot.get("productName")==null){
                        Toast.makeText(ResultActivity.this, "ÜRÜN BİLGİSİNE ULAŞILAMADI", Toast.LENGTH_SHORT).show();
                        binding.textView.setVisibility(View.GONE);
                        binding.textView2.setVisibility(View.VISIBLE);
                        binding.textView2.setText("ÜRÜN BİLGİSİNE ULAŞILAMADI");
                        binding.textView3.setVisibility(View.GONE);
                        binding.scrollView.setVisibility(View.GONE);
                    }else{
                        binding.textView.setVisibility(View.VISIBLE);
                        binding.textView2.setVisibility(View.VISIBLE);
                        binding.textView3.setVisibility(View.VISIBLE);
                        binding.scrollView.setVisibility(View.VISIBLE);
                        binding.textView.setText("ÜRÜN ADI : "+productName);
                        binding.textView2.setText("İÇİNDE NE VAR :\n"+ingreditens);
                        binding.textView3.setText("NEDİR:\n"+exWords);
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ResultActivity.this, "ÜRÜN BİLGİSİNE ULAŞILAMADI", Toast.LENGTH_SHORT).show();
                    binding.textView.setVisibility(View.GONE);
                    binding.textView2.setVisibility(View.GONE);
                    binding.textView3.setVisibility(View.GONE);
                }
            });

        }


        private void loadDoc() {

            String s = "";

            for(int x=0; x<=100; x++) {
                s += "Line: " + String.valueOf(x) + "\n";
            }

            binding.textView3.setMovementMethod(new ScrollingMovementMethod());

            binding.textView3.setText(s);
        }
    }
