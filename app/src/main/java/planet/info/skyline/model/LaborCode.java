
package planet.info.skyline.model;



public class LaborCode {

    private String Lalor_ID_PK;
    private String Labor_name;

    public LaborCode(String lalor_ID_PK, String labor_name) {
        Lalor_ID_PK = lalor_ID_PK;
        Labor_name = labor_name;
    }

    public String getLalor_ID_PK() {
        return Lalor_ID_PK;
    }

    public void setLalor_ID_PK(String lalor_ID_PK) {
        Lalor_ID_PK = lalor_ID_PK;
    }

    public String getLabor_name() {
        return Labor_name;
    }

    public void setLabor_name(String labor_name) {
        Labor_name = labor_name;
    }
}
