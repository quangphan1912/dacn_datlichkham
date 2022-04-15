package com.example.datlichkham.fragment.bacsi;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.datlichkham.DatLichActivity;
import com.example.datlichkham.MainActivity;
import com.example.datlichkham.R;
import com.example.datlichkham.model.PhieuKham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.TimeZone;


public class DangKhamFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private String idPhieuKham;

    private EditText edChanDoan, edChiTiet;
    private TextView tvMaPhieuKham, tvTenBn, tvNgay,tvGio;
    private Button btnDuyetlich, btnHuylich;

    private DatabaseReference databaseReference;
    private MainActivity mainActivity;
    public DangKhamFragment() {

    }


    public static DangKhamFragment newInstance(String param1, String param2) {
        DangKhamFragment fragment = new DangKhamFragment();
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
        View view = inflater.inflate(R.layout.fragment_dang_kham, container, false);
        idPhieuKham = getContext().getSharedPreferences("BACSI", Context.MODE_PRIVATE).getString("IDPK", "");
        mainActivity = (MainActivity) getActivity();
        mapping(view);
        getDataFromDb();
        duyetlich();
        huylich();
        suangay();
        suagio();
        return view;
    }

    private void suagio() {
        tvGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
    }

    private void suangay() {
        tvNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
    }

    private void duyetlich() {
        btnDuyetlich.setOnClickListener(v -> {
//            String benh = edChanDoan.getText().toString().trim();
//            String chiTiet = edChiTiet.getText().toString().trim();
            String date= tvNgay.getText().toString().trim();
            String time=tvGio.getText().toString().trim();
            databaseReference = FirebaseDatabase.getInstance().getReference("History").child(idPhieuKham);
//            databaseReference.child("benh").setValue(benh);
//            databaseReference.child("note").setValue(chiTiet);
            databaseReference.child("date").setValue(date);
            databaseReference.child("time").setValue(time);
            databaseReference.child("status").setValue("Đã Duyệt");
            getContext().getSharedPreferences("BACSI", Context.MODE_PRIVATE).edit().putBoolean("DANGKHAM", false).commit();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, new BsLichKhamFragment()).remove(this).commit();
        });
    }
    private void huylich() {
        btnHuylich.setOnClickListener(v -> {
//            String benh = edChanDoan.getText().toString().trim();
//            String chiTiet = edChiTiet.getText().toString().trim();
            databaseReference = FirebaseDatabase.getInstance().getReference("History").child(idPhieuKham);
//            databaseReference.child("benh").setValue(benh);
//            databaseReference.child("note").setValue(chiTiet);
            // databaseReference.child("status").setValue("Hoàn thành");
            databaseReference.child("status").setValue("Đã Hủy");
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

    private void showTimeDialog() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                tvGio.setText(time);
            }
        };

        new TimePickerDialog(mainActivity, onTimeSetListener, hour, minute, true).show();
    }

    private void showDateDialog() {
        Calendar calendar=Calendar.getInstance();
        int n1=calendar.get(Calendar.DATE);
        int m1=calendar.get(Calendar.MONTH);
        int y1=calendar.get(Calendar.YEAR);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if(y1>year)
                {
                    Toast.makeText(mainActivity, "Vui lòng chọn năm lớn hơn hoặc bằng năm hiện tại!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(m1>month&&y1==year)
                {
                    Toast.makeText(mainActivity, "Vui lòng chọn tháng lớn hơn hoặc bằng tháng hiện tại!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(n1>=dayOfMonth&&y1==year&&m1==month){
                    Toast.makeText(mainActivity, "Vui lòng chọn ngày lớn hơn ngày hiện tại!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    month = month + 1;
                    String date = dayOfMonth + "/" + month + "/" + year;
                    tvNgay.setText(date);
                }
            }
        };

        new DatePickerDialog(mainActivity, onDateSetListener, year, month, day).show();
    }

    private void mapping(View view) {
//        edChanDoan = view.findViewById(R.id.edChanDoan);
//        edChiTiet = view.findViewById(R.id.edChiTiet);
        tvMaPhieuKham = view.findViewById(R.id.tvMaPhieuKham_dangKham);
        tvTenBn = view.findViewById(R.id.tvTenBn_dangKham);
        btnDuyetlich = view.findViewById(R.id.btnDuyetlich);
        btnHuylich =view.findViewById(R.id.btnHuylich);
        tvNgay=view.findViewById(R.id.tvNgay123);
        tvGio=view.findViewById(R.id.tvGio123);
    }
}