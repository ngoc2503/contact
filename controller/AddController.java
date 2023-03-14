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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddController implements Initializable {
    private ObservableList<Contact> observableList = FXCollections.observableArrayList();
    private final String CONTACT = "src/Data/contact.txt";
    @FXML
    private TextField firstNameId, lastNameId, phoneId, emailId;
    @FXML
    private DatePicker birthDateId;
    @FXML
    private ComboBox<Group> groupId;
    @FXML
    private Button buttonSave;
    @FXML
    private Label labelFirstName, labelLastName,labelPhone, labelEmail, labelBirthDate;

    /**
     * setObservableList
     * @param observableList
     */
    public void setObservableList(ObservableList<Contact> observableList) {
        this.observableList = observableList;
    }

    /**
     * SetGroup
     * @param observableList
     */
    public void setGroupId(ObservableList<Group> observableList) {
        groupId.setValue(observableList.get(1));
        groupId.setItems(observableList);
    }

    /**
     * Sự kiện khi người dùng ấn vào nút add
     */
    @FXML
    private void addNewContact(ActionEvent event) throws Exception{

        String firstName = firstNameId.getText();
        if (firstName.trim().equals("")) {
            labelFirstName.setText("Trường này không được bỏ trống.");
            return;
        }
        labelFirstName.setText("");
        String lastName = lastNameId.getText();
        if (lastName.trim().equals("")){
            labelLastName.setText("Trường này không được bỏ trống.");
            return;
        }
        labelLastName.setText("");
        String phone = phoneId.getText();
        String phonePartem = "[\\d]{9,10}";
        if (phone.matches(phonePartem)) {
            labelPhone.setText("");
        }else{
            labelPhone.setText("Số điện thoại không hợp lệ");
            return;
        }
        String email = emailId.getText();
        String emailPartem = "\\w+@\\w+.\\w+(.\\w+){0,}";
        if (email.matches(emailPartem)) {
            labelEmail.setText("");
        }else{
            labelEmail.setText("Email không hợp lệ");
            return;
        }
       // String birthDate = birthDateId.getValue().toString();
        LocalDate birth = birthDateId.getValue();
        if (birth == null) {
            labelBirthDate.setText("Chưa chọn ngày sinh");
            return;
        }
        labelBirthDate.setText("");
        String group = groupId.getValue().getGroupName();

        // Tạo đối tượng Contact
        Contact mcontact = new Contact(firstName, lastName, phone, email, birth.toString(), group);
        // Thêm vào contacts
        observableList.add(mcontact);
        ReadAndWrite.writeContact(observableList, CONTACT);
        // Đóng cửa sổ
        closeScene(event);
    }
    /**
     * SỰ kiện khi người dùng ấn vào nút close
     * @param event
     */
    @FXML
    private void closeScene(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Group>() {
            @Override
            public void changed(ObservableValue<? extends Group> observableValue, Group group, Group t1) {
                if (t1.getGroupName().equals("All")) buttonSave.setDisable(true);
                else buttonSave.setDisable(false);
            }
        });
    }
}
