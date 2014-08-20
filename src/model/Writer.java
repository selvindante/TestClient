package model;

import java.util.Random;

/**
 * Created by Selvin
 * on 28.07.2014.
 */
public class Writer {

    public Writer(final TestClient tc, final int startId, final int endId) {
        new Thread() {
            @Override
            public void run() {
                Random r = new Random();
                int i = startId, j;
                while(tc.isConnected() || tc.isReconfigured()) {
                    try {
                        Thread.sleep(3000 + r.nextInt(7000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    j = r.nextInt(endId - startId + 1);
                    boolean sign = r.nextBoolean();
                    long value = r.nextInt(100);
                    if(!sign) value = value*(-1);
                    if(j < 0) j = j*(-1);
                    tc.addAmount(i + j, value);
                }
            }
        }.start();
    }
}
