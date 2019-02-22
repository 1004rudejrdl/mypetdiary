package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Hospital;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HospitalController implements Initializable{
	public Stage hospitalStage;
	
	@FXML private TableView<Hospital> hTableView;
	@FXML private Button hBtnSearch;
	@FXML private Button hBtnSearchByRegion;
	@FXML private ComboBox<String> hCmbRegion;
	@FXML private TextField hTxtHospitalName;
    
	ObservableList<String> regionList = FXCollections.observableArrayList();
	
	
	ObservableList<Hospital> hospitalObList=FXCollections.observableArrayList();
	ObservableList<Hospital> hospitalObList2=FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//01. ���̺�� �⺻ ������ �Ѵ�.
		setTableView();
		
		//02. �޺��ڽ��� ������ �����Ѵ�.
				
		setComboBoxLocalList();
		
		//03. �������� �˻��� ������ ����Ʈ�� �����ش�.

		hBtnSearchByRegion.setOnAction(e ->  showListOrderByRegion( hCmbRegion.getSelectionModel().getSelectedItem()) );
		
		
		//04. �����̸��� �˻��ϸ� ���̺�信�� ������ ���ش�.

		hBtnSearch.setOnAction(e ->	searchHospitalDataByHospitalName() 		);
		
		
	}



	//01. ���̺�� �⺻ ������ �Ѵ�.
	private void setTableView() {	
		TableColumn tcNo=hTableView.getColumns().get(0);
		tcNo.setCellValueFactory(new PropertyValueFactory<>("no"));
		tcNo.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcLicensedate=hTableView.getColumns().get(1);
		tcLicensedate.setCellValueFactory(new PropertyValueFactory<>("licensedate"));
		tcLicensedate.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcTel=hTableView.getColumns().get(2);
		tcTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
		tcTel.setStyle("-fx-alignment: CENTER;" );
		
		
		TableColumn tcHospital=hTableView.getColumns().get(3);
		tcHospital.setCellValueFactory(new PropertyValueFactory<>("hospital"));
		tcHospital.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcAddr=hTableView.getColumns().get(4);
		tcAddr.setCellValueFactory(new PropertyValueFactory<>("addr"));
		tcAddr.setStyle("-fx-alignment: CENTER;" );
		
		TableColumn tcNaddr=hTableView.getColumns().get(5);
		tcNaddr.setCellValueFactory(new PropertyValueFactory<>("naddr"));
		tcNaddr.setStyle("-fx-alignment: CENTER;" );
	
		/// db�����ؼ� ��������Ʈ�� ���� arraylist �� �����ϴ� �Լ��� �ҷ��� ������  �� ������ �ϳ��� �ҷ����� ��ü�� �����ϰ�
		/// ���̺�信 �־��ش�.
		
		ArrayList<Hospital> dbArrayListHospital=HospitalDAO.getHospitaltbTotalData();
		
		for( Hospital hospital     :   dbArrayListHospital     ) {		
			Hospital hospital1=new Hospital(
											hospital.getNo(),
											hospital.getLicensedate(), 
											hospital.getTel(), 
											hospital.getHospital(), 
											hospital.getAddr(),
											hospital.getNaddr());		
			hospitalObList.add(hospital1);
		}
		
		hTableView.setItems(hospitalObList);
	}

	
	//02. �޺��ڽ��� ������ �����Ѵ�.
	private void setComboBoxLocalList() {
		regionList.addAll("������","������","���ϱ�","������","���ı�","���α�","���Ǳ�",
							"������","���α�","��õ��","��õ��","�߱�","�����","������","���빮��",
							"���۱�","��������","�߶���","������","���빮��","���ʱ�","��걸","������","���ϱ�","����");
		
		hCmbRegion.setItems(regionList);	
	}


	
	//03. �������� �˻��� ������ ����Ʈ�� �����ش�.
	private void  showListOrderByRegion(String region) {
		hospitalObList2.clear();	
		 ArrayList<Hospital> dbArrayListHospital = HospitalDAO.getHospitaltbDataByRegion( hCmbRegion.getSelectionModel().getSelectedItem() );
		
		 System.out.println( hCmbRegion.getSelectionModel().getSelectedItem()    );
		
			for( Hospital hospital     :   dbArrayListHospital   ) {			
				Hospital hospital1=new Hospital(
												hospital.getNo(),
												hospital.getLicensedate(), 
												hospital.getTel(), 
												hospital.getHospital(), 
												hospital.getAddr(),
												hospital.getNaddr());			
			
				hospitalObList2.add(hospital1);
			}
		
			hTableView.setItems(hospitalObList2);
		
			
	}
		
	//04. �����̸��� �˻��ϸ� ���̺�信�� ������ ���ش�.
	private void searchHospitalDataByHospitalName() {
		
		if(!(hTxtHospitalName.getText().isEmpty())) {
			setTableView();
			
			for(Hospital hospital : hospitalObList) {
				if(hTxtHospitalName.getText().trim().equals(hospital.getHospital())) {
					hTableView.getSelectionModel().select(hospital);
					hTableView.scrollTo(hospital);
				}
			}
		
		}
		
		
	}

} //end of controller



