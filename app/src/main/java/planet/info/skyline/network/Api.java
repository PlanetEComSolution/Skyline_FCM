package planet.info.skyline.network;

public class Api {
    /**************************************************EP2**********************************/
    public static final String API_BILLABLE_NONBILLABLE_CODE = "bind_code1";
    public static final String API_MY_SWO = "GetSwoByUserDealer";//1.0 tech
    public static final String API_MY_SWO_AWO = "GetAWOByUserDealerrole";//1.1 both

    public static final String API_UNASSIGNED_SWO_AWO = "GetSwoByDealerbyRole";//2.0 both
    public static final String API_UNASSIGNED_SWO = "GetSwoByDealer";//2.1    tech

    public static final String API_VENDOR = "GetVendorByDealer";
    public static final String API_AUTH_USAGE_CHARGE = "GetAuthUsageCharge";
    public static final String API_GetClientNotification = "GetClientNotification";
    public static final String API_updateclientfiles = "updateclientfiles";
    public static final String API_ShowClientFileByJobeID = "ShowClientFileByJobeID";
    public static final String API_GetClientFileByID = "GetClientFileByID";
    public static final String API_deleteFile = "deleteFile";
    public static final String API_BindJob = "BindJob";
    public static final String API_AllRecentlyUuploadedFiles = "AllRecentlyUuploadedFiles";
    public static final String API_saveclientFileByClient = "saveclientFileByClient";
    public static final String API_UploadPhotoTech = "UploadPhotoTech";
    public static final String API_UploadPhotoTech_new = "UploadPhotoTech_new";
    public static final String API_add_item_descwithFile = "add_item_descwithFile";
    public static final String API_ShowClientDashStatus = "ShowClientDashStatus";
    public static final String API_ShowStatus = "ShowStatus";
    public static final String API_ProofRenderByStatus = "ProofRenderByStatus";
    public static final String API_GetHelpDetails = "GetHelpDetails";
    public static final String API_GetJobFileByTextJob11 = "GetJobFileByTextJob11";

    public static final String API_varify_tech = "varify_tech";
    public static final String API_GetAuthTimeSheet = "GetAuthTimeSheet";
    public static final String API_GetSwoByJObDealer = "GetSwoByJObDealer";
    public static final String API_bindClientByDealer = "bindClientByDealer";
    public static final String API_GetAllDetailbyJobtext_New = "GetAllDetailbyJobtext_New";
    public static final String API_EditTimesheet3_Dec = "EditTimesheet3_Dec";
    public static final String API_GetPauseLIst = "GetPauseLIst";
    public static final String API_GetallJobByDealerID = "GetallJobByDealerID";
    public static final String API_bind_billable_nonBillable_code1 = "bind_billable_nonBillable_code1";
    public static final String API_GetAwoDetailByJob = "GetAwoDetailByJob";
    public static final String API_bindclock_swo_tatus = "bindclock_swo_tatus";
    public static final String API_GetVersion = "GetVersion";
    public static final String API_GetJobDetailsBySWO = "GetJobDetailsBySWO";
    public static final String API_GetClientUserInfo = "GetClientUserInfo";
    public static final String API_GetSkylineJob = "GetSkylineJob";
    public static final String API_UpdateProjectPhotoStatusByClient = "UpdateProjectPhotoStatusByClient";
    public static final String API_SaveProjectFileComment = "SaveProjectFileComment";
    public static final String API_GetProjectFileforClient = "GetProjectFileforClient";
    public static final String API_ShowProjectphotosByJobeID = "ShowProjectphotosByJobeID";
    public static final String API_GetProjectFileComment = "GetProjectFileComment";
    public static final String API_SendMailtoGuestByClient = "SendMailtoGuestByClient";
    public static final String API_ViewCommentsByPhoto = "ViewCommentsByPhoto";
    public static final String API_ShowProofRender = "ShowProofRender";
    public static final String API_UpdateProofRenderStatusByClient = "UpdateProofRenderStatusByClient";
    public static final String API_SendProofMailtoGuestByClient = "SendProofMailtoGuestByClient";
    public static final String API_stop_tech_with_resion = "stop_tech_with_resion";
    public static final String API_ViewCommentsByProof = "ViewCommentsByProof";
    public static final String API_send_po_and_job = "send_po_and_job";
    public static final String API_add_item_desc = "add_item_desc";
    public static final String API_missing_crate = "missing_crate";
    public static final String API_GetClientUserList = "GetClientUserList";
    public static final String API_ShareProjectFile = "ShareProjectFile";
    public static final String API_mail = "mail";
    public static final String API_GetJobFileByTextJob = "GetJobFileByTextJob";
    public static final String API_Bind_Job_client = "Bind_Job_client";
    public static final String API_Bind_Job_client11 = "Bind_Job_client11";
    public static final String API_GetSwoDetailByID = "GetSwoDetailByID";//3.0   tech
    public static final String API_GetSwo_AWODetailByID = "GetSwoDetailByIDRole";//3.1  both
    public static final String API_bindSWOStatus = "bindSWOStatus";//4.0  tech
    public static final String API_bind_SWO_AWO_Status = "bindSWOStatusRole";//4.1    both
    public static final String API_SaveTech = "SaveTech";
    public static final String API_SaveUser = "SaveTechuser"; // not required this functionality for AWO
    public static final String API_GetFutureTimeEntry = "GetFutureTimeEntry";
    public static final String API_BindCountry = "BindCountry";
    public static final String API_BindStateByCountryID = "BindStateByCountryID";
    public static final String API_BindCityByStateID = "BindCityByStateID";
    public static final String API_UpdateClientUser = "UpdateClientUser";
    public static final String API_check_job_exist = "check_job_exist";
    public static final String API_GetJobBySWO = "GetJobBySWO";
    public static final String API_GetClientUserListAll = "GetClientUserListAll";
    public static final String API_BILLABLE_TIMESHEET = "timesheet03_April";//5.0   tech
    public static final String API_BILLABLE_TIMESHEET_Tech_Artist = "timesheet30_july";//5.1  both
    public static final String API_NON_BILLABLE_TIMESHEET = "timesheetNonBillable3_Dec";
    public static final String API_CHANGE_TIME_CODE_TIMESHEET = "timesheetold3_Dec";
    public static final String API_SAVE_USAGE_REPORT = "SaveUsageCharges";
    public static final String API_USAGE_REPORT_LIST = "GetUsageCharges";
    public static final String API_FETCH_PROJECTFILE_FOLDER = "BindProjectFileFolder";
    public static final String API_FETCH_PROJECTFILE = "GetrojectFile";

