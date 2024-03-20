import java.util.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.event.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import javafx.collections.*;
import class_lib.customer;
import class_lib.passworderror;
import class_lib.usererror;
import class_lib.*;
import javafx.beans.property.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import class_lib.bank;


//************************************************************************************************************************************************************ */
//outer event handler class 
//handles go back requests from login and signup screens (scene2 and scene3)

class gotitle implements EventHandler<ActionEvent>{
    Stage s;
    Scene sc;
    Label l;
    gotitle(Stage s, Scene sc, Label l){
        this.s = s;
        this.sc = sc;
        this.l = l;
    }
    public void handle(ActionEvent ae){
        s.setScene(sc);
        l.setText("");
    }
}

//************************************************************************************************************************************************************* */
//main class


public class App extends Application {
    int x = 0;
    int log = 0;
    ToggleGroup tg2;
    customer[] customers = new customer[100];
    Admin admin;
    static int c = 0;

//*********************************************************************************************************************************************************** */
//web scarper

    static void get_data(){
        try {
            String url = "https://www.screener.in/screens/885655/top-100-stocks/";

            Document document = Jsoup.connect(url).get();
            Elements rows = document.select("tr[data-row-company-id]");
            int count = 0;
            for (Element row : rows) {
                String companyName = row.select("td:nth-child(2) a").text();
                String stockPrice = row.select("td:nth-child(3)").text();
                String marketcap = row.select("td:nth-child(5)").text();
                bank.stocks[count].name = companyName;             
                bank.stocks[count].shares = (int)Double.parseDouble(marketcap);
                bank.stocks[count].price = (int)Double.parseDouble(stockPrice);
                count+=1;
                if (count == 10){
                    break;
                }
                
                
            }
        } 
        catch (Exception e) {
            
        }
    }

//********************************************************************************************************************************************************** */
//realtime thread       
//thread which scrapes data simultaneously and displays 

    class stockrealtime extends Thread{
        int flag = 0;
        int breaker = 0;
        stockrealtime(){
            start();
            get_data();
        }
        public void run(){
            while(true){
                
                if(flag == 1){
                    synchronized (this) {
                        try {
                            wait(); 
                        } 
                        catch (InterruptedException e) {

                        }
                    }
                }
                else{
                    get_data();
                    try{
                        Thread.sleep(5000);
                    }
                    catch(Exception e){

                    }
                }
                try{
                    RadioButton b = (RadioButton) tg2.getSelectedToggle();
                    b.setSelected(false);
                    b.fire();
                }
                catch(Exception e){

                }

                if(breaker == 1){
                    break;
                }
                
            }
        }

        public synchronized void end(){
            breaker = 1;
        }
        public synchronized void pauseThread() {
            flag = 1;
        }

        public synchronized void resumeThread() {
            flag = 0;
            notify();
        }
    }


    stockrealtime strk = new stockrealtime();

//*********************************************************************************************************************************************************** */
//update threads    
//keeps creates threads which keeps updating the labels

    class multithread extends Thread {
        int i;
        Label l;
        int flag = 0;
        multithread(Label l, int i) {
            this.l = l;
            this.i = i;
            start();
        }
        public void run() {
            while (true) {
                // Use an anonymous inner class to run code on the JavaFX Application Thread
                Platform.runLater(new Runnable() {
                    public void run() {
                        l.setText("Market cap (in crores) = " + bank.stocks[i].shares +" | Stock price = " + bank.stocks[i].price);
                    }
                });

                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    
                }
                if (flag == 1){
                    break;
                }
            }
        }

        public void end(){
            flag = 1;
        }
    }

    multithread threads[] = new multithread[10];

