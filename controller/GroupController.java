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
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GroupController implements Initializable {
    private ObservableList<Group> observableListGroup = FXCollections.observableArrayList();
    private ObservableList<Contact> contactObservableList = FXCollections.observableArrayList();
    private final String GROUP = "src/Data/group.txt";
    private final String CONTACT = "src/Data/contact.txt";

    @FXML
    private ListView<Group> listViewGroup;
    @FXML
    private TextField textFieldGroup, textFieldSearch;
    @FXML
    private Button updateId, deleteId, buttonSearch;

    /**
     * Đóng cửa sổ chương trình
     * @param event
     */
    @FXML
    private void closeNowScene(ActionEvent event){
       Node sourse = (Node) event.getSource();
       Stage stage = (Stage) sourse.getScene().getWindow();
       stage.close();
    }

    /**
     * Hiển thị danh sách group vào trong một listView.
     * @param observableList
     */
    public void setListViewGroup(ObservableList<Group> observableList) {
        this.observableListGroup = observableList;
        listViewGroup.setItems(observableListGroup);
    }
    /**
     * set contactObservableList
     */
    public void setContactObservableList(ObservableList<Contact> contactObservableList) {
        this.contactObservableList = contactObservableList;
    }

    /**
     * THêm phần tử vào trong group ListView
     */
    @FXML
    private void addNewGroup(ActionEvent event) throws Exception{
        // Lấy dữ liệu từ textField
        String newGroup = textFieldGroup.getText();
        // Kiểm tra nếu rỗng return
        if (newGroup.trim().equals("")){
            nameIsEmpty();
            return;
        }
        // Nếu tên group đang thêm vào đã tồn tại return
        for (int i = 0; i < observableListGroup.size(); i++) {
            if (new Group(newGroup).getGroupName().equals(observableListGroup.get(i).getGroupName())){
                return;
            }
        }
        // Thêm group mới vào trong danh sách
        observableListGroup.add(new Group(newGroup));
        textFieldGroup.setText("");
        // Ghi dữ liệu vào file
        ReadAndWrite.writeGroup(observableListGroup, GROUP);
    }

    /**
     * Chỉnh sửa group
     * @param event
     */
    @FXML
    private void updateGroup(ActionEvent event) throws Exception{
        Group group = listViewGroup.getSelectionModel().getSelectedItem();
        if(group == null) {
            showErrorDialog();
            return;
        }
        String newGoupName = textFieldGroup.getText();
        if (newGoupName.trim().equals("")) {
            nameIsEmpty();
            return;
        }
        // Vị trí của group trong listView
        int indexSelect = listViewGroup.getSelectionModel().getSelectedIndex();
        // Lấy giá trị từ textField tạo group mới
        Group newGroup = new Group(newGoupName);
        // Xóa group cũ trong danh sách và cập nhật tên group mới vào đúng vị trí
        observableListGroup.remove(indexSelect);
        observableListGroup.add(indexSelect, newGroup);
        ObservableList<Contact> ct = FXCollections.observableArrayList();
        // Cập nhật lại bảng contact
        for (Contact x : contactObservableList) {
            if (x.getGroupName().equalsIgnoreCase(group.getGroupName())){
                x.setGroupName(newGroup.getGroupName());
            }
            ct.add(x);
        }
        contactObservableList.clear();
        contactObservableList.addAll(ct);
        // Ghi mới các dữ liệu cập nhật xuống file
        ReadAndWrite.writeGroup(observableListGroup, GROUP);
        ReadAndWrite.writeContact(contactObservableList, CONTACT);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set Disable cho button search
        buttonSearch.setDisable(true);
        // Bắt sự kiện trên listView
        listViewGroup.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Group>() {
            @Override
            public void changed(ObservableValue<? extends Group> observableValue, Group group, Group t1) {
                // Không cho chỉnh sửa group có tên là All
                if (t1.getGroupName().equals("All")) {
                    updateId.setDisable(true);
                    deleteId.setDisable(true);
                }
                else{
                    updateId.setDisable(false);
                    deleteId.setDisable(false);
                }
                textFieldGroup.setText(t1.getGroupName());
            }
        });
    }

    /**
     * Xóa phần tử trong group
     */
    @FXML
    private void deleteGroup() throws Exception{
        // Lấy group đang được chọn
        Group group = listViewGroup.getSelectionModel().getSelectedItem();
        // Kiểm tra group có rỗng không nếu rỗng thì return
        if(group == null) {
            showErrorDialog();
            return;
        }
        // Nếu xóa tên group đồng thời xóa toàn bộ danh bạ có chứa tên group này
        ObservableList<Contact> ct = FXCollections.observableArrayList();

        for (Contact contact : contactObservableList) {
            if (contact.getGroupName().equalsIgnoreCase(group.getGroupName()) == false) {
                ct.add(contact);
            }
        }
        // Hiển thị hộp thoại cảnh báo người dùng nếu xóa group
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Contact");
        alert.setContentText("Nếu bạn xóa tên group\nToàn bộ danh bạ có tên group này cũng bị xóa");
        Optional<ButtonType> result = alert.showAndWait();
        // Người dùng xác nhận xóa
        if (result.get() == ButtonType.OK) {
            contactObservableList.clear();
            contactObservableList.addAll(ct);
            // Xóa tên group khỏi danh sách
            observableListGroup.remove(group);
            // Ghi lại dữ liệu vào file
            ReadAndWrite.writeGroup(observableListGroup, GROUP);
            ReadAndWrite.writeContact(contactObservableList, CONTACT);
        }else return;
    }

    /**
     * Khi người dùng chưa chọn vào item có trong group thì hiển thị hộp thoại báo lỗi
     */
    private void showErrorDialog() {
        Alert alertError = new Alert(Alert.AlertType.ERROR);
        alertError.setTitle("Contact");
        alertError.setContentText("Bạn phải chọn một item có trong group!");
        alertError.showAndWait();
    }

    /**
     * Tìm kiếm một item trong group
     */

    @FXML
    private void searchItem() {
        // Lấy dữ liệu từ TextField
        String textSearch = textFieldSearch.getText();
        ObservableList<Group> listGroupSearch = FXCollections.observableArrayList();
        // Tìm tên group có trong list group
        for (int i = 1; i < observableListGroup.size(); i++) {
            if (observableListGroup.get(i).getGroupName().toLowerCase().contains(textSearch.toLowerCase())){
                listGroupSearch.add(observableListGroup.get(i));
            }
        }
        listViewGroup.setItems(listGroupSearch);
    }

    /**
     * Bắt sự kiện textfield khi người dùng tìm kiếm trong group.
     * @param keyEvent
     */
    public void keySearchPressed(KeyEvent keyEvent) {
        if (textFieldSearch.getText().trim().equals("")) {
            buttonSearch.setDisable(true);
            listViewGroup.setItems(observableListGroup);
        }else{
            buttonSearch.setDisable(false);
        }
    }
    /**
     * Hộp thoại này sẽ được hiện khi người dùng chưa nhập tên cho group
     */
    private void nameIsEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Contact");
        alert.setContentText("Bạn chưa nhập tên cho group");
        alert.showAndWait();
    }
}
