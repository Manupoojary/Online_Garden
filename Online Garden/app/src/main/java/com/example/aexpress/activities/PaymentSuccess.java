package com.example.aexpress.activities;

import static com.example.aexpress.utils.Constants.API_BASE_URL;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//import com.example.aexpress.databinding.ActivityDummySuccessBinding;
import com.example.aexpress.Prevalent.Getname;
import com.example.aexpress.databinding.ActivityPaymentSuccessBinding;
import com.example.aexpress.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class PaymentSuccess extends AppCompatActivity {
    ActivityPaymentSuccessBinding binding;

    String parentDbName ="Users";
    String phone,name;

    String orderCode,totalPrice,status,shipdate,shiploc;

    TextView orderview,amountview,statusview;

    private static String url= API_BASE_URL +"/db_insert.php";
    private static  String urlorder = API_BASE_URL +"/userorder.php";

    private static String url2= API_BASE_URL +"/itemquantityupdate.php";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Getname g =new Getname();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        orderCode = getIntent().getStringExtra("orderCode");
        totalPrice = getIntent().getStringExtra("totalPrice");
        status = getIntent().getStringExtra("status");
        shipdate =g.shipdate;
        phone = Getname.UserPhone;
        name=Getname.UserName;
        shiploc=Getname.location;

        binding.app.setText("RAZORPAY");
        binding.uname.setText(name);
        binding.amt.setText(totalPrice);
        binding.oc.setText(orderCode);


        updatepaymentstatus();
        Insertiontoordertrack();
        updateproductquantity();

//        binding.webview.setMixedContentAllowed(true);
//        binding.webview.loadUrl(Constants.PAYMENT_URL + orderCode);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateproductquantity()
    {
        int id=Getname.itemcode;
        int qty = Getname.ItemQty;

        StringRequest request=new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
//                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> param=new HashMap<String,String>();
                param.put("id", String.valueOf(id));
                param.put("qty", String.valueOf(qty));
                return param;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

        //order status updation end
    }

    void updatepaymentstatus()
    {

        //        Order Status Updation start

        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
//                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> param=new HashMap<String,String>();
                param.put("t3",orderCode);
                return param;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);

        //order status updation end
    }

    void Insertiontoordertrack()
    {
        //        Insertion to ordertrack table
        StringRequest request1=new StringRequest(Request.Method.POST, urlorder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
//                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> param=new HashMap<String,String>();
                param.put("ph",phone);
                param.put("od",orderCode);
                param.put("amt",totalPrice);
                param.put("shdate",shipdate);
                param.put("shloc",shiploc);
                return param;
            }
        };


        RequestQueue queue1= Volley.newRequestQueue(getApplicationContext());
        queue1.add(request1);



    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(PaymentSuccess.this, MainActivity.class);
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }
}