//************************************************************************************************************************************************************ */
//start function

    public void start(Stage st1) {
        try{
            admin = new Admin();
        }
        catch(Exception e){

        }
//************************************************************************************************************************************************************** */
//stages creation
        Stage st2 = new Stage();
        st2.setTitle("screen 2");
        Stage st3 = new Stage();
        st3.setTitle("screen 3");
        Stage st4 = new Stage();
        st4.setTitle("screen 4");
        Stage st5 = new Stage();
        st5.setTitle("screen 5");
        Stage st6 = new Stage();
        st6.setTitle("screen 6");
        
        
        
        st1.setTitle("Screen 1");

//************************************************************************************************************************************************************* */
//terms and conditions screen
//scene t

        Canvas canvas = new Canvas(600, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.BLUE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("TERMS AND CONDITIONS\n\nI ABIDE TO FOLLOW THE RULES OF THIS BANK\n\nI ALLOW TO RECIEVE PROMOTIONAL EMAILS\n\n\nIN CASE OF ENQUIRES PLEASE CONTACT PRIYANSHU", 50, 100);
        Image image = new Image("Picture1.jpg");
        ImageView imageView = new ImageView(image);
        gc.drawImage(image, 400, 300, 100, 100);

        StackPane roott = new StackPane(canvas);
        Scene scenet = new Scene(roott, 600, 500);

        

//************************************************************************************************************************************************************* */
//title screen
//scene1

       VBox root1 = new VBox(10);
        root1.setAlignment(Pos.CENTER);
        root1.setStyle("-fx-background-color: #333333;"); // Dark background color

        // Create Labels
        Label l_1_f = new Label();
        Label l_1_1 = new Label("WELCOME TO BANK SYSTEM");
        l_1_1.setStyle("-fx-font-size: 30px; -fx-text-fill: white;"); // White text color
        l_1_f.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        // Create Buttons
        Button bns = new Button("SIGN UP");
        Button bls = new Button("LOG IN");
        Button stock = new Button("SEE MARKET");
        Button b_1_t = new Button("Read terms and conditions");

        // Apply styling to buttons
        String buttonStyle = "-fx-background-color: #666666; -fx-text-fill: white; -fx-font-weight: bold;";
        bns.setStyle(buttonStyle);
        bls.setStyle(buttonStyle);
        stock.setStyle(buttonStyle);

        buttonStyle = "-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;";
        b_1_t.setStyle(buttonStyle);

        // Add nodes to the VBox
        root1.getChildren().addAll(l_1_1, bns, bls, stock, l_1_f, b_1_t);

        // Create the scene
        Scene scene1 = new Scene(root1, 600, 500);


//************************************************************************************************************************************************************* */
//login screen
//scene2

        GridPane loginGrid = new GridPane();
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(20));
        

        // Username TextField
        TextField tf1 = new TextField();
        tf1.setPromptText("Username");
        loginGrid.add(tf1, 0, 1, 2, 1);

        // Password PasswordField
        PasswordField tf2 = new PasswordField();
        tf2.setPromptText("Password");
        loginGrid.add(tf2, 0, 2, 2, 1);

        // Label for displaying instructions
        Label l_2_ti = new Label("Enter username and password");
        l_2_ti.setTextFill(Color.WHITE);

        // Label for displaying invalid messages
        Label l_2_f = new Label("");
        l_2_f.setTextFill(Color.RED);

        // Confirm Button
        Button conf = new Button("Confirm");
        conf.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        // Go Back Button
        Button gbb1 = new Button("Go Back");
        gbb1.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");

        // Add nodes to the GridPane
        loginGrid.add(l_2_ti, 0, 0, 2, 1);
        loginGrid.add(l_2_f, 0, 3, 2, 1);
        loginGrid.add(conf, 0, 4, 2, 1);
        loginGrid.add(gbb1, 0, 5, 2, 1);
        loginGrid.setStyle("-fx-background-color: #333333;");
        // Create the scene
        Scene scene2 = new Scene(loginGrid, 600, 500);


//********************************************************************************************************************************************** */
//signup screen
//scene3

        GridPane root3 = new GridPane();
        root3.setHgap(10);
        root3.setVgap(10);
        root3.setStyle("-fx-background-color: #333333;"); // Dark background color

        // Labels and TextFields
        Label l_3_1 = new Label("Enter name:");
        l_3_1.setTextFill(Color.WHITE);
        TextField tf3 = new TextField();
        tf3.setPromptText("Enter your name");

        Label l_3_2 = new Label("Enter password:");
        l_3_2.setTextFill(Color.WHITE);
        PasswordField tf4 = new PasswordField();
        tf4.setPromptText("Enter password");

        Label l_3_3 = new Label("Enter password again:");
        l_3_3.setTextFill(Color.WHITE);
        PasswordField tf5 = new PasswordField();
        tf5.setPromptText("Enter password again");

        Label l_3_4 = new Label("Enter Aadhar:");
        l_3_4.setTextFill(Color.WHITE);
        TextField ageField = new TextField();
        ageField.setPromptText("Enter your Aadhar");

        Label l_3_5 = new Label("Select gender:");
        l_3_5.setTextFill(Color.WHITE);
        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleRadioButton = new RadioButton("MALE");
        maleRadioButton.setToggleGroup(genderGroup);
        maleRadioButton.setTextFill(Color.WHITE);
        RadioButton femaleRadioButton = new RadioButton("FEMALE");
        femaleRadioButton.setToggleGroup(genderGroup);
        femaleRadioButton.setTextFill(Color.WHITE);
        RadioButton otherRadioButton = new RadioButton("OTHERS");
        otherRadioButton.setToggleGroup(genderGroup);
        otherRadioButton.setTextFill(Color.WHITE);

        Label l_3_6 = new Label("Select Nationality:");
        l_3_6.setTextFill(Color.WHITE);
        ChoiceBox<String> NationalityChoiceBox = new ChoiceBox<>();
        NationalityChoiceBox.getItems().addAll("INDIA", "USA", "CANADA", "JAPAN", "FRANCE");

        // Buttons
        Button gbb2 = new Button("Go Back");
        gbb2.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");

        Button cnf = new Button("Confirm");
        cnf.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        // Label for displaying error messages
        Label l_3_f = new Label();
        l_3_f.setTextFill(Color.RED);

        // Add nodes to the GridPane
        root3.add(l_3_1, 1, 2);
        root3.add(tf3, 2, 2);
        root3.add(l_3_2, 1, 3);
        root3.add(tf4, 2, 3);
        root3.add(l_3_3, 1, 4);
        root3.add(tf5, 2, 4);
        root3.add(l_3_4, 1, 5);
        root3.add(ageField, 2, 5);
        root3.add(l_3_5, 1, 6);
        root3.add(maleRadioButton, 2, 6);
        root3.add(femaleRadioButton, 2, 7);
        root3.add(otherRadioButton, 2, 8);
        root3.add(l_3_6, 1, 9);
        root3.add(NationalityChoiceBox, 2, 9);
        root3.add(gbb2, 1, 10);
        root3.add(cnf, 1, 11);
        root3.add(l_3_f, 1, 13, 4, 1);

        // Create the scene
        Scene scene3 = new Scene(root3, 600, 500);

//************************************************************************************************************************************************************** */
//TRADING SCREEN
//scene4

        GridPane root4 = new GridPane();

        TextField t_4_1 = new TextField("0");
        TextField t_4_2 = new TextField("0");

        

        Label l_4_1 = new Label("Enter number of share you want to buy :");
        l_4_1.setTextFill(Color.WHITE);
        Label l_4_2 = new Label();
        Label l_4_3 = new Label("Enter number of shares you want to sell : ");
        l_4_3.setTextFill(Color.WHITE);
        Label l_4_f = new Label();
        l_4_2.setTextFill(Color.WHITE);
        l_4_f.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");

        Button b_4_1 = new Button("BUY");
        Button b_4_2 = new Button("SELL");
        Button b_4_g = new Button("GO BACK");

        b_4_1.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        // Add style to the "SELL" button
        b_4_2.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        // Add style to the "GO BACK" button
        b_4_g.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");
        
        tg2 = new ToggleGroup();
        RadioButton[] rbs2 = new RadioButton[10];

        root4.setHgap(10);
        root4.setVgap(5);

        rbs2[0] = new RadioButton("A)  " + bank.stocks[0].name);
        rbs2[1] = new RadioButton("B)  " + bank.stocks[1].name);
        rbs2[2] = new RadioButton("C)  " + bank.stocks[2].name);
        rbs2[3] = new RadioButton("D)  " + bank.stocks[3].name);
        rbs2[4] = new RadioButton("E)  " + bank.stocks[4].name);
        rbs2[5] = new RadioButton("F)  " + bank.stocks[5].name);
        rbs2[6] = new RadioButton("G)  " + bank.stocks[6].name);
        rbs2[7] = new RadioButton("H)  " + bank.stocks[7].name);
        rbs2[8] = new RadioButton("I)  " + bank.stocks[8].name);
        rbs2[9] = new RadioButton("J)  " + bank.stocks[9].name);

        for(int i = 1; i<11 ;i++){
            rbs2[i-1].setToggleGroup(tg2);
            rbs2[i-1].setTextFill(Color.WHITE);
            root4.add(rbs2[i-1], 2, i+1);
        }

        root4.add(l_4_2, 2, 1);     
        root4.add(b_4_1, 4, 14);
        root4.add(b_4_2, 4, 16);
        root4.add(b_4_g, 4, 18);
        root4.add(l_4_1, 2, 14);
        root4.add(l_4_3, 2, 16);
        root4.add(l_4_f, 2, 17);
        root4.add(t_4_1, 3, 14);
        root4.add(t_4_2, 3, 16);
        rbs2[0].setSelected(true);
        root4.setStyle("-fx-background-color: #333333;");
        Scene scene4 = new Scene(root4, 600, 400);

//*************************************************************************************************************************************************************** */
//dashboard
//scene5
        BorderPane bpc = new BorderPane();
        GridPane root5 = new GridPane();
        root5.setVgap(20);
        root5.setHgap(10);
        root5.setPadding(new Insets(20));

        // Create Labels
        Label l_5_1 = new Label();
        l_5_1.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        l_5_1.setTextFill(Color.WHITE);

        Label l_5_f = new Label();
        l_5_f.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        l_5_f.setTextFill(Color.WHITE);
        // Create TextFields and Buttons
        TextField t_5_1 = new TextField();
        t_5_1.setPromptText("Enter amount");
        t_5_1.setPrefWidth(100);

        Button b_5_1 = new Button("Deposit");
        Button b_5_2 = new Button("Transfer");
        Button b_5_3 = new Button("Log Out");
        Button b_5_4 = new Button("TRADE");

        // Apply styling to buttons
        b_5_1.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        b_5_2.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        b_5_3.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
        b_5_4.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        // Add nodes to the GridPane
        root5.add(l_5_1, 0, 1);
       
        root5.add(t_5_1, 0, 4);
        root5.add(b_5_1, 1, 4);
        root5.add(b_5_2, 0, 5);
        root5.add(b_5_3, 0, 7);
        root5.add(b_5_4, 0, 6);
        root5.add(l_5_f, 0, 8);
        bpc.setCenter(root5);
        // Create the scene
        bpc.setStyle("-fx-background-color: #333333;");
        Scene scene5 = new Scene(bpc, 600, 500);

//*************************************************************************************************************************************************************** */
//TRANSFER SCREEN
//scene6

        GridPane root6 = new GridPane();
        root6.setHgap(10);
        root6.setVgap(10);
        root6.setStyle("-fx-background-color: #333333;"); // Dark background color

        // Labels and TextFields
        Label l_6_1 = new Label("To User: ");
        l_6_1.setTextFill(Color.WHITE);
        TextField t_6_1 = new TextField();

        Label l_6_2 = new Label("Amount: ");
        l_6_2.setTextFill(Color.WHITE);
        TextField t_6_2 = new TextField();

        Label l_6_3 = new Label("Enter your password: ");
        l_6_3.setTextFill(Color.WHITE);
        PasswordField t_6_3 = new PasswordField();

        Label l_6_f = new Label();
        

        // Buttons
        Button b_6_1 = new Button("Confirm");
        b_6_1.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        Button b_6_g = new Button("Go Back");
        b_6_g.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");

        // Add nodes to the GridPane
        root6.add(l_6_1, 2, 2);
        root6.add(t_6_1, 4, 2);
        root6.add(l_6_2, 2, 3);
        root6.add(t_6_2, 4, 3);
        root6.add(l_6_3, 2, 4);
        root6.add(t_6_3, 4, 4);
        root6.add(l_6_f, 2, 9);
        root6.add(b_6_1, 2, 8);
        root6.add(b_6_g, 5, 9);

        // Create the scene
        Scene scene6 = new Scene(root6, 600, 500);

//************************************************************************************************************************************************************** */
//ADMIN SCREEN
//scene7

        BorderPane bc = new BorderPane();
        bc.setStyle("-fx-background-color: #333333;"); // Dark background color

        // Create a GridPane
        GridPane root7 = new GridPane();
        root7.setHgap(10);
        root7.setVgap(5);

        // Create Labels for table headers
        Label labels_n[] = new Label[21];
        Label labels_a[] = new Label[21];
        Label labels_c[] = new Label[21];
        Label labels_s[] = new Label[21];
        labels_n[0] = new Label();
        labels_c[0] = new Label();
        labels_a[0] = new Label();
        labels_s[0] = new Label();
        labels_n[0].setTextFill(Color.WHITE);
        labels_a[0].setTextFill(Color.WHITE);
        labels_s[0].setTextFill(Color.WHITE);
        labels_c[0].setTextFill(Color.WHITE);

        labels_n[0].setText("User");
        labels_n[0].setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");
        labels_a[0].setText("Amount in Bank");
        labels_a[0].setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");
        labels_s[0].setText("Amount in Shares");
        labels_s[0].setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");
        labels_c[0].setText("Nationality");
        labels_c[0].setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");

        root7.add(labels_n[0], 2, 2);
            root7.add(labels_a[0], 6, 2);
            root7.add(labels_c[0], 4, 2);
            root7.add(labels_s[0], 8, 2);




        // Add labels to the GridPane in a table format
        for (int i = 1; i < 21; i++) {
            labels_n[i] = new Label("X");
            labels_a[i] = new Label("X");
            labels_n[i].setTextFill(Color.WHITE);
            labels_a[i].setTextFill(Color.WHITE);
            root7.add(labels_n[i], 2, i + 2);
            root7.add(labels_a[i], 6, i + 2);

            labels_c[i] = new Label("X");
            labels_s[i] = new Label("X");
            labels_c[i].setTextFill(Color.WHITE);
            labels_s[i].setTextFill(Color.WHITE);
            root7.add(labels_c[i], 4, i + 2);
            root7.add(labels_s[i], 8, i + 2);
        }

        // Buttons and Labels
        Button b_7_1 = new Button("Stock Manipulation");
        b_7_1.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        Button b_7_g = new Button("LOG OUT");
        b_7_g.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");
        
        Label l_7_1 = new Label();
        l_7_1.setText("Bank Liquidity: " + (bank.money+bank.liability) + "\nBank Liability: "+ bank.liability + "\nBank Assets: " + bank.money);
        l_7_1.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        b_7_g.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");

        // Add elements to the GridPane
        root7.add(b_7_g, 10, 2);
        root7.add(b_7_1, 10, 4);
        root7.add(l_7_1, 8, 26);

        // Add GridPane to the center of the BorderPane
        bc.setCenter(root7);

        // Create the scene
        Scene scene7 = new Scene(bc, 700, 650);

//************************************************************************************************************************************************************** */
//STOCKBOARD
//scene8

        BorderPane bc2 = new BorderPane();
        GridPane root8 = new GridPane();

        Button b_8_c = new Button("close");
        
        Label l_8_s = new Label("  SEE STOCK DATA IN REAL-TIME AND COMPARE");
        Label l_8_f = new Label();
        l_8_f.setTextFill(Color.WHITE);
        l_8_s.setTextFill(Color.WHITE);
        l_8_s.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        Label l_8[] = new Label[10];
        b_8_c.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");
        CheckBox c_8[] = new CheckBox[10];
        c_8[0] = new CheckBox("A)  " + bank.stocks[0].name);
        
        c_8[1] = new CheckBox("B)  " + bank.stocks[1].name);
        c_8[2] = new CheckBox("C)  " + bank.stocks[2].name);
        c_8[3] = new CheckBox("D)  " + bank.stocks[3].name);
        c_8[4] = new CheckBox("E)  " + bank.stocks[4].name);
        c_8[5] = new CheckBox("F)  " + bank.stocks[5].name);
        c_8[6] = new CheckBox("G)  " + bank.stocks[6].name);
        c_8[7] = new CheckBox("H)  " + bank.stocks[7].name);
        c_8[8] = new CheckBox("I)  " + bank.stocks[8].name);
        c_8[9] = new CheckBox("J)  " + bank.stocks[9].name);

        for (int i = 1; i < 11; i++) {
            c_8[i - 1].setTextFill(Color.WHITE); // Set text color of labels to white
        }

        
        for(int i = 1; i<11 ;i++){
            l_8[i-1] = new Label();
            l_8[i-1].setTextFill(Color.WHITE);
            root8.add(c_8[i-1], 2, i+2);
            root8.add(l_8[i-1], 3, i+2);
        }

        root8.setHgap(15);
        root8.setVgap(10);
        bc2.setTop(l_8_s);
        l_8_s.setAlignment(Pos.CENTER);
        root8.add(b_8_c, 2, 16);
        bc2.setCenter(root8);
        bc2.setStyle("-fx-background-color: #333333;");
        Scene scene8 = new Scene(bc2 , 600, 500);


//************************************************************************************************************************************************************* */
//STOCKUPDATER
//scene9

        GridPane root9 = new GridPane();   
        Label l_9_1 = new Label("STOCK UPDATER");
        Label l_9_f = new Label("Market cap = " + bank.stocks[0].shares +"\nStock price = " + bank.stocks[0].price);
        Label l_9_2 = new Label("New market cap: ");
        Label l_9_3 = new Label("New price: ");
        
        Button update = new Button("update");
        ToggleButton tg_9_1 = new ToggleButton("stop realtime");
        Button b_9_g = new Button("go back");

        l_9_1.setStyle("-fx-font-size: 17px; -fx-text-fill: white;");
        root9.add(tg_9_1, 5,8);
        root9.add(l_9_1, 2, 2);
        Label l__ = new Label(" ");

        TextField t_9_2 = new TextField();
        TextField t_9_3 = new TextField();

        ToggleGroup tg1 = new ToggleGroup();
        RadioButton[] rbs1 = new RadioButton[10];

        root9.setHgap(10); // Horizontal gap between cells
        root9.setVgap(5);

        rbs1[0] = new RadioButton("A)  " + bank.stocks[0].name);
        rbs1[1] = new RadioButton("B)  " + bank.stocks[1].name);
        rbs1[2] = new RadioButton("C)  " + bank.stocks[2].name);
        rbs1[3] = new RadioButton("D)  " + bank.stocks[3].name);
        rbs1[4] = new RadioButton("E)  " + bank.stocks[4].name);
        rbs1[5] = new RadioButton("F)  " + bank.stocks[5].name);
        rbs1[6] = new RadioButton("G)  " + bank.stocks[6].name);
        rbs1[7] = new RadioButton("H)  " + bank.stocks[7].name);
        rbs1[8] = new RadioButton("I)  " + bank.stocks[8].name);
        rbs1[9] = new RadioButton("J)  " + bank.stocks[9].name);



        for(int i = 1; i<11 ;i++){
            
            rbs1[i-1].setToggleGroup(tg1);
            rbs1[i-1].setTextFill(Color.WHITE);
            root9.add(rbs1[i-1], 2, i+2);
        }
        root9.add(l__, 6, 1);
    
        root9.add(l_9_2, 4,5);
        root9.add(t_9_2, 5,5);
        root9.add(l_9_3, 4, 7);
        root9.add(t_9_3, 5,7);


        root9.add(update, 5, 9);

        root9.add(l_9_f, 5,11);
        root9.add(b_9_g, 9, 14);
        l_9_f.setTextFill(Color.WHITE);
        l_9_2.setTextFill(Color.WHITE);
        l_9_3.setTextFill(Color.WHITE);
        update.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        b_9_g.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-weight: bold;");
        root9.setStyle("-fx-background-color: #333333;");
        Scene scene9 = new Scene(root9, 600, 500);
        
//************************************************************************************************************************************************************* */
//scene creation ends, handler methods from here on
//************************************************************************************************************************************************************* */

//innerclass which defines the labels are updated according to which radiobuttons are pressed
//radiobutton handler

        class stocker implements EventHandler<ActionEvent>{
            Label l1;
          
            ToggleGroup tg1a;
            RadioButton[] rb1a;
            stocker(Label l1, ToggleGroup tg1, RadioButton[] rb1){
                this.l1 = l1;
               
                this.tg1a = tg1;
                this.rb1a = rb1;
            }
            public void handle(ActionEvent ae){
                
                for(int i = 1; i<11 ;i++){
                        if((tg1a.getSelectedToggle()) == rb1a[i-1]){
                            if(l1 == l_9_f)
                                l1.setText("Market cap = " + bank.stocks[i-1].shares +"\nStock price = " + bank.stocks[i-1].price);
                            else
                                l1.setText("Shares owned " + customers[x].shares[i-1] + " | market cap = " + bank.stocks[i-1].shares +" | price = " + bank.stocks[i-1].price);
                        }
                        
                    }
                
            }
        }

//object creation and nodes at which it will act

//radiobuttons of scene9 (stockupdater screen)

        stocker obj1 = new stocker(l_9_f, tg1, rbs1);
        rbs1[0].setSelected(true);
        
        rbs1[0].setOnAction(obj1);
        rbs1[1].setOnAction(obj1);
        rbs1[2].setOnAction(obj1);
        rbs1[3].setOnAction(obj1);
        rbs1[4].setOnAction(obj1);
        rbs1[5].setOnAction(obj1);
        rbs1[6].setOnAction(obj1);
        rbs1[7].setOnAction(obj1);
        rbs1[8].setOnAction(obj1);
        rbs1[9].setOnAction(obj1);

//radiobuttons of scene4(trading screen)

        stocker obj3 = new stocker(l_4_2, tg2, rbs2);
        rbs2[0].setOnAction(obj3);
        rbs2[1].setOnAction(obj3);
        rbs2[2].setOnAction(obj3);
        rbs2[3].setOnAction(obj3);
        rbs2[4].setOnAction(obj3);
        rbs2[5].setOnAction(obj3);
        rbs2[6].setOnAction(obj3);
        rbs2[7].setOnAction(obj3);
        rbs2[8].setOnAction(obj3);
        rbs2[9].setOnAction(obj3);

//************************************************************************************************************************************************************ */
//innerclass which will handle label updation based on checkboxs
//checkbox handler

        class realt implements EventHandler<ActionEvent>{
            public void handle(ActionEvent ae){
                CheckBox b = (CheckBox) ae.getSource();
                String s = b.getText();
                char c = s.charAt(0);
                int i = (int) c;
                
                if(b.isSelected()){

                   threads[i-65] = new multithread(l_8[i-65] ,i-65);
                }
                else{
                    (threads[i-65]).flag = 1;
                    l_8[i-65].setText("");
                }
            }
        }

//object creation and nodes on which it will act
//checkboxes of scene8 (Stockboard screen)

        realt obj2 = new realt();

        c_8[0].setOnAction(obj2);
        c_8[1].setOnAction(obj2);
        c_8[2].setOnAction(obj2);
        c_8[3].setOnAction(obj2);
        c_8[4].setOnAction(obj2);
        c_8[5].setOnAction(obj2);
        c_8[6].setOnAction(obj2);
        c_8[7].setOnAction(obj2);
        c_8[8].setOnAction(obj2);
        c_8[9].setOnAction(obj2);

//************************************************************************************************************************************************************* */
//sign up screen handler

        class checker implements EventHandler<ActionEvent> {

            public void handle(ActionEvent ae) {
                l_3_f.setText("");
                try{
                // Check if any of the required fields are null
                    if (tf3.getText() == null || tf4.getText() == null) {
                        l_3_f.setText("Username and password cannot be null.");
                        return;
                    }

                    String password = tf4.getText();
                    String confirmPassword = tf5.getText();

                    if (!password.equals(confirmPassword)) {
                        l_3_f.setText("Passwords do not match.");
                        return;
                    } else if (genderGroup.getSelectedToggle() == null) {
                        l_3_f.setText("Please select a gender.");
                        return;
                    }

                    String Nationality = NationalityChoiceBox.getValue(); // Retrieve the selected Nationality here

                    if (Nationality == null || Nationality.isEmpty()) {
                        l_3_f.setText("Please select a Nationality.");
                        return;
                    }

                    try {
                        customers[c] = new customer(tf3.getText(), tf4.getText(), Integer.parseInt(ageField.getText()), ((RadioButton)genderGroup.getSelectedToggle()).getText() ,NationalityChoiceBox.getValue());
                    } catch (passworderror e) {
                        l_3_f.setText(e+"\nPassword must be at least 7 characters long.");
                        return;
                    } catch (usererror e) {
                        l_3_f.setText(e+"\nUsername must contain only alphabets/digits/_ and be at least 5 characters long.");
                        return;
                    }
                      catch(Exception e){
                        l_3_f.setText("Aadhar number must be less than 8 digits");
                        return;
                      }
                }
                catch(Exception e){
                    l_3_f.setText("ERROR WARNING");
                    e.printStackTrace();
                    return;
                }
                
                c += 1;
                l_1_f.setText("USER ADDED SUCCESSFULLY");
                st1.setScene(scene1);
                labels_n[c].setText(customers[c - 1].name);
                labels_a[c].setText("" + customers[c - 1].get_amount());
                labels_c[c].setText(customers[c-1].Nationality);
                labels_s[c].setText(""+customers[c-1].getsmoney());
                l_7_1.setText("Bank Liquidity: " + (bank.money+bank.liability) + "\nBank Liability: "+ bank.liability + "\nBank Assets: " + bank.money);
                (genderGroup.getSelectedToggle()).setSelected(false);
                ageField.setText("");
            }
        }

        checker v = new checker();
        
        cnf.setOnAction(v);

//************************************************************************************************************************************************************* */      
//class already defined outside(not a inner class)
//object creation and nodes on which it will act
//go back button for titlescreen from login and signup

        gotitle objgotitle = new gotitle(st1, scene1, l_1_f);
        gbb1.setOnAction(objgotitle);
        gbb2.setOnAction(objgotitle);

//************************************************************************************************************************************************************* */
//buttons on scene1 (title screen)

//sign in button
        bns.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                tf3.clear();
                tf4.clear();
                tf5.clear();
                l_3_f.setText("");
                st1.setScene(scene3);
            }
        });

