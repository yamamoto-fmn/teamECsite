package com.internousdev.galaxy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.internousdev.galaxy.dto.CartInfoDTO;
import com.internousdev.galaxy.util.DBConnector;

public class CartInfoDAO {

	public List<CartInfoDTO>getCartList(String userId) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();
		List<CartInfoDTO> cartInfoDTOList = new ArrayList<CartInfoDTO>();

		String sql = "SELECT "
				+ "ci.id as id,"
				+ "ci.user_id as user_id,"
				+ "ci.product_id as product_id,"
				+ "ci.product_count as product_count,"
				+ "pi.price as price,"
				+ "pi.product_name as product_name,"
				+ "pi.product_name_kana as product_name_kana,"
				+ "pi.image_file_path as image_file_path,"
				+ "pi.image_file_name as image_file_name,"
				+ "pi.release_date as release_date,"
				+ "pi.release_company as release_company,"
				+ "pi.status as status,"
				+ "(ci.product_count * pi.price) as subtotal,"
				+ "ci.regist_date as regist_date,"
				+ "ci.update_date as update_date "
				+ "FROM cart_info as ci "
				+ "LEFT JOIN product_info as pi "
				+ "ON ci.product_id = pi.product_id "
				+ "WHERE ci.user_id = ? "
				+ "ORDER BY update_date DESC, regist_date DESC";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			while(rs.next()){
				CartInfoDTO cartDTO = new CartInfoDTO();

				cartDTO.setId(rs.getInt("id"));
				cartDTO.setUserId(rs.getString("user_id"));
				cartDTO.setProductId(rs.getInt("product_id"));
				cartDTO.setProductCount(rs.getInt("product_count"));
				cartDTO.setPrice(rs.getInt("price"));
				cartDTO.setProductName(rs.getString("product_name"));
				cartDTO.setProductNameKana(rs.getString("product_name_kana"));
				cartDTO.setImageFilePath(rs.getString("image_file_path"));
				cartDTO.setImageFileName(rs.getString("image_file_name"));
				cartDTO.setReleaseDate(rs.getDate("release_date"));
				cartDTO.setReleaseCompany(rs.getString("release_company"));
				cartDTO.setStatus(rs.getString("status"));
				cartDTO.setSubTotal(rs.getInt("subtotal"));

				cartInfoDTOList.add(cartDTO);
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return cartInfoDTOList;
	}

	public int getTotalPrice(String userId) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "SELECT sum(product_count * price) as total_price "
				+ "FROM cart_info ci "
				+ "JOIN product_info pi "
				+ "ON ci.product_id = pi.product_id "
				+ "WHERE user_id = ? "
				+ "GROUP BY user_id";

		int totalPrice = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				totalPrice = rs.getInt("total_price");
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return totalPrice;
	}

	public int insertCartInfo(String userId,int productId,int productCount) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "INSERT INTO cart_info(user_id, product_id, product_count, regist_date, update_date) "
				+ "VALUES (?,?,?,now(),now())";

		int count = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ps.setInt(3, productCount);
			count = ps.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {

			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public int updateProductCount(String userId,int productId,int productCount) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "UPDATE cart_info "
				+ "SET product_count = (product_count + ?), update_date = now() "
				+ "WHERE user_id = ? AND product_id = ?";

		int count = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, productCount);
			ps.setString(2, userId);
			ps.setInt(3, productId);
			count = ps.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();

		}finally {

			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public int deleteCartProduct(String userId,int productId) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "DELETE FROM cart_info WHERE user_id = ? AND product_id = ?";

		int count = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			count = ps.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	public int deleteAllCartInfo(String userId) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "DELETE FROM cart_info WHERE user_id = ?";

		int count = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			count = ps.executeUpdate();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}

	public boolean searchProductId(String userId,int productId) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "SELECT COUNT(id) as count "
				+ "FROM cart_info "
				+ "WHERE user_id = ? AND product_id = ?";

		boolean bool = false;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setInt(2, productId);
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {

				if(rs.getInt("count") > 0) {
					bool = true;
				}
			}

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return bool;
	}

	public int updateTemporaryId(String userId,String tempUserId,int productId) {

		DBConnector db = new DBConnector();
		Connection con = db.getConnection();

		String sql = "UPDATE cart_info SET user_id = ?, update_date = now() "
				+ "WHERE user_id = ? AND product_id = ?";

		int count = 0;

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, tempUserId);
			ps.setInt(3, productId);
			count = ps.executeUpdate();

		}catch(SQLException e) {
			e.printStackTrace();

		}finally {

			try {
				con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

}
