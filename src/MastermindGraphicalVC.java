import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Author:  Hari Dahal
 * File: MastermindGraphicalVC.java
 * Created on 4/13/2016
 */

public class MastermindGraphicalVC extends Application implements Observer{

    private MastermindModel model;
    private Text statusMessage;
    private GridPane grid;
    private Button peekBtn;

    @Override
    public void init(){
        this.model = new MastermindModel();
        model.addObserver(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane border = new BorderPane();
        Pane text_box = new HBox();

        this.statusMessage = new Text("You have " + model.getRemainingGuesses
                () + " guesses remaining.");


        text_box.getChildren().add(statusMessage);
        text_box.setPadding(new Insets(10, 0, 0, 10));

        border.setTop(text_box);

        this.grid = new GridPane();
        int gap = 2;
        grid.setVgap(gap);
        grid.setHgap(gap);

        this.makeSolutionButton();

        for ( int r = 1; r < 10; ++r ) {
            grid.add(makePegList(), 0, r);
            for ( int c = 2; c < 6; ++c ) {

                grid.add( this.makeRandomButton(), c, r );
            }
        }

        grid.setGridLinesVisible(false);
        grid.setPadding(new Insets(40, 40, 50, 50));

        border.setCenter(grid);

        border.setRight(this.makeButtonList());

        //border.getChildren().addAll(btn);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(border));
        primaryStage.setTitle("Mastermind Game");
        primaryStage.show();

    }


    private void makeSolutionButton(){
        for (int c = 2; c < 6; c++) {
            Button btn = new Button();
            btn.setMinSize(40, 40);
            grid.add(btn, c, 0 );
        }
    }


    private Button makeRandomButton(){
        Button btn = new Button();
        // setting max size causes buttons to fill the cell.
        btn.setMinSize( 40, 40 );



        //btn.setBackground(Color.);

        /*Random rand = new Random();
        String randomize_color = colors[rand.nextInt(model.UNIQUE_SYMBOLS)];
        String bg_color = "-fx-background-color: " + randomize_color;

        btn.setStyle("-fx-background-radius: 5,4,3,5;");
        btn.setStyle(bg_color);*/
        return btn;
    }

    private Pane makePegList() {
        Pane pane = new VBox();

        for (int i = 0; i <2; i++) {
            Pane h_pane = new HBox();
            for (int j = 0; j < 2; j++) {
                RadioButton rbutton = new RadioButton();
                //rbutton.setMinHeight(23);

                h_pane.getChildren().add(rbutton);
            }
            pane.getChildren().add(h_pane);
        }
        pane.setPadding(new Insets(0, 10, 0, 0));
        return pane;
    }

    /**
     * Making a pane that consists the buttons (VBOX)
     * @return a pane
     */
    private Pane makeButtonList(){

        Pane pane = new VBox(10);

        //Insets PADDING = new Insets(0, 0, 50, 0);

        Button resetBtn = new Button("New Game");
        resetBtn.setOnAction(event -> model.reset());

        this.peekBtn = new Button("Peek");
        peekBtn.setOnAction(event -> model.peek());

        Button  guessBtn = new Button("Guess");
        guessBtn.setOnAction(event -> model.makeGuess());

        pane.getChildren().addAll(resetBtn, peekBtn, guessBtn);
        pane.setPadding(new Insets(70, 20, 0, 0));
        return pane;
    }





    @Override
    public void update(Observable o, Object arg) {
        assert o == this.model: "Unexpected subject of observation";
        displayGame();
    }

    private void displayGame() {
        displayBoard();
        displayMessage();

    }

    private void displayMessage() {
        if (this.model.getVictoryStatus()){
            statusMessage.setText("You won the game!!");
        } else if (this.model.getRemainingGuesses() == 0){
            statusMessage.setText("You lost the game!!");
        } else {
            statusMessage.setText("You have " + model.getRemainingGuesses
                    () + " guesses remaining.");
        }
    }

    private void displayBoard() {
        ArrayList<Integer> sol = this.model.getSolution();
        ArrayList<Character> clues = this.model.getClueData();
        ArrayList<Integer> guesses = this.model.getGuessData();
        String[] colors = {"red", "green", "blue", "yellow", "white", "black"};
        for(int i = 0; i < MastermindModel.CODE_LENGTH; i++) {
            if (sol.get(i) > 0) {
                this.peekBtn.setText("(Un) Peek");
                Button btn = new Button();
                String bg_color = colors[sol.get(i)-1] + ";";
                String color_it = "-fx-background-color: " + bg_color;
                btn.setStyle(color_it);
                btn.setMinSize(40, 40);
                grid.add(btn, i+2, 0 );
            }else{
                makeSolutionButton();
                this.peekBtn.setText("Peek");
            }
        }
    }



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
