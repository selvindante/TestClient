package model;

import java.util.Scanner;

/**
 * Created by Selvin
 * on 28.07.2014.
 */
public class DaemonUIListener {
    private Scanner input = new Scanner(System.in);

    public DaemonUIListener(final TestClient tc, final UserInterface ui) {
        Thread dl = new Thread() {
            @Override
            public void run() {
                while(tc.isWorking()) {
                    switch (input.nextLine()) {
                        case "help":
                            ui.showCommands();
                            break;
                        case "conf":
                            ui.showConfig(tc);
                            break;
                        case "cnct":
                            tc.connect();
                            break;
                        case "run":
                            ui.show("Creating of threads...");
                            tc.createThreads();
                            break;
                        case "dscnct":
                            tc.disconnect();
                            break;
                        case "setr":
                            ui.show("Set count of readers:");
                            tc.setRCount(Integer.parseInt(input.nextLine()));
                            break;
                        case "setw":
                            ui.show("Set count of writers:");
                            tc.setWCount(Integer.parseInt(input.nextLine()));
                            break;
                        case "setrng":
                            ui.show("Set first value of range:");
                            int first = Integer.parseInt(input.nextLine());
                            ui.show("Set latest value of range:");
                            tc.setIdList(first, Integer.parseInt(input.nextLine()));
                            break;
                        case "setip":
                            ui.show("Set IP of service:");
                            tc.setIp(input.nextLine());
                            break;
                        case "setport":
                            ui.show("Set port of service:");
                            tc.setPort(Integer.parseInt(input.nextLine()));
                            break;
                        case "stadd":
                            tc.stAdd();
                            break;
                        case "stget":
                            tc.stGet();
                            break;
                        case "rstst":
                            tc.resetSt();
                            break;
                        case "exit":
                            tc.stop();
                            break;
                        default:
                            ui.show("Unknown command. Use \"help\" for list of commands.");
                    }

                }
            }
        };
        dl.setDaemon(true);
        dl.start();
    }
}
