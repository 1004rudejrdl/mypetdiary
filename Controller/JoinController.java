package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

import Model.Join;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class JoinController implements Initializable{
	public Stage joinStage;
	
	@FXML private TextField jTxtEmail;
	@FXML private TextField jTxtName;
	@FXML private TextField jTxtBirth;
	@FXML private TextField jTxtNickName;
	@FXML private TextField jTxtPhone;
	
	@FXML private PasswordField jTxtPassWord;
	@FXML private PasswordField jTxtPassWordOk;
	@FXML private ComboBox<String> jCmbGender; // �޺��ڽ�_����,����
	@FXML private Button jBtnRegister;
	@FXML private Button jBtnCheck;
	@FXML private Button jBtnPwCheck;
	
	
	ObservableList<Join> joinMemberShip=FXCollections.observableArrayList();
	ObservableList<String> jListGender=FXCollections.observableArrayList();

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		
		//01.�޺��ڽ��� ������ �����Ѵ�.
		setComboBoxGender();
		
		//02. �ؽ�Ʈ�ʵ� �Է°� ���伳��___�� ���̵�� @ ����, �� ��й�ȣ,������� 8�ڸ��� �Է¹޵���
		setTextFieldInputFormat();
			
		//03.��ȣ�� ���� �Է°� ����
		jTxtName.textProperty().addListener(inputDecimalFormatName); 	// ����, �ѱ۸� �Է�, 5���� ������
		jTxtNickName.textProperty().addListener(inputDecimalFormatName); 	
		inputDecimalFormatElevenDigit(jTxtPhone); 		//�ڵ��� ��ȣ 11�ڸ� ���� 
		
				
		//04. ���̵� �ߺ�Ȯ��___ ���̵� DB���� ���ؼ� �ߺ��Ǵ��� Ȯ���ϰ�, �̸��� ���Ŀ� �´����� Ȯ�� �� ������ ���� �Է��Ѵ�.
		jBtnCheck.setOnAction(e -> hadlejBtnIDcheckAction() );

		//05. ��й�ȣ Ȯ��
		jBtnPwCheck.setOnAction(e->matchPasswordField()  );
		
		//06.�Ϸ��ư�� ������ ȭ���� �α��� ȭ������ �ٽ� ��ȯ�ȴ�. + membershipDB �� �� ������ ����ȴ�.
		jBtnRegister.setOnAction(e -> handlejBtnRegister() );

		
		
	}

	//01.�޺��ڽ��� ������ �����Ѵ�.
	private void setComboBoxGender() {
		jListGender.addAll("����","����");
		jCmbGender.setItems(jListGender);		
	}

	
	//02. �ؽ�Ʈ�ʵ� �Է°� ���伳��___�� �̸����� @ ����, �� ��й�ȣ,������� 8�ڸ��� �Է¹޵���
	private void setTextFieldInputFormat() {
		inputDecimalFormatEightDigit( jTxtPassWord);
		inputDecimalFormatEightDigit( jTxtPassWordOk);
		inputDecimalFormatEightDigit( jTxtBirth);
	
	}

	
	///03. ��ȣ�� ���� �Է°� ����
	ChangeListener<String> inputDecimalFormatName = (observable, oldValue, newValue) -> {

	      if (newValue != null && !newValue.equals("")) {

	         if (!newValue.matches("\\D*") || newValue.length() > 5) {

	            ((StringProperty) observable).setValue(oldValue);
	         }
	      }
	   };
	
	   
	// 04. ���̵�(�̸���) �ߺ�Ȯ��___ �̸����� DB���� ���ؼ� �ߺ��Ǵ��� Ȯ���ϰ�, �̸��� ���Ŀ� �´����� Ȯ�� �� ������ ���� �Է��Ѵ�.
	private void hadlejBtnIDcheckAction() {
		
		if(jTxtEmail.getText().isEmpty()) {
			RootController.callAlert("�̸��� �Է� ����: ��ĭ ���� �Է����ֽñ� �ٶ��ϴ�.");
			return;
		}
		
		// 04_01. �̸��� �ؽ�Ʈ�ʵ��� ���� '@'�� �����ϰ� �ִ°��� Ȯ���ϴ� �۾�
		if (!(jTxtEmail.getText().contains("@"))) {
			RootController.callAlert("�̸��� �Է� ����: �̸����� ������ �߸��Ǿ����ϴ�.");
			return;
		} 
		
		// 04_02. �Է¹��� �̸����� DB�� �̹� �ִ°� Ȯ���ϴ� �۾�
		if	(emailCheck(jTxtEmail.getText())) {
			RootController.callAlert("���̵� �ߺ�: ���Ұ����� ���̵��Դϴ�. �ٽ� �Է¹ٶ��ϴ�.");
			jTxtEmail.clear();
		}else {
			RootController.callAlert("���̵� ��밡��: ��밡���� ���̵��Դϴ�.");
			jTxtEmail.setDisable(true);
			jBtnCheck.setDisable(true);
		}	
		
	}

	
	//04_02_01.�̸��� �ߺ�Ȯ�ο��� ���̴� �Լ�
	public boolean emailCheck(String email) {
		boolean result=false;
		
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		
		String sql ="select email from joinmembershiptb where email=? ";
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, email);
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				result=true;
			}else {
				result=false;
			}
			rs.close();	
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {	pstmt.close(); }
				if (con != null) {	con.close(); }
			} catch (Exception e) {	
				e.printStackTrace();
			}
		}
		return result;
	}
	


	// 05. ��й�ȣ Ȯ��
	private void matchPasswordField() {
		if(!(jTxtPassWord.getText().equals(jTxtPassWordOk.getText()))) {
			RootController.callAlert("��й�ȣ ��ġ ����: ��й�ȣ�� ��ġ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
			jTxtPassWord.clear(); 
			jTxtPassWordOk.clear();
		}else if(jTxtPassWord.getText().isEmpty() 	||	jTxtPassWordOk.getText().isEmpty() ){
			RootController.callAlert("��й�ȣ�Է� ����: ��ĭ ���� �Է����ּ���.");
		}else {
			RootController.callAlert("��й�ȣ ��ġ: ��й�ȣ�� ��ġ�մϴ�.");
			jTxtPassWord.setDisable(true);
			jTxtPassWordOk.setDisable(true);
			jBtnPwCheck.setDisable(true);
		}
	}


	

	
	//06.�Ϸ��ư�� ������ ȭ���� �α��� ȭ������ �ٽ� ��ȯ�ȴ�. + membershipDB �� �� ������ ����ȴ�.
	private void handlejBtnRegister() {	
		
		if( jBtnCheck.isDisable() &&  jBtnPwCheck.isDisable()) {
			
			if(jTxtEmail.getText().isEmpty() ||
					jTxtPassWord.getText().isEmpty() ||
					jTxtName.getText().isEmpty() ||
					jTxtBirth.getText().isEmpty() ||
					jCmbGender.getSelectionModel().isEmpty() ||
					jTxtNickName.getText().isEmpty() ||
					jTxtPhone.getText().isEmpty()) {
				
				RootController.callAlert("ȸ������ ����: ��ĭ���� ��� �Է¹ٶ��ϴ�.");
				return;
			}
			
			try {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
				Parent root = loader.load();
				
				RootController rootController=loader.getController();
				rootController.primaryStage=primaryStage;
				
				Scene scene=new Scene(root);
				primaryStage.setScene(scene);
				joinStage.close();
				primaryStage.show();
				
				
				Join join=new Join(	
						jTxtEmail.getText(),
						jTxtPassWord.getText(),
						jTxtName.getText(),
						jTxtBirth.getText(),
						jCmbGender.getSelectionModel().getSelectedItem(),
						jTxtNickName.getText(),
						jTxtPhone.getText()		
						);				
				joinMemberShip.add(join);
				
				
				int count=JoinDAO.insertJoinData(join);
				if(count!=0) {
					System.out.println("�Է¼���: �����ͺ��̽� �Է��� ����");
					RootController.callAlert("ȸ�����ԿϷ�: ȸ�������� �Ϸ�Ǿ����ϴ�.");
				}
				
			} catch (IOException e) {
				System.out.println("ȭ����ȯ ����: ȭ����ȯ �����߻�");						
			}		
		}else{
				RootController.callAlert( "�ߺ�Ȯ�� �� ��й�ȣȮ�� : ���̵� �ߺ�Ȯ�ΰ� ��й�ȣȮ���� �ݵ�� �������ּ���.");
		}
		
		
	}

	
	// �Է°� �ʵ� ���� ���� ���: ���� 8 �ڸ� ����___ ��й�ȣ, �������
	private void inputDecimalFormatEightDigit(TextField textField) {

		DecimalFormat format = new DecimalFormat("########");
		// ���� �Է½� ���� ���� �̺�Ʈ ó��
		textField.setTextFormatter(new TextFormatter<>(event -> {
			// �Է¹��� ������ ������ �̺�Ʈ�� ������.
			if (event.getControlNewText().isEmpty()) {
				return event;
			}
			// ������ �м��� ���� ��ġ�� ������.
			ParsePosition parsePosition = new ParsePosition(0);
			// �Է¹��� ����� �м���ġ�� �������������� format ����� ��ġ���� �м���.
			Object object = format.parse(event.getControlNewText(), parsePosition);
			// ���ϰ��� null �̰ų�,�Է��ѱ��̿ͱ����м���ġ���� ���������(�� �м������������� ����)�ų�,�Է��ѱ��̰� 9�̸�(8�ڸ��� �Ѿ����� ����.)
			// �̸� null ������.
			if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
					|| event.getControlNewText().length() == 9) {
				return null;
			} else {
				return event;
			}
		}));
		
	}
	
	// �Է°� �ʵ� ���� ���� ���: ���� 11 �ڸ� ����___ �ڵ�����ȣ
	private void inputDecimalFormatElevenDigit(TextField textField) {

			DecimalFormat format = new DecimalFormat("###########");
			// ���� �Է½� ���� ���� �̺�Ʈ ó��
			textField.setTextFormatter(new TextFormatter<>(event -> {
				// �Է¹��� ������ ������ �̺�Ʈ�� ������.
				if (event.getControlNewText().isEmpty()) {
					return event;
				}
				// ������ �м��� ���� ��ġ�� ������.
				ParsePosition parsePosition = new ParsePosition(0);
				// �Է¹��� ����� �м���ġ�� �������������� format ����� ��ġ���� �м���.
				Object object = format.parse(event.getControlNewText(), parsePosition);
				// ���ϰ��� null �̰ų�,�Է��ѱ��̿ͱ����м���ġ���� ���������(�� �м������������� ����)�ų�,�Է��ѱ��̰� 12�̸�(11�ڸ��� �Ѿ����� ����.)
				// �̸� null ������.
				if (object == null || parsePosition.getIndex() < event.getControlNewText().length()
						|| event.getControlNewText().length() == 12 || event.getControlNewText()=="-") {
					return null;
				} else {
					return event;
				}
			}));
			
		}

	
}
