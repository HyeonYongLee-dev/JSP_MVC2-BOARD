package sec01.ex01;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MemberDAO {
	private DataSource dataFactory;
	private Connection conn;
	private PreparedStatement psmt;
	
	public MemberDAO() {
		
		try {
			Context ctx=new InitialContext();
			Context envContext=(Context) ctx.lookup("java:/comp/env");
			dataFactory=(DataSource) envContext.lookup("jdbc/oracle");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<MemberVO> listMembers() {
	List<MemberVO> membersList=new ArrayList<MemberVO>();
	
	try {
		conn=dataFactory.getConnection();
		String query="select * from t_member order by joinDate desc";
		System.out.println(query);
		psmt=conn.prepareStatement(query);
		ResultSet rs=psmt.executeQuery();
		
		while(rs.next()) {
			String id=rs.getString("id");
			String pwd=rs.getString("pwd");
			String name=rs.getString("name");
			String email=rs.getString("email");
			Date joinDate=rs.getDate("joinDate");
			
			MemberVO memberVO=new MemberVO(id,pwd,name,email,joinDate);
			membersList.add(memberVO);
		}
			rs.close();
			psmt.close();
			conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return membersList;
	}
	
	
	public void addMember(MemberVO m) {
		try {
			conn=dataFactory.getConnection();
			String id=m.getId();
			String pwd=m.getPwd();
			String name=m.getName();
			String email=m.getEmail();
			String query="insert into t_member(id,pwd,name,email)"+"values(?,?,?,?)";
			System.out.println(query);
			psmt=conn.prepareStatement(query);
			psmt.setString(1, id);
			psmt.setString(2, pwd);
			psmt.setString(3, name);
			psmt.setString(4, email);
			psmt.executeUpdate();
			psmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public MemberVO findMember(String _id) {
		MemberVO memInfo = null;
		try {
			conn=dataFactory.getConnection();
			String query="select * from t_member where id=?";
			psmt=conn.prepareStatement(query);
			psmt.setString(1, _id);
			System.out.println(query);
			ResultSet rs= psmt.executeQuery();
			rs.next();
			String id=rs.getString("id");
			String pwd=rs.getString("pwd");
			String name=rs.getString("name");
			String email=rs.getString("email");
			Date joinDate=rs.getDate("joinDate");
			memInfo = new MemberVO(id, pwd, name, email, joinDate);
			psmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return memInfo;
	}
	
	public void modMember(MemberVO memberVO) {
		String id = memberVO.getId();
		String pwd = memberVO.getPwd();
		String name = memberVO.getName();
		String email = memberVO.getEmail();
		
		try {
			conn=dataFactory.getConnection();
			String query="update t_member set pwd=?, name=?, email=? where id=?";
			System.out.println(query);
			psmt=conn.prepareStatement(query);
			psmt.setString(1, pwd);
			psmt.setString(2, name);
			psmt.setString(3, email);
			psmt.setString(4, id);
			
			psmt.executeUpdate();
			psmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delMember(String id) {
		try {
			conn=dataFactory.getConnection();
			String query="delete from t_member where id=?";
			System.out.println(query);
			psmt=conn.prepareStatement(query);
			psmt.setString(1, id);
			psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}