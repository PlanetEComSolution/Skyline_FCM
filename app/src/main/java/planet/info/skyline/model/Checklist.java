package planet.info.skyline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Checklist {


    private String Id1;


    private String Name;



    private String Date;


    private String IsChecked;


    private String Id2;


    public Checklist(String id1, String name, String date, String isChecked, String id2) {
        Id1 = id1;
        Name = name;
        Date = date;
        IsChecked = isChecked;
        Id2 = id2;
    }

 /*   public Checklist(String name, String isChecked) {
        Name = name;
        IsChecked = isChecked;
    }*/

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }





    public String getId1() {
        return Id1;
    }

    public void setId1(String id1) {
        Id1 = id1;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIsChecked() {
        return IsChecked;
    }

    public void setIsChecked(String isChecked) {
        IsChecked = isChecked;
    }

    public String getId2() {
        return Id2;
    }

    public void setId2(String id2) {
        Id2 = id2;
    }
}