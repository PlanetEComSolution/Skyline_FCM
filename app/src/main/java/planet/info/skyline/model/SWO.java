
package planet.info.skyline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SWO {

    @SerializedName("ID_PK")
    @Expose
    private String iDPK;
    @SerializedName("TXT_SWO")
    @Expose
    private String tXTSWO;
    @SerializedName("TXT_LINK")
    @Expose
    private String tXTLINK;
    @SerializedName("JOB_ID")
    @Expose
    private String jOBID;
    @SerializedName("EMP_ID")
    @Expose
    private String eMPID;
    @SerializedName("EST_HOUR")
    @Expose
    private String eSTHOUR;
    @SerializedName("TODAY_DATE")
    @Expose
    private String tODAYDATE;
    @SerializedName("Random_no")
    @Expose
    private String randomNo;
    @SerializedName("view_status")
    @Expose
    private String viewStatus;
    @SerializedName("spc_status")
    @Expose
    private String spcStatus;
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("Deparmentid")
    @Expose
    private String deparmentid;
    @SerializedName("Today_Time")
    @Expose
    private String todayTime;
    @SerializedName("Hours_Based_On")
    @Expose
    private String hoursBasedOn;
    @SerializedName("serviceDueDate")
    @Expose
    private String serviceDueDate;
    @SerializedName("updatedate_d")
    @Expose
    private String updatedateD;
    @SerializedName("swo_name")
    @Expose
    private String swoName;
    @SerializedName("rental_status")
    @Expose
    private String rentalStatus;
    @SerializedName("swo_type")
    @Expose
    private String swoType;
    @SerializedName("mswoid")
    @Expose
    private String mswoid;
    @SerializedName("tech_id")
    @Expose
    private String techId;
    @SerializedName("Sec_tech_id")
    @Expose
    private String secTechId;
    @SerializedName("SpcID")
    @Expose
    private String spcID;
    @SerializedName("dealerID")
    @Expose
    private String dealerID;
    @SerializedName("ExtraID")
    @Expose
    private String extraID;
    @SerializedName("SWO_Status_new")
    @Expose
    private String sWOStatusNew;
    @SerializedName("Description")
    @Expose
    private String description;

    /**
     * No args constructor for use in serialization
     * 
     */
    public SWO() {
    }

    /**
     * 
     * @param rentalStatus
     * @param hoursBasedOn
     * @param iDPK
     * @param serviceDueDate
     * @param techId
     * @param eMPID
     * @param tXTLINK
     * @param spcID
     * @param eSTHOUR
     * @param updatedateD
     * @param swoName
     * @param secTechId
     * @param extraID
     * @param todayTime
     * @param description
     * @param viewStatus
     * @param dealerID
     * @param deparmentid
     * @param tODAYDATE
     * @param swoType
     * @param spcStatus
     * @param randomNo
     * @param mswoid
     * @param tXTSWO
     * @param sWOStatusNew
     * @param comments
     * @param jOBID
     */
    public SWO(String iDPK, String tXTSWO, String tXTLINK, String jOBID, String eMPID, String eSTHOUR, String tODAYDATE, String randomNo, String viewStatus, String spcStatus, String comments, String deparmentid, String todayTime, String hoursBasedOn, String serviceDueDate, String updatedateD, String swoName, String rentalStatus, String swoType, String mswoid, String techId, String secTechId, String spcID, String dealerID, String extraID, String sWOStatusNew, String description) {
        super();
        this.iDPK = iDPK;
        this.tXTSWO = tXTSWO;
        this.tXTLINK = tXTLINK;
        this.jOBID = jOBID;
        this.eMPID = eMPID;
        this.eSTHOUR = eSTHOUR;
        this.tODAYDATE = tODAYDATE;
        this.randomNo = randomNo;
        this.viewStatus = viewStatus;
        this.spcStatus = spcStatus;
        this.comments = comments;
        this.deparmentid = deparmentid;
        this.todayTime = todayTime;
        this.hoursBasedOn = hoursBasedOn;
        this.serviceDueDate = serviceDueDate;
        this.updatedateD = updatedateD;
        this.swoName = swoName;
        this.rentalStatus = rentalStatus;
        this.swoType = swoType;
        this.mswoid = mswoid;
        this.techId = techId;
        this.secTechId = secTechId;
        this.spcID = spcID;
        this.dealerID = dealerID;
        this.extraID = extraID;
        this.sWOStatusNew = sWOStatusNew;
        this.description = description;
    }

    public String getIDPK() {
        return iDPK;
    }

    public void setIDPK(String iDPK) {
        this.iDPK = iDPK;
    }

    public String getTXTSWO() {
        return tXTSWO;
    }

    public void setTXTSWO(String tXTSWO) {
        this.tXTSWO = tXTSWO;
    }

    public String getTXTLINK() {
        return tXTLINK;
    }

    public void setTXTLINK(String tXTLINK) {
        this.tXTLINK = tXTLINK;
    }

    public String getJOBID() {
        return jOBID;
    }

    public void setJOBID(String jOBID) {
        this.jOBID = jOBID;
    }

    public String getEMPID() {
        return eMPID;
    }

    public void setEMPID(String eMPID) {
        this.eMPID = eMPID;
    }

    public String getESTHOUR() {
        return eSTHOUR;
    }

    public void setESTHOUR(String eSTHOUR) {
        this.eSTHOUR = eSTHOUR;
    }

    public String getTODAYDATE() {
        return tODAYDATE;
    }

    public void setTODAYDATE(String tODAYDATE) {
        this.tODAYDATE = tODAYDATE;
    }

    public String getRandomNo() {
        return randomNo;
    }

    public void setRandomNo(String randomNo) {
        this.randomNo = randomNo;
    }

    public String getViewStatus() {
        return viewStatus;
    }

    public void setViewStatus(String viewStatus) {
        this.viewStatus = viewStatus;
    }

    public String getSpcStatus() {
        return spcStatus;
    }

    public void setSpcStatus(String spcStatus) {
        this.spcStatus = spcStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDeparmentid() {
        return deparmentid;
    }

    public void setDeparmentid(String deparmentid) {
        this.deparmentid = deparmentid;
    }

    public String getTodayTime() {
        return todayTime;
    }

    public void setTodayTime(String todayTime) {
        this.todayTime = todayTime;
    }

    public String getHoursBasedOn() {
        return hoursBasedOn;
    }

    public void setHoursBasedOn(String hoursBasedOn) {
        this.hoursBasedOn = hoursBasedOn;
    }

    public String getServiceDueDate() {
        return serviceDueDate;
    }

    public void setServiceDueDate(String serviceDueDate) {
        this.serviceDueDate = serviceDueDate;
    }

    public String getUpdatedateD() {
        return updatedateD;
    }

    public void setUpdatedateD(String updatedateD) {
        this.updatedateD = updatedateD;
    }

    public String getSwoName() {
        return swoName;
    }

    public void setSwoName(String swoName) {
        this.swoName = swoName;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public String getSwoType() {
        return swoType;
    }

    public void setSwoType(String swoType) {
        this.swoType = swoType;
    }

    public String getMswoid() {
        return mswoid;
    }

    public void setMswoid(String mswoid) {
        this.mswoid = mswoid;
    }

    public String getTechId() {
        return techId;
    }

    public void setTechId(String techId) {
        this.techId = techId;
    }

    public String getSecTechId() {
        return secTechId;
    }

    public void setSecTechId(String secTechId) {
        this.secTechId = secTechId;
    }

    public String getSpcID() {
        return spcID;
    }

    public void setSpcID(String spcID) {
        this.spcID = spcID;
    }

    public String getDealerID() {
        return dealerID;
    }

    public void setDealerID(String dealerID) {
        this.dealerID = dealerID;
    }

    public String getExtraID() {
        return extraID;
    }

    public void setExtraID(String extraID) {
        this.extraID = extraID;
    }

    public String getSWOStatusNew() {
        return sWOStatusNew;
    }

    public void setSWOStatusNew(String sWOStatusNew) {
        this.sWOStatusNew = sWOStatusNew;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
