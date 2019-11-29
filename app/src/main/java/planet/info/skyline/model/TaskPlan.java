
package planet.info.skyline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskPlan {

    @SerializedName("ID_PK")
    @Expose
    private String iDPK;
    @SerializedName("task")
    @Expose
    private String task;
    @SerializedName("DeadlineDate")
    @Expose
    private String deadlineDate;
    @SerializedName("ComletedDate")
    @Expose
    private String comletedDate;
    @SerializedName("DeadlineDate1")
    @Expose
    private String deadlineDate1;
    @SerializedName("ComletedDate1")
    @Expose
    private String comletedDate1;
    @SerializedName("AssignTO")
    @Expose
    private String assignTO;
    @SerializedName("Job_Status")
    @Expose
    private String jobStatus;
    @SerializedName("Job_ID_PK")
    @Expose
    private String jobIDPK;
    @SerializedName("stID")
    @Expose
    private String stID;
    @SerializedName("Satate")
    @Expose
    private Object satate;
    @SerializedName("Ename")
    @Expose
    private String ename;
    @SerializedName("SortTask")
    @Expose
    private String sortTask;
    @SerializedName("dept_id")
    @Expose
    private Object deptId;
    @SerializedName("totalchklst")
    @Expose
    private String totalchklst;
    @SerializedName("selectedchklst")
    @Expose
    private String selectedchklst;
    @SerializedName("DTaskID")
    @Expose
    private String dTaskID;
    @SerializedName("docCount")
    @Expose
    private String docCount;
    @SerializedName("TimeBudget")
    @Expose
    private String timeBudget;
    @SerializedName("StartDate")
    @Expose
    private String startDate;
    @SerializedName("AssignUser")
    @Expose
    private String assignUser;
    @SerializedName("IsUsed")
    @Expose
    private String isUsed;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("AssignTO1")
    @Expose
    private Object assignTO1;
    @SerializedName("UserID")
    @Expose
    private String userID;
    @SerializedName("CName")
    @Expose
    private String cName;
    @SerializedName("IsGreen")
    @Expose
    private String isGreen;
    @SerializedName("DP_SDP_Name")
    @Expose
    private String dPSDPName;
    @SerializedName("DeptIDS")
    @Expose
    private String deptIDS;
    @SerializedName("TaskA")
    @Expose
    private String taskA;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TaskPlan() {
    }

    /**
     * 
     * @param startDate
     * @param totalchklst
     * @param dPSDPName
     * @param deadlineDate
     * @param iDPK
     * @param jobStatus
     * @param docCount
     * @param deadlineDate1
     * @param assignUser
     * @param deptIDS
     * @param selectedchklst
     * @param assignTO1
     * @param description
     * @param stID
     * @param timeBudget
     * @param jobIDPK
     * @param comletedDate
     * @param satate
     * @param dTaskID
     * @param isUsed
     * @param comletedDate1
     * @param assignTO
     * @param task
     * @param cName
     * @param sortTask
     * @param isGreen
     * @param userID
     * @param taskA
     * @param ename
     * @param deptId
     */
    public TaskPlan(String iDPK, String task, String deadlineDate, String comletedDate, String deadlineDate1, String comletedDate1, String assignTO, String jobStatus, String jobIDPK, String stID, Object satate, String ename, String sortTask, Object deptId, String totalchklst, String selectedchklst, String dTaskID, String docCount, String timeBudget, String startDate, String assignUser, String isUsed, Object description, Object assignTO1, String userID, String cName, String isGreen, String dPSDPName, String deptIDS, String taskA) {
        super();
        this.iDPK = iDPK;
        this.task = task;
        this.deadlineDate = deadlineDate;
        this.comletedDate = comletedDate;
        this.deadlineDate1 = deadlineDate1;
        this.comletedDate1 = comletedDate1;
        this.assignTO = assignTO;
        this.jobStatus = jobStatus;
        this.jobIDPK = jobIDPK;
        this.stID = stID;
        this.satate = satate;
        this.ename = ename;
        this.sortTask = sortTask;
        this.deptId = deptId;
        this.totalchklst = totalchklst;
        this.selectedchklst = selectedchklst;
        this.dTaskID = dTaskID;
        this.docCount = docCount;
        this.timeBudget = timeBudget;
        this.startDate = startDate;
        this.assignUser = assignUser;
        this.isUsed = isUsed;
        this.description = description;
        this.assignTO1 = assignTO1;
        this.userID = userID;
        this.cName = cName;
        this.isGreen = isGreen;
        this.dPSDPName = dPSDPName;
        this.deptIDS = deptIDS;
        this.taskA = taskA;
    }

    public String getIDPK() {
        return iDPK;
    }

    public void setIDPK(String iDPK) {
        this.iDPK = iDPK;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getComletedDate() {
        return comletedDate;
    }

    public void setComletedDate(String comletedDate) {
        this.comletedDate = comletedDate;
    }

    public String getDeadlineDate1() {
        return deadlineDate1;
    }

    public void setDeadlineDate1(String deadlineDate1) {
        this.deadlineDate1 = deadlineDate1;
    }

    public String getComletedDate1() {
        return comletedDate1;
    }

    public void setComletedDate1(String comletedDate1) {
        this.comletedDate1 = comletedDate1;
    }

    public String getAssignTO() {
        return assignTO;
    }

    public void setAssignTO(String assignTO) {
        this.assignTO = assignTO;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getJobIDPK() {
        return jobIDPK;
    }

    public void setJobIDPK(String jobIDPK) {
        this.jobIDPK = jobIDPK;
    }

    public String getStID() {
        return stID;
    }

    public void setStID(String stID) {
        this.stID = stID;
    }

    public Object getSatate() {
        return satate;
    }

    public void setSatate(Object satate) {
        this.satate = satate;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getSortTask() {
        return sortTask;
    }

    public void setSortTask(String sortTask) {
        this.sortTask = sortTask;
    }

    public Object getDeptId() {
        return deptId;
    }

    public void setDeptId(Object deptId) {
        this.deptId = deptId;
    }

    public String getTotalchklst() {
        return totalchklst;
    }

    public void setTotalchklst(String totalchklst) {
        this.totalchklst = totalchklst;
    }

    public String getSelectedchklst() {
        return selectedchklst;
    }

    public void setSelectedchklst(String selectedchklst) {
        this.selectedchklst = selectedchklst;
    }

    public String getDTaskID() {
        return dTaskID;
    }

    public void setDTaskID(String dTaskID) {
        this.dTaskID = dTaskID;
    }

    public String getDocCount() {
        return docCount;
    }

    public void setDocCount(String docCount) {
        this.docCount = docCount;
    }

    public String getTimeBudget() {
        return timeBudget;
    }

    public void setTimeBudget(String timeBudget) {
        this.timeBudget = timeBudget;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getAssignUser() {
        return assignUser;
    }

    public void setAssignUser(String assignUser) {
        this.assignUser = assignUser;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getAssignTO1() {
        return assignTO1;
    }

    public void setAssignTO1(Object assignTO1) {
        this.assignTO1 = assignTO1;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCName() {
        return cName;
    }

    public void setCName(String cName) {
        this.cName = cName;
    }

    public String getIsGreen() {
        return isGreen;
    }

    public void setIsGreen(String isGreen) {
        this.isGreen = isGreen;
    }

    public String getDPSDPName() {
        return dPSDPName;
    }

    public void setDPSDPName(String dPSDPName) {
        this.dPSDPName = dPSDPName;
    }

    public String getDeptIDS() {
        return deptIDS;
    }

    public void setDeptIDS(String deptIDS) {
        this.deptIDS = deptIDS;
    }

    public String getTaskA() {
        return taskA;
    }

    public void setTaskA(String taskA) {
        this.taskA = taskA;
    }

}
