package com.example.proyek1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_STORAGE = 100;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aplikasi Catatan Proyek 1");
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, InsertAndViewActivity.class);
                Map<String, Object> data = (Map<String, Object>) parent.getAdapter().getItem(position);
                intent.putExtra("File Name", data.get("name").toString());
                Toast.makeText(MainActivity.this, "You Clicked" + data.get("nama"), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                tampilkanDialogKonfirmasiHapusCatatan(data.get("nama").toString());
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (periksaIzinPenyimpanan) {
                mengambilListFilePadaFolder();
            }
        } else {
            mengambilListFilePadaFolder();
        }
    }


    public boolean periksaIzinPenyimpanan() {
        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[](Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_STORAGE)
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mengambilListFilePadaFolder();
                }
            break;
        }
    }

    void mengambilListFilePadaFolder(){
        String path = Environment.getExternalStorageDirectory().toString() + "/kominfo.proyek1";
        File directory = new File(path);
        if (directory.exists()) {
            File[] files = directory.listFiles();
            String[] filenames = new String[files.length];
            String[] dateCreated = new String[files.length];
// ini belum beressssss
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy HH:mm:nn");
// tengah ini blom beres
            ArrayList<Map<String, Object>> itemDataList = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < files.length; i++){
                filenames[i] = files[i].getName();
                Date lasModDate = new Date(files[i].lastModified());
                dateCreated[i] = simpleDateFormat.format(lasModDate);
                Map<String, Object> listItemMap = new HashMap<>();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_tambah:
                Intent intent = new Intent(this, InsertAndViewActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void tampilkanDialogKonfirmasiHapusCatatan(){
        new AlertDialog.Builder(this)
        .setTitle("Hapus Catatan Ini?")
        .setMessage("Apakah anda yakin ingin Menghapus Catatan"+filename+"?")
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                hapusFile(filename);
            }
        })
        .setNegativeButton(android.R.string.no, null).show();
    }

    void hapusFile(String filename){
        String path = Environment.getExternalStorageDirectory().toString() + "/kominfo.proyek1";
        File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
        mengambilListFilePadaFolder();
    }
}