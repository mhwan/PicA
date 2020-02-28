package com.picaproject.pica.Item;

import java.io.Serializable;

public class ContactItem implements Serializable, Comparable<ContactItem> {
    private String name;
    private String phoneNumber;
    private long personId;
    private int id;
    private boolean isChecked;
    private ContactStatus status = ContactStatus.NONREGISTER;

    public ContactStatus getStatus() {
        return status;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

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

    @Override
    public int compareTo(ContactItem o) {
        if (this.getName() != null) {
            return this.getName().compareTo(o.getPhoneNumber());

        }
        else
            return this.getPhoneNumber().compareTo(o.getPhoneNumber());
    }

    public enum ContactStatus { REGISTER, NONREGISTER }
}
