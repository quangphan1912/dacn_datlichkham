package com.example.datlichkham.fragment.nguoidung;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datlichkham.CapNhatThongTinActivity;
import com.example.datlichkham.MainActivity;
import com.example.datlichkham.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoiMatKhauActivity extends AppCompatActivity {
    Button huydoi, btndmk;
    private SharedPreferences prefs;
    private DatabaseReference reference;

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    private EditText mk, mkm, nlmkm;
    String passwordDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapping();
        prefs = getSharedPreferences("PREFS", MODE_PRIVATE);
        String getUsername = prefs.getString(USERNAME, "");
        reference = FirebaseDatabase.getInstance().getReference("users").child(getUsername);
        huydoi();
        showAllUserData(getUsername);
        doimatkhau();
    }

    private void showAllUserData(String getUsername) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                passwordDB= dataSnapshot.child("password").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void doimatkhau() {
        btndmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mk.getText().toString().equals(""))
                {
                    Toast.makeText(DoiMatKhauActivity.this, "Vui lòng không bỏ trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordDB.equals(mk.getText().toString())&&mkm.getText().toString().equals(nlmkm.getText().toString())){
                    reference.child("password").setValue(mkm.getText().toString().trim());
                    startActivity(new Intent(DoiMatKhauActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(DoiMatKhauActivity.this, "Vui lòng nhập đúng password!", Toast.LENGTH_SHORT).show();
                    return;

                }
            }
        });
    }

    private void huydoi() {
        huydoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoiMatKhauActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void mapping() {
        mk=findViewById(R.id.mk);
        mkm=findViewById(R.id.mkm);
        nlmkm=findViewById(R.id.nlmkm);
        huydoi=findViewById(R.id.huydoi);
        btndmk=findViewById(R.id.btndmk);
    }
}