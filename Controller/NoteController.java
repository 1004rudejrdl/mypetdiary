package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import Model.NoteDiary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NoteController implements Initializable{
	public Stage noteStage;
	
	@FXML private TextField dTxtMeal;
	@FXML private TextField dTxtPee;
	@FXML private ComboBox<String> dCmbPoo;
	@FXML private ComboBox<String> dCmbBrush;
	@FXML private TextField dTxtPlay;
	@FXML private TextArea dTxtMemo;
	@FXML private Button dBtnClear;
	@FXML private Button dBtnSave;
	@FXML private DatePicker dDatePicker;
	
	
	//�޺��ڽ��� ����ϱ����� ObservableList 
	ObservableList<String> dListPoo = FXCollections.observableArrayList();
	ObservableList<String> dListBrush = FXCollections.observableArrayList();
	
	//DB ������ ���� ObservableList
	ObservableList<NoteDiary> noteObList=FXCollections.observableArrayList();
	ArrayList<String>dateArrayList;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//00. ��������Ŀ ���ó�¥�� ����	
		LocalDateTime ldt=LocalDateTime.now();	
		dDatePicker.setValue((	ldt.toLocalDate()) );	

		//01.�޺��ڽ��� �����Ѵ�.
		setComboBox();
		
		//02. �����ư�� ������ ��Ʈ ������ DB �� ����ȴ�. 
		dBtnSave.setOnAction(e -> saveNoteDataToDB() );
		
		//03. �Է¹��� ��� ���� �ʱ�ȭ�Ѵ�.
		dBtnClear.setOnAction(e -> textFieldClearAction() );
		
	}


	//01.�޺��ڽ��� �����Ѵ�.
	private void setComboBox() {
		//�뺯
		dListPoo.addAll("����","����","������","����","�ܴ���","����","����");
		dCmbPoo.setItems(dListPoo);
		//��ġ����
		dListBrush.addAll("��ġ�Ϸ�","��ġ����");
		dCmbBrush.setItems(dListBrush);
		
	}


	//02. �����ư�� ������ ��Ʈ ������ DB �� ����ȴ�. 
	private void saveNoteDataToDB() {
		
		String dayCheck=null;
		
		dateArrayList=NoteDAO.getDayfromDiarytb();
		for(String nd : dateArrayList) {
			if(dDatePicker.getValue().toString() .equals(nd)) {
				dayCheck="duplication"; // datepicker�� ������ ��¥�� db�� ��¥�� �ߺ��Ǹ� "duplication" �� ����
			}
		}
		
		
		////// ��ĭ�Է½� �˸��ֱ�//////
		if ((dTxtMeal.getText().isEmpty()) || (dTxtPee.getText().isEmpty()) || (dCmbPoo.getSelectionModel().isEmpty())
				|| (dCmbBrush.getSelectionModel().isEmpty()) || (dTxtPlay.getText().isEmpty())
				|| (dTxtMemo.getText().isEmpty())) {
			RootController.callAlert("�Է¿���: ��� ���� �Է����ּ���.");

			////// ���õ� ��¥�� ����� ��Ʈ�� ������ �˸��ֱ�, DB�� ���� �ȵ�.//////
		} else if (dayCheck == "duplication") {
			RootController.callAlert("��Ʈ �Է� ����: �����Ͻ� ��¥���� �̹� ��ϵ� ��Ʈ�� �����մϴ�. �ٸ� ��¥�� �������ּ���.");
			dDatePicker.setValue(null);

			return;

		} else if (dayCheck == null) {
			NoteDiary noteDiary = new NoteDiary(dDatePicker.getValue().toString(), dTxtMeal.getText(),
					dTxtPee.getText(), dCmbPoo.getSelectionModel().getSelectedItem(),
					dCmbBrush.getSelectionModel().getSelectedItem(), dTxtPlay.getText(), dTxtMemo.getText());

			noteObList.add(noteDiary);

			int count = NoteDAO.insertNoteData(noteDiary);

			if (count != 0) {
				RootController.callAlert("��Ʈ ���: ��Ʈ�� ������ ��ϵǾ����ϴ�.");
			}
			noteStage.close();
		}
	}

	
	//03. �Է¹��� ��� ���� �ʱ�ȭ�Ѵ�.
	private void textFieldClearAction() {
		dDatePicker.setValue(null);
		dTxtMeal.clear();
		dTxtPee.clear();
		dCmbPoo.getSelectionModel().clearSelection();
		dCmbBrush.getSelectionModel().clearSelection();
		dTxtPlay.clear();
		dTxtMemo.clear();
	}

}
