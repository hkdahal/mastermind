import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.util.*;


/**
 * Author:  Hari Dahal
 * File: MastermindGraphicalVC.java
 * Created on 4/13/2016
 */

public class MastermindGraphicalVC extends Application implements Observer{


    // different atrributes to keep track of
    private MastermindModel model;  // the model
    private Text statusMessage;     // the status message
    private GridPane grid;          // the grid (the game)
    private Button peekBtn;         // the peek button
    private Button guessBtn;        // the guess button
    private Stage the_stage;

    // an array of colors
    private String[] colors = {"black", "white", "blue", "yellow", "red",
            "green"};

    // initial guesses of user
    private ArrayList<Integer> user_gueses = new ArrayList<>(Arrays.asList(0,
            0,0,0));

    private HBox userInputBar;  // a HBox bar from which user inputs guesses

    @Override
    public void init(){
        this.model = new MastermindModel();
        model.addObserver(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane border = new BorderPane();   // root Node
        Pane text_box = new HBox();             // HBox to store the text

        this.statusMessage = new Text("You have " + model.getRemainingGuesses
                () + " guesses remaining.");


        text_box.getChildren().add(statusMessage);
        text_box.setPadding(new Insets(10, 0, 0, 10));

        border.setTop(text_box);        // Text at the top

        this.grid = makeNewGridPane();

        border.setCenter(grid);         // the Grid at the Center

        // the User control buttons (new game, peek, guess) at the Right
        border.setRight(this.makeButtonList());

        this.userInputBar = makeUserInputBar();

        Text info = new Text("  Click below to input guesses.");
        info.setTextAlignment(TextAlignment.CENTER);


        VBox userInput = new VBox();
        userInput.getChildren().addAll(info, userInputBar);
        userInput.setPadding(new Insets(0,10,25,100));
        //userInput.setPadding(new Insets(0,0,10,0));
        //userInputBar.getChildren().add(info);
        // user input bar at the bottom
        border.setBottom(userInput);

        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(border));
        primaryStage.setTitle("Mastermind Game");
        primaryStage.show();
        this.the_stage = primaryStage;
    }


    private GridPane makeNewGridPane(){
        GridPane a_grid = new GridPane();
        int gap = 2;
        a_grid.setVgap(gap);
        a_grid.setHgap(gap);

        //this.makeSolutionButton(a_grid);

        //a_grid.getChildren().add(this.makeSolutionButton());

        a_grid.add(this.makeSolutionButton(), 2,0);
        for ( int r = 1; r < 11; ++r ) {
            a_grid.add(makePegList(), 0, r);

            HBox button_box = new HBox(2.5);
            for ( int c = 0; c < 4; ++c ) {
                Button btn = new Button();
                btn = this.makeRandomButton(btn);
                //a_grid.add(btn, c, r );
                button_box.getChildren().add(btn);
            }
            a_grid.add(button_box, 2, r);
        }

        a_grid.setGridLinesVisible(false);
        a_grid.setPadding(new Insets(40, 40, 40, 50));

        return a_grid;
    }


    private HBox makeSolutionButton(){
        HBox button_box = new HBox(2.5);
        for (int i = 0; i < 4; i++) {
            Button btn = new Button();
            btn = makeRandomButton(btn);
            btn.setId("Solution Button");
            button_box.getChildren().add(btn);
            //theGrid.add(makeRandomButton(btn), c, 0 );
        }
        return button_box;
    }

    private Button makeRandomButton(Button btn){
        btn.setMinSize( 40, 40 );
        btn.setId("0");
        btn.setDisable(false);
        btn.setStyle("-fx-background-color: lightgray");
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

        int index = Integer.parseInt(btn.getAccessibleText());
        user_gueses.set(index, i);
        if (!user_gueses.contains(0)){
            guessBtn.setDisable(false);
        }
    }

