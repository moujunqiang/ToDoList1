package hk.polyu.eie.eie3109.todolist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Add Customer
     */
    private TextView mTextview;
    /**
     * Name
     */
    private TextView mTextview2;
    private EditText mETName;
    /**
     * Gender
     */
    private TextView mTextview3;
    /**
     * M
     */
    private RadioButton mRBM;
    /**
     * F
     */
    private RadioButton mRBF;
    private RadioGroup mRadioGroup;
    /**
     * BACK
     */
    private Button mBack;
    /**
     * BACK
     */
    private Button mAdd;
    private SalesDB sdb;
    private SQLiteDatabase writableDatabase;
    private String gender = "Male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        initView();
    }

    public void initDb() {
        sdb = new SalesDB(getApplicationContext(), null, null, 0);
        writableDatabase = sdb.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SalesDB.CUSTOMER_GENDER, gender);
        contentValues.put(SalesDB.CUSTOMER_NAME, mETName.getText().toString());
        writableDatabase.insertOrThrow(SalesDB.TABLE_CUSTOMER, null, contentValues);
        sdb.close();
        mAdd.setEnabled(false);

    }


    private void initView() {
        mTextview = (TextView) findViewById(R.id.textview);
        mTextview2 = (TextView) findViewById(R.id.textview2);
        mETName = (EditText) findViewById(R.id.ETName);
        mTextview3 = (TextView) findViewById(R.id.textview3);
        mRBM = (RadioButton) findViewById(R.id.RBM);
        mRBF = (RadioButton) findViewById(R.id.RBF);
        mRBM.setChecked(true);
        mRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        mBack = (Button) findViewById(R.id.Back);
        mBack.setOnClickListener(this);
        mAdd = (Button) findViewById(R.id.Add);
        mAdd.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getId() == R.id.RBF) {
                    gender = "Male";

                } else {
                    gender = "Female";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.Back:
                finish();
                break;
            case R.id.Add:
                initDb();
                break;
        }
    }
}