package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Hospital;
import Model.Join;

public class HospitalDAO {

	//01. ���̺��� ��ü������ ��� �������� �Լ�
		public static ArrayList<Hospital> getHospitaltbTotalData(){
			ArrayList<Hospital> dbArrayListHospital=new ArrayList<>();
			//1.1 �����ͺ��̽� hospitaltb�� �ִ� ���ڵ带 ��� �������� ������
			String selectHospital = "select * from hospitaltb";
			// 1.2 �����ͺ��̽� Connection �� �����;� �Ѵ�.
			Connection con = null;
			// 1.3 �������� �����ؾ��� Statement�� �������Ѵ�.
			PreparedStatement psmt = null;
			// 1.4. �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱⰴü ___ResultSet
			ResultSet rs=null;			
			try {
				con = DBUtility.getConection();
				psmt = con.prepareStatement(selectHospital);
				// 1.5 ������ ���������͸� ������ �������� �����϶�(������� �������� ������)
				//executeQuery();  �������� �����ؼ� ����� �����ö� ����ϴ� ������
				//executeUpdate() �� �������� �����ؼ� ���̺� ������ �� �� ����ϴ� ������
				rs= psmt.executeQuery(); 
				if (rs==null) {
					System.out.println("select ���� : select������ ����. ���˹ٶ�");
					return null;
				}				
				while(rs.next()) {
					Hospital hospital=new Hospital(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6)
							);
					dbArrayListHospital.add(hospital);
				}		
			} catch (SQLException e) {
				System.out.println("���� ���� : �����ͺ��̽� ���Կ� �����߽��ϴ�. ���˹ٶ�");
				e.printStackTrace();
			} finally {
				// 1.6 �ڿ���ü�� �ݾƾ��Ѵ�.
				try {
					if (psmt != null) {
						psmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException e) {
					System.out.println("�ڿ��ݱ� ���� : psmt,con �ݱ����");
				}
			}	
			return dbArrayListHospital;
		}	

		//02. ��������Ʈ���� �˻��� �ϴ� ��� 
		public static ArrayList<Hospital> getHospitaltbDataByRegion(String Region){
			ArrayList<Hospital> dbArrayListHospital=new ArrayList<>();
			String selectHospitalByRegion="select * from hospitaltb where addr like ? ";     	 	//'����Ư���� ������%'								
																							
			Connection con = null;		
			PreparedStatement psmt = null;			
			ResultSet rs=null;			
			try {
				con = DBUtility.getConection();
				psmt = con.prepareStatement(selectHospitalByRegion);
				psmt.setString(1, "����Ư���� "+ Region+"%");
				
			//	System.out.println( "����Ư���� "+ Region+"%"  );

				rs= psmt.executeQuery(); 
				
				dbArrayListHospital.clear();//////////////??????????????
				
				while(rs.next()) {
					Hospital hospital=new Hospital(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3),
							rs.getString(4),
							rs.getString(5),
							rs.getString(6)	
							);
					
					dbArrayListHospital.add(hospital);
				}		
			} catch (SQLException e) {
				System.out.println("���� ���� : �����ͺ��̽� ���Կ� �����߽��ϴ�. ���˹ٶ�");
				e.printStackTrace();
			} finally {
				// 1.6 �ڿ���ü�� �ݾƾ��Ѵ�.
				try {
					if (psmt != null) {
						psmt.close();
					}
					if (con != null) {
						con.close();
					}
				} catch (SQLException e) {
					RootController.callAlert("�ڿ��ݱ� ���� : psmt,con �ݱ����");
				}
			}	
			return dbArrayListHospital;
		}

}
