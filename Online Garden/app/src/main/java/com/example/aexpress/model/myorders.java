package com.example.aexpress.model;

public class myorders
{
    private String phoneno,ordercode,amount,shipdate,shiploc,paymentstatus;


    public myorders(String phoneno, String ordercode, String amount, String shipdate, String shiploc, String paymentstatus)
    {
        this.phoneno = phoneno;
        this.ordercode = ordercode;
        this.amount = amount;
        this.shipdate = shipdate;
        this.shiploc = shiploc;
        this.paymentstatus = paymentstatus;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShipdate() {
        return shipdate;
    }

    public void setShipdate(String shipdate) {
        this.shipdate = shipdate;
    }

    public String getShiploc() {
        return shiploc;
    }

    public void setShiploc(String shiploc) {
        this.shiploc = shiploc;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }
}
