
package planet.info.skyline.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskFile {

    @SerializedName("id_pk")
    @Expose
    private String idPk;
    @SerializedName("FilePath")
    @Expose
    private String filePath;
    @SerializedName("UploadedOn")
    @Expose
    private String uploadedOn;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TaskFile() {
    }

    /**
     * 
     * @param filePath
     * @param idPk
     * @param uploadedOn
     */
    public TaskFile(String idPk, String filePath, String uploadedOn) {
        super();
        this.idPk = idPk;
        this.filePath = filePath;
        this.uploadedOn = uploadedOn;
    }

    public String getIdPk() {
        return idPk;
    }

    public void setIdPk(String idPk) {
        this.idPk = idPk;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadedOn() {
        return uploadedOn;
    }

    public void setUploadedOn(String uploadedOn) {
        this.uploadedOn = uploadedOn;
    }

}
