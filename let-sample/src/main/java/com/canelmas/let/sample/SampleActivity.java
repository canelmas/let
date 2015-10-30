/*
 * Copyright (C) 2015 Can Elmas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.canelmas.let.sample;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.canelmas.let.AskPermission;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;

public class SampleActivity extends AppCompatActivity implements RuntimePermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sample);

        // Contacts
        findViewById(R.id.btn_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContacts();
            }
        });

        // Location
        findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessLocationAndDoSomething();
            }
        });

        // Native fragment
        findViewById(R.id.btn_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SampleActivity.this, SampleFragmentActivity.class));
            }
        });

        // v4 fragment
        findViewById(R.id.btn_v4_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SampleActivity.this, SampleV4FragmentActivity.class));
            }
        });

    }



    @AskPermission(ACCESS_FINE_LOCATION)
    private void accessLocationAndDoSomething() {
        Toast.makeText(SampleActivity.this, "just accessed location and will make use of it!", Toast.LENGTH_SHORT).show();
    }

    @AskPermission({
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    })
    private void showContacts() {
        startActivity(new Intent(this, ContactsActivity.class));
    }

    @AskPermission(CALL_PHONE)
    private void call(){
        final Intent intent = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:00123124234234"));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Let.handle(requestCode, permissions, grantResults);
    }

    @Override
    public void onShowPermissionRationale(List<String> permissions, final RuntimePermissionRequest request) {

        //  tell user why you need those permissions
        final StringBuilder sb = new StringBuilder();

        for (String permission : permissions) {
            sb.append(getRationale(permission));
            sb.append("\n");
        }

        new AlertDialog.Builder(this).setTitle("Permission Required!")
                .setMessage(sb.toString())
                .setCancelable(true)
                .setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.retry();
                    }
                })
                .show();

        // TODO: 20/10/15 what happens if request not retried

    }

    private String getRationale(String permission) {
        if (permission.equals(Manifest.permission.READ_CONTACTS) || permission.equals(Manifest.permission.WRITE_CONTACTS)) {
            return getString(R.string.rationale_contacts);
        } else if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return getString(R.string.rationale_location);
        } else {
            return getString(R.string.rationale_storage_general);
        }
    }

    @Override
    public void onPermissionDenied(List<DeniedPermission> results) {

        /**
         * Let's just do nothing if permission is denied without
         * 'Never ask Again' checked.
         *
         * If it's the case show more informative message and prompt user
         * to the app settings screen.
         */

        final StringBuilder sb = new StringBuilder();

        for (DeniedPermission result : results) {

            if (result.isNeverAskAgain()) {
                sb.append("onNeverShowAgain for " + result.getPermission());
                sb.append("\n");
            }

        }

        if (sb.length() != 0) {

            new AlertDialog.Builder(this).setTitle("Go Settings and Grant Permission")
                    .setMessage(sb.toString())
                    .setCancelable(true)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, 1);

                            dialog.dismiss();
                        }
                    }).show();

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
