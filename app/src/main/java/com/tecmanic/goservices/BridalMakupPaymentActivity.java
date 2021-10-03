package com.tecmanic.goservices;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BridalMakupPaymentActivity extends AppCompatActivity {


    TextView title,number,number1;
    ImageView back_img,search;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridal_makup_payment);

        title = findViewById(R.id.title);
        title.setText("Select Payment Method");

        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        linear = findViewById(R.id.linear);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BridalMakupPaymentActivity.this,BridalMakupOrderSuccessActivity.class);
                startActivity(intent);
            }
        });




    }
}
