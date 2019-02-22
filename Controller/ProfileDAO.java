package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Join;
import Model.Profile;

public class ProfileDAO {
	public static ArrayList<Profile> dbArrayListProfile=new ArrayList<>();
	
	//01. ���������� ����ϴ� �Լ�
	public static int insertJoinData(Profile profile) {
		//1.1 �����ͺ��̽��� petinfotb �� �Է��ϴ� ������
		StringBuffer insertJoin=new StringBuffer();
		insertJoin.append("insert into petinfotb2 ");
		insertJoin.append("(petno,petname,petkind,petbirth,petgender,petvaccine,email,imgpath) ");
		insertJoin.append("values ");
		insertJoin.append("(null,?,?,?,?,?,?,?) ");
		//1.2 �����ͺ��̽� Connection �� �����;��Ѵ�.
		Connection con=null;
		//1.3 �������� �����ؾ��� Statement�� �������Ѵ�.
		PreparedStatement psmt=null;
		int count=0;
		
		ArrayList<Join> join =JoinDAO.getJoinMembershipEmailData();
		for( Join j: join) {
	
			System.out.println(j.getEmail());
		}
		
		try {
			con=DBUtility.getConection();
			psmt=con.prepareStatement(insertJoin.toString());
			//1.4 �������� ���� �����͸� �����Ѵ�.
			//pr=join.email;
			psmt.setString(1, profile.getPetname() );
			psmt.setString(2, profile.getPetkind() );
			psmt.setString(3, profile.getPetbirth() );
			psmt.setString(4, profile.getPetgender() );
			psmt.setString(5, profile.getPetvaccine() );		
			psmt.setString(6, RootController.EMAIL);//////////
			psmt.setString(7, profile.getImgpath() );
			

			//1.5 ���� �����͸� ������ �������� �����϶�
			count=psmt.executeUpdate();			
			if(count==0) {
				RootController.callAlert("���� ���� ����: �������� ���� ���˹ٶ�");
				return count;
			}
		
		} catch (SQLException e) {
			RootController.callAlert("���� ����: �����ͺ��̽� ���� ����. ���˹ٶ�");
			e.printStackTrace();
		} finally {
			try {
				if(psmt != null) { psmt.close(); } 
				if(con != null) { con.close(); } 
			
			}catch (Exception e) {
				RootController.callAlert("�ڿ��ݱ� ����: psmt,con �ݱ� ����.");
			}
			
			}

		return count;
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	//02. ���������� �������� �Լ� ____ ����, �̸�,����,����
	

	public static ArrayList<Profile> getPetinfoDataToDB(){

		//2.1 db �� joinmembershiptb2 ���� �̸��� Į���� ���븸 �� �������� ������
		String selectPetInfo= "select petkind, petname, petgender, petbirth "+  "from petinfotb2 where email=? ";
		//2.2 �����ͺ��̽� Connection�� �����;��Ѵ�.
		Connection con=null;
		//2.3 �������� �����ؾ��� Statememt �� �������Ѵ�.
		PreparedStatement psmt =null;
		//2.4 �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱⰴü
		ResultSet rs =null;
		
		
		try {
				con = DBUtility.getConection();
				psmt = con.prepareStatement(selectPetInfo);
				psmt.setString(1, RootController.EMAIL);
				
				rs=psmt.executeQuery();
				
				
				if(rs == null) {
					RootController.callAlert("���������� �������� ���� : ������ ���� ���˹ٶ�");
					return null;
				}
				dbArrayListProfile.clear(); ///////////// 
				while(rs.next()) {
					Profile petProfile=new Profile(
							rs.getString("petkind"),
							rs.getString("petname"),
							rs.getString("petgender"),
							rs.getString("petbirth")
						);
					
						System.out.println("aaa@naver.com �̿����� ������"+rs.getString("petname"));/////////////
						System.out.println(rs.getString("petgender"));////////////////
						
						dbArrayListProfile.add(petProfile);
				}
		} catch (SQLException e) {
				RootController.callAlert("dbArrayListJoin ���� ����:  ���� ���˹ٶ�");
			} finally {
				// 2.6 �ڿ���ü�� �ݾƾ��Ѵ�.
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
			return dbArrayListProfile;
		}
	
	
	
	
	
	
	//03. ���̺��� ������ ���� �������� �����ϴ� �Լ� ('�����ϱ�' ��ư�� ������ ����. -- �̸����� �˻��ؼ� ������! )
	public static int deletePetinfoData(String petname) {
		String deletePet = "delete from petinfotb2 where petname= ? ";
		
		Connection con=null;
		PreparedStatement pstmt=null;
		
		int count=0;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(deletePet);
			pstmt.setString(1, petname);
			
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
	
}