//log in button
        bls.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                tf1.clear();
                tf2.clear();
                l_2_f.setText("");
                st1.setScene(scene2);
            }
        });

//read terms and condition button
        b_1_t.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                st5.setScene(scenet);
                st5.show();
            }
        });
//view stockboard button
        stock.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                l_8_f.setText("Stock shares = " + bank.stocks[0].shares +"\nStock price = " + bank.stocks[0].price);
                st4.setScene(scene8);
                st4.show();
            }
        });

//************************************************************************************************************************************************************* */
//confirm button on scene2 (log in screen)

        conf.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent event) {   
                int f = 0;
                String uname = tf1.getText();
                String pass = tf2.getText();
                if(uname.equals("admin") && pass.equals("admin")){
                    for(int i = 0; i<c ;i++){
                        labels_n[i+1].setText(customers[i].name);
                        labels_a[i+1].setText(""+customers[i].get_amount());
                        labels_c[i+1].setText(customers[i].Nationality);
                        labels_s[i+1].setText(""+customers[i].getsmoney());
                        l_7_1.setText("Bank Liquidity: " + (bank.money+bank.liability) + "\nBank Liability: "+ bank.liability + "\nBank Assets: " + bank.money);
                    }
                        st3.setScene(scene7);
                        st3.show();
                        f = 1;
                        st1.setScene(scene1);
                        return;
                }
                else if(log == 1){
                    st2.close(); 
                }
                for(int i = 0; i<c; i++){
                    if(uname.equals(customers[i].name) && pass.equals(customers[i].get_pass())){
                        x = i;
                        l_4_2.setText("Shares owned " + customers[x].shares[0] + " | market cap = " + bank.stocks[0].shares +" | price = " + bank.stocks[0].price);
                        l_5_1.setText("User name = "+ customers[x].name+"\nUser amount = "+customers[x].get_amount()+"\nAadhar number = "+customers[x].aadhar+"\nGender = "+customers[x].gender+"\nNationality = "+customers[x].Nationality);
                        l_1_f.setText("");
                        log = 1;
                        st2.setScene(scene5);
                        st2.show();
                        f = 1;
                        break;
                    }
                }
                if(f == 0){
                    l_2_f.setText("username/password invalid");
                    st1.setScene(scene2);
                    return;
                }
                st1.setScene(scene1);
            }
        });

