package model;

/**
 * Created by Selvin
 * on 28.07.2014.
 */
public class UserInterface {

    public UserInterface(TestClient tc) {
        System.out.println("Test client started.");
    }

    public void show() {
        System.out.println();
    }

    public void show(String str) {
        System.out.println(str);
    }

    public void showConfig(TestClient tc) {
        System.out.println( "Configuration:\n" +
                "readers - " + tc.getRCount() +
                ", writers - " + tc.getWCount() +
                ", ID list range from " + tc.getStartId() + " to " + tc.getEndId() +
                ", service ip - " + tc.getIp() +
                ", service port - " + tc.getPort() +
                ".");
    }

    public void showCommands() {
        System.out.println( "Service commands:\n" +
                "\"help\" - show commands of client,\n" +
                "\"conf\" - show configuration of client,\n" +
                "\"cnct\" - connect to server,\n" +
                "\"run\" - create threads, " +
                "\"dscnct\" - stop threads and disconnect client,\n" +
                "\"setr\" - set rCount,\n" +
                "\"setw\" - set wCount,\n" +
                "\"setrng\" - set range of idList,\n" +
                "\"setip\" - set IP of service,\n" +
                "\"setport\" - set port of service,\n" +
                "\"stadd\" - get statistic for getAmount() method,\n" +
                "\"stget\" - get statistic for addAmount() method,\n" +
                "\"rstst\" - reset statistic of service,\n" +
                "\"exit\" - close client" +
                ".");
    }

    public void changeConfig() {
        System.out.println("Changing configuration...");
    }

    public void closing() {
        System.out.println("Closing of client...");
    }
}
