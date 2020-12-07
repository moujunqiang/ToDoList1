package hk.polyu.eie.eie3109.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerMainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * ADD CUSTOMER
     */
    private Button mButton;
    /**
     * DISPLAY CUSTOMERS
     */
    private Button mButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        initView();
    }

    private void initView() {
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mButton2 = (Button) findViewById(R.id.button2);
        mButton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.button:
                startActivity(new Intent(this,AddCustomerActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this,DispalyCustmerActivity.class));

                break;
        }
    }
}