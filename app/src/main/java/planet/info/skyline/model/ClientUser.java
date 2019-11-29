package planet.info.skyline.model;

public class ClientUser {
    String ename;
    String txt_Mail;
    String Id_Pk;
    String int_client_id;
    String MasterStatus;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    boolean isChecked;




    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getTxt_Mail() {
        return txt_Mail;
    }

    public void setTxt_Mail(String txt_Mail) {
        this.txt_Mail = txt_Mail;
    }

    public String getId_Pk() {
        return Id_Pk;
    }

    public void setId_Pk(String id_Pk) {
        Id_Pk = id_Pk;
    }

    public String getInt_client_id() {
        return int_client_id;
    }

    public void setInt_client_id(String int_client_id) {
        this.int_client_id = int_client_id;
    }



    public ClientUser(String ename, String txt_Mail, String id_Pk, String int_client_id, boolean isChecked,String MasterStatus) {
        this.ename = ename;
        this.txt_Mail = txt_Mail;
        this.  Id_Pk = id_Pk;
        this.int_client_id = int_client_id;
        this.isChecked =isChecked;
        this.MasterStatus =  MasterStatus;
    }

    public String getMasterStatus() {
        return MasterStatus;
    }

    public void setMasterStatus(String masterStatus) {
        MasterStatus = masterStatus;
    }

    @Override
    public String toString() {
        return "ClientUser{" +
                "ename='" + ename + '\'' +
                ", txt_Mail='" + txt_Mail + '\'' +
                ", Id_Pk='" + Id_Pk + '\'' +
                ", int_client_id='" + int_client_id + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
