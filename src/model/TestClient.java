package model;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Selvin
 * on 28.07.2014.
 */
public class TestClient {
    private boolean isWorking = true;
    private boolean isReconfigured = true;
    private boolean isConnected = false;
    private String serviceIP = "127.0.0.1";
    private int port = 1234;
    private int rCount = 1, wCount = 1, startId = 1, endId = 20;
    private List<Integer> idList;
    private String configPath = "config\\config.txt";
    private File config;
    private FileWriter fw;
    private FileReader fr;
    private BufferedWriter bw;
    private BufferedReader br;
    private UserInterface ui;

    // Socket
    private Socket clientSocket;
    BufferedReader msgFromSrv;
    PrintWriter msgToSrv;

    public TestClient() {
        ui = new UserInterface(this);
        try {
            config = new File(configPath);
            File dir = config.getParentFile();
            if (!config.exists()) {
                dir.mkdir();
                config.createNewFile();
                fw = new FileWriter(config, true);
                bw = new BufferedWriter(fw);
                bw.write(String.valueOf(rCount));
                bw.newLine();
                bw.write(String.valueOf(wCount));
                bw.newLine();
                bw.write(String.valueOf(startId));
                bw.newLine();
                bw.write(String.valueOf(endId));
                bw.newLine();
                bw.write(serviceIP);
                bw.newLine();
                bw.write(String.valueOf(port));
                bw.close();
                fw.close();
            }
            else {
                fr = new FileReader(configPath);
                br = new BufferedReader(fr);
                rCount = Integer.parseInt(br.readLine());
                wCount = Integer.parseInt(br.readLine());
                startId = Integer.parseInt(br.readLine());
                endId = Integer.parseInt(br.readLine());
                serviceIP = br.readLine();
                port = Integer.parseInt(br.readLine());
                br.close();
                br.close();
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ui.showConfig(this);
        ui.showCommands();
        idList = new ArrayList<>(endId-startId);
        for(int i = startId; i <= endId; i++) {
            idList.add(i);
        }
        new DaemonUIListener(this, ui);
    }

    public void connect() {
        if(!isConnected) {
            try {
                ui.show("Connecting with service...");
                clientSocket = new Socket(serviceIP, port);
                msgToSrv = new PrintWriter(clientSocket.getOutputStream(), true);
                msgFromSrv = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                isConnected = true;
                ui.show("Client connected to service!");
            } catch (Exception e) {
                ui.show("Unable to connect to service!\n" + e.getMessage());
                isConnected = false;
            }
        }
        else ui.show("Client already connected.");
    }

    public void disconnect() {
        if(isConnected) {
            try {
                ui.show("Disconnecting from service...");
                msgToSrv.close();
                msgFromSrv.close();
                clientSocket.close();
                isConnected = false;
                ui.show("Client disconnected from service!");
            } catch (Exception e) {
                ui.show("Cannot disconnect from service!\n" + e.getMessage());
                isConnected = true;
            }
        }
        else ui.show("Client already disconnected.");
    }

    public void setRCount(int rCount) {
        ui.changeConfig();
        this.rCount = rCount;
        overwrite();
        ui.showConfig(this);
        reconfiguration();
    }

    public void setWCount(int wCount) {
        ui.changeConfig();
        this.wCount = wCount;
        overwrite();
        ui.showConfig(this);
        reconfiguration();
    }

    public void setIdList(int startId, int endId) {
        ui.changeConfig();
        this.startId = startId;
        this.endId = endId;
        idList.clear();
        for(int i = startId; i <= endId; i++) {
            idList.add(i);
        }
        overwrite();
        ui.showConfig(this);
        reconfiguration();
    }

    public void setIp(String serviceIP) {
        ui.changeConfig();
        this.serviceIP = serviceIP;
        overwrite();
        ui.showConfig(this);
        reconfiguration();
    }

    public void setPort(int port) {
        ui.changeConfig();
        this.port = port;
        overwrite();
        ui.showConfig(this);
        reconfiguration();
    }

    public synchronized void getAmount(Integer id) {
        ui.show("Client: get@" + id);
        msgToSrv.println("get@" + id);
        try {
            String str = msgFromSrv.readLine();
            if(str.substring(0, str.lastIndexOf("@")).equals("Service: get@" + id)) {
                ui.show(str);
            }
        } catch (IOException e) {
            ui.show("Cannot receive message from service!");
        }
    }

    public synchronized void addAmount(Integer id, Long value) {
        ui.show("Client: add@" + id + "@" + value);
        msgToSrv.println("add@" + id + "@" + value);
    }

    public synchronized void stAdd() {
        new Thread() {
            @Override
            public void run() {
                msgToSrv.println("stat@add");
                try {
                    String str = msgFromSrv.readLine();
                    ui.show(str.substring(str.lastIndexOf("@") + 1));
                } catch (IOException e) {
                    ui.show("Cannot receive message from service!");
                }
            }
        }.start();

    }

    public synchronized void stGet() {
        new Thread() {
            @Override
            public void run() {
                msgToSrv.println("stat@get");
                try {
                    String str = msgFromSrv.readLine();
                    ui.show(str.substring(str.lastIndexOf("@") + 1));
                } catch (IOException e) {
                    ui.show("Cannot receive message from service!");
                }
            }
        }.start();
    }

    public synchronized void resetSt() {
        msgToSrv.println("stat@reset");
        ui.show("Resetting of service statistic.");
    }

    public boolean isWorking() {
        return isWorking;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean isReconfigured() {
        return isReconfigured;
    }

    public void createThreads() {
        if(isConnected) {
            isReconfigured = false;
            for (int i = 0; i < rCount; i++) {
                new Reader(this, startId+(i*(endId-startId+1)/rCount), startId+(i+1)*(endId-startId+1)/rCount-1);
            }
            ui.show(rCount + " readers created.");
            for (int i = 0; i < wCount; i++) {
                new Writer(this, startId+(i*(endId-startId+1)/wCount), startId+(i+1)*(endId-startId+1)/wCount-1);
            }
            ui.show(wCount + " writers created.");
        }
        else ui.show("Client not connected to service!");
    }

    public void stop() {
        if(isConnected) disconnect();
        ui.closing();
        isWorking = false;
    }

    public int getRCount() {
        return rCount;
    }

    public int getWCount() {
        return wCount;
    }

    public int getStartId() {
        return startId;
    }

    public int getEndId() {
        return endId;
    }

    public String getIp() {
        return serviceIP;
    }

    public int getPort() {
        return port;
    }

    private void overwrite() {
        try {
            fw = new FileWriter(configPath);
            bw = new BufferedWriter(fw);
            bw.write(String.valueOf(this.rCount));
            bw.newLine();
            bw.write(String.valueOf(this.wCount));
            bw.newLine();
            bw.write(String.valueOf(this.startId));
            bw.newLine();
            bw.write(String.valueOf(this.endId));
            bw.newLine();
            bw.write(serviceIP);
            bw.newLine();
            bw.write(String.valueOf(port));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reconfiguration() {
        isReconfigured = true;
        if(isConnected) {
            disconnect();
            connect();
        }
    }

}
