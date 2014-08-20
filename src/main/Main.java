package main;

import model.TestClient;

/**
 * Created by Selvin
 * on 28.07.2014.
 */
public class Main {
    public static void main(String[] args) {

        new Thread() {
            @Override
            public void run() {
                TestClient tc = new TestClient();
                while(tc.isWorking()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}
