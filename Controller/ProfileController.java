package Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Profile;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ProfileController implements Initializable{
	public Stage profileStage;
	
	@FXML private ImageView pImgView;
	@FXML private Button pBtnImgRegister;
	@FXML private TextField pTxtName;
	@FXML private TextField pTxtBirth;
	@FXML private ComboBox<String> pCmbGender;
	@FXML private ComboBox<String> pCmbKind;
	@FXML private ComboBox<String> pCmbVaccine;
	@FXML private Button pBtnSave;
	@FXML private Button pBtnDelete;
	@FXML private TableView<Profile> pTableView;
	
	
	File file = new File("F:/03.javaProject/MyProject2_0211/src/images/cat_3775226.png");
	ObservableList<String> pListGender=FXCollections.observableArrayList();
	ObservableList<String> pListKind=FXCollections.observableArrayList();
	ObservableList<String> pListVaccine=FXCollections.observableArrayList();
	
	
	
	ObservableList<Profile> profileObList=FXCollections.observableArrayList();
	ObservableList<Profile> profileObList2=FXCollections.observableArrayList(); // ����,�̸�,����,���� ����
	ArrayList<Profile> dbArrayListProfile=new ArrayList<>();
	
	
	//�̹��� ����� ���� ����ʵ� + ������ ���� ����//
	private File selectFile=null; 
	public static File imageDir=new File("C:/myProjectImages");		//������ ���� ����
	private String fileName="";
	/////////////////////////////////
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//00.������ ������ ���̺� ��������.
		settingPetData();
	
		//00. �Է°����� ������ �ؽ�Ʈ�ʵ� �Է°� ����
		pTxtName.textProperty().addListener(inputDecimalFormatName); 	// �̸��� ����,�ѱ۸�
		inputDecimalFormatEightDigit(pTxtBirth); 	// ���� 8�ڸ� ��������
		
		//01.������ ����� �޺��ڽ� ������ �����Ѵ�.
		setComboBoxDate();
	
		//02. �̹��� ��Ϲ�ư�� Ŭ�������� ó���ϴ� �Լ�(����â�� ������.)
		pBtnImgRegister.setOnAction(e -> handlePbtnImgRegisterAction() );		
	
		//03.���� ��ư�� ������ ������ ������ DB �� ����ǰ�, ����ȭ������ �ٽ� ��ȯ�ȴ�. + ���̺�信 ������ ����.
		pBtnSave.setOnAction(e -> handlePbtnSaveAction() );
		
		//04. ���̺��� ������ �����ϰ� �����ϱ� ��ư�� ������ ȭ���� ���̺��� ������ �����ǰ�, DB ������ �ش� ������ �����ȴ�
		pBtnDelete.setOnMouseClicked(e-> deletePetData( pTableView.getSelectionModel().getSelectedItem())  );

		
	}


	//������ ������ ���̺� ��������.
	private void settingPetData() {
		TableColumn tcpetkind = pTableView.getColumns().get(0);
		tcpetkind.setCellValueFactory(new PropertyValueFactory<>("petkind"));
		tcpetkind.setStyle("-fx-alignment: CENTER;");

		TableColumn tcpetname = pTableView.getColumns().get(1);
		tcpetname.setCellValueFactory(new PropertyValueFactory<>("petname"));
		tcpetname.setStyle("-fx-alignment: CENTER;");

		TableColumn tcpetgender = pTableView.getColumns().get(2);
		tcpetgender.setCellValueFactory(new PropertyValueFactory<>("petgender"));
		tcpetgender.setStyle("-fx-alignment: CENTER;");

		TableColumn tcpetbirth = pTableView.getColumns().get(3);
		tcpetbirth.setCellValueFactory(new PropertyValueFactory<>("petbirth"));
		tcpetbirth.setStyle("-fx-alignment: CENTER;");

		ArrayList<Profile> dbArrayListProfile = ProfileDAO.getPetinfoDataToDB();
		for (Profile pf : dbArrayListProfile) {
			profileObList2.addAll(pf);
		}
		pTableView.setItems(profileObList2);

	}

	
	//01.������ ����� �޺��ڽ� ������ �����Ѵ�.
	private void setComboBoxDate() {
		// ����
		pListGender.addAll("��","��(�߼�ȭ)","��","��(�߼�ȭ)","��");
		pCmbGender.setItems(pListGender);
		
		// �ݷ����� ����
		pListKind.addAll("��","�����","�䳢","����ġ","�ܽ���","�ź���","��","�̱��Ƴ�","��Ÿ");
		pCmbKind.setItems(pListKind);
		
		// ���ʿ�����������
		pListVaccine.addAll("��","������","������","�����Ϸ�");
		pCmbVaccine.setItems(pListVaccine);
		
		
	}

	//02. �̹��� ��Ϲ�ư�� Ŭ�������� ó���ϴ� �Լ�(����â�� ������.) 
	//			+   ������ '�����ϱ�'��ư�� ������ DB �� �̹��� ��ΰ� ����ȴ�. + ���̾_'��1 ü�߰���'�� ����� �̹����� ���.
		
		private void handlePbtnImgRegisterAction() {
			FileChooser fileChooser= new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image File",  "*.png" , "*.jpg" , "*.gif" ));
			selectFile=fileChooser.showOpenDialog(profileStage);
			String localURL=null;
			if(selectFile != null) {
				try {
					localURL=selectFile.toURI().toURL().toString();
				}catch(MalformedURLException e){  }
			}
			
			pImgView.setImage(new Image(localURL,false));
			pImgView.setStyle("-fx-alignment: CENTER;");
			fileName=selectFile.getName();

		}

	



	//03_01. �����ʵ�� ������ �Է��Ѵ�. _ 03�� �Լ� �ȿ��� ���̴� �Լ�! + ���̺�信 ������ ������.
	private void insertPetProfileData() {
		
		if(		pTxtName.getText().isEmpty() || 
				pCmbKind.getSelectionModel().isEmpty() ||
				pTxtBirth.getText().isEmpty() ||
				pCmbGender.getSelectionModel().isEmpty() ||
				pCmbVaccine.getSelectionModel().isEmpty() ) 
		{  	
			RootController.callAlert("�Է¿���: ��� ĭ�� �Է����ּ���.");	
			return;
			}

		
			Profile profile=new Profile(
						pTxtName.getText(),
						pCmbKind.getSelectionModel().getSelectedItem(), 
						pTxtBirth.getText(), 
						pCmbGender.getSelectionModel().getSelectedItem(),
						pCmbVaccine.getSelectionModel().getSelectedItem(), 
						fileName
			);
				
		
			profileObList.add(profile);

			int count = ProfileDAO.insertJoinData(profile);
			if (count != 0) {
				System.out.println("�Է¼���: �����ͺ��̽� �Է��� ����");
			}
					
			Profile profileForTable = new Profile(
					pCmbKind.getSelectionModel().getSelectedItem(), 
					pTxtName.getText(),
					pCmbGender.getSelectionModel().getSelectedItem(),
					pTxtBirth.getText() );
			
			profileObList2.add(profileForTable);
			

			TableColumn tcpetkind=pTableView.getColumns().get(0);
			tcpetkind.setCellValueFactory(new PropertyValueFactory<>("petkind"));
			tcpetkind.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetname=pTableView.getColumns().get(1);
			tcpetname.setCellValueFactory(new PropertyValueFactory<>("petname"));
			tcpetname.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetgender=pTableView.getColumns().get(2);
			tcpetgender.setCellValueFactory(new PropertyValueFactory<>("petgender"));
			tcpetgender.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetbirth=pTableView.getColumns().get(3);
			tcpetbirth.setCellValueFactory(new PropertyValueFactory<>("petbirth"));
			tcpetbirth.setStyle("-fx-alignment: CENTER;" );
			
			pTableView.setItems(profileObList2);
			
	}



	//03.���� ��ư�� ������ ������ ������ DB �� ����ǰ�, ����ȭ������ �ٽ� ��ȯ�ȴ�. + �̹����� ���嵵 �ȴ�!!!! (		imageSave();		)
	private void handlePbtnSaveAction() {
		ArrayList <Profile> dbArrayListProfile =  ProfileDAO.getPetinfoDataToDB();
		String petname=null;
		for(Profile pf:dbArrayListProfile) {
			petname=pf.getPetname();
		}
		

		if(pTxtName.getText().equals(petname)) {
			RootController.callAlert("�����̸� ����: ������ ��ϵ� ������ �̸��� �����մϴ�. �ٸ� �̸��� ������ּ���.");
			return;
		}
		
			
		imageSave();		//�̹����� ����___c://images/���õ��̹����̸���.jpg 
		
		insertPetProfileData();		//�����ʵ�� ������ �Է��Ѵ�. ___DB ���� ����ȴ�.
		
		
		pTxtName.clear();
		pCmbKind.setValue(null);
		pTxtBirth.clear();
		pCmbGender.setValue(null);
		pCmbVaccine.setValue(null);		
		pImgView.setImage(new Image(getClass().getResource("../images/cat_3775226.png").toString() ));
		
		
		
	}

	
	//04. ���̺��� ������ �����ϰ�, �����ϱ� ��ư�� ������ ȭ���� ���̺��� ������ �����ǰ�, DB ������ �ش� ������ �����ȴ�
	private void deletePetData(Profile selectedPet) {	
		int count= ProfileDAO.deletePetinfoData( pTableView.getSelectionModel().getSelectedItem().getPetname() );
		
		if(count != 0) {
	
			RootController.callAlert("�����Ϸ�: ������ �Ϸ�Ǿ����ϴ�.");
			profileObList2.remove(selectedPet);
			dbArrayListProfile.remove(selectedPet);
			imageDelete(); 
		}
		
	}
	

		// �̹��� ���� ---  
		private void imageSave() {
			if(!imageDir.exists()	) {
				imageDir.mkdirs();
			}
			FileInputStream fis = null;
			BufferedInputStream bis=null;
			FileOutputStream fos=null;
			BufferedOutputStream bos=null;
			// ���õ� �̹����� c:/images/"���õ��̹����̸���" ���� �����Ѵ�.
			
			try {
				fis=new FileInputStream(selectFile);
				bis=new BufferedInputStream(fis);
				
			//FileChooser ���� ���õ� ���ϸ��� c:/kkk/pppp/jjjj.�۹���.jpg => selectFile
			//selectFile.getName() => �۹���.jpg�� �����´�.
			//���ο� ���ϸ��� �����ϴµ� => student12345678_�۹���.jpg �� ���������.
			//imageDir.getAbsolutePath() + "\\" + fileName => C://images/student12345678_�۹���.jpg �̸����� �����Ѵ�.
			//	c:/kkkk/pppp/jjjj/�۹���.jpg ������ �о c://images/student12345678_�۹���.jpg �̷��� ����ȴ�.
				
				fileName="pet"+System.currentTimeMillis()+""+selectFile.getName();
				fos=new FileOutputStream(imageDir.getAbsolutePath()+"\\"+fileName);
				bos=new BufferedOutputStream(fos);
				
				int data=1;
				while(((data=bis.read())!=-1)) {
				bos.write(data);
				bos.flush();
					
				}
			} catch (Exception e) {
				
				
			}finally {
				try {
				if(fis != null) {fis.close();}
				if(bis != null) {bis.close();}
				if(fos != null) {fos.close();}
				if(bos != null) {bos.close();}
					
				}catch(IOException e){}
			}// end of finally
		}
			
		// �̹��� ���� --- 
		private void imageDelete() {
			Profile selectedPet=pTableView.getSelectionModel().getSelectedItem();
			File imageFile=new File(imageDir.getAbsolutePath()+"\\"+selectedPet.getImgpath() );
			boolean delFlag = false;
			if(imageFile.exists() && imageFile.isFile()) {
				delFlag=imageFile.delete();
				if(delFlag==false) {
					//System.out.println("�̹��� ���� ����: �̹��� ���� ���� ���˹ٶ�");
				}			
			}
		}
		
		///////////////////////////////////////////////////////////////////////////////////// 
	
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

	
		////����,�ѱ۸� ���̵����ϴ� �Լ�
		ChangeListener<String> inputDecimalFormatName = (observable, oldValue, newValue) -> {

		      if (newValue != null && !newValue.equals("")) {

		         if (!newValue.matches("\\D*") || newValue.length() > 10) {

		            ((StringProperty) observable).setValue(oldValue);
		         }
		      }
		   };
		

	
}