//************************************************************************************************************************************************************* */
//Buttons of scene4 (TRADING SCREEN)

//go back button
        b_4_g.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                t_4_1.setText("0");
                t_4_2.setText("0");
                l_4_f.setText("");
                RadioButton b = (RadioButton) tg2.getSelectedToggle();
                    b.setSelected(false);
                    rbs2[0].setSelected(true);
                st2.setScene(scene5);
                l_5_1.setText("User name = "+ customers[x].name+"\nUser amount = "+customers[x].get_amount()+"\nAadhar number = "+customers[x].aadhar+"\nGender = "+customers[x].gender+"\nNationality = "+customers[x].Nationality);
            }
        });

//buy button
        b_4_1.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                int res;
                String s = ((RadioButton) tg2.getSelectedToggle()).getText();
                char c = s.charAt(0);
                int i = (int) c;
                i = i-65;
                
                try{
                    bank.liability += customers[x].get_amount();
                    bank.money -= customers[x].get_amount();
                    res = customers[x].buy(i, Integer.parseInt(t_4_1.getText()));
                    bank.money += customers[x].get_amount();
                    bank.liability -= customers[x].get_amount();
                }
                catch(Exception e){
                    res = 0;
                }
                
                
                if(res == 1){
                    l_4_f.setText("success");
                }
                else{
                    l_4_f.setText("failed");
                }
                labels_n[x+1].setText(customers[x].name);
                labels_a[x+1].setText("" + customers[x].get_amount());
                labels_c[x+1].setText(customers[x].Nationality);
                labels_s[x+1].setText(""+customers[x].getsmoney());
                t_4_1.setText("0");
                t_4_2.setText("0");
                l_5_1.setText("User name = "+ customers[x].name+"\nUser amount = "+customers[x].get_amount()+"\nAadhar number = "+customers[x].aadhar+"\nGender = "+customers[x].gender+"\nNationality = "+customers[x].Nationality);
                l_4_2.setText("Shares owned " + customers[x].shares[i] + " | market cap = " + bank.stocks[i].shares +" | price = " + bank.stocks[i].price);
            }
        });

