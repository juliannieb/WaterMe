package mx.com.magoo.waterme;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by julian on 10/11/16.
 */
public class WaterMe extends Application {

    public ParseObject plant;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("dXnSGRLk4Ka7mYfCJ6wjFxJwDv7Hj2D2IpMTopsW")
                        .clientKey("fOjFRghYvoBb5MK7X02ox62PDcDBWgJodtysbRgu")
                        //.server("https://www.parse.com/apps/waterme--4/collections") // The trailing slash is important.
                        .build()
        );
    }
}
