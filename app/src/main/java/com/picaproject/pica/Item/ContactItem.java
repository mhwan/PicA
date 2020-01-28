package com.picaproject.pica.Item;

import java.io.Serializable;

public class ContactItem implements Serializable {
    private String name;
    private String phoneNumber;
    private long personId;
    private int id;
    private boolean isChecked;

    public ContactItem(){}
    public ContactItem(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.replace("-", "");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    @Override
    public int hashCode() {
        return getPhoneNumber().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ContactItem)
            return getPhoneNumber().equals(((ContactItem) o).getPhoneNumber());

        return false;
    }
}
