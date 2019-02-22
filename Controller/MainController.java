package Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable{
	public Stage mainStage;
	
	@FXML private Button mBtnProfile;
	@FXML private Button mBtnDiary;
	@FXML private Button mBtnHospital;
	@FXML private Button mBtnFeed;
	@FXML private ImageView mImgViewBack;
	
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//01. ������ ����� ������ ������ ��� ȭ���� ���.
		mBtnProfile.setOnAction( e-> handleMbtnProfileAction() );
		
		
		//02. ���̾ ��ư�� ������ ���̾(�޷�) ȭ���� ���.
		mBtnDiary.setOnAction(e -> handleMbtnDiaryAction() );
				
		
		//03. ���� ��ư�� ������ ��������Ʈ ȭ���� ���.
		mBtnHospital.setOnAction(e-> handleMbtnHospitalAction() );
		
			
		//04. ��� ��ư�� ������ ��Ἲ�км� ȭ���� ���.
		mBtnFeed.setOnAction(e -> handleMbtnFeedAction() );
		
		
		//00. ȭ��ǥ(��) �� ������ �ٽ� �α��� ȭ������ ���ư���.
		mImgViewBack.setOnMouseClicked( e-> handleMimgViewBackAction() );
		
	
	}
	

	//01. ������ ����� ������ ������ ��� ȭ���� ���.
	private void handleMbtnProfileAction() {

		try {
			Stage profileStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/profile_profiletable.fxml"));
			Parent root = loader.load();
		
			
			ProfileController profileController=loader.getController();
			profileController.profileStage=profileStage;
			
			Scene scene=new Scene(root);
			profileStage.setScene(scene);
			System.out.println("ȭ����ȯ: �����ʵ�� ȭ������ ��ȯ�˴ϴ�.");
			profileStage.setTitle("������");
			profileStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	
	
	//02. ���̾ ��ư�� ������ ���̾(�޷�) ȭ���� ���.
	private void handleMbtnDiaryAction() {
		try {
			Stage diaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/diary_edited.fxml"));
			Parent root = loader.load();
			 
			DiaryController diaryController=loader.getController();
			diaryController.diaryStage=diaryStage;
			
			Scene scene = new Scene(root);
			diaryStage.setScene(scene);
			mainStage.close();
			diaryStage.show();

		} catch (IOException e) {
			System.out.println("���̾ ȭ���� ����� �ȶ����ϴ�.");
			e.printStackTrace();
		}

	}

	// 03. ���� ��ư�� ������ ��������Ʈ ȭ���� ���.
	private void handleMbtnHospitalAction() {
		try {
			Stage hospitalStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/hospital.fxml"));
			Parent root = loader.load();
			 
			HospitalController hospitalController=loader.getController();
			hospitalController.hospitalStage=hospitalStage;
			
			Scene scene = new Scene(root);
			hospitalStage.setScene(scene);
			hospitalStage.setTitle("�����˻�");
			hospitalStage.show();

		} catch (IOException e) {
			System.out.println("�����˻� ȭ���� ����� �ȶ����ϴ�.");
			e.printStackTrace();
		}

		
	}


	//04. ��� ��ư�� ������ ��Ἲ�км� ȭ���� ���.
	private void handleMbtnFeedAction() {
		try {
			Stage catFeedStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/cat_feed.fxml"));
			Parent root = loader.load();
			 
			CatfeedController catFeedController=loader.getController();
			catFeedController.catFeedStage=catFeedStage;
			
			Scene scene = new Scene(root);
			catFeedStage.setScene(scene);
			//mainStage.close();
			catFeedStage.setTitle("��Ἲ�км�ǥ");
			catFeedStage.show();

		} catch (IOException e) {
			System.out.println("��Ἲ�км�ǥ ȭ���� ����� �ȶ����ϴ�.");
			e.printStackTrace();
		}

		
	}

	
	
	
	

	//00. ȭ��ǥ(��) �� ������ �ٽ� �α��� ȭ������ ���ư���.
	private void handleMimgViewBackAction() {

		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
			Parent root = loader.load();
						
			RootController rootController=loader.getController();
			rootController.primaryStage=primaryStage;
			
			Scene scene=new Scene(root);
			primaryStage.setScene(scene);
			System.out.println("ȭ����ȯ: �α��� ȭ������ ��ȯ�˴ϴ�.");
			mainStage.close();
			primaryStage.setTitle("�α���");
			primaryStage.show();
			
		} catch (IOException e) {
			System.out.println("ȭ�� ��ȯ ����: �α���ȭ�� ��ȯ�� ���� �߻�");
		}
		
	}

}
