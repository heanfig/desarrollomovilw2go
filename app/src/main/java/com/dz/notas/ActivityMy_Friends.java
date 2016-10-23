package com.dz.notas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dz.notas.Adapters.RecyclerViewAdapter;
import com.zopp.appxilio.R;
import com.dz.notas.models.User;

import java.util.ArrayList;
import java.util.List;

public class ActivityMy_Friends extends AppCompatActivity {

    private List<User> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_my__friends);

        /*persons = new ArrayList<>();
        persons.add(new User("Emma Wilson", "23 years old", "123456",""));
        persons.add(new User("Emma Wilson2", "13 years old", "123456",""));
        persons.add(new User("Emma Wilson3", "223 years old", "123456",""));*/

        ArrayList<String> myValues = new ArrayList<String>();
        myValues.add("KitKat");
        myValues.add("Lollipop");
        myValues.add("Marshmallow");
        myValues.add("Marshmallow");
        myValues.add("Marshmallow");
        myValues.add("Marshmallow");

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(myValues);
        RecyclerView myView =  (RecyclerView)findViewById(R.id.rv);
        myView.setHasFixedSize(true);
        myView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        myView.setLayoutManager(llm);

    }
}
