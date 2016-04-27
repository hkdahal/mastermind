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

    // an array of colors
    private String[] colors =
            {"black", "white", "blue", "yellow", "red", "green"};

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


        // User input bar at the bottom
        VBox userInput = new VBox();
        this.userInputBar = makeUserInputBar();
        Text info = new Text("  Click below to input guesses.");
        info.setTextAlignment(TextAlignment.CENTER);
        userInput.getChildren().addAll(info, userInputBar);
        userInput.setPadding(new Insets(0,10,25,100));
        border.setBottom(userInput);

        primaryStage.setResizable(false);   // not resizable
        primaryStage.setScene(new Scene(border));
        primaryStage.setTitle("Mastermind Game");   // title
        primaryStage.show();
    }


    /**
     * Makes a grid and returns it
     * Contains all the pegs and the buttons
     * @return a grid with everything filled in
     */
    private GridPane makeNewGridPane(){
        GridPane a_grid = new GridPane();
        int gap = 2;
        a_grid.setVgap(gap);
        a_grid.setHgap(gap);

        // making solution buttons first
        a_grid.add(this.makeSolutionBar(), 2,0);

        // making MAX_GUESSES HBoxes to hold buttons inside them
        for ( int r = 1; r < MastermindModel.MAX_GUESSES + 1; ++r ) {
            a_grid.add(makePegList(), 0, r);

            HBox button_box = new HBox(2.5);
            for ( int c = 0; c < MastermindModel.CODE_LENGTH; ++c ) {
                Button btn = new Button();
                btn = this.makeRandomButton(btn);
                button_box.getChildren().add(btn);
            }
            a_grid.add(button_box, 2, r);
        }
        // spaces around the grid
        a_grid.setPadding(new Insets(40, 40, 40, 50));

        return a_grid;
    }


    // this method makes and returns a solution bar
    // contains the buttons with solution button properties
    private HBox makeSolutionBar(){
        HBox button_box = new HBox(2.5);
        for (int i = 0; i < 4; i++) {
            Button btn = new Button();
            btn = makeRandomButton(btn);
            button_box.getChildren().add(btn);
            //theGrid.add(makeRandomButton(btn), c, 0 );
        }
        return button_box;
    }


    // this method helps update the properties of a general button
    private Button makeRandomButton(Button btn){
        btn.setMinSize( 40, 40 );
        btn.setId("0");
        btn.setDisable(false);
        btn.setStyle("-fx-background-color: lightgray");
        return btn;
    }

    // this method creates all the clues-displaying pegs
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

    // this method creates the user input bar from where user guesses
    // changes the properties of button as user inputs
    // has Event handling
    private HBox makeUserInputBar(){
        HBox the_slide = new HBox(2);
        for (int i = 0; i < MastermindModel.CODE_LENGTH; i++) {
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

    // this method resets the user input bar buttons
    // updates to default properties (colors and IDs)
    private void resetUserInputBar() {

        int i = 0;
        for (Node btn: userInputBar.getChildren()){
            btn.setAccessibleText("" + i);
            i++;
            btn.setStyle("-fx-background-color: lightgray");
            btn.setId("0");
            user_gueses = new ArrayList<>(Arrays.asList(0,0,0,0));
        }
        this.guessBtn.setDisable(true);
    }

    /**
     * Making a pane that consists the buttons (VBOX)
     * Events are handled as each button is clicked
     * @return a pane
     */
    private Pane makeButtonList(){

        Pane pane = new VBox(10);

        //Insets PADDING = new Insets(0, 0, 50, 0);

        Button resetBtn = new Button("New Game");
        resetBtn.setOnAction(event -> newGameEvent());  // Event handled

        this.peekBtn = new Button("Peek");
        peekBtn.setOnAction(event -> this.model.peek()); // Event handled

        this.guessBtn = new Button("Guess");
        guessBtn.setDisable(true);    // initially the Guess button is disabled
        guessBtn.setOnAction(event -> guessBtnEvent());  // Event handled


        // adding all the buttons to the pane
        pane.getChildren().addAll(resetBtn, peekBtn, guessBtn);
        pane.setPadding(new Insets(70, 20, 0, 0));
        return pane;
    }

    // this event handling when a button is clicked
    // mainly this comes up for the user input bar
    private void btnClickedEvent(Button btn) {

        int i = Integer.parseInt(btn.getId()) + 1;
        if (i > 6){
            i = 1;
        }
        int col_index = i-1;
        colorTheButton(col_index, btn);
        btn.setId("" + i);

        int index = Integer.parseInt(btn.getAccessibleText());
        user_gueses.set(index, i);
        if (!user_gueses.contains(0)){
            guessBtn.setDisable(false);
        }else{
            guessBtn.setDisable(true);
        }
    }


    /**
     * Handles things that happen when a "New Game" is clicked
     * First, resets the model.
     * Then, resets the user_guesses
     * updates the Peek Button text and enables that button.
     * Then, resets the user input
     *
     */
    private void newGameEvent(){
        this.model.reset();
        user_gueses = new ArrayList<>(Arrays.asList(0,0,0,0));
        this.peekBtn.setText("Peek");
        this.peekBtn.setDisable(false);
        resetUserInputBar();
    }

    /**
     * Handles the events when "Guess" button is clicked.
     * Feeds the model's setFullGuessRow with the user's guesses.
     * Makes the guess with model's makeGuess
     * If the user wins or loses, "Peek" and "Guess" buttons are disabled,
     * and resets the user input bar.
     */
    private void guessBtnEvent() {
        this.model.setFullGuessRow(user_gueses);
        this.model.makeGuess();
        if (this.model.getVictoryStatus() ||
                this.model.getRemainingGuesses() == 0){
            peekBtn.setDisable(true);
            guessBtn.setDisable(true);
        }
        resetUserInputBar();
    }


    @Override
    public void update(Observable o, Object arg) {
        assert o == this.model: "Unexpected subject of observation";
        displayGame();
    }

    // displays the game calling display helper methods
    private void displayGame() {
        displayBoard();
        displayMessage();

    }

    // update the display message
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

    /**
     * This helper method updates the display of the board.
     * First, retrieves the data from the model: Solution, ClueData, GuessData.
     * Then, updates the board by catching each child and updating its
     * properties as needed.
     * Inside the HBox, it changes the colors of the buttons as per the
     * guesses data. Inside the VBox, where the pegs (circles) are, the
     * colors of the circles are changed.
     * A little logic is tweaked for the solution display by looking at the
     * contents of the retrieved solution data.
     */
    private void displayBoard() {
        ArrayList<Integer> sol = this.model.getSolution();
        ArrayList<Character> clues = this.model.getClueData();
        ArrayList<Integer> guesses = this.model.getGuessData();
        System.out.println(sol);
        System.out.println(clues);
        System.out.println(guesses);


        int i = 0; // guesses index
        int j = 0; // clues index

        for (int k = 20; k > 0; k--) {
            Node aNode = this.grid.getChildren().get(k);
            if (aNode instanceof HBox){
                for (Node the_btn: ((HBox) aNode).getChildren()) {
                    int guessed_int = guesses.get(i);

                    if (guessed_int > 0) {
                        int col_index = guessed_int - 1;
                        colorTheButton(col_index, (Button)the_btn);
                    }else{
                        the_btn.setId("0");
                        the_btn.setStyle("-fx-background-color: lightgray");
                    }
                    i++;
                }
            }else if (aNode instanceof VBox){
                for (Node h_box: ((VBox) aNode).getChildren()){
                    if (h_box instanceof HBox){
                        for (Node cl_circ: ((HBox) h_box).getChildren()){
                            if (cl_circ instanceof Circle){
                                if (clues.get(j).equals('B')){
                                    ((Circle) cl_circ).setFill(Color.BLACK);
                                }else if (clues.get(j).equals('W')){
                                    ((Circle) cl_circ).setFill(Color.WHITE);
                                }else{
                                    ((Circle) cl_circ).setFill(Color.LIGHTGRAY);
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
                    int col_index = sol.get(m) - 1;
                    colorTheButton(col_index, (Button)the_btn);
                    m++;
                }
            }
        }
    }


    /**
     * Given the color index and a button, this helper method colors that
     * button.
     * @param col_index - color index for colors array
     * @param the_btn - the button to be updated
     */
    private void colorTheButton(int col_index, Button the_btn){
        String the_color = this.colors[col_index];
        String color_it = "-fx-background-color: " +
                the_color + ";";
        the_btn.setStyle(color_it);

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
