import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.Observable;
import java.util.Observer;

/**
 * Author:  Hari Dahal
 * File: MastermindGraphicalVC.java
 * Created on 4/13/2016
 */

class MastermindGraphicalVC extends Application implements Observer{

    private MastermindModel model;
    private TextField displayThings;

    @Override
    public void init(){
        this.model = new MastermindModel();
        model.addObserver(this);
        //super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowPane pane = new FlowPane();

        Button btn = new Button("Click me!");
        //btn.setOnAction(event -> model.);
        this.displayThings = new TextField("Displaying...");
        displayThings.setEditable(false);

        pane.getChildren().addAll(btn, displayThings);
        primaryStage.setScene(new Scene(pane));
        primaryStage.setTitle("Mastermind Game");

        primaryStage.show();

    }


    /*private void playGame(){
        doTheThing();
    }

    private void doTheThing() {
        System.out.println("Doing the thing...");
        // the main command loop kind
    }*/

    @Override
    public void update(Observable o, Object arg) {
        assert o == this.model: "Unexpected subject of observation";
        displayThings.setText("Displaying....");
        //displayGame();
    }

    /*private void displayGame() {
        displayBoard();
        displayMessage();
        displayGuessStatus();
    }

    private void displayGuessStatus() {
        System.out.println("Guess status");
        // connecting graphical interface and model
    }

    private void displayMessage() {
        System.out.println("Message!");
        // will display message with this connecting to model
    }

    private void displayBoard() {
        System.out.println("This is UI board!!!");
    }
*/
    /**
     * The main method used to play a game.
     *
     * @param args Command line arguments -- unused
     */
    public static void main(String[] args) {
        //game.playGame();
        Application.launch(args);
    }
}
