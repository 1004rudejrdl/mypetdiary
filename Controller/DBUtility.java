package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtility {
	public static Connection getConection() {
		// 1. MySqp database class �ε��Ѵ�.
		Connection con = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// 2. �ּ�,���̵�,��й�ȣ�� ���ؼ� ���ӿ�û�Ѵ�.
			con = DriverManager.getConnection("jdbc:mysql://localhost/petdiarydb", "root", "123456");
		//	System.out.println("���� ����: �����ͺ��̽� ���ῡ �����Ͽ����ϴ�.");

		} catch (Exception e) {
			//RootController.callAlert("���� ����: �����ͺ��̽� ���ῡ �����Ͽ����ϴ�.");
			return null;
		}

		return con;

	}
}
