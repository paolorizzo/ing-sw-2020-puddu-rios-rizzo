package it.polimi.ingsw;

public class ClientLauncher {
    public static void main(String[] args) {
        new Thread(() -> {
            javafx.application.Application.launch(GuiApp.class);
        }).start();
    }
}