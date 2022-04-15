package com.example.datlichkham.fragment.nguoidung;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datlichkham.DangNhapActivity;
import com.example.datlichkham.MainActivity;
import com.example.datlichkham.R;
import com.example.datlichkham.adapter.SettingAdapter;
import com.example.datlichkham.model.Setting;

import java.util.ArrayList;
import java.util.List;


public class NdSettingFragment extends Fragment {
    private TextView tvIdTaiKhoan;
    private ListView lvSetting;
    List<Setting> mLists;
    private Context context;

    int imgdangxuat;
    private MainActivity mainActivity;

    public NdSettingFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static NdSettingFragment newInstance() {
        NdSettingFragment fragment = new NdSettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nd_setting, container, false);
        mainActivity= (MainActivity) getActivity();
        mapping(view);
        addSettingItem();
        setDataAdapter();
        return view;
    }

    private void setDataAdapter() {
        SettingAdapter settingAdapter = new SettingAdapter(context, mLists);
        lvSetting.setDivider(null);
        lvSetting.setAdapter(settingAdapter);
        lvSetting.setOnItemClickListener((parent, view, position, id) -> {
            switch (position){
                case 2:
                    Intent intent = new Intent(mainActivity, DoiMatKhauActivity.class);
                    startActivity(intent);
                    break;
                case 3:
//                    context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().clear().commit();
//                    context.startActivity(new Intent(context, DangNhapActivity.class));
//                    getActivity().finish();
//                    break;
                    context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit().clear().commit();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Bạn có muốn thoát?");
                    alertDialogBuilder.setCancelable(false);
                    alertDialogBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(context, DangNhapActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertDialogBuilder.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                default:
                    break;
            }
        });
    }

    private void addSettingItem() {
        mLists = new ArrayList<>();

        Setting obj = new Setting();

        obj.setTitle("Tài Khoản");
        mLists.add(obj);
        obj.setImage(R.drawable.icon_thongtintaikhoan);

        Setting obj2 = new Setting();

        obj2.setTitle("Thêm sổ bảo hiểm");
        mLists.add(obj2);
        obj2.setImage(R.drawable.icon_sobaohiem);

        Setting obj3 = new Setting();

        obj3.setTitle("Đổi mật khẩu");
        mLists.add(obj3);
        obj3.setImage(R.drawable.icon_doimatkhau);

        Setting obj4 = new Setting();

        imgdangxuat = R.drawable.icon_dangxuat;
        obj4.setTitle("Đăng xuất");
        mLists.add(obj4);
        obj4.setImage(imgdangxuat);

    }

    private void mapping(View view) {
        tvIdTaiKhoan = view.findViewById(R.id.tv_taiKhoan_setting);
        lvSetting = view.findViewById(R.id.lv_setting);
        tvIdTaiKhoan.setText("Tài khoản: "+context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("USERNAME", ""));
    }
}