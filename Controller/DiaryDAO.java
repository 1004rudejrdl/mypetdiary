package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.NoteDiary;
import Model.Profile;

public class DiaryDAO {
	public static ArrayList<Profile> petinfoList = new ArrayList<>();
	public static ArrayList<NoteDiary> dbArrayListDiary = new ArrayList<>();
	public static int count = 0;
	public static int petno=0;
	// 1.������ �������� �̸�,����,����,������ �����ͼ� ü�߰��� �ǿ� �ֱ����� �Լ�.
	public static ArrayList<Profile> getPetDatabyPetname(){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		
		//���� ���õ� ������ �̸��� �����´�.
		//String petname=
		
		
		
		String sql = "select * from petinfotb2 where petname=? "; 
		
		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, DiaryController.petname);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Profile profile=new Profile(
						rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(8)
						);
				petinfoList.add(profile);	
			}
				
			
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		return petinfoList;
	}
	
	//2. ���̾���̺��� ������ �������� �Լ�
	public static ArrayList<NoteDiary> getDiaryData() {
	/////////////////////////////////////petno �� day �� DB ���� ��Ʈ������ ��������.////////////////////////////////////////////////
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		////���� ������ȣ�� �����´�!  __ �α��ν� �Է��� email �� �ش� �����̸����� petno �� �����´�.
		String sql = "select petno " + "from petinfotb2 where email=? and petname=? ";

		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				petno = rs.getInt("petno");
				System.out.println(petno); ///////// �ܼ�/////////
			}
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sql2 = "select day,meal,pee,poo,brushing,play,memo,count(memo) from diarytb where petno=? and day=? ";

		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql2);

			pstmt.setInt(1, petno);
			pstmt.setString(2, DiaryController.noteDay);

			rs = pstmt.executeQuery();

			System.out.println(DiaryController.noteDay + "�� �ɰ��ΰ�!"); ////////// �ܼ�Ȯ��

			while (rs.next()) {
				NoteDiary noteDiary = new NoteDiary(rs.getString("day"), rs.getString("meal"), rs.getString("pee"),
						rs.getString("poo"), rs.getString("brushing"), rs.getString("play"), rs.getString("memo"));

				count = rs.getInt(8);
				dbArrayListDiary.add(noteDiary);
			}

		} catch (SQLException e1) {
			RootController.callAlert("��Ʈ������ �ҷ����� ����: ��Ʈ ������ �ҷ����� ����");
			e1.printStackTrace();
		} finally {
				// �ڿ���ü�� �ݾƾ��Ѵ�.
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				RootController.callAlert("�ڿ��ݱ� ���� : psmt,con �ݱ����");
			}
		}

		return dbArrayListDiary;
	}
	
	
	
	//3. ���̾���̺��� �������� ��Ʈ������ �������� ���� �Լ�
	public static ArrayList<NoteDiary> getPreviousDiaryData( ) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		////���� ������ȣ�� �����´�!  __ �α��ν� �Է��� email �� �ش� �����̸����� petno �� �����´�.
		String sql = "select petno " + "from petinfotb2 where email=? and petname=? ";

		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, RootController.EMAIL);
			pstmt.setString(2, DiaryController.petname);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				petno = rs.getInt("petno");
				System.out.println(petno); ///////// �ܼ�/////////
			}
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		String sql2 = "select day,meal,pee,poo,brushing,play,memo,count(memo) from diarytb where petno=? and day=? ";

		try {
			con = DBUtility.getConection();
			pstmt = con.prepareStatement(sql2);

			pstmt.setInt(1, petno);
			pstmt.setString(2, DiaryController.previousMonthDate);

			rs = pstmt.executeQuery();
			//System.out.println(petno);
		
			while (rs.next()) {
				NoteDiary noteDiary = new NoteDiary(rs.getString("day"), rs.getString("meal"), rs.getString("pee"),
						rs.getString("poo"), rs.getString("brushing"), rs.getString("play"), rs.getString("memo"));

				count = rs.getInt(8);
				dbArrayListDiary.add(noteDiary);
			}

		} catch (SQLException e1) {
			System.out.println("��Ʈ������ �ҷ����� ����: ��Ʈ ������ �ҷ����� ����");
			e1.printStackTrace();
		} finally {
// �ڿ���ü�� �ݾƾ��Ѵ�.
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				System.out.println("�ڿ��ݱ� ���� : psmt,con �ݱ����");
			}
		}

		return dbArrayListDiary;
	}

}
