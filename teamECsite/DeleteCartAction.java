package com.internousdev.galaxy.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.galaxy.dao.CartInfoDAO;
import com.internousdev.galaxy.dto.CartInfoDTO;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteCartAction extends ActionSupport implements SessionAware{

	public List<CartInfoDTO>cartInfoDTOList;
	private Map<String,Object>session;
	private int totalPrice;
	private Integer[] checkList;

	public String execute(){

		String result = ERROR;

		if(!session.containsKey("tempUserId") && !session.containsKey("userId")) {
			return "sessionTimeout";
		}

		CartInfoDAO cartInfoDAO = new CartInfoDAO();

		String userId = null;
		String tempLoginId = String.valueOf(session.get("loginFlg"));
		int loginFlg = "null".equals(tempLoginId)? 0 : Integer.parseInt(tempLoginId);

		if(loginFlg == 1) {
			userId = session.get("userId").toString();
		}else {
			userId = String.valueOf(session.get("tempUserId"));
		}

		int count = 0;

		for(Integer productId:checkList) {
			count += cartInfoDAO.deleteCartProduct(userId,productId);
		}

		if(count == checkList.length) {
			cartInfoDTOList = cartInfoDAO.getCartList(userId);
			totalPrice = cartInfoDAO.getTotalPrice(userId);
			result = SUCCESS;
		}

		return result;
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

	public Integer[] getCheckList(){
		return checkList;
	}

	public void setCheckList(Integer[]checkList) {
		this.checkList = checkList;
	}

}
