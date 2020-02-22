package tw.sample.samplelinetv.okhttp;

public interface OkHttpCallBack {

    void Result(String response);

    void Error(String error);

    void Finish();
}

