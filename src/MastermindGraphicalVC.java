import java.util.Observable;
import java.util.Observer;

/**
 * Author:  Hari Dahal
 * File: MastermindGraphicalVC.java
 * Created on 4/13/2016
 */

class MastermindGraphicalVC implements Observer{

    private MastermindModel model;

    public MastermindGraphicalVC(){
        this.model = new MastermindModel();
        this.model.addObserver(this);

    }

    private void playGame(){
        doTheThing();
    }

    private void doTheThing() {
        System.out.println("Doing the thing...");
        // the main command loop kind
    }

    @Override
    public void update(Observable o, Object arg) {
        displayGame();
    }

    private void displayGame() {
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

    /**
     * The main method used to play a game.
     *
     * @param args Command line arguments -- unused
     */
    public static void main(String[] args) {
        MastermindGraphicalVC game = new MastermindGraphicalVC();
        game.playGame();
    }
}