    private Pane makePegList() {
        Pane pane = new VBox();

        for (int i = 0; i <2; i++) {
            Pane h_pane = new HBox();
            for (int j = 0; j < 2; j++) {
                Circle clue_circle = new Circle(10);
                clue_circle.setFill(Color.LIGHTGRAY);
                h_pane.getChildren().add(clue_circle);
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
        peekBtn.setOnAction(event -> this.model.peek());

        this.guessBtn = new Button("Guess");
        guessBtn.setDisable(true);
        guessBtn.setOnAction(event -> guesBtnEvent());

        //Button exitBtn = new Button("Exit");
        //exitBtn.setStyle("-fx-background-color: red");
        //exitBtn.setOnAction(event -> the_stage.close());

        pane.getChildren().addAll(resetBtn, peekBtn, guessBtn);
        pane.setPadding(new Insets(70, 20, 0, 0));
        return pane;
    }


    private void newGameEvent(){
        this.model.reset();
        user_gueses = new ArrayList<>(Arrays.asList(0,0,0,0));
        this.peekBtn.setText("Peek");
        this.peekBtn.setDisable(false);
        for (Node aNode: this.grid.getChildren()){
            if (aNode instanceof VBox){
                //System.out.println(((VBox) aNode).getChildren());
                for (Node h_box: ((VBox) aNode).getChildren()){
                    if (h_box instanceof HBox){
                        //System.out.println(((HBox) h_box).getChildren());
                        for (Node cl_circ: ((HBox) h_box).getChildren()){
                            if (cl_circ instanceof Circle){
                                ((Circle) cl_circ).setFill(Color.LIGHTGRAY);

                            }
                        }
                    }
                }
            }else if (aNode instanceof HBox){
                for (Node the_btn: ((HBox) aNode).getChildren()) {
                    the_btn.setId("0");
                    the_btn.setStyle("-fx-background-color: lightgray");
                }
            }
        }
        resetUserInputBar();
    }

    private void guesBtnEvent() {
        this.model.setFullGuessRow(user_gueses);
        this.model.makeGuess();
        if (this.model.getVictoryStatus()){
            //peekEvent();
            peekBtn.setDisable(true);
            guessBtn.setDisable(true);
        }else if (this.model.getRemainingGuesses() == 0){
            peekBtn.setDisable(true);
        }
        resetUserInputBar();
    }

    private void resetUserInputBar() {

        for (Node btn: this.userInputBar.getChildren()){
            if (btn instanceof Button){
                btn.setId("0");
                btn.setStyle("-fx-background-color: lightgray");
            }
        }
        this.guessBtn.setDisable(true);
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
            statusMessage.setText("You cracked the code!!");
        } else if (this.model.getRemainingGuesses() == 0){
            statusMessage.setText("You ran out of guesses!!");
        } else {
            statusMessage.setText("You have " + model.getRemainingGuesses
                    () + " guesses remaining.");
        }
    }

    private HBox makeUserInputBar(){
        HBox the_slide = new HBox(2);
        for (int i = 0; i < 4; i++) {
            Button btn = new Button();
            btn.setMinSize( 40, 40 );
            btn.setId("0");
            btn.setAccessibleText("" + i);
            btn.setStyle("-fx-background-color: lightgray");
           // btn.setPadding(new Insets(0,10,30,10));
            btn.setOnAction(event -> btnClickedEvent(btn));
            the_slide.getChildren().add(btn);
        }
        the_slide.setPadding(new Insets(5,0,0,0));
        return the_slide;
    }


    private void displayBoard() {
        ArrayList<Integer> sol = this.model.getSolution();
        ArrayList<Character> clues = this.model.getClueData();
        ArrayList<Integer> guesses = this.model.getGuessData();

        /*System.out.println("Solution: " + sol);
        System.out.println("User_Guesses: " + user_gueses);
        System.out.println("Clues: " + clues);
        System.out.println("GuessesData: " + guesses);*/


        int i = 0;
        int j = 0;
        //System.out.println("Size: " + this.grid.getChildren().size());
        //System.out.println(this.grid.getChildren());
        //for (Node aNode: this.grid.getChildren()){

        for (int k = 20; k > 0; k--) {
            Node aNode = this.grid.getChildren().get(k);
            if (aNode instanceof HBox){
                for (Node the_btn: ((HBox) aNode).getChildren()) {
                    int guessed_int = guesses.get(i);
                    //System.out.println(guessed_int);

                    if (guessed_int > 0) {
                        String the_color = this.colors[guessed_int - 1];
                        String color_it = "-fx-background-color: " +
                                the_color + ";";
                        the_btn.setStyle(color_it);
                    }
                    i++;
                }
            }else if (aNode instanceof VBox){
                for (Node h_box: ((VBox) aNode).getChildren()){
                    if (h_box instanceof HBox){
                        //System.out.println(((HBox) h_box).getChildren());
                        for (Node cl_circ: ((HBox) h_box).getChildren()){
                            if (cl_circ instanceof Circle){
                                //r_bt.setStyle("-fx-background-color: aqua");
                                if (clues.get(j).equals('B')){
                                    ((Circle) cl_circ).setFill(Color.BLACK);
                                }else if (clues.get(j).equals('W')){
                                    ((Circle) cl_circ).setFill(Color.WHITE);
                                }
                                j++;

                            }
                        }
                    }
                }
            }
        }

        HBox the_solution_box;

        if (this.grid.getChildren().get(0) instanceof HBox) {
            the_solution_box = (HBox) this.grid.getChildren().get(0);
            if (sol.contains(0)) {
                this.peekBtn.setText("Peek");
                for (Node the_btn: the_solution_box.getChildren()){
                    the_btn.setStyle("-fx-background-color: lightgray");
                }
            }else{
                this.peekBtn.setText("(Un) Peek");
                int m = 0;
                for (Node the_btn: the_solution_box.getChildren()){
                    the_btn.setStyle("-fx-background-color: lightgray");
                    String bg_color = colors[sol.get(m)-1] + ";";
                    String color_it = "-fx-background-color: " + bg_color;
                    the_btn.setStyle(color_it);
                    m++;
                }

            }
        }
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
