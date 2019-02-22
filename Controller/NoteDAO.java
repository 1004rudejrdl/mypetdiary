package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.NoteDiary;

public class NoteDAO {
	public static ArrayList<NoteDiary> dbArrayListDiary=new ArrayList<>();
	public static ArrayList<String> dateArrayList=new ArrayList<>();
	
	//01. ��Ʈ�� ������ ��� ����ϴ� �Լ�
	public static int insertNoteData(NoteDiary noteDiary) {
		//petinfotb2 ���� petno �� ã�ƿ´�.
		int petno=0;
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		
		String sql="select petno "+"from petinfotb2 where email=? and petname=? " ;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				petno=rs.getInt("petno");
			}
			
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		//////// ��Ʈ���� DB ���� sql �� ////////
		StringBuffer insertNote=new StringBuffer();
		insertNote.append("insert into diarytb ");
		insertNote.append("(day,meal,pee,poo,brushing,play,memo,petno) ");
		insertNote.append("values ");
		insertNote.append("(?,?,?,?,?,?,?,?) ");
		
		
		int count=0;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(insertNote.toString());
		
			pstmt.setString(1, noteDiary.getDay());
			pstmt.setString(2, noteDiary.getMeal());
			pstmt.setString(3, noteDiary.getPee());
			pstmt.setString(4, noteDiary.getPoo());
			pstmt.setString(5, noteDiary.getBrushing());
			pstmt.setString(6, noteDiary.getPlay());
			pstmt.setString(7, noteDiary.getMemo());
			pstmt.setInt(8,  petno);
			
			count=pstmt.executeUpdate();
			
			if(count==0) {
				RootController.callAlert("���� ���� ����: ��Ʈ ���� ���� ����. ���˹ٶ�");
				return count;
			}	
		} catch (SQLException e) {
			RootController.callAlert("���� ����: ��Ʈ������ ���� ����. ���˹ٶ�");
			e.printStackTrace();
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
	
	//02. ���̾���̺��� ��ü ������ �������� �Լ�.
	public static ArrayList<NoteDiary> getDiaryTotalData(){
		int petno=0;
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		////���� ������ȣ�� �����´�! 
		String sql="select petno "+"from petinfotb2 where email=? and petname=? " ;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				petno=rs.getInt("petno");
			}
			
			String sql2="select day,meal,pee,poo,brushing,play,memo from diarytb where petno=?  ";
			pstmt=con.prepareStatement(sql2);
			
			pstmt.setInt(1, petno);

			rs=pstmt.executeQuery();
		
			while(rs.next()) {	// ���,�Һ�,�뺯,��ġ,����,����,�޸�
				NoteDiary noteDiary = new NoteDiary(
							rs.getString("day"), 
							rs.getString("meal"), 
							rs.getString("pee"), 
							rs.getString("poo"), 
							rs.getString("brushing"), 
							rs.getString("play"), 
							rs.getString("memo")	);
				dbArrayListDiary.add(noteDiary);
			}
			
		} catch (SQLException e1) {
			RootController.callAlert("��Ʈ������ �ҷ����� ����: ��Ʈ ������ �ҷ����� ����");
			e1.printStackTrace();
		}
		
		return dbArrayListDiary;
	}

	//03. ���̾���̺��� ����ϸ� �ҷ��ͼ� �迭�� �ִ� �Լ�//////////////////////���� ���� �����ؾ���.

	public static ArrayList<String> getDayfromDiarytb(){
		int petno=0;
		Connection con=null;
		PreparedStatement pstmt =null;
		ResultSet rs=null;
		////���� ������ȣ�� �����´�! 
		String sql="select petno "+"from petinfotb2 where email=? and petname=? " ;
		
		try {
			con=DBUtility.getConection();
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);
			
			rs=pstmt.executeQuery();
			while(rs.next()) {
				petno=rs.getInt("petno");
				//System.out.println(petno); 	
			}
		
		String sql2= "select day from diarytb where petno=? ";
		pstmt=con.prepareStatement(sql2);
		
		pstmt.setInt(1, petno);

		rs=pstmt.executeQuery();
		while(rs.next()) {		
			dateArrayList.add(rs.getString(1));		
		}
	
		for(String day: dateArrayList) {
			System.out.println(day);
		}
		

		} catch (SQLException e1) {
			RootController.callAlert("��Ʈ������ �ҷ����� ����: ��Ʈ ������ �ҷ����� ����");
			e1.printStackTrace();
		}
		
		
	return dateArrayList;
	
	}
	
}
