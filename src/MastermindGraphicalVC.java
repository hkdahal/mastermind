import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;

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

    private HBox userInputBar;

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

        this.userInputBar = makeUserInputBar();

        border.setBottom(userInputBar);

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

        for ( int r = 1; r < 11; ++r ) {
            a_grid.add(makePegList(), 0, r);
            for ( int c = 2; c < 6; ++c ) {
                Button btn = new Button();
                btn = this.makeRandomButton(btn);
                a_grid.add(btn, c, r );
            }
        }

        a_grid.setGridLinesVisible(false);
        a_grid.setPadding(new Insets(40, 40, 50, 50));

        return a_grid;
    }


    private void makeSolutionButton(GridPane theGrid){
        for (int c = 2; c < 6; c++) {
            Button btn = new Button();
            btn.setId("Solution Button");
            theGrid.add(makeRandomButton(btn), c, 0 );
        }
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
        peekBtn.setOnAction(event -> peekEvent());

        this.guessBtn = new Button("Guess");
        guessBtn.setDisable(true);
        guessBtn.setOnAction(event -> guesBtnEvent());

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

            }else if (aNode instanceof Button){
                ((Button) aNode).setText("");
                aNode.setId("0");
                aNode.setStyle("-fx-background-color: lightgray");
            }
        }
        resetUserInputBar();
    }

    private void peekEvent(){
        this.model.peek();
        ArrayList<Integer> sol = this.model.getSolution();
    }


    private void guesBtnEvent() {
        this.model.setFullGuessRow(user_gueses);
        this.model.makeGuess();
        if (this.model.getVictoryStatus()){
            peekEvent();
            //peekBtn.setDisable(true);
            guessBtn.setDisable(true);
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
        the_slide.setPadding(new Insets(0,10,30,100));
        return the_slide;
    }


    private void displayBoard() {
        ArrayList<Integer> sol = this.model.getSolution();
        ArrayList<Character> clues = this.model.getClueData();
        ArrayList<Integer> guesses = this.model.getGuessData();

        System.out.println("Solution: " + sol);
        System.out.println("User_Guesses: " + user_gueses);
        System.out.println("Clues: " + clues);
        System.out.println("GuessesData: " + guesses);


        int i = 0;
        int j = 0;
        System.out.println("Size of children: " + this.grid.getChildren().size());
        //for (Node aNode: this.grid.getChildren()){
        for (int k = 53; k > 3; k--) {
            Node aNode = this.grid.getChildren().get(k);
            if (aNode instanceof Button) {
                if (!aNode.getId().equals("Solution Button")) {
                    int guessed_int = guesses.get(i);
                    //System.out.println(guessed_int);

                    if (guessed_int > 0) {
                        String the_color = this.colors[guessed_int - 1];
                        String color_it = "-fx-background-color: " +
                                the_color + ";";
                        ((Button) aNode).setText(" " + k);
                        aNode.setStyle(color_it);
                    }
                    i++;
                }
            }

            if (aNode instanceof VBox){
                //System.out.println(((VBox) aNode).getChildren());
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

        for(int m = 0; m < MastermindModel.CODE_LENGTH; m++) {
            if (sol.get(m) > 0) {
                this.peekBtn.setText("(Un) Peek");
                Node btn = grid.getChildren().get(m); //new Button();
                String bg_color = colors[sol.get(m)-1] + ";";
                String color_it = "-fx-background-color: " + bg_color;
                btn.setStyle(color_it);
                btn.setDisable(false);
            }else{
                this.peekBtn.setText("Peek");
                Node btn = grid.getChildren().get(m); //new Button();

                btn.setStyle("-fx-background-color: lightgray");
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
