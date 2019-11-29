package planet.info.skyline.model;
public class Attachment {

    private String FileName;
    private String FileURL;
    private String Date;

    public Attachment(String fileName, String fileURL, String date) {
        FileName = fileName;
        FileURL = fileURL;
        Date = date;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileURL() {
        return FileURL;
    }

    public void setFileURL(String fileURL) {
        FileURL = fileURL;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}