package Controller;


import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import Model.NoteDiary;
import Model.Profile;
import Model.Weight;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DiaryController implements Initializable{
	public Stage diaryStage;	
	
	/// ��1. ü�߰���
	@ FXML private TextField wTxtName;
	@ FXML private TextField wTxtBirth;
	@ FXML private TextField wTxtGender;
	@ FXML private TextField wTxtKind;
	@ FXML private ImageView wImageView;

	@ FXML private TableView<Weight> wTableView;
	@ FXML private DatePicker wDatePicker;
	@ FXML private TextField wTxtWeight;
	@ FXML private Button wBtnGetWeightTb;		//db�� ����� ü�� �����Ͱ��� �ҷ����� ��ư
	@ FXML private ImageView wImgViewBack;
	@ FXML private ImageView wImgSave; 
	@ FXML private Button wBtnChartShow; //��Ʈ�� �����ִ� ��ư
	@	FXML private Button wBtnDataDel; // ���̺�信�� �� ���� �����ؼ� �ش� �����͸� �������� �ϴ� ��ư
	
	 
	//��2. ���̾
	@ FXML private TextField dTxtMonth;
	@ FXML private Button dBtnWrite;
	@ FXML private Button dBtnLogout;
	@ FXML private TextField dTxtFeed;  
	@ FXML private TextField dTxtPee;
	@ FXML private TextField dTxtPoo;
	
	@FXML private TextField dTxtBrushing;
	@FXML private TextField dTxtPlay;
	@FXML private TextArea dTxtMemo;
	@FXML private DatePicker dPreDatePicker;
	@FXML private Button dBtnDatePicker;
	@FXML private TableView<NoteDiary> dNoteTableView;

	
	 //��Ʈ//
	 @FXML  private LineChart<?, ?> lineChart;
	 @FXML private CategoryAxis x;
	 @FXML  private NumberAxis y;
	

	ObservableList<Weight> weightObList = FXCollections.observableArrayList();  //ü�� �����͸� ���� observableArrayList
	
	Weight weight;
	ArrayList<Weight> wDBArrayList=new ArrayList<>();
			
	
	
	public static String petname=null;
	public static String noteDay=null;
	public static String selectedRecordDate=null;
	public static String previousMonthDate=null;
	
	
	@ FXML private Button btn00; 	@ FXML private Button btn01; 	@ FXML private Button btn02;		@ FXML private Button btn03; 
	@ FXML private Button btn04; 	@ FXML private Button btn05;		@ FXML private Button btn06;		@ FXML private Button btn10; 	
	@ FXML private Button btn11; 	@ FXML private Button btn12; 	@ FXML private Button btn13;		@ FXML private Button btn14;	
	@ FXML private Button btn15;  	@ FXML private Button btn16;		@ FXML private Button btn20; 	@ FXML private Button btn21;
	@ FXML private Button btn22;  	@ FXML private Button btn23; 	@ FXML private Button btn24; 	@ FXML private Button btn25;
	@ FXML private Button btn26;		@ FXML private Button btn30;		@ FXML private Button btn31; 	@ FXML private Button btn32;
	@ FXML private Button btn33;		@ FXML private Button btn34;		@ FXML private Button btn35;	 	@ FXML private Button btn36;
	@ FXML private Button btn40; 	@ FXML private Button btn41; 	@ FXML private Button btn42; 	@ FXML private Button btn43;
	@ FXML private Button btn44;		@ FXML private Button btn45;		@ FXML private Button btn46;		@ FXML private Button btn50;
	@ FXML private Button btn51;		@ FXML private Button btn52;		@ FXML private Button btn53;		@ FXML private Button btn54;
	@ FXML private Button btn55;		@ FXML private Button btn56;		
	

	Button[] btnArray = new Button[42];

	LocalDateTime ldt=LocalDateTime.now();		
	Calendar cal=Calendar.getInstance();
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//	blockwBtnGetWeighttb();
		
		//��1_ ü�� �Է°� ����
		inputDecimalFormat(wTxtWeight);
		
		///////////////////////////////// ��_1.ü�߰��� //////////////////////////////////////
		//��1_00. ����� ���� ����Ʈ�� ���̾�α�â���� �����ְ�, �����ϵ����Ѵ�.
		showPetListAndChoice();
		
		//��1_02. ü���� �Է¹ް� �� �����Ͱ� ���̺� �信 ���õȴ�.	
		wImgSave.setOnMouseClicked(	e-> setWeightDataTableView()  );
		
		//��1_03. �Է��� ü�� �����͸� �ҷ��ͼ� ���̺� �������� ��.		
		wBtnGetWeightTb.setOnAction(e -> getWeightDataFromDB() );	 // '���������ͺҷ�����' ��ư
		
		//��1_04. '��Ʈ����' ��ư�� Ŭ���ϸ� ������Ʈ�� ȭ�鿡 ��������.	
		wBtnChartShow.setDisable(true);
		wBtnChartShow.setOnAction(e-> showWeightChangingChart() );
			
		
		//��1_05. '�����ϱ�' ��ư�� ������ ���̺�信�� ������ �ش� ���� �����Ͱ� �����ȴ�. DB ������ �����ȴ�. //////////////////////	
		wBtnDataDel.setOnAction(e -> deleteSelectedWeightData(wTableView.getSelectionModel().getSelectedItem() ) );
		
		
		//��1_06. ���̺�並 Ŭ�������� ����â�� �߰�, �� �Է¹��� ������ �����Ǿ� DB�� ����, ���̺�䵵 ����
		wTableView.setOnMouseClicked(e-> editWeightData(e) );
		
	
		
		///////////////////////////////// ��_2. ���̾//////////////////////////////////////
		
		//��2_00.�޷¿� �޸� �����ִ� ĭ�� �Է��� ���ϵ��� ���´�.
		setDiaryTextFieldSetEditableFalse();
		

		// ��2_01.�޷��� ������
		showDiary();
			
		
		// ��2_02. ���ʸ�� �������� ������ ���̾ ��Ʈ�� ȭ���� ��ȯ�ȴ�. ���.
		dBtnWrite.setOnAction(e-> openDiaryWritePage() );

		
		//��2_04. �����޼���(DatePicker) ���� ��¥�� �����ϰ� ��ư�� �����ϸ� �������� �����Ͱ� ȭ�鿡 ���.
		
		dBtnDatePicker.setOnMouseClicked( e -> showPreviousMonthNoteData( ) );
		
		//��2_05. �α׾ƿ� ��ư�� ������ �α����������� ���ư���.
		dBtnLogout.setOnAction(e -> logoutAndShowLoginPage() );
		
		
		//<- ȭ��ǥ ��ư�� ������ ����ȭ������ ��ȯ�ȴ�.
		wImgViewBack.setOnMouseClicked(e-> handleWimgViewBackAction() );		
		
		
	}
	
	
	//��1_00. ����� ���� ����Ʈ�� ���̾�α�â���� �����ְ�, �����ϵ����Ѵ�.
	private void showPetListAndChoice() {
		try {

			Stage petChoiceStage = new Stage(StageStyle.UTILITY);
			petChoiceStage.initModality(Modality.WINDOW_MODAL);
			petChoiceStage.initOwner(diaryStage);
			petChoiceStage.setTitle("�ݷ���������");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/petchoice.fxml"));
			Parent root = loader.load();
	
			//���̵�//
			TableView<Profile> pChoiceTableView = (TableView<Profile>)root.lookup("#pChoiceTableView");
			Button pChoiceOk =(Button)root.lookup("#pChoiceOk");
			
		
			////���̺�� ����/////
			TableColumn tcpetkind=pChoiceTableView.getColumns().get(0);
			tcpetkind.setCellValueFactory(new PropertyValueFactory<>("petkind"));
			tcpetkind.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetname=pChoiceTableView.getColumns().get(1);
			tcpetname.setCellValueFactory(new PropertyValueFactory<>("petname"));
			tcpetname.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetgender=pChoiceTableView.getColumns().get(2);
			tcpetgender.setCellValueFactory(new PropertyValueFactory<>("petgender"));
			tcpetgender.setStyle("-fx-alignment: CENTER;" );
			
			TableColumn tcpetbirth=pChoiceTableView.getColumns().get(3);
			tcpetbirth.setCellValueFactory(new PropertyValueFactory<>("petbirth"));
			tcpetbirth.setStyle("-fx-alignment: CENTER;" );
			
			//���̺�信 �� ������ DB ���� �����´�.__ petinfotb2 ����
			
			ObservableList<Profile> petList=FXCollections.observableArrayList();
			ArrayList<Profile> dbArrayListProfile=new ArrayList<>();
			
			dbArrayListProfile=ProfileDAO.getPetinfoDataToDB();
			for(Profile profile: dbArrayListProfile) {
				petList.addAll(profile);
			}
			
			
			pChoiceTableView.setItems(petList);

			//// ���̺�並 �����ϸ� �����̸��� �ش�.
			pChoiceTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					
					petname= pChoiceTableView.getSelectionModel().getSelectedItem().getPetname();
					System.out.println(petname);
					
				}
			});

			Scene scene=new Scene(root);
			petChoiceStage.setScene(scene);
			petChoiceStage.show();
			petChoiceStage.setAlwaysOnTop(true);
			petChoiceStage.setResizable(false);
			
			pChoiceOk.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					
					if(pChoiceTableView.getSelectionModel().getSelectedItem()==null) {
						
						petChoiceStage.setAlwaysOnTop(false);
						RootController.callAlert("���� ����: ������ �������ּ���");
						petChoiceStage.setAlwaysOnTop(true);
						return;
					}
					settingPetProfile();
					petChoiceStage.close();
					
				}
			});

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	//��1_ 00�� �Լ� �ȿ��� ���δ�.
	private void settingPetProfile() {		
		// ���̺�信�� �� ���� �����ϸ� �����ʿ� ����� ������ ������ DB ���� �ҷ��ͼ� ü�߰��� ���� �ؽ�Ʈ�ʵ忡 ���õǵ���
		//1. petinfotb2 ���� �����͸� �����´�. ��? �̸�, ����,����,����
			
			ArrayList<Profile> petinfoList = DiaryDAO.getPetDatabyPetname();
		
			for(Profile profile: petinfoList) {
				String petname=profile.getPetname();
				String petkind=profile.getPetkind();
				String petbirth=profile.getPetbirth();
				String petgender=profile.getPetgender();		
				String imgpath=profile.getImgpath();
				Image image=new Image("file:///"+ProfileController.imageDir.getAbsolutePath()+"/"+imgpath);
							
				wTxtName.setText(petname);
				wTxtBirth.setText(petbirth);
				wTxtGender.setText(petgender);
				wTxtKind.setText(petkind);
				wImageView.setImage(image);
		
				wTxtName.setEditable(false);
				wTxtBirth.setEditable(false);
				wTxtGender.setEditable(false);
				wTxtKind.setEditable(false);		
			}			
	}


	//��1_02. ��1_ü���Է�â���� ���� ������ ���̺� �並 �����Ѵ�. + weighttb�� �����͸� �����Ѵ�.
	private void setWeightDataTableView() {
		wBtnChartShow.setDisable(false);
		
		//02_02. ���̺��
		TableColumn tcRecordate=wTableView.getColumns().get(0);
		tcRecordate.setCellValueFactory(new PropertyValueFactory<>("recordate"));
		tcRecordate.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcWeight=wTableView.getColumns().get(1);
		tcWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
		tcWeight.setStyle("-fx-alignment: CENTER;" );
		

		/// ��ϳ�¥, ü�߸� ���̺�信 !//
		Weight weight=new Weight(wDatePicker.getValue().toString(),
														wTxtWeight.getText() );
		
		weightObList.add(weight);
			
		wTableView.setItems(weightObList);

		int count=WeightDAO.insertWeightData(weight);
		if(count !=0 ) {
			//RootController.callAlert("�Է¼��� : �����ͺ��̽� �Է��� �����Ǿ����ϴ�.");
		}
	
	}
	

	

	//��1_03. �Է��� ü�� �����͸� �ҷ��ͼ� ���̺� �������� ��. ---- '���������� �ҷ�����'
	private void getWeightDataFromDB() {
		ArrayList<Weight> wDBArrayList=new ArrayList<>();
		
		TableColumn tcRecordate=wTableView.getColumns().get(0);
		tcRecordate.setCellValueFactory(new PropertyValueFactory<>("recordate"));
		tcRecordate.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcWeight=wTableView.getColumns().get(1);
		tcWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
		tcWeight.setStyle("-fx-alignment: CENTER;" );
		
		wTableView.setItems(weightObList);
		
		wDBArrayList=WeightDAO.getWeighttbTotalData();
		
		if(wDBArrayList.isEmpty()) {
			RootController.callAlert("���������͸� �ҷ��� �� �����ϴ�: ����� �����Ͱ� �����ϴ�.");
			wBtnGetWeightTb.setDisable(true);
		}else {		
			for(Weight weight : wDBArrayList ) {
				weightObList.add(weight);
			}
			wBtnGetWeightTb.setDisable(true);
			wBtnChartShow.setDisable(false);
			
		}
	}

	
	
	//��1_04. '��Ʈ����' ��ư�� Ŭ���ϸ� ������Ʈ�� ȭ�鿡 ��������.
	private void showWeightChangingChart() {
		ArrayList<Weight> wDBArrayList=new ArrayList<>();
		XYChart.Series series = new XYChart.Series<>();
	
		series.getData().clear();
		lineChart.getData().clear();
		
		wDBArrayList=WeightDAO.getWeighttbTotalData();	
		
		for (Weight weight : wDBArrayList) {

			String recordDate = weight.getRecordate();
			double weightData = Double.parseDouble(weight.getWeight());

			series.getData().add(new XYChart.Data(recordDate, weightData));

		}
		lineChart.getData().addAll(series);
		lineChart.setAnimated(false);
	}
	
	
		//��1_05. '�����ϱ�' ��ư�� ������ ���̺�信�� ������ �ش� ���� �����Ͱ� �����ȴ�. DB ������ �����ȴ�. //////////////////////
		private void deleteSelectedWeightData(Weight selectedItem) {
			selectedRecordDate=wTableView.getSelectionModel().getSelectedItem().getRecordate();
			Weight selectWeightData=wTableView.getSelectionModel().getSelectedItem();
			int selectWeightDataIndex=wTableView.getSelectionModel().getSelectedIndex();
				
			int count=WeightDAO.deleteWeightData(WeightDAO.petNo); 
		
			if(count != 0) {
				wDBArrayList.clear();
				weightObList.clear();
				
				wDBArrayList=WeightDAO.getWeighttbTotalData();
				for(Weight weight : wDBArrayList) {
					weightObList.add(weight);
				}
	
			}else {
				return;
			}
			
		}

		//��1_06. ���̺�並 Ŭ�������� ����â�� �߰�, �� �Է¹��� ������ �����Ǿ� DB�� ����, ���̺�䵵 ����
		private void editWeightData(MouseEvent e) {

			Weight selectWeightData = wTableView.getSelectionModel().getSelectedItem();
			int selectWeightDataIndex = wTableView.getSelectionModel().getSelectedIndex();

			//System.out.println(" ������ �ε�����:" + selectWeightDataIndex);

		if (e.getClickCount() == 2) {

			try {

				Stage weightNoteStage = new Stage(StageStyle.UTILITY);
				weightNoteStage.initModality(Modality.WINDOW_MODAL);
				weightNoteStage.initOwner(diaryStage);
				weightNoteStage.setTitle("ü�߱�� ����");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/weightnote.fxml"));
				Parent root;
				root = loader.load();

				DatePicker wRecordDatePicker = (DatePicker) root.lookup("#wRecordDatePicker");
				TextField wTxtWeight = (TextField) root.lookup("#wTxtWeight");
				Button wBtnRegister = (Button) root.lookup("#wBtnRegister");
				Button wBtnCancel = (Button) root.lookup("#wBtnCancel");

				// ü�� �Է°� ����
				inputDecimalFormat(wTxtWeight);

				// ���õ� ���� �ϴ� weightnote â�� �����Ѵ�.
				wTxtWeight.setText(selectWeightData.getWeight());
				wRecordDatePicker.setValue(LocalDate.parse(selectWeightData.getRecordate()));
				selectedRecordDate = selectWeightData.getRecordate();

				//System.out.println("���õ� ��¥��:" + selectedRecordDate);

			wBtnCancel.setOnAction( (event) -> {  weightNoteStage.close(); 	});
			
			wBtnRegister.setOnMouseClicked(new EventHandler<MouseEvent>() {	 // ���� �����ϸ� ���̺����� ���� �ٲ��, DB ������ ���� �����ȴ�.
				

				@Override
				public void handle(MouseEvent event) {					
					Weight weight=new Weight(wRecordDatePicker.getValue().toString(),  wTxtWeight.getText()  );
					
					int count= WeightDAO.updateWeightData(weight);
					
					if(count != 0) {
						wDBArrayList.clear();
						weightObList.clear();				
						System.out.println("DB�����Ϸ�: ������ �Ϸ�Ǿ����ϴ�.");    					
					}else {
						return;
					}
					////////////////////////////////////////////
					wDBArrayList = WeightDAO.getWeighttbTotalData();
					for(Weight updatedWeight : wDBArrayList ) {
						weightObList.add(updatedWeight);
					}
					
					wTableView.setItems(weightObList);
					weightNoteStage.close();
					////////////////////////////////////////////

				}
			});
			
			Scene scene=new Scene(root);
			weightNoteStage.setScene(scene);
			weightNoteStage.show();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
		}
		

	///////////////////////////////// ��_2. ���̾//////////////////////////////////////
	
	

	//��2_00.�޷¿� �޸� �����ִ� ĭ�� �Է��� ���ϵ��� ���´�.
	private void setDiaryTextFieldSetEditableFalse() {
		
		dTxtFeed.setEditable(false);
		dTxtPee.setEditable(false);
		dTxtPoo.setEditable(false);
		dTxtBrushing.setEditable(false);
		dTxtPlay.setEditable(false);
		dTxtMemo.setEditable(false);
		
	}


	// 02. ���ʸ�� �������� ������ ���̾ ��Ʈ�� ȭ���� ��ȯ�ȴ�. ���.
	private void openDiaryWritePage() {	
		try {

			Stage noteStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/note.fxml"));
			Parent root = loader.load();

			NoteController noteController = loader.getController();
			noteController.noteStage = noteStage;

			Scene scene = new Scene(root);
			noteStage.setScene(scene);
			noteStage.show();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/////////////���̾ �޷� ����////////////////////////////////
	private void showDiary() {
		
		Button[ ]btnArray= 
				{btn00, btn01, btn02, btn03, btn04, btn05, btn06, 
				btn10, btn11, btn12, btn13, btn14, btn15, btn16,
				btn20, btn21, btn22, btn23, btn24, btn25, btn26, 
				btn30, btn31, btn32, btn33, btn34, btn35, btn36,
				btn40, btn41, btn42, btn43, btn44, btn45, btn46,
				btn50, btn51, btn52, btn53, btn54, btn55, btn56};
		
		int year=ldt.getYear();
		int month = ldt.getMonthValue();
		int day=ldt.getDayOfMonth();
		cal.set(year, month-1, 1);		
		int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);			
		
		int firstday=0;
		int lastDay=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		switch(dayOfWeek) {
			case 1 : firstday=0; break;
			case 2 : firstday=1; break;
			case 3 : firstday=2; break;
			case 4 : firstday=3; break;
			case 5 : firstday=4; break;
			case 6 : firstday=5; break;
			case 7 : firstday=6; break;
		}	
					
		int j=1;
		for(int i=firstday;  i<lastDay+firstday; i++) {			
			btnArray[i].setText(""+j);
			j++;

			final int ii=i;
		
			btnArray[i].setOnMouseClicked(e->			// �޷¹�ư�� ������ ����Ǵ� �͵�
			{			
				 // �޷��� ������ String ���� 2019-02-08 �̷������� �����Բ� ���� noteDay �� �����Ѵ�.
				String btn= btnArray[ii].getText();
				if(  Integer.valueOf(btn)  <= 9) {
					btn="0"+btn;
					noteDay=ldt.now().toString().substring(0, 7) +"-"+ btn;
				//	System.out.println(noteDay);   ////// 2019-02-01 ~ 2019-02-09
				} else {
					btn=btn;
					noteDay=ldt.now().toString().substring(0, 7) +"-"+ btn;
				//	System.out.println(noteDay);	////////2019-02-10 ~ 2019-02-28
				}
					ArrayList<NoteDiary> dbArrayListDiary=DiaryDAO.getDiaryData();
	
				/////////////////////////////////////////////////////////////////////////////////////////
				// �޷��� �������� �ߴ� ��¥��, ��ϵ� day �� ������ �� ���̺��� ���� �����ͼ� �ؽ�Ʈ�ʵ忡 �ش�. 
				for (NoteDiary note : dbArrayListDiary) {
					String dbDay = note.getDay();
					if (noteDay.equals(dbDay) ) {
	
						btnArray[ii].setStyle("-fx-background-color: #FFD0AF;");
					
						dTxtFeed.setText(note.getMeal());
						dTxtPee.setText(note.getPee());
						dTxtPoo.setText(note.getPoo());
						dTxtBrushing.setText(note.getBrushing());
						dTxtPlay.setText(note.getPlay());
						dTxtMemo.setText(note.getMemo());

						setDiaryTextFieldSetEditableFalse();
					}
				} // end of for-each
				if (DiaryDAO.count == 0) {
					dTxtFeed.clear();
					dTxtPee.clear();
					dTxtPoo.clear();
					dTxtBrushing.clear();
					dTxtPlay.clear();
					dTxtMemo.clear();
					RootController.callAlert("��Ʈ�������: �ش糯¥���� �Է��Ͻ� ��Ʈ�� �����ϴ�.");
				}
			});
		}
		dTxtMonth.setText(String.valueOf(month + "��"));
		dTxtMonth.setEditable(false);
		dTxtMonth.setStyle("-fx-alignment:CENTER");

	}

	// DatePicker �� �������� ������ �׶��� ������ ������. 
	private void showPreviousMonthNoteData(  ) {
		
		previousMonthDate=dPreDatePicker.getValue().toString();
		System.out.println(previousMonthDate);
		
		ArrayList<NoteDiary> dbArrayListDiary=DiaryDAO.getPreviousDiaryData( );
		/////////////////////////////////////////////////////////////////////////////////////////
		// �޷��� �������� �ߴ� ��¥��, ��ϵ� day �� ������ �� ���̺��� ���� �����ͼ� �ؽ�Ʈ�ʵ忡 �ش�. 
		
		for (NoteDiary note : dbArrayListDiary) {
			//String dbDay = dDatePicker.getValue().toString();
			//System.out.println(dbDay);
			
			if(previousMonthDate.equals(note.getDay())){
				dTxtFeed.setText(note.getMeal());
				dTxtPee.setText(note.getPee());
				dTxtPoo.setText(note.getPoo());
				dTxtBrushing.setText(note.getBrushing());
				dTxtPlay.setText(note.getPlay());
				dTxtMemo.setText(note.getMemo());
				
				setDiaryTextFieldSetEditableFalse(); 
			}
		} // end of for-each
			if(DiaryDAO.count ==0){
				dTxtFeed.clear();
				dTxtPee.clear();
				dTxtPoo.clear();
				dTxtBrushing.clear();
				dTxtPlay.clear();
				dTxtMemo.clear();			
				RootController.callAlert("��Ʈ�������: �ش糯¥���� �Է��Ͻ� ��Ʈ�� �����ϴ�.");				
			}
	}
	//��2_05. �α׾ƿ� ��ư�� ������ �α����������� ���ư���.
	private void logoutAndShowLoginPage() {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/login.fxml"));
			Parent root = loader.load();
						
			RootController rootController=loader.getController();
			rootController.primaryStage=primaryStage;
			
			Scene scene=new Scene(root);
			primaryStage.setScene(scene);
			RootController.callAlert("�α׾ƿ�: �α׾ƿ��Ǿ����ϴ�.");
			diaryStage.close();
			primaryStage.setTitle("�α���");
			primaryStage.show();
			
		} catch (IOException e) {
			RootController.callAlert("ȭ�� ��ȯ ����: �α���ȭ�� ��ȯ�� ���� �߻�");
		}
		
	}
		
	//<- ȭ��ǥ ��ư�� ������ ����ȭ������ ��ȯ�ȴ�.
	private void handleWimgViewBackAction() {
		try {
			Stage mainStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/main.fxml"));
			Parent root = loader.load();
						
			MainController mainController=loader.getController();
			mainController.mainStage=mainStage;
			
			Scene scene=new Scene(root);
			mainStage.setScene(scene);
			System.out.println("ȭ����ȯ: ���� ȭ������ ��ȯ�˴ϴ�.");
			diaryStage.close();
			mainStage.show();
			
		} catch (IOException e) {
			System.out.println("ȭ�� ��ȯ ����: ����ȭ�� ��ȯ�� ���� �߻�");
		}
		
	}

	////////////////ü���Է°� ����///////////////////////////////////////
	

	// �Է°� �ʵ� ���� ���� ���: �Ҽ��� ��°�ڸ�������________________ok
	private void inputDecimalFormat(TextField textField) {

		DecimalFormat format = new DecimalFormat("##.##");
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
					|| event.getControlNewText().length() == 6) {
				return null;
			} else {
				return event;
			}
		}));
		
	}
	
	

	
}
