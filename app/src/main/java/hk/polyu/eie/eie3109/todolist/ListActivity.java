package hk.polyu.eie.eie3109.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity
{
    private static final String myStringList_key = "StringList";
    private ListView myToDoList;
    private SharedPreferences.Editor editor;
    private ArrayList<String> myStringList =new ArrayList<>();
    private boolean allowRemove = true;
    private ArrayAdapter<String> myAdapter;
    TextView TVMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Button btnBack = findViewById(R.id.btnBack);
         TVMessage = findViewById(R.id.TVMessage);
        myToDoList = findViewById(R.id.LVList);

        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myStringList);
        myToDoList.setAdapter(myAdapter);
        myToDoList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                ListView options = new ListView(ListActivity.this);
                final String[] choices = new String[]{"Add", "Edit", "Remove"};
                options.setAdapter(new ArrayAdapter<>(ListActivity.this, android.R.layout.simple_list_item_1, choices));
                builder.setView(options);
                final Dialog dialog = builder.create();
                dialog.show();
                final int currentPos = position;
                options.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        String message = "Item " + currentPos + " " + choices[position];
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        switch (position)
                        {
                            case 0:
                            case 1:
                                configEditDialog(currentPos, position);
                                break;
                            case 2:
                                if (allowRemove)
                                {
                                    myStringList.remove(currentPos);
                                    myAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), R.string.lastItemWarning, Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        allowRemove = myStringList.size() > 1;
                        dialog.dismiss();
                    }
                });
            }
        });
        if (btnBack != null)
        {
            btnBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
        }
        getData();



    }
    public void getData(){
        Context c;
        try
        {
            c = this.createPackageContext(MainActivity.PREFERENCE_PACKAGE, CONTEXT_IGNORE_SECURITY);
            SharedPreferences sharedPreferences = c.getSharedPreferences(MainActivity.PREFERENCE_NAME, MainActivity.MODE);
            String name = sharedPreferences.getString("Name", "Default Name");
            editor = sharedPreferences.edit();
            String stringList = sharedPreferences.getString(myStringList_key, "null");
            myStringList.clear();
            ArrayList<String> strings = mapStringList(stringList, ';');
            if (strings.size()==0||stringList.equals("null")){
                resetStringList();
            }else {
                myStringList.clear();
                myStringList.addAll(strings);
            }

            myAdapter.notifyDataSetChanged();


            allowRemove = myStringList.size() > 1;
            if (TVMessage != null) {
                String message = "Hi! " + name;
                TVMessage.setText(message);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    @Override
    protected void onPause()
    {
        super.onPause();
    }


    private void resetStringList()
    {

        ArrayList<String> simpledate = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            simpledate.add("Empty " + i);
        }
        String stringList = reduceStringList(simpledate, ';');
        myStringList.clear();
        myStringList.addAll(simpledate);
        myAdapter.notifyDataSetChanged();
        editor.putString(myStringList_key, stringList);
        editor.apply();
        editor.commit();
    }

    private String reduceStringList(ArrayList<String> stringList, char delimiter)
    {
        String result = "";
        for (String s : stringList)
        {
            result += s + delimiter;
        }
        return result.substring(0, result.length() - 1);
    }

    private ArrayList<String> mapStringList(String s, char delimiter)
    {
        ArrayList<String> result = new ArrayList<>();
        String temp = "";
        for (char c : s.toCharArray())
        {
            if (c != delimiter)
            {
                temp += c;
            }
            else
            {
                result.add(temp);
                temp = "";
            }
        }
        result.add(temp);
        return result;
    }

    private void configEditDialog(final int pos, final int mode)
    {
        final Dialog dialogForm = new Dialog(ListActivity.this);
        dialogForm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForm.setContentView(R.layout.form_operation);
        TextView TVTitle = dialogForm.findViewById(R.id.TVTitle);
        final EditText ETText = dialogForm.findViewById(R.id.ETText);
        Button btnSubmit = dialogForm.findViewById(R.id.btnSubmit);
        if (TVTitle != null) TVTitle.setText(mode == 0 ? R.string.TVTitleAdd : R.string.TVTitleEdit);
        if (ETText != null && mode == 1) ETText.setText(myStringList.get(pos));

        if (btnSubmit != null)
        {
            btnSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mode == 0)
                    {
                        myStringList.add(pos, ETText.getText().toString());
                    }
                    else
                    {
                        myStringList.set(pos, ETText.getText().toString());
                    }
                    myAdapter.notifyDataSetChanged();
                    dialogForm.dismiss();
                }
            });
        }
        dialogForm.show();
    }
}
