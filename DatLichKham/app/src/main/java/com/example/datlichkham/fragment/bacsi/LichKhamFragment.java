package com.example.datlichkham.fragment.bacsi;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datlichkham.R;
import com.example.datlichkham.model.PhieuKham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LichKhamFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private String idPhieuKham;

    private EditText edChanDoan, edChiTiet;
    private TextView tvMaPhieuKham, tvTenBn, tvNgay,tvGio;
    private Button btnHoanThanh;

    private DatabaseReference databaseReference;

    public LichKhamFragment() {

    }


    public static LichKhamFragment newInstance(String param1, String param2) {
        LichKhamFragment fragment = new LichKhamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_kham, container, false);
        idPhieuKham = getContext().getSharedPreferences("BACSI", Context.MODE_PRIVATE).getString("IDPK", "");
        mapping(view);
        getDataFromDb();
        hoanthanh();
        return view;
    }

    private void hoanthanh() {
        btnHoanThanh.setOnClickListener(v -> {
            String benh = edChanDoan.getText().toString().trim();
            String chiTiet = edChiTiet.getText().toString().trim();
            databaseReference = FirebaseDatabase.getInstance().getReference("History").child(idPhieuKham);
            databaseReference.child("benh").setValue(benh);
            databaseReference.child("note").setValue(chiTiet);
            databaseReference.child("status").setValue("Hoàn thành");
            getContext().getSharedPreferences("BACSI", Context.MODE_PRIVATE).edit().putBoolean("DANGKHAM", false).commit();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, new BsLichKhamFragment()).remove(this).commit();
        });
    }

    private void getDataFromDb() {
        databaseReference = FirebaseDatabase.getInstance().getReference("History");
        databaseReference.orderByChild("id");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getKey().equalsIgnoreCase(idPhieuKham)){
                        PhieuKham obj = ds.getValue(PhieuKham.class);
                        tvTenBn.setText(obj.getTenBn());
                        tvMaPhieuKham.setText(obj.getId());
                        tvNgay.setText(obj.getDate());
                        tvGio.setText(obj.getTime());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void mapping(View view) {
        edChanDoan = view.findViewById(R.id.edChanDoan);
        edChiTiet = view.findViewById(R.id.edChiTiet);
        tvMaPhieuKham = view.findViewById(R.id.tvMaPhieuKham_dangKham);
        tvTenBn = view.findViewById(R.id.tvTenBn_dangKham);
        btnHoanThanh =view.findViewById(R.id.btnHoanthanh);
        tvNgay=view.findViewById(R.id.tvNgay123);
        tvGio=view.findViewById(R.id.tvGio123);
    }
}