//sell button
        b_4_2.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                int res;
                String s = ((RadioButton) tg2.getSelectedToggle()).getText();
                char c = s.charAt(0);
                int i = (int) c;
                i = i-65;
                try{
                    bank.liability += customers[x].get_amount();
                    bank.money -= customers[x].get_amount();
                    res = customers[x].sell(i, Integer.parseInt(t_4_2.getText()));
                    bank.money += customers[x].get_amount();
                    bank.liability -= customers[x].get_amount();
                }
                catch(Exception e){
                    res = 0;
                }
                
                if(res == 1){
                    l_4_f.setTextFill(Color.GREEN);
                    l_4_f.setText("success");
                }
                else{
                    l_4_f.setTextFill(Color.RED);
                    l_4_f.setText("failed");
                }
                labels_n[x+1].setText(customers[x].name);
                labels_a[x+1].setText("" + customers[x].get_amount());
                labels_c[x+1].setText(customers[x].Nationality);
                labels_s[x+1].setText(""+customers[x].getsmoney());
                t_4_1.setText("0");
                t_4_2.setText("0");
                l_5_1.setText("User name = "+ customers[x].name+"\nUser amount = "+customers[x].get_amount()+"\nAadhar number = "+customers[x].aadhar+"\nGender = "+customers[x].gender+"\nNationality = "+customers[x].Nationality);
                l_4_2.setText("Shares owned " + customers[x].shares[i] + " | market cap = " + bank.stocks[i].shares +" | price = " + bank.stocks[i].price);
            }
        });

