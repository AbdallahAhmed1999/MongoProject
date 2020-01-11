package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilController {

    public static void changeScene(String fileName,String title) throws IOException {
        Parent root = FXMLLoader.load(UtilController.class.getResource("..\\view\\"+fileName+".fxml"));
        Scene scene = new Scene(root);
        Stage stage = Main.getStage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth())/2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public static String md5(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
