package hk.polyu.eie.eie3109.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PhoneActivity extends AppCompatActivity
{
    private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_NAME_INDEX = 1;
    private static final int CONTACT_KEY_INDEX = 2;
    private static final int CONTACT_HAS_PHONE_INDEX = 1;
    private ListView myPhoneList;
    private ContentResolver cr;
    private List<String> stringList =new ArrayList<>();
    private SimpleCursorAdapter myCursorAdaptor;
    private final String[] projection =
    {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.LOOKUP_KEY,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        cr = getContentResolver();
        Button btnBack = findViewById(R.id.btnBack);

        myPhoneList = findViewById(R.id.LVPhoneList);
        showContacts();
        if (btnBack != null)
        {
            btnBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    finish();
                }
            });
        }

        myPhoneList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                boolean hasPhoneNumber = !((Cursor) myCursorAdaptor.getItem(i)).getString(CONTACT_HAS_PHONE_INDEX).equals("0");
                String message = "";
                if (hasPhoneNumber)
                {
                    String id = ((Cursor) myCursorAdaptor.getItem(i)).getString(CONTACT_ID_INDEX);
                    Log.v("Phone Activity: ", "id selected :" + id);
                    Cursor phoneCursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phoneCursor.moveToNext())
                    {
                        if (phoneCursor.getString(1) != null) message += phoneCursor.getString(1) + '\n';

                    }
                }
                else
                {
                    message = "No Available Phone Number.";
                }

                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.titleTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PhoneActivity.this, stringList.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showContacts()
    {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) !=
                        PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[])overridden method
        }
        else
        {
            Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);

            myCursorAdaptor = new SimpleCursorAdapter(this, R.layout.list_item, c, new String[] {ContactsContract.Contacts.DISPLAY_NAME }, new int[] {R.id.TVRow}, 0);
            myPhoneList.setAdapter(myCursorAdaptor);
            queryContactPhoneNumber();

        }
    }
    private void queryContactPhoneNumber() {
        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols, null, null, null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(nameFieldColumnIndex);
            String number = cursor.getString(numberFieldColumnIndex);
            stringList.add(number);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 100)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                showContacts();
            }
            else
            {
                Toast.makeText(this, "We cannot display the name because you haven't got permission.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
