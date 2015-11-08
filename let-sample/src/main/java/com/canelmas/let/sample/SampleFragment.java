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
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.canelmas.let.AskPermission;
import com.canelmas.let.DeniedPermission;
import com.canelmas.let.Let;
import com.canelmas.let.RuntimePermissionListener;
import com.canelmas.let.RuntimePermissionRequest;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class SampleFragment extends Fragment
        implements View.OnClickListener, RuntimePermissionListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container, false);

        view.findViewById(R.id.btn_test).setOnClickListener(this);

        return view;
    }

    @Override
    @AskPermission({
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE
    })
    public void onClick(View v) {
        Toast.makeText(getActivity(), "it works inside fragment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowPermissionRationale(List<String> permissions, final RuntimePermissionRequest runtimePermissionRequest) {

        final StringBuilder sb = new StringBuilder();

        for (String permission : permissions) {

            Log.d(SampleActivity.class.getCanonicalName(), permission + " needed!");

            sb.append(permission + " is needed because.");
            sb.append("\n");

        }

        new AlertDialog.Builder(getActivity()).setTitle("Permission Required!")
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
                        runtimePermissionRequest.retry();
                    }
                })
                .show();


    }

    @Override
    public void onPermissionDenied(List<DeniedPermission> results) {
        Toast.makeText(getActivity(), "onPermissionDenied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Let.handle(this, requestCode, permissions, grantResults);
    }
}
