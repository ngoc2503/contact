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

public class UpdateController implements Initializable {

    private ObservableList<Contact> observableList = FXCollections.observableArrayList();
    private int index; // Vị trí trong observableList.
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
    private Label labelFirstName, labelLastName, labelPhone, labelEmail;

    /**
     * setObservableList
     * @param observableList
     */
    public void setObservableList(ObservableList<Contact> observableList){
        this.observableList = observableList;
    }

    /**
     * THiết lập vị trí người dùng đang chọn trên danh sách.
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public void setData(Contact contact) {
        firstNameId.setText(contact.getFirstName());
        lastNameId.setText(contact.getLastName());
        phoneId.setText(contact.getPhone());
        emailId.setText(contact.getEmail());
        String birthDate = contact.getBirthDate();
        int year = Integer.parseInt(birthDate.substring(0,birthDate.indexOf("-")));
        int month = Integer.parseInt(birthDate.substring(birthDate.indexOf("-")+1, birthDate.lastIndexOf("-")));
        int day = Integer.parseInt(birthDate.substring(birthDate.lastIndexOf("-")+1));
        birthDateId.setValue(LocalDate.of(year, month, day));
        groupId.setValue(new Group(contact.getGroupName()));
    }

    public void setGroupId(ObservableList<Group> observableList) {
        groupId.setItems(observableList);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        groupId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Group>() {
            @Override
            public void changed(ObservableValue<? extends Group> observableValue, Group group, Group t1) {
                if(t1.getGroupName().equals("All")) {
                    buttonSave.setDisable(true);
                }else{
                    buttonSave.setDisable(false);
                }
            }
        });
    }

    /**
     * Bắt sự kiện khi ngươi dùng nhấn vào button close
     * @param event
     */
    @FXML
    private void closeScene(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Sự kiện khi người dùng ấn vào BUtton Sava
     * @param event
     */
    public void saveNewContact(ActionEvent event) throws Exception{
        // Lấy giá trị từ các trường
        String firstName = firstNameId.getText();
        if (firstName.trim().equals("")) {
            labelFirstName.setText("Trường này không được để trống");
            return;
        }
        labelFirstName.setText("");
        String lastName = lastNameId.getText();
        if (lastName.trim().equals("")){
            labelLastName.setText("Trường này không được để trống");
            return;
        }
        labelLastName.setText("");
        String phone = phoneId.getText();
        String phonePartem = "[\\d]{9,11}";
        if (phone.matches(phonePartem)) {
            labelPhone.setText("");
        }else{
            labelPhone.setText("Số điện thoại không hợp lệ.");
            return;
        }
        String email = emailId.getText();
        String emailPartem = "\\w+@\\w+.\\w+(\\w+){0,}";
        if (email.matches(emailPartem)) {
            labelEmail.setText("");
        }else{
            labelEmail.setText("Email không hợp lệ");
            return;
        }
        String birthDate = birthDateId.getValue().toString();
        String group = groupId.getValue().toString();
        // Tạo đối tượng Contact mới
        Contact contact = new Contact(firstName, lastName, phone, email, birthDate, group);
        // Cập nhật vào trong danh sách
        observableList.remove(index);
        observableList.add(index, contact);
        ReadAndWrite.writeContact(observableList, CONTACT);
        closeScene(event);
    }
}
