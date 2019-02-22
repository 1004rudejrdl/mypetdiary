package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Join;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootController implements Initializable {
	public Stage primaryStage;
	public static String EMAIL;
	@FXML private TextField txtId;
	
	@FXML private TextField txtPassWord;
	@FXML private Button btnLogin;
	@FXML private Button btnJoin;
	@FXML private Button btnFindId;
	@FXML private Button btnFindPassWord;
	@FXML private ImageView imgViewLogo;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//00. �̹��� ��ư�� ������ ���̵�� ��й�ȣ�� �ڵ����� ���õǴ� ���(�׽�Ʈ�� ���� �Ϸ���)
		imgViewLogo.setOnMouseClicked(e -> clickImgLoginSetting() );
	
		// 00. ȸ������ ��ư�� ������ ȸ������ â�� ���.
		btnJoin.setOnAction(e-> handleBtnJoinAction() );
		
		// 01. �α��� ��ư�� ������ ����ȭ������ â�� �ٲ��.
		btnLogin.setOnAction(e -> handleBtnLoginAction() );
			
		//02. ���̵�ã�� ��ư�� ������ ���̵�ã�� â�� ���. __ ���̾�α�â
		btnFindId.setOnAction(e -> handleBtnFindIdAction() );

		//03. ��й�ȣã�� ��ư�� ������ ��й�ȣã�� â�� ���.__ ���̾�α�â
		btnFindPassWord.setOnAction(e->handleBtnFindPassWordAction()  );
		
	}

	//00. �̹��� ��ư�� ������ ���̵�� ��й�ȣ�� �ڵ����� ���õǴ� ���(�׽�Ʈ�� ���� �Ϸ���)
	private void clickImgLoginSetting() {
		txtId.setText("aaa@naver.com");
		txtPassWord.setText("12345678");
		
	}


	// 00. ȸ������ ��ư�� ������ ȸ������ â�� ���.
	private void handleBtnJoinAction() {
		try {
			Stage joinStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/join.fxml"));
			Parent root = loader.load();
			
			JoinController joinController=loader.getController();
			joinController.joinStage=joinStage;
			
			Scene scene=new Scene(root);
			joinStage.setScene(scene);
			primaryStage.close();
			joinStage.setTitle("ȸ������");
			joinStage.show();
		
		} catch (Exception e) {
			e.printStackTrace();
			callAlert("ȭ�� ��ȯ ����: ȭ�� ��ȯ�� ���� �߻�");
		}

		
	}

	
	// 01. �α��� ��ư�� ������ ����ȭ������ â�� �ٲ��.
	private void handleBtnLoginAction() {
		
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		
		String email=txtId.getText().trim();
		String pw= txtPassWord.getText().trim();	
		
		String sql = "select email, password " + "from joinmembershiptb where email=? and password=? ";
		
		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, email);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				email=rs.getString("email");
				callAlert("�α��� ����: �α��� �����Ͽ����ϴ�.");

				EMAIL = email;
				try {
					Stage mainStage = new Stage();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main.fxml"));
					Parent root = loader.load();
								
					MainController mainController=loader.getController();
					mainController.mainStage=mainStage;
					
					Scene scene=new Scene(root);
					mainStage.setScene(scene);
					primaryStage.close();
					mainStage.setTitle("����������");
					mainStage.show();
					
				} catch (IOException e) {
					callAlert("ȭ�� ��ȯ ����: ����ȭ�� ��ȯ�� ���� �߻�");
				}

				}else {
					callAlert("�α��� ����:�α��ν����Ͽ����ϴ�." );
					
				}
			
				rs.close();
				
		
		} catch (Exception e1) {
			System.out.println("������ ���� : email, password �񱳿� �����߽��ϴ�. ���˹ٶ�");
			e1.printStackTrace();
			
		} finally {
			// �ڿ���ü�� �ݾƾ��Ѵ�.
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				RootController.callAlert("�ڿ��ݱ� ���� : psmt,con �ݱ����");
			}
		}
	}


	//02. ���̵�ã�� ��ư�� ������ ���̵�ã�� â�� ���. __ ���̾�α�â
	private void handleBtnFindIdAction() {
		String id=null;
		//ArrayList <Join> dbArrayListJoin;
		try {
			Stage idSearchStage = new Stage(StageStyle.UTILITY);
			idSearchStage.initModality(Modality.WINDOW_MODAL);
			idSearchStage.initOwner(primaryStage);
			idSearchStage.setTitle("���̵� ã��");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/idsearch.fxml"));
			Parent root = loader.load();
			
			
			// id ã�ƿ���
			TextField idStxtName=(TextField)root.lookup("#idStxtName");
			TextField idStxtPhone=(TextField)root.lookup("#idStxtPhone");
			Button idSbtnOk=(Button)root.lookup("#idSbtnOk");
			
			
			//02_01.�޴�����ȣ 11�ڸ� ���� 
			inputDecimalFormatElevenDigit(idStxtPhone); 
			
			Scene scene=new Scene(root);
			idSearchStage.setScene(scene);
			idSearchStage.show();
			
			idSbtnOk.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					
					Connection con=null;
					PreparedStatement pstmt=null;
					ResultSet rs=null;
	
					String idSname=idStxtName.getText().trim();
					String idSphone= idStxtPhone.getText().trim();	
					
					String sql = "select email " + "from joinmembershiptb where name=? and phone=? ";
					
					try {				
						con = DBUtility.getConection();
						pstmt = con.prepareStatement(sql);
						pstmt.setString(1, idSname);
						pstmt.setString(2, idSphone);
						
						rs = pstmt.executeQuery();
						
						if(rs.next()) {
							rs.getString("email");
					
							RootController.callAlert("���̵� ã�� �Ϸ�:"+rs.getString("email"));				
							txtId.setText(rs.getString("email"));
							idSearchStage.close();
						}else {
							callAlert("���̵� ã�� ���: ã���ô� ���̵� �����ϴ�.");
						}
						
					
					
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
										
					} //end of  try-catch (DBUtility.getConnecton() �� try-catch)

				}
			});
			
		} catch (IOException e) { 
			System.out.println("���̵�ã�� â�� ����� �ȶ����ϴ�.");
		}

	}



	//03. ��й�ȣã�� ��ư�� ������ ��й�ȣã�� â�� ���.__ ���̾�α�â
	private void handleBtnFindPassWordAction() {
		try {
			Stage pwSearchStage = new Stage(StageStyle.UTILITY);
			pwSearchStage.initModality(Modality.WINDOW_MODAL);
			pwSearchStage.initOwner(primaryStage);
			pwSearchStage.setTitle("��й�ȣ ã��");

			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/passwordsearch.fxml"));
			Parent root = loader.load();
			
			
			// id ã�ƿ���
			TextField pwStxtName=(TextField)root.lookup("#pwStxtName");
			TextField pwStxtEmail=(TextField)root.lookup("#pwStxtEmail");
			TextField pwStxtBirth=(TextField)root.lookup("#pwStxtBirth");
			TextField pwStxtPhone=(TextField)root.lookup("#pwStxtPhone");
			Button pwSbtnOk=(Button)root.lookup("#pwSbtnOk");
			Button pwSbtnIdSearch=(Button)root.lookup("#pwSbtnIdSearch");
			
			Scene scene=new Scene(root);
			pwSearchStage.setScene(scene);
			pwSearchStage.show();
				

			
			//03_02. ������� 8�ڸ�
			inputDecimalFormatEightDigit(pwStxtBirth);
			
			//03_03. �޴�����ȣ 11�ڸ�
			inputDecimalFormatElevenDigit(pwStxtPhone);
			
			pwSbtnOk.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Connection con=null;
					PreparedStatement pstmt=null;
					ResultSet rs= null;
					
					String sql= "select password "+"from joinmembershiptb where name=? and email=? and birth=? and phone=? " ;
					
					try {
						con=DBUtility.getConection();
						pstmt=con.prepareStatement(sql);
					
						pstmt.setString(1, pwStxtName.getText().trim());
						pstmt.setString(2, pwStxtEmail.getText().trim());
						pstmt.setString(3, pwStxtBirth.getText().trim());
						pstmt.setString(4, pwStxtPhone.getText().trim());
					
						rs=pstmt.executeQuery();
						
						if(rs.next()) {
							rs.getString("password");
							
							callAlert("��й�ȣ ã�� �Ϸ�: "+rs.getString("password") );
							pwStxtName.clear();
							pwStxtEmail.clear();
							pwStxtBirth.clear();
							pwStxtPhone.clear();
							
						}else {
							callAlert("��й�ȣ ã�� ���: ã���ô� ��й�ȣ�� �����ϴ�.");
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
				} 
			}); // end of pwSbtnOk.setOnAction(event ~~)
			
			
			// ���̵� �������� �ʴ´ٸ� -> ���̵�ã�� â���� ��ȯ
			pwSbtnIdSearch.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					pwSearchStage.close();
					 handleBtnFindIdAction();
					
				}
			});
				
		} catch (Exception e) { 	
			System.out.println("�н����� ã�� â�� ����� �ȶ����ϴ�.");
		}
	}

	
	//��Ÿ: �˸�â(�߰��� �� �ݷ��� ������ ��)  ����-- "�������� : ���� ����� �Է����ּ���."
	public static void callAlert(String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("�˸�");
		alert.setHeaderText(contentText.substring(0, contentText.lastIndexOf(":")));
		alert.setContentText(contentText.substring(contentText.lastIndexOf(":")+1));
		alert.showAndWait();
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
					|| event.getControlNewText().length() == 12 ||  event.getControlNewText()=="-") {
				return null;
			} else {
				return event;
			}
		}));
		
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
	
	
	
}
