package com.example.aexpress.activities;

import static com.example.aexpress.utils.Constants.API_BASE_URL;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.aexpress.Prevalent.Getname;
import com.example.aexpress.R;
import com.example.aexpress.adapters.OrderAdapter;
import com.example.aexpress.databinding.ActivityMyOrdersBinding;
import com.example.aexpress.model.myorders;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrders extends AppCompatActivity {

    private static String url= API_BASE_URL +"/getmyorders.php";
    String phone;

    ActivityMyOrdersBinding binding;
    NavigationView navigationView;
    OrderAdapter orderAdapter;

    ArrayList<String> valuesList = new ArrayList<>();

    ArrayList<myorders> orders ;
    private ProgressDialog loadingbar;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigationView=findViewById(R.id.navigationview);
        orders = new ArrayList<myorders>();
        orderAdapter = new OrderAdapter(this, orders);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phone = Getname.UserPhone;
        boolean k =  getorder();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.orderlist.setLayoutManager(layoutManager);
//        binding.orderlist.addItemDecoration(itemDecoration);
        binding.orderlist.setAdapter(orderAdapter);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
//        navigationView.setVisibility(View.GONE);
        return super.onSupportNavigateUp();
    }

    boolean getorder()
    {
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
//                Toast.makeText(MyOrders.this, response, Toast.LENGTH_SHORT).show();
                if(response.isEmpty())
                {
//                    loadingbar.setMessage("Your order list is empty");
//                    loadingbar.setCancelable(true);
//                    loadingbar.show();
                    Toast.makeText(MyOrders.this, "Your Order list is empty", Toast.LENGTH_LONG).show();

                }
                else {
                    try {
                        JSONArray obj = new JSONArray(response);
                        for (int i = 0; i < obj.length(); i++) {
                            String value = obj.getString(i);

                            valuesList.add(value);

                        }

                        String[] valuesArray = valuesList.toArray(new String[0]);

                        for (String jsonValue : valuesArray) {
                            JSONObject jsonObject = new JSONObject(jsonValue);
                            myorders ord = new myorders(
                                    jsonObject.getString("phoneno"),
                                    jsonObject.getString("ordercode"),
                                    jsonObject.getString("amount"),
                                    jsonObject.getString("shipdate"),
                                    jsonObject.getString("shiploc"),
                                    jsonObject.getString("paymentstatus")

                            );
                            orders.add(ord);
                        }
                        orderAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }


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
                return param;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
        return true;

    }
}