//************************************************************************************************************************************************************* */
//Buttons of scene5 (DASHBOARD)

//DEPOSIT BUTTON
        b_5_1.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                try{
                    customers[x].deposit(Integer.parseInt(t_5_1.getText()));
                    bank.money += Integer.parseInt(t_5_1.getText());
                    t_5_1.setText("0");
                     l_5_1.setText("User name = "+ customers[x].name+"\nUser amount = "+customers[x].get_amount()+"\nAadhar number = "+customers[x].aadhar+"\nGender = "+customers[x].gender+"\nNationality = "+customers[x].Nationality);
                    System.out.println(bank.money);
                    st2.close();
                    l_5_f.setText("");
                    st2.setScene(scene5);
                    st2.show();
                    for(int i = 0; i<c ;i++){
                        labels_n[i+1].setText(customers[i].name);
                        labels_a[i+1].setText(""+customers[i].get_amount());
                        labels_c[i+1].setText(customers[i].Nationality);
                        labels_s[i+1].setText(""+customers[i].getsmoney());
                        l_7_1.setText("Bank Liquidity: " + (bank.money+bank.liability) + "\nBank Liability: "+ bank.liability + "\nBank Assets: " + bank.money);
                    }
                }
                catch(Exception e){
                    l_5_f.setText("Enter valid details");
                    
                }
            }
        });

