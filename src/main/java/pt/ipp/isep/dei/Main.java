package pt.ipp.isep.dei;

import pt.ipp.isep.dei.controller.global.SaveDataBaseController;
import pt.ipp.isep.dei.data.config.ActualDatabaseConnection;
import pt.ipp.isep.dei.data.config.ConnectionFactory;
import pt.ipp.isep.dei.data.config.DatabaseDropper;

public class Main {

    public static void main(String[] args) throws Exception {
        ConnectionFactory.getInstance().getDatabaseConnection();
        ActualDatabaseConnection.getDb().getConnection().setAutoCommit(false);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                new SaveDataBaseController().run();
                ActualDatabaseConnection.getDb().getConnection().commit();
                ActualDatabaseConnection.getDb().getConnection().close();
                System.out.println("Data saved successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        try {
            MainMenuApp menu = new MainMenuApp();
            menu.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}