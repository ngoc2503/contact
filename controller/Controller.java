package controller;

import Entity.Contact;
import Entity.Group;
import Entity.ReadAndWrite;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private ObservableList<Contact> listContact = FXCollections.observableArrayList();
  /*  private ObservableList<Contact> listContact = FXCollections.observableArrayList(
            new Contact("Linh", "Nguyễn", "0362456851", "linh@gmail.com",
                     "1995-5-12", "Family"),
            new Contact("Ngọc", "Nguyễn", "0362526314", "ngoc@gmail.com",
                    "1998-3-10", "Friend"),
            new Contact("Huyền", "Trịnh", "0962456850", "huyen@gmail.com",
                    "1995-7-22", "Colleagues"),
            new Contact("Minh", "Vũ", "0826598547", "minh@gmail.com",
                    "1990-9-02", "Family"),
            new Contact("Trung", "Nguyễn", "0983265214", "trung@gmail.com",
                    "1996-8-09", "Family")
    );*/
    private ObservableList<Group> listGroups = FXCollections.observableArrayList();
    /*private ObservableList<Group> listGroups = FXCollections.observableArrayList(
            new Group("All"), new Group("Family"),
           new Group("Friend"),new Group("Colleagues")
    );*/
   @FXML
    TableView<Contact> table;
   @FXML
   private TableColumn firstName;
   @FXML
   private TableColumn lastName;
   @FXML
   private TableColumn phone;
   @FXML
   private TableColumn email;
   @FXML
   private TableColumn birthDate;
   @FXML
   private TableColumn groupName;
   @FXML
   private AnchorPane anchorPaneMain;
   @FXML
   private ComboBox<Group> comboBoxGroup;
   @FXML
   private TextField textFieldSearchId;
   @FXML
   private Button buttonIdSearch;
    private final String CONTACT = "src/Data/contact.txt";
    private final String GROUP = "src/Data/group.txt";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        try {
            listContact = ReadAndWrite.loadContact(CONTACT);
            listGroups = ReadAndWrite.loadGroup(GROUP);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Set Dissable for button search
        buttonIdSearch.setDisable(true);
        // TableView Contact
        firstName.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));
        phone.setCellValueFactory(new PropertyValueFactory<Contact, String>("phone"));
        email.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));
        birthDate.setCellValueFactory(new PropertyValueFactory<Contact, String>("birthDate"));
        groupName.setCellValueFactory(new PropertyValueFactory<Contact, String>("groupName"));
        table.setItems(listContact);

        // ComboBoxGroup
        comboBoxGroup.setValue(listGroups.get(0));
        comboBoxGroup.setItems(listGroups);

        // Bắt sự kiện khi người dùng chọn trong ComboBox
        comboBoxGroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Group>() {
            @Override
            public void changed(ObservableValue<? extends Group> observableValue, Group group, Group t1) {
                ObservableList<Contact> contacts = FXCollections.observableArrayList();
                if (t1.getGroupName().equals("All")){
                    table.setItems(listContact);
                    return;
                }
                for (int i = 0; i < listContact.size(); i++) {
                    Contact contact = listContact.get(i);
                    if(t1.getGroupName().equals(contact.getGroupName())) {
                        contacts.add(contact);
                    }
                }
                table.setItems(contacts);
            }
        });
    }

    /**
     * Thêm liên lạc mới khi người dùng click vào Button Add.
     * @param event
     */
    public void addNewContact(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../UI/addContact.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Contact");
            stage.setScene(scene);
            // Tạo đối tượng addController
            AddController addController = loader.getController();
            // Call setGroupId();
            addController.setGroupId(listGroups);
            // Call setObservableList
            addController.setObservableList(listContact);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteContact(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() == null) {
            showDialogError();
            return;
        }
        // Tạo alertDialor confirmation
        Alert alertDialog = new Alert(Alert.AlertType.CONFIRMATION);
        alertDialog.setTitle("Contact");
        alertDialog.setHeaderText("Delete a contact.");
        alertDialog.setContentText("Bạn có chắc là muốn xóa liên lạc này không");
        Optional<ButtonType> result = alertDialog.showAndWait();
        Contact contactSelected = table.getSelectionModel().getSelectedItem();
        if (result.get() == ButtonType.OK) {
            listContact.remove(contactSelected);
        }
        // Xóa xong phần tử đồng thời ghi dữ liệu xuống file
        try {
            ReadAndWrite.writeContact(listContact, CONTACT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void updateContact(ActionEvent event) {

        if (table.getSelectionModel().getSelectedItem() == null) {
            showDialogError();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../UI/updateContact.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Contact");
            stage.setScene(scene);
            // Tạo đối tượng UpdateController
            UpdateController updateController = loader.getController();
            // Gọi hàm setData(Contact contact) trong Updatacontroller
            updateController.setData(table.getSelectionModel().getSelectedItem());
            // Gọi hàm setGroup();
            updateController.setGroupId(listGroups);
            // Gọi hàm setBbservableList()
            updateController.setObservableList(listContact);
            // Gọi hàm setIndex().
            updateController.setIndex(table.getSelectionModel().getSelectedIndex());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Cập nhật xong phần tử đồng thời ghi dữ liệu xuống file
    }

    /**
     * Hiển thị hộp thoại cho phép người dùng quản lý các group
     */
    public void showDialogGroupname() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../UI/group.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            GroupController groupController = loader.getController();
            groupController.setListViewGroup(listGroups);
            groupController.setContactObservableList(listContact);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tìm kiếm trong Contact
     */
    @FXML
    private void showSearchButton(KeyEvent keyEvent) {
        if (textFieldSearchId.getText().trim().equals("")) {
            table.setItems(listContact);
            buttonIdSearch.setDisable(true);
        }else {
            buttonIdSearch.setDisable(false);
        }
    }

    @FXML
    private void searchContact() {
        String textSearch = textFieldSearchId.getText();
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        for (int i = 0; i < listContact.size(); i++) {
            Contact contact = listContact.get(i);
            if (contact.toString().toLowerCase().contains(textSearch.toLowerCase())) {
                contacts.add(contact);
            }
        }
        if (contacts.size() == 0) {
            showDialogSearchError();
            return;
        }
        table.setItems(contacts);
    }
    private void showDialogError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Contact");
        alert.setContentText("Please select a contact.");
        alert.showAndWait();
    }
    private void showDialogSearchError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Contact");
        alert.setContentText("Kiểm tra lại dữ liệu tìm kiếm.");
        alert.showAndWait();
    }
}