//TRANSFER BUTTON
        b_5_2.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                st2.close();
                st2.setScene(scene6);
                st2.show();
            }
        });

//LOG OUT BUTTON
        b_5_3.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                log = 0;
                st2.close();
            }
        });

//TRADE BUTTON
        b_5_4.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                st2.setScene(scene4);
                
            }
        });

//************************************************************************************************************************************************************* */
//buttons of scene6(TRANSFER SCREEN)

//CONFIRM TRANSFER BUTTON
        b_6_1.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                if (t_6_3.getText().equals(customers[x].get_pass())){
                    
                    int f = 0;
                    int loc = 0;
                    int amount = Integer.parseInt(t_6_2.getText());
                    
                    for(int i = 0; i<c; i++){
                        if(t_6_1.getText().equals(customers[i].name)){
                            f = 1;
                            loc = i;
                        }
                    }
                    if (f == 0){
                        l_6_f.setTextFill(Color.RED);
                        l_6_f.setText("user not found");
                        
                    }
                    int success = 0;
                    if (f == 1){
                        success = customers[x].transfer(amount, customers[loc]);
                    }
                    if (success == 1){
                        l_6_f.setTextFill(Color.GREEN);
                        l_6_f.setText("Success");
                    }
                    else{
                        l_6_f.setTextFill(Color.RED);
                        l_6_f.setText("FAILED");
                    }
                }
                else{
                    l_6_f.setTextFill(Color.RED);
                    l_6_f.setText("password invalid");
                }
                for(int i = 0; i<c ;i++){
                        labels_n[i+1].setText(customers[i].name);
                        labels_a[i+1].setText(""+customers[i].get_amount());
                        labels_c[i+1].setText(customers[i].Nationality);
                        labels_s[i+1].setText(""+customers[i].getsmoney());
                        l_7_1.setText("Bank Liquidity: " + (bank.money+bank.liability) + "\nBank Liability: "+ bank.liability + "\nBank Assets: " + bank.money);
                    }
                st2.close();
                st2.setScene(scene6);
                st2.show();
            }
        });

