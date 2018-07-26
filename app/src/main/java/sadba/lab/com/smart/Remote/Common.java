package sadba.lab.com.smart.Remote;

public class Common {

    public static final String BASE_URL = "https://smart.education.sn/";

    public static IMyAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IMyAPI.class);
    }
}
