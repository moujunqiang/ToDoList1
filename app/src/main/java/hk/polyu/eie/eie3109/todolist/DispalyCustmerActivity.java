package hk.polyu.eie.eie3109.todolist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DispalyCustmerActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * BACK
     */
    private Button mBtnBack;
    private ListView mLVCustomerList;
    private SalesDB salesDB;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispaly_custmer);
        showContacts();
        initView();
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[])overridden method
        } else {


        }
    }

    private void initView() {
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);
        mLVCustomerList = (ListView) findViewById(R.id.LVCustomerList);
        salesDB = new SalesDB(getApplicationContext(), null, null, 0);
        SQLiteDatabase readableDatabase = salesDB.getReadableDatabase();

        cursor =
                readableDatabase.query(SalesDB.TABLE_CUSTOMER, new String[]{SalesDB.CUSTOMER_ID, SalesDB.CUSTOMER_NAME, SalesDB.CUSTOMER_GENDER}, null, null, null, null, null);
        int resultCount = cursor.getCount();
        final ArrayList<String> customers_name = new ArrayList<>();
        if (resultCount > 0 && cursor.moveToNext()) {
            for (int i = 0; i < resultCount; i++) {
                customers_name.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }

        mLVCustomerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customers_name));
        mLVCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                queryContactPhoneNumber(customers_name.get(position));

            }
        });
    }

    private void queryContactPhoneNumber(String customerName) {
        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols, null, null, null);
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // 取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String name = cursor.getString(nameFieldColumnIndex);
            String number = cursor.getString(numberFieldColumnIndex);

            if (name.equals(customerName)) {
                numbers.add(number);
            }
        }
        if (numbers.size() == 0) {
            Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, numbers.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        salesDB.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
}