import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
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

        GridPane grid = new GridPane();
        int gap = 2;
        grid.setVgap(gap);
        grid.setHgap(gap);
        // add specifying both column and row.
        // javafx docs talk about 1-based columns and rows but...

        for (int c = 2; c < 6; c++) {
                grid.add( this.makeRandomButton(), c, 0 );
        }

        for ( int r = 1; r < 10; ++r ) {
            grid.add(makePegList(), 0, r);
            for ( int c = 2; c < 6; ++c ) {

                grid.add( this.makeRandomButton(), c, r );
            }
        }

        grid.setGridLinesVisible(false);
        grid.setPadding(new Insets(50, 40, 50, 50));

        border.setCenter(grid);

        border.setRight(this.makeButtonList());

        //border.getChildren().addAll(btn);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(border));
        primaryStage.setTitle("Mastermind Game");
        primaryStage.show();

    }

    private Button makeRandomButton(){
        Button btn = new Button();
        // setting max size causes buttons to fill the cell.
        btn.setMinSize( 40, 40 );
        //btn.setBackground(Color.);

        Random rand = new Random();
        Background bg = new Background(
                new BackgroundFill( Color.color(rand.nextDouble(),
                        rand.nextDouble(), rand.nextDouble()),
                        new CornerRadii(5), new Insets(0,1,1,0)) );
        btn.setBackground(bg);
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

        Button peekBtn = new Button("Peek");
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

        statusMessage.setText("You have " + model.getRemainingGuesses
                () + " guesses remaining.");

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
