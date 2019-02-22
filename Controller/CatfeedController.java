package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.CatFeed;
import Model.Hospital;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CatfeedController implements Initializable{
	public Stage catFeedStage;
	
	
	@FXML private ImageView feedImag;
	@FXML private TableView<CatFeed> feedTableView;
	@FXML private TextField feedTxtSearch;
	@FXML private Button feedBtnSearch;
	@FXML private ComboBox<String> feedPetKind;
	
	ObservableList<CatFeed> catFeedOblist	=	FXCollections.observableArrayList();
	ObservableList<String> PetKindCmbList = FXCollections.observableArrayList();
	
	
	CatFeed catFeed=null;
	ArrayList<CatFeed> cfDbArrayList = new ArrayList<>();
	
	Connection con=null;
	PreparedStatement pstmt=null;
	ResultSet rs=null;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//00. �޺��ڽ�(��������) �� �����Ѵ�.
		setComboBoxPetList();
		
		//01. ��Ἲ�км�ǥ ���̺�� �⺻������ �Ѵ�.
		setCatFeedTableView();
		
		
		//02. db ���� ����������̺��� �����ͼ� ,���̺�信 ����ش�.
		getCatFeedDatafromDB();
		
		//03. ������ �˻��ϸ� ���̺�信�� ������ ���ش�.
		feedBtnSearch.setOnAction(e -> searchCatFeedDataByFeedName() );
		
		//04. ���̺�信�� ������ �ϸ� ����̹����� ���δ�.
		
		
	}

	private void setComboBoxPetList() {
		PetKindCmbList.addAll("��","�����","�䳢","����ġ","�ܽ���","�ź���","��","�̱��Ƴ�");
		feedPetKind.setItems(PetKindCmbList);
		feedPetKind.getSelectionModel().select("�����");
	}

	//01. ��Ἲ�км�ǥ ���̺�� �⺻������ �Ѵ�.
	private void setCatFeedTableView() {
		//��ǰ��
		TableColumn tcproductname=feedTableView.getColumns().get(0);
		tcproductname.setCellValueFactory(new PropertyValueFactory<>("productname"));
		tcproductname.setStyle("-fx-alignment: CENTER;" );
		
		//���ܹ�
		TableColumn tccrudeprotein=feedTableView.getColumns().get(1);
		tccrudeprotein.setCellValueFactory(new PropertyValueFactory<>("crudeprotein"));
		tccrudeprotein.setStyle("-fx-alignment: CENTER;" );
		
		//������
		TableColumn tccrudefat=feedTableView.getColumns().get(2);
		tccrudefat.setCellValueFactory(new PropertyValueFactory<>("crudefat"));
		tccrudefat.setStyle("-fx-alignment: CENTER;" );
		
		//������
		TableColumn tccrudefiber=feedTableView.getColumns().get(3);
		tccrudefiber.setCellValueFactory(new PropertyValueFactory<>("crudefiber"));
		tccrudefiber.setStyle("-fx-alignment: CENTER;" );
		
		//��ȸ��
		TableColumn tccrudeash=feedTableView.getColumns().get(4);
		tccrudeash.setCellValueFactory(new PropertyValueFactory<>("crudeash"));
		tccrudeash.setStyle("-fx-alignment: CENTER;" );
		
		//ź��ȭ��
		
		TableColumn tccarbohydrate=feedTableView.getColumns().get(5);
		tccarbohydrate.setCellValueFactory(new PropertyValueFactory<>("carbohydrate"));
		tccarbohydrate.setStyle("-fx-alignment: CENTER;" );
		
		//Į�θ�
		
		TableColumn tccalorie=feedTableView.getColumns().get(6);
		tccalorie.setCellValueFactory(new PropertyValueFactory<>("calorie"));
		tccalorie.setStyle("-fx-alignment: CENTER;" );

		//������
		
		TableColumn tcproductionregion=feedTableView.getColumns().get(7);
		tcproductionregion.setCellValueFactory(new PropertyValueFactory<>("productionregion"));
		tcproductionregion.setStyle("-fx-alignment: CENTER;" );
		

	}

	
	//02. db ���� ����������̺��� �����ͼ� ,���̺�信 ����ش�.
	private void getCatFeedDatafromDB() {

	String sql="select * from catfood ";
	
	try {
		con=DBUtility.getConection();
		pstmt=con.prepareStatement(sql);
		
		rs=pstmt.executeQuery();
		
		while(rs.next()) {
			catFeed=new CatFeed(
					rs.getString(1),
					rs.getString(2), 
					rs.getString(3), 
					rs.getString(4), 
					rs.getString(5), 
					rs.getString(6), 
					rs.getString(7), 
					rs.getString(8)
					);
			cfDbArrayList.add(catFeed);
		}
		
	} catch (SQLException e) {
		System.out.println("��Ἲ�км�ǥ �ҷ����� ����:�����߽��ϴ�.");
		e.printStackTrace();
	}finally {
		// 1.6 �ڿ���ü�� �ݾƾ��Ѵ�.
		try {
			if (pstmt != null) {
				pstmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			System.out.println("�ڿ��ݱ� ���� : psmt,con �ݱ����" );
		}
	}	

		feedTableView.setItems(catFeedOblist);

		for (CatFeed feed : cfDbArrayList) {
			catFeedOblist.add(feed);
		}

	}
	
	//03. ������ �˻��ϸ� ���̺�信�� ������ ���ش�.
	private void searchCatFeedDataByFeedName() {
		for(CatFeed feed : catFeedOblist) {
			if(feedTxtSearch.getText().trim().equals(feed.getProductname()) ) {    
				feedTableView.getSelectionModel().select(feed);
				feedTableView.scrollTo(feed);
			}
		}
	}
	
	
	
}
