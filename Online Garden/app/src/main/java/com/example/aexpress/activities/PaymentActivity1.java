package com.example.aexpress.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aexpress.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;


import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity1 extends AppCompatActivity implements PaymentResultListener {

    private EditText amountEdt;
    private Button payBtn;
    String orderCode;
    String totalPrice;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment1);


         orderCode = getIntent().getStringExtra("orderCode");
         totalPrice = getIntent().getStringExtra("totalPrice");

                String samount = totalPrice;

                // rounding off the amount.
                int amount = Math.round(Float.parseFloat(samount) * 100);

                // initialize Razorpay account.
                Checkout checkout = new Checkout();

                // set your id as below
                checkout.setKeyID("rzp_test_Sah1d1fypaQIic");

                // set image
//                checkout.setImage(R.drawable.heycolleagues);

                // initialize json object
                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", "Online Garden");

                    // put description
                    object.put("description", "Test payment");

                    // to set theme color
                    object.put("theme.color", "#25383C");

                    // put the currency
                    object.put("currency", "INR");

                    // put amount
                    object.put("amount", amount);

                    // put mobile number
                    object.put("prefill.contact", "XXXXXXXXXX");

                    // put email
                    object.put("prefill.email", "XXXXXXXX@gmail.com");

                    // open razorpay to checkout activity
                    checkout.open(PaymentActivity1.this, object);


                }
                catch (JSONException e) {
//                    e.printStackTrace();
                }

    }

    @Override
    public void onPaymentSuccess(String s) {
        // this method is called on payment success.
//        Intent intent = new Intent(PaymentActivity1.this, PaymentSuccessActivity.class);
//        intent.putExtra("orderCode", orderCode);
//        startActivity(intent);
        Toast.makeText(this, "Payment is successful : " , Toast.LENGTH_SHORT).show();
//       String status = Paymentupdate.updatepayment(orderCode);
//        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(PaymentActivity1.this, PaymentSuccess.class);
        intent.putExtra("orderCode", orderCode);
        intent.putExtra("totalPrice",totalPrice);
        intent.putExtra("status","Success");
        startActivity(intent);
    }


    @Override
    public void onPaymentError(int i, String s) {
        // on payment failed.
        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
    }
}


