package application.gui.components;

import application.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SceneBuilder {

    private static SceneBuilder _instance;
    private static final String _DOUBLE_CLICK_ROW_TEXT = "DOUBLE-CLICK SELECTION";
    private static final String _REMOVE_BUTTON_TEXT = "Remove";
    private static final String _FILTER_TEXT = "Filter";
    private static final String _PASSWORD_FAILED_MESSAGE = "Password verification failed. Try again.";

    private Scene _scene;
    private StackPane _root_root;
    private Label _messageErrorLabel;
    private Button _buttonSave;
    private Button _buttonCancel;
    private final int _PASSWORD_LENGTH_MIN = 8; // make at least 10 later
    private final String _PASSWORD_TEXT_DEFAULT = "ENTER PASSWORD";
    private final String _CONFIRM_PASSWORD_TEXT = "CONFIRM PASSWORD";
    private final String _ENTER_PLATFORM_TEXT = "ENTER PLATFORM NAME";
    private final String _ENTER_LOGIN_TEXT = "ENTER LOGIN";
    private final String _LOGIN_BUTTON_TEXT = "Login";
    private final String _SAVE_BUTTON_TEXT = "Save";
    private final String _CANCEL_BUTTON_TEXT = "Cancel";
    private final String _PASSWORD_CREATE_MATCH = "Password successfully created.";
    private final String _PASSWORD_CREATE_MISMATCH = "Passwords did not match. Try again or cancel.";
    private final String _PASSWORD_TOO_SHORT = "Password must contain at least " + _PASSWORD_LENGTH_MIN + " characters!";
    private final String _CREATE_PASSWORD_TITLE = "Create Password";
    private final String _LOGIN_TITLE = "Login";
    private final String _MAIN_TITLE = "Password Keeper";
    private final String _ADDER_TITLE = "Enter Credentials";
    private final String _LOGOUT_BUTTON_TEXT = "Logout";
    private final String _ADD_BUTTON_TEXT = "Add";
    private final String _COPY_BUTTON_TEXT = "Copy";

    private PwData _selectedItem = new PwData();
    private boolean postponeDataLoad;

    private TableView<PwData> dataTable = new TableView<>();

    private ObservableList<PwData> data = FXCollections.observableArrayList();
    private TableRow<PwData> selectedRow = new TableRow<>();

    private SceneBuilder() {
        Logger.debug(this, "constructor");
        postponeDataLoad = false;
    }

    public static SceneBuilder getInstance(){
        if (_instance == null){
            _instance = new SceneBuilder();
        }
        return _instance;
    }

    public void init(WindowManager window) throws IOException, NoSuchAlgorithmException {
        _root_root = new StackPane();
        _root_root.prefHeightProperty().bind(window.getStage().heightProperty());
        _root_root.prefWidthProperty().bind(window.getStage().widthProperty());
        KeyManager.getInstance().load();
    }

    public void addLayer(StackPane layout){
        Logger.debug(this, "addLayer");
        _root_root.getChildren().add(layout);
    }
    // TODO: make this return scene according to scenetype indicated

    public Scene getScene(){
        Logger.debug(this , "getScene");
        return _scene;
    }

    public double getWidth(){
        Logger.debug(this , "getWidth");
        return _root_root.getWidth();
    }

    public double getHeight(){
        Logger.debug(this , "getHeight");
        return _root_root.getHeight();
    }

    public StackPane buildCreatePasswordLayer() {
        Logger.debug(this, "buildCreatePasswordLayer");
        setTitle(_CREATE_PASSWORD_TITLE);
        StackPane createRoot = new StackPane();
        PasswordField pass1 = new PasswordField();
        pass1.setPromptText(_PASSWORD_TEXT_DEFAULT);
        pass1.requestFocus();
        PasswordField pass2 = new PasswordField();
        pass2.setPromptText(_PASSWORD_TEXT_DEFAULT);
        _messageErrorLabel = new Label("");
        _buttonSave = new Button(_SAVE_BUTTON_TEXT);
        _buttonCancel = new Button(_CANCEL_BUTTON_TEXT);

        // TODO: figure out how to assign window title
//        buttonSave.setMnemonicParsing(true);

        _buttonSave.setOnAction(ae -> {
            if (submitPasswordIfValid(pass1.getText(), pass2.getText(), createRoot)) {
                backToMain();
                prepareKey(pass1.getText().getBytes());
                loadData();
                updateTable();
            }
            updateErrorMessage(_PASSWORD_CREATE_MISMATCH);
        });

        _buttonSave.setDefaultButton(true);

        _buttonCancel.setOnAction(ae -> {
            exitProgram();
        });

        // TODO: get size attributes from window
        _messageErrorLabel.prefHeightProperty().bind(createRoot.heightProperty().divide(5));

        VBox layoutBase = new VBox(10);
        layoutBase.setPrefSize(createRoot.widthProperty().doubleValue(),
                createRoot.heightProperty().doubleValue());

        HBox layoutSub = new HBox(10);
        layoutSub.setPrefSize(createRoot.widthProperty().doubleValue(),
                createRoot.heightProperty().doubleValue());

        Label layoutBanner = new Label();
        layoutBanner.prefWidthProperty().bind(createRoot.widthProperty());
        layoutBanner.prefHeightProperty().bind(createRoot.heightProperty().divide(2));

        VBox.setMargin(pass1, new Insets(0,10,10,10));
        VBox.setMargin(pass2, new Insets(10,10,10,10));
        HBox.setMargin(_buttonSave, new Insets(10,10,10,10));
        HBox.setMargin(_buttonCancel, new Insets(10,10,10,10));
        VBox.setMargin(_messageErrorLabel, new Insets(10,10,10,10));
        layoutBase.getChildren().addAll(layoutBanner, pass1, pass2);
        layoutSub.getChildren().addAll(_buttonSave, _buttonCancel);
        layoutBase.getChildren().addAll(layoutSub, _messageErrorLabel);
        createRoot.getChildren().add(layoutBase);
        createRoot.setAlignment(Pos.CENTER);
        createRoot.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return createRoot;
    }

    public StackPane buildLoginLayer(){
        Logger.debug(this, "buildLoginLayer");
        setTitle(_LOGIN_TITLE);
        StackPane loginRoot = new StackPane();
        Label header = new Label();
        header.setMinHeight(100);
        PasswordField pass = new PasswordField();
        Label loginMessageLabel = new Label();
        pass.setPromptText(_PASSWORD_TEXT_DEFAULT);

        // Buttons
        AnchorPane anchorPane = new AnchorPane();
        Button loginButton = new Button(_LOGIN_BUTTON_TEXT);
        Button exitButton = new Button("Exit");
        anchorPane.getChildren().addAll(loginButton, exitButton);

        loginButton.prefHeightProperty().bind(_root_root.heightProperty().divide(6));
        loginButton.prefWidthProperty().bind(_root_root.widthProperty().divide(6));
        exitButton.prefHeightProperty().bind(_root_root.heightProperty().divide(6));
        exitButton.prefWidthProperty().bind(_root_root.widthProperty().divide(6));

        exitButton.setFont(Font.font(12+exitButton.getWidth()/100));


        loginButton.setDefaultButton(true);
        loginButton.setOnAction(ae -> {
            Logger.debug(this, "loginButton");
            try {
                PasswordManager.getInstance().load();
                if (isPasswordCorrect(pass.getText())) {
                    loginInitializeData(pass.getText().getBytes());
                } else {
                    Logger.log("Password is incorrect.");
                    loginMessageLabel.setText(_PASSWORD_FAILED_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            backToMain();
        });

        loginButton.setOnKeyPressed(ke ->{
            Logger.debug(this, "setOnKeyPressed");
            if (ke.getCode() == KeyCode.ENTER){
                loginInitializeData(pass.getText().getBytes());
            }
        });

        exitButton.setOnAction(ae -> {
            Logger.debug(this, "exitButton");
            exitProgram();
        });

        VBox layoutBase = new VBox(10);
        HBox layoutSub = new HBox(10);

        VBox.setMargin(header, new Insets(0,10,10,10));
        VBox.setMargin(pass, new Insets(10,10,10,10));
        HBox.setMargin(loginButton, new Insets(10,10,10,10));
        HBox.setMargin(exitButton, new Insets(10,10,10,10));
        VBox.setMargin(loginMessageLabel, new Insets(10,10,10,10));

        layoutSub.getChildren().addAll(loginButton, exitButton);
        layoutBase.getChildren().addAll(header, pass, layoutSub, loginMessageLabel);

        layoutBase.setAlignment(Pos.CENTER);
        loginRoot.getChildren().add(layoutBase);
        loginRoot.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        return loginRoot;
    }

    private void loginInitializeData(byte[] pass) {
        Logger.log("Password is correct!"); // set scene to password manager
        try {
            LoginManager.getInstance()
                    .sendPasswordToKeyManager(pass);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        prepareKey(pass);
        loadData();
        updateTable();
    }

    public StackPane buildPasswordManagerLayer() {
        Logger.debug(this, "buildPasswordManagerLayer");
        setTitle(_MAIN_TITLE);
        StackPane passwordManagerRoot = new StackPane();
        TableColumn platformCol = new TableColumn("Platform");
        TableColumn loginCol = new TableColumn("Login");
        TableColumn updatedCol = new TableColumn("Last Updated");

        platformCol.prefWidthProperty().bind(passwordManagerRoot.widthProperty().divide(3));
        platformCol.setMaxWidth(Double.MAX_VALUE);
        platformCol.setCellValueFactory(new PropertyValueFactory<PwData, String>("platform"));

        loginCol.prefWidthProperty().bind(passwordManagerRoot.widthProperty().divide(3));
        loginCol.setMaxWidth(Double.MAX_VALUE);
        loginCol.setCellValueFactory(new PropertyValueFactory<PwData, String>("login"));

        updatedCol.prefWidthProperty().bind(passwordManagerRoot.widthProperty().divide(3));
        updatedCol.setMaxWidth(Double.MAX_VALUE);
        updatedCol.setCellValueFactory(new PropertyValueFactory<PwData, String>("dateUpdated"));

        if (!postponeDataLoad) {
//            DataManager.getInstance().loadData();
//            loadDataToObservableList(data);
////            filterData(_filterTextContainer);
        }

        // Nodes to exist on panel
        TextField filterField = new TextField();
        Button addButton = new Button(_ADD_BUTTON_TEXT);
        Button removeButton = new Button(_REMOVE_BUTTON_TEXT);
        Label passwordLabel = new Label();
        Button copyButton = new Button(_COPY_BUTTON_TEXT);
        Button logoutButton = new Button(_LOGOUT_BUTTON_TEXT);

        //
        // Configuring table
        dataTable.setPadding(new Insets(0,0,1,0));
        dataTable.setItems(data);
        dataTable.getColumns().addAll(platformCol, loginCol, /*passCol, */updatedCol);
//        dataTable.setEditable(true);
        dataTable.setRowFactory(tv -> {
            TableRow<PwData> row = new TableRow<>();
            row.setOnMouseClicked(ae -> {
                if (!row.isEmpty() && ae.getButton() == MouseButton.PRIMARY
                        && ae.getClickCount() > 1) {

                    PwData clickedRow = row.getItem();
//                    selectedRow = row;
                    getItem(row.getItem());
                    passwordLabel.setText(clickedRow.getPass());
                } else {
                    passwordLabel.setText(_DOUBLE_CLICK_ROW_TEXT);
                }
                selectedRow = row;
            });
            return row ;
        });

        // Set up password label
        passwordLabel.textProperty().setValue(_DOUBLE_CLICK_ROW_TEXT);
        passwordLabel.setAlignment(Pos.CENTER);

        // Adds ability for remove button to delete saved information from table (not saved)
        removeButton.setOnAction(ae -> removeItem(_selectedItem));

        // Pressing enter will launch add interface
        addButton.setDefaultButton(true);

        // Adds ability to add data to table (not yet save)
        addButton.setOnAction(ae -> {
            Logger.debug(this, "Add button Pressed!");
            StackPane adderLayout = new StackPane();
            adderLayout.setPrefSize(passwordManagerRoot.getWidth(), passwordManagerRoot.getHeight());
            passwordManagerRoot.setVisible(false);
            addLayer(buildAdder());
        });


        // Adds ability for copy button to copy password from password field to system clipboard
        copyButton.setOnAction(ae -> { // TODO: add single-click copy source to keep password hidden
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(passwordLabel.getText());
            clipboard.setContent(content);
        });

        filterField.setPromptText(_FILTER_TEXT);

        // Adds ability for filter field to filter the list based on string entered
        filterField.textProperty().addListener((observable, oldValue, newValue) ->
                filterData(newValue));

        VBox verticalOverlay = new VBox();
        HBox buttonRow = new HBox();

        passwordLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(addButton, Priority.ALWAYS);
        HBox.setHgrow(passwordLabel, Priority.ALWAYS);
        HBox.setHgrow(removeButton, Priority.ALWAYS);
        HBox.setHgrow(copyButton, Priority.ALWAYS);
        HBox.setHgrow(logoutButton, Priority.ALWAYS);

        buttonRow.getChildren().addAll(addButton,
                                        removeButton,
                                        passwordLabel,
                                        copyButton,
                                        logoutButton);

        verticalOverlay.getChildren().addAll(filterField, dataTable, buttonRow);
        passwordManagerRoot.getChildren().add(verticalOverlay);
        passwordManagerRoot.setVisible(false); // hides layer until login or password created
        return passwordManagerRoot;
    }

    private StackPane buildAdder() {
        Logger.debug(this, "buildAdder");
        setTitle(_ADDER_TITLE);
        StackPane adderRoot = new StackPane();
        adderRoot.setPrefSize(adderRoot.getWidth(), adderRoot.getHeight());
        adderRoot.setPadding(new Insets(10,10,10,10));

        TextField platformField = new TextField();
        platformField.setPromptText(_ENTER_PLATFORM_TEXT);
        TextField loginField = new TextField();
        loginField.setPromptText(_LOGIN_BUTTON_TEXT);
        TextField passwordField = new TextField();
        passwordField.setPromptText(_PASSWORD_TEXT_DEFAULT);
        TextField passwordConfirmField = new TextField();
        passwordConfirmField.setPromptText(_CONFIRM_PASSWORD_TEXT);

        VBox nodeColumn = new VBox();
        nodeColumn.setSpacing(10);
        HBox buttonsRow = new HBox();

        Button saveButton = new Button(_SAVE_BUTTON_TEXT);
        saveButton.setDefaultButton(true);
        Button cancelButton = new Button(_CANCEL_BUTTON_TEXT);

        HBox.setMargin(saveButton, new Insets(0,10,0,10));
        HBox.setMargin(cancelButton, new Insets(0,10,0,10));

        buttonsRow.getChildren().addAll(saveButton, cancelButton);
        nodeColumn.getChildren().addAll(platformField,
                                        loginField,
                                        passwordField,
                                        passwordConfirmField,
                                        buttonsRow);
        adderRoot.getChildren().addAll(nodeColumn);

        saveButton.setOnMouseClicked(me -> {
            Logger.debug(this, "saveButton clicked");
            // TODO: Simplify code
            // TODO: STILL ADVANCES EVEN WITH FIELDS EMPTY!
            Group fieldGroup = new Group();
            fieldGroup.getChildren().addAll(platformField,
                    loginField,
                    passwordField,
                    passwordConfirmField);

            if (isFormComplete(fieldGroup.getChildren())){
                submitData(createDataFromFields(fieldGroup.getChildren()));
                backToMain();
                updateTable();
            }
        });

        saveButton.setOnKeyPressed(ke -> {
            Logger.debug(this, "Save button activated by enter key");
            if (ke.getCode() == KeyCode.ENTER) {
                // TODO: Send field data through below method
                Group fieldGroup = new Group();
                fieldGroup.getChildren().addAll(platformField,
                        loginField,
                        passwordField,
                        passwordConfirmField);

                if (isFormComplete(fieldGroup.getChildren())){
                    submitData(createDataFromFields(fieldGroup.getChildren()));
                    backToMain();
                }
            }
        });

        cancelButton.setOnKeyPressed(ke -> {
            Logger.debug(this, "ke.getCode() == " + ke.getCode());
            if(ke.getCode() == KeyCode.ESCAPE) {
                backToMain();
            }
        });

        cancelButton.setOnMouseClicked(me -> {
            Logger.debug(this, "cancelButton clicked");
            backToMain();
        });

        return adderRoot;
    }

    private void setTitle(String title) {
        UserInterfaceManager.getInstance().getWindowManager().setTitle(title);
    }

    private void prepareKey(byte[] bytes) {
        try {
            LoginManager.getInstance().sendPasswordToKeyManager(bytes);
        } catch (Exception e) {
            Logger.debug(this, "ERROR: Session key assignment failed!");
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            UserInterfaceManager.getInstance().loadData();
            loadDataToObservableList(data);
        } catch (Exception e) {
            Logger.debug(this, "ERROR OCCURRED DURING DATA LOAD!");
            e.printStackTrace();
        }
    }

    private void saveDataReturnToMain(StackPane adderLayout) {
        // TODO: Needs needs fieldGroup.getChildren data
//        submitData(createDataFromFields(fieldGroup.getChildren()));
//        removeLayer(adderLayout);
//        updateTable();
    }

    private void removeLayer(StackPane surfaceRootNode) {
        _root_root.getChildren().remove(surfaceRootNode);
        StackPane pane = (StackPane)_root_root.getChildren()
                .toArray()[_root_root.getChildren().size() - 1];
        pane.setVisible(true);
    }

    private void submitData(PwData dataFromFields) {
        Logger.debug(this, "submitData");
        DataManager.getInstance().getAllData().add(dataFromFields);
        Logger.log(dataFromFields.toString());
    }

    private PwData createDataFromFields(ObservableList<Node> fieldGroup) {
        Logger.debug(this, "createDataFromFields");
        Object[] fields = fieldGroup.toArray();
        PwData data = new PwData();
        Logger.log(((TextField)fields[0]).getText());
        Logger.log(((TextField)fields[1]).getText());
        Logger.log(((TextField)fields[2]).getText());
        Logger.log(((TextField)fields[3]).getText());
        data.setPlatform(((TextField)fields[0]).getText());
        data.setLogin(((TextField)fields[1]).getText());
        data.setPass(((TextField)fields[2]).getText());
        return data;
    }

    private boolean isFormComplete(ObservableList<Node> fieldGroup) {
        Logger.debug(this, "isForComplete");
        for (Node field : fieldGroup){
            Logger.debug(this, "***************** " + ((TextField)field).getText());
            if(((TextField)field).getText().length() < 1) {
                // TODO: Turn prompt text to red?
                // TODO: Apply message below to label?
                Logger.log("Empty field found! Did not save!");
                return false;
            }
        }
        Logger.debug(this, "All fields have been submitted!");
        return true;
    }

    private void loadDataToObservableList(ObservableList<PwData> data){
        Logger.debug(this, "loadDataToObservableList");
        Logger.debug(this, "Size of data == " + DataManager.getInstance().getAllData().toArray().length);
        for (Object datum : DataManager.getInstance().getAllData().toArray()){
            data.add((PwData)datum);
        }
    }

    private void filterData(String text) {
        Logger.debug(this, "filterData");
        ObservableList<PwData> obsTemp = FXCollections.observableArrayList();
        for (PwData data : DataManager.getInstance().getAllData()) {
            if (data.getPlatform().toLowerCase().contains(text.toLowerCase())) {
                obsTemp.add(data);
            }
        }
        dataTable.getItems().clear();
        dataTable.setItems(obsTemp);
    }

    private void removeItem(PwData selectedItem) {
        Logger.debug(this, "removeItem");
        if (DataManager.getInstance().remove(selectedItem)) {
            Logger.debug(this, "item removed");
            updateTable();
        }
        else Logger.debug(this, "Error: item not removed!");
    }

    private void updateTable() {
        Logger.debug(this, "updateTable");
        dataTable.getItems().clear();
        loadDataToObservableList(data);
        dataTable.setItems(data);
    }

    private void getItem(PwData item) {
        Logger.debug(this, "getItem");
        _selectedItem = item;
    }

    /** Compares password entered at login layout with stored password hash */
    private boolean isPasswordCorrect(String password) throws Exception {
        Logger.debug(this, "loginIfPasswordCorrect");
        return PasswordManager.getInstance().verifyPassword(password.getBytes());
    }

    private boolean submitPasswordIfValid(String password1, String password2, StackPane createRoot) {
        Logger.debug(this, "submitPasswordIfValid");

        if (compareCreatedPassword(password1, password2)){
            if (password1.length() < _PASSWORD_LENGTH_MIN) {
                updateErrorMessage(_PASSWORD_TOO_SHORT);
            } else {
                updateErrorMessage(_PASSWORD_CREATE_MATCH);
                try {

                    submitCreatedPassword(password1);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void updateErrorMessage(String message) {
        Logger.debug(this, "updateErrorMessage");
        _messageErrorLabel.setText(message);
    }

    private void submitCreatedPassword(String pass) throws IOException,
            InvalidKeySpecException, NoSuchAlgorithmException {
        Logger.debug(this, "submitCreatedPassword");
        LoginManager.getInstance().createAssignPasswordHash(pass.getBytes());
        LoginManager.getInstance().savePasswordHashFile();
        PasswordManager.getInstance().save();
    }

    private boolean compareCreatedPassword(String password1, String password2) {
        Logger.debug(this, "compareCreatedPassword");
        return password1.equals(password2);
    }

    // TODO: run shutdown through window manager instead? Might need to pass WindowManager through
    private void exitProgram() {
        Logger.debug(this, "exitProgram");
        System.exit(0);
//        _windowManager.exit();
    }

    public void createScene() {
        _scene = new Scene(_root_root, getWidth(), getHeight());
    }

    public void postponeDataLoad(boolean doPostpone) {
        postponeDataLoad = doPostpone;
    }

    /** Removes given */
    private void backToMain() {
        Logger.debug(this, "backToMain");
        removeLayer((StackPane)_root_root.getChildren().toArray()[_root_root.getChildren().size() -1]);
        setTitle(_MAIN_TITLE);
    }
}
