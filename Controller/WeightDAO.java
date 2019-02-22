package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Join;
import Model.Weight;

public class WeightDAO {	
	
	public static int petNo=0;
	
	//01. ü�������� ����ϴ� �Լ�
	public static int insertWeightData(Weight weight) {
		//select ������ petno�� �����´�
		int petno=0;
	
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		try {
			String sql ="select petno "+"from petinfotb2 where email=? and petname=? ";
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				
				petno= rs.getInt("petno");
				System.out.println(petno);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StringBuffer insertWeight=new StringBuffer();
		insertWeight.append("insert into weighttb ");
		insertWeight.append("(petno,weight,recorddate) ");
		insertWeight.append("values ");
		insertWeight.append("(?,?,?) ");
		//1.2 �����ͺ��̽� Connection �� �����´�.

		int count=0;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(insertWeight.toString());
	
			//1.4 �������� ���� �����͸� ����
			pstmt.setInt(1, petno);
			pstmt.setString(2, weight.getWeight());
			pstmt.setString(3, weight.getRecordate());
			
			count=pstmt.executeUpdate();
			
			if(count==0) {
			//	RootController.callAlert("���� ���� ����: �������� ���� ���˹ٶ�");
				return count;
			}
	
		} catch (SQLException e) {
			//RootController.callAlert("���� ����: �����ͺ��̽� ���� ����. ���˹ٶ�");
			e.printStackTrace();
		}finally {
			try {
				if (pstmt != null) {  pstmt.close(); }
				if (con != null) { 	con.close();	}
			} catch (SQLException e) {
				RootController.callAlert("�ڿ��ݱ� ����: psmt,con �ݱ� ����.");
			}
		}
		return count;
	}
	
	//02.ü������ ���̺��� ��ü ������ ��� �������� �Լ�
	public static ArrayList<Weight> getWeighttbTotalData(){
		ArrayList<Weight> wDBArrayList = new ArrayList<>();
		int petno=0;
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		
		try {
			String sql ="select petno "+"from petinfotb2 where email=? and petname=? ";
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {			
				petno= rs.getInt("petno");		
				//System.out.println("�̸��� �̸��Ϸ� ������ ������ ��ȣ��: "+petno);	//////////////// �ֿܼ��� Ȯ��
				
			}	
			
			// ������ petno ��  weighttb �����͸� �� �����´�.
			String sql2 ="select weight,recorddate from weighttb where petno=? " ;
			pstmt=con.prepareStatement(sql2);
			
			pstmt.setInt(1, petno);
			rs=pstmt.executeQuery();
		
			
			wDBArrayList.clear();
			while(rs.next()) {			
				Weight weight=new Weight( 					// �������, ü��
						rs.getString("recorddate"), 
						rs.getString("weight") );			
				wDBArrayList.add(weight);	
			}		
		} catch (SQLException e) {	
			//RootController.callAlert("������ �ҷ����� ����: ������ �ҷ����Ⱑ �����߽��ϴ�.");
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) { pstmt.close(); }
				if(con != null) {con.close(); }
			} catch (SQLException e) {
			//RootController.callAlert("�ڿ��ݱ� ����: pstmt,con �ݱ� ����");
			}
		}

		return wDBArrayList;
	}
	
	
	 public static int deleteWeightData(int petno) { // petno �� DiaryDAO.petno �� �־�����Ѵ�. 
		 	Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			int count=0;			
		 try {
				String sql ="select petno "+"from petinfotb2 where email=? and petname=? ";
				con=DBUtility.getConection();
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, RootController.EMAIL);
				pstmt.setString(2, DiaryController.petname);
		
				rs=pstmt.executeQuery();
				while(rs.next()) {			
					petNo= rs.getInt("petno");
					System.out.println(petNo);
				}		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		 
		 String delWeightData= "delete from weighttb where petno= ? and recorddate=? ";
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(delWeightData);
			pstmt.setInt(1, petNo);	
			pstmt.setString(2, DiaryController.selectedRecordDate); 	// 	wTableView.getSelectionModel().getSelectedItem().getRecordDate()  
		
			count=pstmt.executeUpdate();
			if(count ==0) {
				System.out.println("delete����: delete������ ���� ���˹ٶ�");
				return count;
			}
			

		} catch (SQLException e) {
			System.out.println("delete����: �����ͺ��̽� delete ��������. ���˹ٶ�");
		} finally {
			try {
				if(pstmt != null) { pstmt.close(); } 
				if(con != null) { con.close(); } 
			
			}catch (Exception e) {
				RootController.callAlert("�ڿ��ݱ� ����: psmt,con �ݱ� ����.");
			}
			
			}

		return count;
		
		
	}
	
	 //03. ü�������� �����ϰ� �� ���� DB ������ �����ϵ��� �ϴ� �Լ�
	 public static int updateWeightData(Weight weight ) {
		 int count =0;
			 Connection con=null;
			 PreparedStatement pstmt=null;
			 ResultSet rs=null;
			 
			 String updateSql="update weighttb set weight=? , recorddate=? where recorddate=? " ;
			 
			
		 try {	
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(updateSql);
			
			pstmt.setDouble(1, Double.parseDouble(weight.getWeight()) );
			pstmt.setString(2, weight.getRecordate());
			pstmt.setString(3, DiaryController.selectedRecordDate);
	
			count=pstmt.executeUpdate();
			
		if(count==0) {
				System.out.println("���� ���� ����: ���� ���������� ���˹ٶ�");
				return count;
		}
		 
		 } catch (SQLException e) {
			 System.out.println("���� ����: �����ͺ��̽� ���� �����߾��. ���˹ٶ��ϴ�.");
		 } finally {
			 try {
					if(pstmt != null) { pstmt.close(); } 
					if(con != null) { con.close(); } 
				
				}catch (Exception e) {
					RootController.callAlert("�ڿ��ݱ� ����: psmt,con �ݱ� ����.");
				}						 
		 }
		 return count;
	 }
	 
}

