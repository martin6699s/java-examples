package main.com.qlk.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class HttpSessionListenerExample implements HttpSessionListener {

    private static int sessions;


    @Override public void sessionCreated(HttpSessionEvent httpSessionEvent) {

        sessions++;

        System.out.println("Inside sessionCreated one session is added to counter,and sessions = "+ sessions);


    }

    @Override public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        sessions--;
        System.out.println("Inside sessionDestroyed one session is deduct from counter,and sessions = " + sessions);
    }
}
