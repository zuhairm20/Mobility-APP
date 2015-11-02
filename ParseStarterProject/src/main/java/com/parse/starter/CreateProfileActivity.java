package com.parse.starter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class CreateProfileActivity extends AppCompatActivity {

    EditText phoneNumEdit;
    EditText ageEdit;
    EditText disabilityEdit;
    EditText nameEdit;
    CheckBox female;
    CheckBox male;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



         user = ParseUser.getCurrentUser();

         female =  (CheckBox) findViewById(R.id.checkbox_female);
         male =  (CheckBox) findViewById(R.id.checkbox_male);


         phoneNumEdit = (EditText) findViewById(R.id.phoneNumEdit);
         ageEdit = (EditText) findViewById(R.id.ageEdit);
         disabilityEdit = (EditText) findViewById(R.id.disabilityEdit);
         nameEdit = (EditText) findViewById(R.id.nameEdit);

        if (user.get("Name").toString() != null) {
            nameEdit.setText(user.get("Name").toString());
        }

        if (user.get("Age").toString() !=null)
        {
            ageEdit.setText(user.get("Age").toString());
        }

        if (user.get("Gender").toString()!= null){
            if (user.get("Gender").toString() == "F")
            {
                female.setChecked(true);
            }

            else
            {
                male.setChecked(true);
            }
        }






        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.fab);

        saveButton.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(229, 204, 255)));
        saveButton.setRippleColor(Color.rgb(220, 189, 250));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = phoneNumEdit.getText().toString();
                String age = ageEdit.getText().toString();
                String disability = disabilityEdit.getText().toString();
                String name = nameEdit.getText().toString();

                if (nameEdit.getText().toString() != null)
                {
                user.put("Name", name);
                }

                user.put("PhoneNumber", phoneNumber);
                user.put("Disability", disability);
                user.put("Age", age);

                if (male.isChecked()){
                    user.put("Gender", "M");
                }

                if (female.isChecked()){
                    user.put("Gender", "F");
                }

                if ((!male.isChecked()) && (!female.isChecked())){
                    //shake right hurr
                    showToast(getApplicationContext(), "You must select a gender to select a gender");
                }

            }
        });
    }


    protected static void showToast(Context con, CharSequence text) {
        Toast.makeText(con, text, Toast.LENGTH_SHORT).show();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked){

        }
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_male:
                if (checked)

                else
                // Remove the meat
                break;
            case R.id.checkbox_female:
                if (checked)
                // Cheese me
                else
                // I'm lactose intolerant
                break;
            // TODO: Veggie sandwich
        }
    }

}
