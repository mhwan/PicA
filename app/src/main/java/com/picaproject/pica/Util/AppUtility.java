package com.picaproject.pica.Util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.picaproject.pica.Item.ContactItem;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class AppUtility {
    private static AppUtility instance;

    private AppUtility() {
    }

    public synchronized static AppUtility getAppinstance() {
        if (instance == null)
            instance = new AppUtility();
        return instance;
    }

    public ArrayList<ContactItem> getContactList(Context context) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts._ID
        };

        String[] selectionArgs = null;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                + " COLLATE LOCALIZED ASC";

        Cursor cursor = context.getContentResolver().query(uri, projection, null,
                null, null);

        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();
        Log.d("contacts Count!", cursor.getCount()+"");
        if (cursor.moveToFirst()) {
            do {
                long person_id = cursor.getLong(2);

                ContactItem contactItem = new ContactItem();
                contactItem.setPhoneNumber(cursor.getString(0));
                contactItem.setName(cursor.getString(1));
                contactItem.setPersonId(person_id);

                hashlist.add(contactItem);
            } while (cursor.moveToNext());
        }

        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);

        for (int i = 0; i < contactItems.size(); i++) {
            contactItems.get(i).setId(i);
        }
        return contactItems;
    }
}
