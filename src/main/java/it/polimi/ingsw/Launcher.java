package it.polimi.ingsw;

public class Launcher {
    public static void main(String[] args) {
        if(args.length == 0){
            //no args => gui
            new Thread(() -> {
                javafx.application.Application.launch(GuiApp.class);
            }).start();
        }else if(args.length == 1){
            if(args[0].equals("-cli")){
                //run cli
                CliApp cli = new CliApp();
                cli.run();
            }else
                printHelp();
        }else if(args.length == 2){
            if(args[0].equals("-server")){
                //read the port
                int port = 0;
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e){
                    System.out.println("The second argument must be a number !");
                    printHelp();
                    return;
                }
                if(port < 1024 || port > 65535){
                    System.out.println("The port must be a number in range [1024, 65535] !");
                    printHelp();
                    return;
                }
                //run server
                ServerApp server = new ServerApp();
                server.run(port);
            }else
                printHelp();
        }else
            printHelp();

    }
    public static void printHelp(){
        System.out.println("******SANTORINI LAUNCHER******");
        System.out.println("gui - no arguments");
        System.out.println("cli - 1 argument \"-cli\"");
        System.out.println("server - 2 arguments \"-server\" <number of port>");
    }
}