package com.example.datlichkham;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.datlichkham.adapter.LichSuNDAdapter;
import com.example.datlichkham.model.PhieuKham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class DatLichActivity extends AppCompatActivity {
    private EditText edNgay, edGio;
    private Button btnTaoPhieu, btnHuy;
    private DatabaseReference ref;
    private SharedPreferences prefs;
    private TextView luot;
    String idBs, idBn, tenBs, tenBn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_lich);
        getSupportActionBar().hide();
        edNgay = findViewById(R.id.edNgay_TaoPhieuKham);
        edGio = findViewById(R.id.edGio_TaoPhieuKham);
        btnTaoPhieu = findViewById(R.id.btnTaoPhieuKham);
        btnHuy = findViewById(R.id.btnHuyPhieuKham);
        luot=findViewById(R.id.tvLuot_DatLich);

        idBs = getIntent().getStringExtra("IDBS");
        tenBs = getIntent().getStringExtra("TENBS");

        TextView tvTenBs = findViewById(R.id.tvTenBs_datLichNd);
        tvTenBs.setText("Bác sĩ: "+tenBs);

        prefs = getSharedPreferences("PREFS", MODE_PRIVATE);
        idBn = prefs.getString("USERNAME", "");
        tenBn = prefs.getString("FULLNAME", "");

        edNgay.setOnClickListener(v -> {
            showDateDialog();
        });
        edGio.setOnClickListener(v -> {
            showTimeDialog();
        });
        btnHuy.setOnClickListener(v -> {
            finish();
        });
        btnTaoPhieu.setOnClickListener(v -> {
            taoPhieuKham();
        });

        luotdat();
    }

    private void luotdat() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("History");
        databaseReference.orderByChild("idBs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    int luotdat = 0;
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if(ds.child("idBs").getValue(String.class).equalsIgnoreCase(idBs)){
                            luotdat++;

                        }
                        Log.e("alo",luotdat+"");
                        luot.setText(luotdat);

                    }
                } catch (NullPointerException e){
                    Log.e("===//", ""+e);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void taoPhieuKham() {
        Boolean checkError = true;
        if(edNgay.getText().toString().trim().isEmpty()){
            edNgay.setError("Không được bỏ trống ngày");
            checkError = false;
        }
        if(edGio.getText().toString().trim().isEmpty()){
            edGio.setError("Không được bỏ trống thời gian");
            checkError = false;
        }
        if(checkError){

            
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DatLichActivity.this);
            alertDialogBuilder.setMessage("Xác nhận đặt?");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    ref = FirebaseDatabase.getInstance().getReference().child("History");
                    PhieuKham phieuKham = new PhieuKham("null", "null", "null", "null", "null", "Đang chờ", "null", "null", "null", "null", 0);
                    phieuKham.setId(ref.push().getKey());
                    phieuKham.setIdBs(idBs);
                    phieuKham.setTenBs(tenBs);
                    phieuKham.setIdBn(idBn);
                    phieuKham.setTenBn(tenBn);
                    phieuKham.setDate(edNgay.getText().toString().trim());
                    phieuKham.setTime(edGio.getText().toString().trim());
                    ref.child(phieuKham.getId()).setValue(phieuKham);
                    finish();
                }
            });
            alertDialogBuilder.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
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
                edGio.setText(time);
            }
        };

        new TimePickerDialog(this, onTimeSetListener, hour, minute, true).show();
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
                    Toast.makeText(DatLichActivity.this, "Vui lòng chọn năm lớn hơn hoặc bằng năm hiện tại!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(m1>month&&y1==year)
                {
                    Toast.makeText(DatLichActivity.this, "Vui lòng chọn tháng lớn hơn hoặc bằng tháng hiện tại!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(n1>=dayOfMonth&&y1==year&&m1==month){
                    Toast.makeText(DatLichActivity.this, "Vui lòng chọn ngày lớn hơn ngày hiện tại!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    month = month + 1;
                    String date = dayOfMonth + "/" + month + "/" + year;
                    edNgay.setText(date);
                }
            }
        };

        new DatePickerDialog(this, onDateSetListener, year, month, day).show();
    }


}