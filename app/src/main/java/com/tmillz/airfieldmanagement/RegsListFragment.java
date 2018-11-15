package com.tmillz.airfieldmanagement;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

public class RegsListFragment extends ListFragment {

    private String dataArray [];

    public RegsListFragment() {
        dataArray =  new String[] {"One", "Two", "Three" };
    }

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        ListAdapter listAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, dataArray);
        setListAdapter(listAdapter);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<String> str = new ArrayList<>();
        str.add("five");
        str.add("six");
        str.add("seven");

        MyObjects mObjects = new MyObjects("name",20, str);
        return inflater.inflate(R.layout.regulations_list, container, false);
    }


    public static class MyObjects implements Parcelable {

        private int age;
        private String name;

        private ArrayList<String> address;

        public MyObjects(String name, int age, ArrayList<String> address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        public MyObjects(Parcel source) {
            age = source.readInt();
            name = source.readString();
            address = source.createStringArrayList();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(age);
            dest.writeString(name);
            dest.writeStringList(address);
        }

        public int getAge() {
            return age;
        }

        public String getName() {
            return name;
        }

        public ArrayList<String> getAddress() {
            if (!(address == null))
                return address;
            else
                return new ArrayList<String>();
        }

        public static final Creator<MyObjects> CREATOR = new Creator<MyObjects>() {
            @Override
            public MyObjects[] newArray(int size) {
                return new MyObjects[size];
            }

            @Override
            public MyObjects createFromParcel(Parcel source) {
                return new MyObjects(source);
            }
        };

    }

}
    