    public static final String API_VerifyLaborCode = "bind_code1CheckCode";//Staging
    public static final String API_getTimesheetAuth = "GetTimeSheetTechPerm";//Staging

    public static final String API_GetJobFileByTextJob11withrole = "GetJobFileByTextJob11withrole"; //old modified for swo beta
   // public static final String API_GetJobFileByTextJob11withrole = "GetAllFilewithrole";  //new satging
    /**************************************************EP1**********************************/

    public static final String API_ELEMENT_OTHER_CRATE = "/element_other_crate.php?id=";
    public static final String API_ELEMENT_OTHER_CRATE_ACC = "/element_other_crate_acc.php?id=";
    public static final String API_ELEMENT_PATH = "/admin/uploads/accessories/";
    public static final String API_CRATES_LIST = "/crate_web_service.php?id=";
    public static final String API_COLLATERAL_PATH = "/admin/uploads/collateral/";
    public static final String API_CRATE_INFO_UPDATE = "/crate_info_updates.php?";
    public static final String API_CRATE_LOCATION_UPDATE = "/update_location_web.php?id=";
    public static final String API_CHECK_CRATE = "/checkin_other_crate.php?cid=";
    public static final String API_DEFECT_CRATE = "/defect_crate_webservice.php?id=";
    public static final String API_SHOW_CRATE = "/enlarge_crate.php?id=";
    public static final String API_CRATE_LOCATION = "/location_web_service.php?id=";
    public static final String API_FETCH_EXHIBIT = "/graphics_other_crate.php?id=";
    public static final String API_FETCH_ACCESSORY = "/graphics_other_crate_acc.php?id=";
    public static final String API_FETCH_CRATE_LOCATION = "/location_web_service_location.php?LocationId=";
    public static final String API_FETCH_CRATE_NAME = "/get_crate_name.php?id=";
    public static final String API_STORE_ALONE_CRATE = "/store_ship_alone_crate.php?cid=";
    public static final String API_CHECK_CRATE_PACKED_IN_OTHER_CRATE = "/crate_packin_another_crate.php?client_id=";
    public static final String API_MOVE_CRATE_TO_OTHER_LOCATION = "/move_crate_another_location.php?tech_id=";
    public static final String API_GET_BIN_NAME = "/find_win_location.php?id=";
    public static final String API_GET_BIN_NAME_1 = "/find_location_win.php?id=";
    public static final String API_GET_AREA_LOCATION = "/find_area_location.php?id=";
    public static final String API_UPDATE_CRATE = "/crate_web_service_updates.php?sel=";
    public static final String API_UPDATE_CRATE_1 = "/crate_web_servic_updates.php?sel=";
    public static final String API_GET_INSIDE_CRATE_LIST = "/crateslist_insidecrate_webservice.php?id=";
    public static final String API_GET_SERVICE_ORDER_COUNT = "/web_service_order_count.php?client=";
    public static final String API_GET_ORDER_COUNT = "/web_service_order_list.php?client=";
    public static final String API_GET_CRATE_NAME_BY_CARTE_ID = "/crate_number_webservice.php?id=";
    public static final String API_FETCH_CRATE_LOCATION_1 = "/show_location.php?id=";
    public static final String API_UPLOAD = "/UploadToServer.php";


}
