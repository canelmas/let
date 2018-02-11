/*
 * Copyright (C) 2018 Can Elmas
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

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by can on 30/10/15.
 */
public class ContactsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "Let-Sample-Contacts";
    private TextView mMessageText = null;

    private static String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";

    /**
     * Projection for the content provider query includes the id and primary name of a contact.
     */
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
    /**
     * Sort order for the query. Sorted by primary name in ascending order.
     */
    private static final String ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_contacts);

        mMessageText = (TextView) findViewById(R.id.contact_message);

        // Register a listener to add a dummy contact when a button is clicked.
        Button button = (Button) findViewById(R.id.contact_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDummyContact();
            }
        });

        // Register a listener to display the first contact when a button is clicked.
        button = (Button) findViewById(R.id.contact_load);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadContact();
            }
        });
    }


    /**
     * Restart the Loader to query the Contacts content provider to display the first contact.
     */
    private void loadContact() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    /**
     * Initialises a new {@link CursorLoader} that queries the {@link ContactsContract}.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, ContactsContract.Contacts.CONTENT_URI, PROJECTION,
                null, null, ORDER);
    }


    /**
     * Dislays either the name of the first contact or a message.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor != null) {
            final int totalCount = cursor.getCount();
            if (totalCount > 0) {
                cursor.moveToFirst();
                String name = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mMessageText.setText(
                        getResources().getString(R.string.contacts_string, totalCount, name));
            } else {
                mMessageText.setText(R.string.contacts_empty);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMessageText.setText(R.string.contacts_empty);
    }

    /**
     * Accesses the Contacts content provider directly to insert a new contact.
     * <p>
     * The contact is called "__DUMMY ENTRY" and only contains a name.
     */
    private void insertDummyContact() {
        // Two operations are needed to insert a new contact.
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

        // First, set up a new raw contact.
        ContentProviderOperation.Builder op =
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        operations.add(op.build());

        // Next, set the name for the contact.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        DUMMY_CONTACT_NAME);
        operations.add(op.build());

        // Apply the operations.
        ContentResolver resolver = getContentResolver();
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            Log.d(TAG, "Could not add a new contact: " + e.getMessage());
        } catch (OperationApplicationException e) {
            Log.d(TAG, "Could not add a new contact: " + e.getMessage());
        }
    }
}
