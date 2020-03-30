package com.internousdev.galaxy.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.galaxy.dao.CartInfoDAO;
import com.internousdev.galaxy.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class CartAction extends ActionSupport implements SessionAware{

	private List<CartInfoDTO>cartInfoDTOList;
	private Map<String,Object>session;
	private int totalPrice;

	public String execute(){

		if(!session.containsKey("tempUserId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}

		String userId = null;
		CartInfoDAO cartInfoDAO = new CartInfoDAO();

		String tempLoginId = String.valueOf(session.get("loginFlg"));
		int loginFlg = "null".equals(tempLoginId)? 0 : Integer.parseInt(tempLoginId);

		if(loginFlg == 1) {
			userId = session.get("userId").toString();
		} else {
			userId = String.valueOf(session.get("tempUserId"));
		}

		cartInfoDTOList = cartInfoDAO.getCartList(userId);
		totalPrice = cartInfoDAO.getTotalPrice(userId);

		return SUCCESS;

	}

	public List<CartInfoDTO>getCartInfoDTOList(){
		return cartInfoDTOList;
	}

	public Map<String,Object>getSession(){
		return session;
	}

	public void setSession(Map<String,Object>session) {
		this.session = session;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

}
