package planet.info.skyline.network;

public class SOAP_API_Client {
    // Make any one true
    private static boolean staging = true, beta = false, live = false;
    //Staging
    private static final String HEADER_Staging = "http://";
    private static final String URL_EP1_Staging = HEADER_Staging + "staging.ep1.businesstowork.com";
    private static final String URL_EP2_Staging = HEADER_Staging + "staging.ep2.businesstowork.com";
    private static final String KEY_NAMESPACE_Staging = "https://tempuri.org/";
    //Beta
    private static final String HEADER_Beta = "https://";
    private static final String URL_EP1_Beta = HEADER_Beta + "beta.ep2.businesstowork.com/ep1";
    private static final String URL_EP2_Beta = HEADER_Beta + "beta.ep2.businesstowork.com";
    private static final String KEY_NAMESPACE_Beta = "https://tempuri.org/";
    //Live
    private static final String HEADER_Live = "https://";
    private static final String URL_EP1_Live = HEADER_Live + "www.exhibitpower2.com/ep1";
    private static final String URL_EP2_Live = HEADER_Live + "www.exhibitpower2.com";
    private static final String KEY_NAMESPACE_Live = "https://tempuri.org/";

    public static String KEY_NAMESPACE = staging ? KEY_NAMESPACE_Staging : beta ? KEY_NAMESPACE_Beta : live ? KEY_NAMESPACE_Live : "";
    public static String URL_EP1 = staging ? URL_EP1_Staging : beta ? URL_EP1_Beta : live ? URL_EP1_Live : "";
    public static String URL_EP2 = staging ? URL_EP2_Staging : beta ? URL_EP2_Beta : live ? URL_EP2_Live : "";

    public static String BASE_URL = URL_EP2 + "/WebService/techlogin_service.asmx?";



}
