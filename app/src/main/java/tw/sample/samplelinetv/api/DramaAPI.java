package tw.sample.samplelinetv.api;

public class DramaAPI {
    private static String Domain = "https://static.linetv.tw/";
    private static String Url_Dramas = "interview/dramas-sample.json";

    // 戲劇列表
    public static String getUrl_Dramas() {
        return String.format("%s%s", Domain, Url_Dramas);
    }

}
