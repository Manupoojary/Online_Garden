package com.example.aexpress.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aexpress.Prevalent.Getname;
import com.example.aexpress.R;
import com.example.aexpress.adapters.CartAdapter;
import com.example.aexpress.databinding.ActivityCheckoutBinding;
import com.example.aexpress.model.Product;
import com.example.aexpress.utils.Constants;
import com.hishd.tinycart.model.Cart;
import com.hishd.tinycart.model.Item;
import com.hishd.tinycart.util.TinyCartHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ActivityCheckoutBinding binding;
    CartAdapter adapter;
    ArrayList<Product> products;
    double totalPrice = 0;
    final int tax = 11;
    ProgressDialog progressDialog;
    Cart cart;

    Spinner spinner;

    String selectedloc,name,phone,em,ad;

    String regexPatternphone = "^[0-9]{10}$";
    String regexPatternemail = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3}$";




    String[] loction ={"Select Shipping Location","Manglore","Udupi","Kundapura"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.nameBox.setText(Getname.UserName);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.phoneBox.setText(Getname.UserPhone);
        }


//        embox =findViewById(R.id.emailBox);
//        addbox = findViewById(R.id.addressBox);



        name=binding.nameBox.getText().toString();
        phone=binding.phoneBox.getText().toString();
        em=binding.emailBox.getText().toString();
        ad =binding.addressBox.getText().toString();



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

        products = new ArrayList<>();

        cart = TinyCartHelper.getCart();

        for(Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
            Product product = (Product) item.getKey();
            int quantity = item.getValue();
            product.setQuantity(quantity);

            products.add(product);
        }

        adapter = new CartAdapter(this, products, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged() {
                binding.subtotal.setText(String.format("INR %.2f",cart.getTotalPrice()));
                totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
                binding.total.setText("INR " + totalPrice);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.cartList.setLayoutManager(layoutManager);
        binding.cartList.addItemDecoration(itemDecoration);
        binding.cartList.setAdapter(adapter);

        binding.subtotal.setText(String.format("INR %.2f",cart.getTotalPrice()));

        totalPrice = (cart.getTotalPrice().doubleValue() * tax / 100) + cart.getTotalPrice().doubleValue();
        binding.total.setText("INR " + totalPrice);


        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Toast.makeText(CheckoutActivity.this, name, Toast.LENGTH_SHORT).show();
//                Toast.makeText(CheckoutActivity.this, binding.emailBox.getText().toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(CheckoutActivity.this, phone, Toast.LENGTH_SHORT).show();
//                Toast.makeText(CheckoutActivity.this, ad, Toast.LENGTH_SHORT).show();
//                Toast.makeText(CheckoutActivity.this, selectedloc, Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(binding.nameBox.getText().toString()))
                {
                    Toast.makeText(CheckoutActivity.this,"Please enter your name....",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(binding.emailBox.getText().toString()))
                {
                    Toast.makeText(CheckoutActivity.this,"Please enter email address....",Toast.LENGTH_SHORT).show();
                }
                else if (!(binding.emailBox.getText().toString()).matches(regexPatternemail))
                {
                    Toast.makeText(CheckoutActivity.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(binding.phoneBox.getText().toString()))
                {
                    Toast.makeText(CheckoutActivity.this,"Please enter phone number....",Toast.LENGTH_SHORT).show();
                }
                else if (!(binding.phoneBox.getText().toString()).matches(regexPatternphone))
                {
                    Toast.makeText(CheckoutActivity.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(binding.addressBox.getText().toString()))
                {
                    Toast.makeText(CheckoutActivity.this,"Please enter Address....",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(selectedloc))
                {
                    Toast.makeText(CheckoutActivity.this,"Please select shipping location....",Toast.LENGTH_SHORT).show();
                }
                else {

                    processOrder();
                }


            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, loction) {
            @Override
            public boolean isEnabled(int position) {
                // Disable items at position 2 and 4
                return !(position == 0 || position == 4);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (!isEnabled(position)) {
                    // If item is disabled, set its alpha (transparency) to 0.5
                    view.setAlpha(0.5f);
                } else {
                    view.setAlpha(1.0f);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!adapter.isEnabled(position)) {
                    // If disabled item is selected, display a toast message and reset selection
//                    Toast.makeText(CheckoutActivity.this, "Cannot select this item.", Toast.LENGTH_SHORT).show();
                    spinner.setSelection(0); // Set the first item as selected
                } else {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    selectedloc = selectedItem;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Getname.location=selectedItem;
                    }
//                    Toast.makeText(CheckoutActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


    }

    void processOrder() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject productOrder = new JSONObject();
        JSONObject dataObject = new JSONObject();
        try {

            productOrder.put("address",binding.addressBox.getText().toString());
            productOrder.put("buyer",binding.nameBox.getText().toString());
            productOrder.put("comment", "Nothing");
            productOrder.put("created_at", Calendar.getInstance().getTimeInMillis());
            productOrder.put("last_update", Calendar.getInstance().getTimeInMillis());
            productOrder.put("date_ship", Calendar.getInstance().getTimeInMillis());
            productOrder.put("email", binding.emailBox.getText().toString());
            productOrder.put("phone", binding.phoneBox.getText().toString());
            productOrder.put("serial", "cab8c1a4e4421a3b");
            productOrder.put("shipping", "");
            productOrder.put("shipping_location", selectedloc);
            productOrder.put("shipping_rate", "0.0");
            productOrder.put("status", "WAITING");
            productOrder.put("tax", tax);
            productOrder.put("total_fees", totalPrice);

            JSONArray product_order_detail = new JSONArray();
            for(Map.Entry<Item, Integer> item : cart.getAllItemsWithQty().entrySet()) {
                Product product = (Product) item.getKey();
                int quantity = item.getValue();
                product.setQuantity(quantity);

                JSONObject productObj = new JSONObject();
                productObj.put("amount", quantity);
                productObj.put("price_item", product.getPrice());
                productObj.put("product_id", product.getId());
                productObj.put("product_name", product.getName());
                product_order_detail.put(productObj);
            }

            dataObject.put("product_order",productOrder);
            dataObject.put("product_order_detail",product_order_detail);

            Log.e("err", dataObject.toString());

        } catch (JSONException e) {}

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Constants.POST_ORDER_URL, dataObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(CheckoutActivity.this, "Success order.", Toast.LENGTH_SHORT).show();
                        String orderNumber = response.getJSONObject("data").getString("code");
                        String total =Double.toString(totalPrice);
                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Successful")
                                .setCancelable(false)
                                .setMessage("Your order number is: " + orderNumber)
                        .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(CheckoutActivity.this, PaymentActivity1.class);
                                    intent.putExtra("orderCode", orderNumber);
                                    intent.putExtra("totalPrice",total);
                                    startActivity(intent);
                            }
                        }).show();
                    } else {
                        new AlertDialog.Builder(CheckoutActivity.this)
                                .setTitle("Order Failed")
                                .setMessage("Something went wrong, please try again.")
                                .setCancelable(false)
                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                        Toast.makeText(CheckoutActivity.this, "Failed order.", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                    Log.e("res", response.toString());
                } catch (Exception e)
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Security","secure_code");
                return headers;
            }
        } ;

        queue.add(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}