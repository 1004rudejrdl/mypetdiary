package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Join;


public class JoinDAO {
	public static ArrayList<Join> dbArrayListJoin=new ArrayList<>();
	
	//01. ȸ�������� ����ϴ� �Լ�
	public static int insertJoinData(Join join) {
		//1.1 �����ͺ��̽��� joinmembershiptb �� �Է��ϴ� ������
		StringBuffer insertJoin=new StringBuffer();
		insertJoin.append("insert into joinmembershiptb ");
		insertJoin.append("(email,password,name,birth,gender,nickname,phone) ");
		insertJoin.append("values ");
		insertJoin.append("(?,?,?,?,?,?,?) ");
		//1.2 �����ͺ��̽� Connection �� �����;��Ѵ�.
		Connection con=null;
		//1.3 �������� �����ؾ��� Statement�� �������Ѵ�.
		PreparedStatement psmt=null;
		int count=0;
		
		try {
			con=DBUtility.getConection();
			psmt=con.prepareStatement(insertJoin.toString());
			//1.4 �������� ���� �����͸� �����Ѵ�.
			
			psmt.setString(1, join.getEmail());
			psmt.setString(2, join.getPassword() );
			psmt.setString(3, join.getName() );
			psmt.setString(4, join.getBirth());
			psmt.setString(5, join.getGender() );
			psmt.setString(6, join.getNickname() );
			psmt.setString(7, join.getPhone() );

			//1.5 ���� �����͸� ������ �������� �����϶�
			count=psmt.executeUpdate();			
			if(count==0) {
				System.out.println("���� ���� ����: �������� ���� ���˹ٶ�");
				return count;
			}
		
		} catch (SQLException e) {
			System.out.println("���� ����: �����ͺ��̽� ���� ����. ���˹ٶ�");
			e.printStackTrace();
		} finally {
			try {
				if(psmt != null) { psmt.close(); } 
				if(con != null) { con.close(); } 
			
			}catch (SQLException e) {
				System.out.println("�ڿ��ݱ� ����: psmt,con �ݱ� ����.");
			}
			
			}

		return count;
	}

	
	//02. ���̺��� ��ü������ ��� �������� �Լ�
	public static ArrayList<Join> getJoinMembershipTotalData(){

		//2.1 �����ͺ��̽� �л����̺� �ִ� ���ڵ带 ��� �������� ������
		String selectMember = "select * from joinmembershiptb ";
		// 2.2 �����ͺ��̽� Connection �� �����;� �Ѵ�.
		Connection con = null;
		// 2.3 �������� �����ؾ��� Statement�� �������Ѵ�.
		PreparedStatement psmt = null;
		// 2.4. �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱⰴü ___ResultSet
		ResultSet rs=null;
		
		
		try {
			con = DBUtility.getConection();
			psmt = con.prepareStatement(selectMember);


			// 1.5 ������ ���������͸� ������ �������� �����϶�(������� �������� ������)
			//executeQuery();  �������� �����ؼ� ����� �����ö� ����ϴ� ������
			//executeUpdate() �� �������� �����ؼ� ���̺� ������ �� �� ����ϴ� ������
			rs= psmt.executeQuery(); 

			if (rs==null) {
				System.out.println("select ���� : select������ ����. ���˹ٶ�");
				return null;
			}
			
			while(rs.next()) {
				Join join=new Join(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getString(7)		
						);
				dbArrayListJoin.add(join);
			}		
		} catch (SQLException e) {
			System.out.println("���� ���� : �����ͺ��̽� ���Կ� �����߽��ϴ�. ���˹ٶ�");
			e.printStackTrace();
		} finally {
			// 1.6 �ڿ���ü�� �ݾƾ��Ѵ�.
			try {
				
				if (psmt != null) { psmt.close(); }
				if (con != null) { con.close(); }
		
			} catch (SQLException e) {
				System.out.println("�ڿ��ݱ� ���� : psmt,con �ݱ����");
			}
		}	
		return dbArrayListJoin;
	}	

	
	
	
	
	////////////////03. joinmembershiptb2 ���� �̸��� �����͸� �������� �Լ�( �α��ζ� �̸��� �񱳸� ���ؼ�______�Ⱦ��ϴ�._________)///////////////
	
	public static ArrayList<Join> getJoinMembershipEmailData(){

	//1.1 db �� joinmembershiptb2 ���� �̸��� Į���� ���븸 �� �������� ������
	String selectEmail= "select email, password  from joinmembershiptb ";
	
	//1.2 �����ͺ��̽� Connection�� �����;��Ѵ�.
	Connection con=null;
	
	//1.3 �������� �����ؾ��� Statememt �� �������Ѵ�.
	PreparedStatement psmt =null;
	
	//1.4 �������� �����ϰ��� �����;��� ���ڵ带 ����ִ� ���ڱⰴü
	ResultSet rs =null;
	
	
	try {
			con = DBUtility.getConection();
			psmt = con.prepareStatement(selectEmail);
			
			rs=psmt.executeQuery();
			if(rs == null) {
				RootController.callAlert("email select ���� : select email ������ ���� ���˹ٶ�");
				return null;
			}
			while(rs.next()) {
				Join join = new Join(rs.getString(1));
				dbArrayListJoin.add(join);
			}
	} catch (SQLException e) {
			RootController.callAlert("dbArrayListJoin ���� ����:  ���� ���˹ٶ�");
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
		return dbArrayListJoin;

	}

}
