package sample.Database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DBConnection {
    private static final String IP = "localhost";
    private static final int PORT = 27017;
    private static final String DB_NAME = "Clinic";

    private static DBConnection dbconnection;
    private static MongoClient client = new MongoClient(IP,PORT);
    private static MongoDatabase db = client.getDatabase(DB_NAME);

    private DBConnection(){}

    public static DBConnection getConnection(){
        if (dbconnection == null){
            dbconnection = new DBConnection();
        }
        return dbconnection;
    }

    public MongoDatabase getDb(){
        return db;
    }

}