//GO BACK BUTTON
        b_6_g.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                t_6_1.setText("");
                t_6_2.setText("");
                t_6_3.setText("");
                l_6_f.setText("");
                l_5_1.setText("User name = "+ customers[x].name+"\nUser amount = "+customers[x].get_amount()+"\nAadhar number = "+customers[x].aadhar+"\nGender = "+customers[x].gender+"\nNationality = "+customers[x].Nationality);
                st2.close();
                st2.setScene(scene5);
                st2.show();
            }
        });

//************************************************************************************************************************************************************* */
//Buttons of scene7 (ADMIN SCREEN)

//LOG OUT BUTTON
        b_7_g.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event) {
                st3.close();
            }
        });

//STOCK MANIPULATOR BUTTON
        b_7_1.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                
                st3.setScene(scene9);
                
            }
        }); 

//************************************************************************************************************************************************************* */
//Buttons of scene8(STOCKBOARD)

//close stockboard button
        b_8_c.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                st4.close();
            }
        });

//************************************************************************************************************************************************************* */
//Button of scene9 (stock manipulation screen)

//update button
        update.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                try{
                    int h=0;
                    for(int i = 1; i<11 ;i++){
                        if((tg1.getSelectedToggle()) == rbs1[i-1]){
                            if(!(t_9_2.getText()).equals("")){
                                bank.stocks[i-1].shares = Integer.parseInt(t_9_2.getText());
                            }
                            if(!(t_9_3.getText()).equals("")){
                                bank.stocks[i-1].price = Integer.parseInt(t_9_3.getText());
                            }
                            l_9_f.setText("Market cap = " + bank.stocks[i-1].shares +"\nStock price = " + bank.stocks[i-1].price);
                            h = i;
                            break;
                        }  
                    }
                    RadioButton b = (RadioButton) tg2.getSelectedToggle();
                    b.setSelected(false);
                    b.fire();
                    t_9_2.setText("");
                    t_9_3.setText("");
                }

                
                catch(Exception e){
                    
                }
            }
        });

//Pause real-time update button
        tg_9_1.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                    if(tg_9_1.isSelected() == true){
                        strk.pauseThread();
                    }
                    else{
                        strk.resumeThread();
                    }    
            }
        }); 

//Go back button
        b_9_g.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent event){
                st3.setScene(scene7);
            }
        });

//******************************************************************************************************************************************************* */
// Set the initial scene
        st1.setScene(scene1);
        st1.show();
    }

//******************************************************************************************************************************************************** */
//closing all threads if still running

    public void stop(){
        strk.end();
        for(int i = 0; i<10; i++){
            try{
                threads[i].end();
            }
            catch(Exception e){

            }
        }
        
        
    }
}

//***********************************************************************************************************************************************************/