package com.tecmanic.goservices;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import at.grabner.circleprogress.CircleProgressView;

public class PaymentActivityy1 extends AppCompatActivity {

    TextView title,number,number1;
    ImageView back_img,search;
    CardView cardview;
    int layout;

    CircleProgressView circleProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        title = findViewById(R.id.title);


        Intent i=getIntent();
        layout=i.getIntExtra("layout",0);
        if(layout==1){
            title.setText("Salon at home for Women");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.darkgreen));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.darkgreen));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.darkgreen));
            circleProgressView.setValue(Float.parseFloat("90"));
        }
        if(layout==2){
            title.setText("Attending Wedding, Party etc.");


            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("87"));
        }if(layout==3){
            title.setText("Bridal Makeup Artist");


            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("87"));
        }
        if(layout==4){
            title.setText("Mehendi Artist for Bride");
        }
        if(layout==11){
            title.setText("Mehendi Artist for Bride");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("87"));
        }
        if(layout==22){
            title.setText("Mehendi Artist for Bride & Guests");
        }if(layout==33){
            title.setText("Mehendi Artist for Bride & Guests");
        }if(layout==44){
            title.setText("Mehendi Artist for Bride & Guests");

            circleProgressView = findViewById(R.id.circleView);
            circleProgressView.setVisibility(View.VISIBLE);
            circleProgressView.setOuterContourColor(getResources().getColor(R.color.blue));
            circleProgressView.setTextSize(20);
            circleProgressView.setBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setSpinBarColor(getResources().getColor(R.color.blue));
            circleProgressView.setValue(Float.parseFloat("90"));

        }


        search = findViewById(R.id.search);
        search.setVisibility(View.GONE);

        back_img = findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cardview = findViewById(R.id.cardview);
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivityy1.this,OrderSucessActivity.class);
                startActivity(intent);
               /* if (layout==1){

                }
                if (layout==2){
                    Intent intent = new Intent(PaymentActivity.this,OrderSucessActivity.class);
                    intent.putExtra("layout",layout);
                    startActivity(intent);
                }
                if (layout==3){
                    Intent intent = new Intent(PaymentActivity.this,OrderSucessActivity.class);
                    intent.putExtra("layout",layout);
                    startActivity(intent);
                }
                if (layout==4){
                    Intent intent = new Intent(PaymentActivity.this,OrderSucessActivity.class);
                    intent.putExtra("layout",layout);
                    startActivity(intent);
                }

                if (layout==11){
                    Intent intent = new Intent(PaymentActivity.this,OrderSucessActivity.class);
                    intent.putExtra("layout",layout);
                    startActivity(intent);
                }

                if (layout==22){
                    Intent intent = new Intent(PaymentActivity.this,OrderSucessActivity.class);
                    intent.putExtra("layout",layout);
                    startActivity(intent);
                }

                if (layout==33){
                    Intent intent = new Intent(PaymentActivity.this,OrderSucessActivity.class);
                    intent.putExtra("layout",layout);
                    startActivity(intent);
                }
                if (layout==44){
                    Intent intent = new Intent(PaymentActivity.this,MehndiArtistBrideGuestNineActivity.class);
                    intent.putExtra("layout",layout);
                    startActivity(intent);
                }
*/

            }
        });

    }
}
