package planet.info.skyline.network;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import planet.info.skyline.model.SWO;
import planet.info.skyline.model.TaskFile;
import planet.info.skyline.model.TaskPlan;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;
import rx.Single;


public interface API_Interface {
    @Multipart
    @POST()
    Call<ResponseBody> uploadMedia(@Part MultipartBody.Part[] image, @Url String url);

    @POST("TaskPlanning/BindJobTaskForTask")
    Observable<List<TaskPlan>> TaskPlanList(@Body HashMap<String, String> body);



    @POST("ClientJob/GetSwoByJob")
    Observable<List<SWO>> GetSwoByJob(@Body HashMap<String, String> body);


    @POST("TaskPlanning/GetTaskFiles")
    Observable<List<TaskFile>> GetTaskFiles(@Body HashMap<String, String> body);


   /* @POST("ClientJob/GetSwoByJob")
    Single<Response<List<SWO>>> GetSwoByJob1(@Body HashMap<String, String> body);*/

}
