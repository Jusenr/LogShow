package com.putao.logshow;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    public static final String PROVIDER_URI = "com.putao.ptlogapp.provider";
    public static final String DB_NAME = "ptlog.db";
    public static final String TABLE_NAME = "t_log";

    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int ERROR = 4;

    private ListView listView;
    private List<String> list;
    private ArrayAdapter<String> adapter;

    Button add, delete, modify, query;

    Uri uri = Uri.parse("content://com.putao.ptlogapp.provider/t_log");

    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
    }

    public void initView() {
        Toast.makeText(getApplicationContext(), "com.putao.ptlogapp", Toast.LENGTH_SHORT).show();

        final EditText pkgName = (EditText) findViewById(R.id.et_packagename);
        pkgName.setEnabled(false);
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packagename = pkgName.getText().toString().trim();
                if (!TextUtils.isEmpty(packagename)) {
                    uri = Uri.parse("content://" + packagename + ".provider/t_log");
                    Toast.makeText(getApplicationContext(), packagename, Toast.LENGTH_SHORT).show();
                } else {
                    uri = Uri.parse("content://com.putao.paiband.en.provider/t_log");
                    Toast.makeText(getApplicationContext(), "com.putao.paiband.en", Toast.LENGTH_SHORT).show();
                }
                pkgName.setEnabled(true);
                pkgName.requestFocus();
                Log.i("Provider_Uri", "Uri: " + uri);
            }
        });

        listView = (ListView) findViewById(R.id.mylistview);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        add = (Button) findViewById(R.id.add);
        delete = (Button) findViewById(R.id.delete);
        modify = (Button) findViewById(R.id.modify);
        query = (Button) findViewById(R.id.query);

        add.setOnClickListener(this);
        delete.setOnClickListener(this);
        modify.setOnClickListener(this);
        query.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            MPermissions.requestPermissions(this, PERMISSIONS_REQUEST_READ_CONTACTS, Manifest.permission.READ_CONTACTS);
        } else {
            if (mContentResolver == null) {
                mContentResolver = getContentResolver();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                ContentValues contentValues = new ContentValues();
                contentValues.put("level", String.valueOf(INFO));
                contentValues.put("content", UUID.randomUUID().toString());
//                contentValues.put("content", "15880076000_TEST");
                Uri newuri = mContentResolver.insert(uri, contentValues);
                Toast.makeText(this, newuri.toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                int result = mContentResolver.delete(uri, "level=?", new String[]{String.valueOf(INFO)});
                Toast.makeText(this, result + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.modify:
                ContentValues cValues = new ContentValues();
                cValues.put("level", String.valueOf(DEBUG));
                cValues.put("content", "15880076000_TEST");
                int result_modify = mContentResolver.update(uri, cValues, "level=?", new String[]{String.valueOf(DEBUG)});
                Toast.makeText(this, result_modify + "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.query:
                Cursor cursor = mContentResolver.query(uri, null, null, null, null);
                if (cursor != null) {
                    list.clear();
                    while (cursor.moveToNext()) {
                        String idString = cursor.getString(cursor.getColumnIndex("id"));
                        String levelString = cursor.getString(cursor.getColumnIndex("level"));
                        String dateString = cursor.getString(cursor.getColumnIndex("date"));
                        String contentString = cursor.getString(cursor.getColumnIndex("content"));
                        if (!TextUtils.isEmpty(dateString)) {
                            Date date = new Date(Long.valueOf(dateString) / 1000L);
                            list.add(idString + "  " + levelString + "  " + date + "\n" + contentString);
                        } else {
                            list.add(idString + "  " + levelString + "\n" + contentString);
                        }
                    }
                    cursor.close();
                    adapter.notifyDataSetChanged();
                }
                break;

            default:
                break;
        }
    }

    @PermissionGrant(PERMISSIONS_REQUEST_READ_CONTACTS)
    public void requestSdcardSuccess() {
        if (mContentResolver == null) {
            mContentResolver = getContentResolver();
        }
    }

    @PermissionDenied(PERMISSIONS_REQUEST_READ_CONTACTS)
    public void requestSdcardFailed() {
        Toast.makeText(this, "DENY ACCESS SDCARD!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
