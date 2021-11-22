package com.example.barcodeactivity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barcodeactivity.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    FirebaseFirestore db;
    ActivityResultLauncher<String> permissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = FirebaseFirestore.getInstance();
        registerLauncher();
    }

    public void scan(View view){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {


                Snackbar.make(view,"CAMERAYA ERİŞİM İZNİ GEREKLİ",Snackbar.LENGTH_INDEFINITE).setAction("İZİN VER", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //İZİN İSTE
                        permissionLauncher.launch(Manifest.permission.CAMERA);
                    }
                }).show();


            }else{
                //İZİN İSTE
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }
        }else{
            // İZİN VERİLDİ
            permissionGranted();


        }
    }

    public void result(View view){
        String barkod = binding.editText.getText().toString();

        if (barkod.matches("")){
            Toast.makeText(MainActivity.this,"LÜTFEN ÜRÜN BARKODUNU OKUTUNUZ",Toast.LENGTH_SHORT).show();

        }else {

            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("barkod",barkod);
            startActivity(intent);

        }

    }

    public void registerLauncher(){

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    //İZİN VERİLDİ
                    permissionGranted();

                }else{
                    //TOST MESAJI
                    Toast.makeText(MainActivity.this, "CAMERA ERİŞİM İZNİ GEREKLİ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void permissionGranted(){

        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setPrompt("FLAŞI AÇMAK İÇİN SES AÇMA TUŞUNA BASIN");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if (intentResult!=null){
            String barkod = intentResult.getContents();
            binding.editText.setText(barkod);
        }else{
            Toast.makeText(MainActivity.this, "BARKOD OKUNAMADI", Toast.LENGTH_SHORT).show();
        }
    }
}