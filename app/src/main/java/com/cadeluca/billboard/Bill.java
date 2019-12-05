package com.cadeluca.billboard;

import java.util.Date;
import java.util.UUID;

public class Bill {

    private UUID mId;
    private String mTitle;
    private Date mDueDate;
    private float mAmountDue;
    private String mDesc;
    private Boolean isPaid;


    public Bill() {
        this(UUID.randomUUID());
    }

    public Bill(UUID id) {
        mId = id;
        mDueDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }

    public float getAmountDue() {
        return mAmountDue;
    }

    public void setAmountDue(float amountDue) {
        this.mAmountDue = amountDue;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }
}
