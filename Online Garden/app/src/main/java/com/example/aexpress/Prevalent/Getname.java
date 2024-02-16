package com.example.aexpress.Prevalent;

import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.example.aexpress.R;
import com.example.aexpress.activities.RegisterActivity;
import java.util.Calendar;
import java.time.LocalDate;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Getname
{

    public static String UserName ="UserName";

    public static String UserPhone ="UserPhone";

    public static String location ="ShipLoc";

    public static int ItemQty=1;
    public  static int itemcode =0;



    // Get the current date
    LocalDate currentDate = LocalDate.now();

    // Increment the date by 2 days
    LocalDate updatedDate = currentDate.plusDays(2);

    // Retrieve the updated date components
    int year = updatedDate.getYear();
    int month = updatedDate.getMonthValue(); // Note: Month starts from 1 (January is 1)
    int day = updatedDate.getDayOfMonth();

    // Display the updated date
    public  String shipdate = day + "/" + month + "/" + year;



}
