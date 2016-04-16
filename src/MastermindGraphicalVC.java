import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import javax.lang.model.type.NullType;
import java.lang.reflect.Array;
import java.util.*;


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
    private Button guessBtn;
    private String[] colors = {"black", "white", "blue", "yellow", "red",
            "green"};
    private ArrayList<Integer> fullGuess = new ArrayList<>();

    private ArrayList<Integer> user_gueses = new ArrayList<>(Arrays.asList(0,
            0,0,0));

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

        this.grid = makeNewGridPane();

        border.setCenter(grid);

        border.setRight(this.makeButtonList());

        //border.getChildren().addAll(btn);
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(border));
        primaryStage.setTitle("Mastermind Game");
        primaryStage.show();
    }


    private GridPane makeNewGridPane(){
        GridPane a_grid = new GridPane();
        int gap = 2;
        a_grid.setVgap(gap);
        a_grid.setHgap(gap);

        this.makeSolutionButton(a_grid);

        for ( int r = 1; r < 10; ++r ) {
            a_grid.add(makePegList(), 0, r);
            for ( int c = 2; c < 6; ++c ) {
                Button btn = this.makeRandomButton();
                if (r==9){
                    btn.setDisable(false);
                }
                a_grid.add(btn, c, r );
            }
        }

        a_grid.setGridLinesVisible(false);
        a_grid.setPadding(new Insets(40, 40, 50, 50));

        return a_grid;
    }


    private void makeSolutionButton(GridPane theGrid){
        for (int c = 2; c < 6; c++) {
            theGrid.add(makeRandomButton(), c, 0 );
        }
    }

    private Button makeRandomButton(){
        Button btn = new Button();
        btn.setMinSize( 40, 40 );
        btn.setId("0");
        btn.setDisable(true);
        btn.setOnAction(event -> btnClickedEvent(btn));
        return btn;
    }

    private void btnClickedEvent(Button btn) {

        int i = Integer.parseInt(btn.getId()) + 1;
        if (i > 6){
            i = 1;
        }
        String bg_color = colors[i-1] + ";";
        String color_it = "-fx-background-color: " + bg_color;
        btn.setStyle(color_it);
        btn.setId("" + i);

        int index = this.grid.getColumnIndex(btn);
        user_gueses.set(index-2, i);
        System.out.println(user_gueses);
        if (!user_gueses.contains(0)){
            guessBtn.setDisable(false);
        }
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
        resetBtn.setOnAction(event -> newGameEvent());

        this.peekBtn = new Button("Peek");
        peekBtn.setOnAction(event -> peekEvent());

        this.guessBtn = new Button("Guess");
        guessBtn.setDisable(true);
        guessBtn.setOnAction(event -> guesBtnEvent());

        Button updateBtn = new Button("Update");
        //updateBtn.setOnAction(event -> this.model.make);


        pane.getChildren().addAll(resetBtn, peekBtn, guessBtn);
        pane.setPadding(new Insets(70, 20, 0, 0));
        return pane;
    }


    private void newGameEvent(){
        this.model.reset();
        user_gueses = new ArrayList<>(Arrays.asList(0,0,0,0));
        for(int i = 0; i < MastermindModel.CODE_LENGTH; i++) {
            this.peekBtn.setText("Peek");
            Button temp_btn = new Button();
            Node btn = grid.getChildren().get(i); //new Button();
            btn.setStyle(temp_btn.getStyle());
        }
    }



    private void peekEvent(){
        this.model.peek();
        ArrayList<Integer> sol = this.model.getSolution();
        for(int i = 0; i < MastermindModel.CODE_LENGTH; i++) {
            if (sol.get(i) > 0) {
                this.peekBtn.setText("(Un) Peek");
                Node btn = grid.getChildren().get(i); //new Button();
                String bg_color = colors[sol.get(i)-1] + ";";
                String color_it = "-fx-background-color: " + bg_color;
                btn.setStyle(color_it);
                btn.setDisable(false);
            }else{
                this.peekBtn.setText("Peek");
                Button temp_btn = new Button();
                Node btn = grid.getChildren().get(i); //new Button();
                /*String bg_color = colors[sol.get(i)-1] + ";";
                String color_it = "-fx-background-color: " + bg_color;*/
                btn.setStyle(temp_btn.getStyle());
            }
        }

    }


    private void guesBtnEvent() {
        this.model.setFullGuessRow(user_gueses);
        this.model.makeGuess();
        if (this.model.getVictoryStatus()){
            peekEvent();
            peekBtn.setDisable(true);
            guessBtn.setDisable(true);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        assert o == this.model: "Unexpected subject of observation";
        displayGame();
    }

    private void displayGame() {
        //displayBoard();
        displayMessage();

    }

    private void displayMessage() {
        if (this.model.getVictoryStatus()){
            statusMessage.setText("You cracked the code!!");
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

    }



    /**
     * The main method used to play a game.
     *
     * @param args Command line arguments -- unused
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
