package planet.info.skyline.model;
public class PausedJob {

    private String jobName;
    private String JobID;
    private String Client_ID;
    private String swo_id;
    private String swo_name;
    private String comp;
    private String txt_Jdes;
    private String SWO_Status_new;
    private String random_no;

    public PausedJob(String jobName, String jobID, String client_ID, String swo_id, String swo_name, String comp, String txt_Jdes, String SWO_Status_new, String random_no) {
        this.jobName = jobName;
        JobID = jobID;
        Client_ID = client_ID;
        this.swo_id = swo_id;
        this.swo_name = swo_name;
        this.comp = comp;
        this.txt_Jdes = txt_Jdes;
        this.SWO_Status_new = SWO_Status_new;
        this.random_no = random_no;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
    }

    public String getClient_ID() {
        return Client_ID;
    }

    public void setClient_ID(String client_ID) {
        Client_ID = client_ID;
    }

    public String getSwo_id() {
        return swo_id;
    }

    public void setSwo_id(String swo_id) {
        this.swo_id = swo_id;
    }

    public String getSwo_name() {
        return swo_name;
    }

    public void setSwo_name(String swo_name) {
        this.swo_name = swo_name;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getTxt_Jdes() {
        return txt_Jdes;
    }

    public void setTxt_Jdes(String txt_Jdes) {
        this.txt_Jdes = txt_Jdes;
    }

    public String getSWO_Status_new() {
        return SWO_Status_new;
    }

    public void setSWO_Status_new(String SWO_Status_new) {
        this.SWO_Status_new = SWO_Status_new;
    }

    public String getRandom_no() {
        return random_no;
    }

    public void setRandom_no(String random_no) {
        this.random_no = random_no;
    }
}