<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/style.css">
<link rel="stylesheet" href="./css/table.css">
<link rel="stylesheet" href="./css/galaxy.css">
<title>カート</title>

	<script>

	    function checkValue(){

	    	var checkList = document.getElementsByClassName("checkList");
	    	var checkFlg = 0;

	    	for(var i=0; i<checkList.length; i++){

	    		if(checkList[i].checked){
	    			checkFlg = 1;
	    			break;
	    		}
	    	}

	    	if(checkFlg == 1){
	    		document.getElementById('deleteButton').disabled="";
	    	}else{
	    		document.getElementById('deleteButton').disabled="true";
	    	}
	    }

	</script>

</head>
<body>

<script src="./js/galaxy.js"></script>

<jsp:include page="header.jsp"/>

	<div id="contents">

		<h1>カート画面</h1>

		<s:if test="cartInfoDTOList != null && cartInfoDTOList.size() > 0">

			<s:form action="DeleteCartAction">

				<table class="list-table">

			  		<thead>
	      				<tr>
	        				<th><s:label value="#"/></th>
	        				<th><s:label value="商品名"/></th>
	        				<th><s:label value="商品名ふりがな"/></th>
	        				<th><s:label value="商品画像"/></th>
	        				<th><s:label value="値段"/></th>
	        				<th><s:label value="発売会社名"/></th>
	        				<th><s:label value="発売年月日"/></th>
	        				<th><s:label value="購入個数"/></th>
	        				<th><s:label value="合計金額"/></th>
	      				</tr>
		  			</thead>

		  			<tbody>
			    		<s:iterator value="cartInfoDTOList">

	   					<tr>
	       					<td><input type="checkbox" name="checkList" class="checkList" value='<s:property value="productId"/>' onchange="checkValue()"/></td>
	       					<td><s:property value="productName"/></td>
	   						<td><s:property value="productNameKana"/></td>
	   						<td><img src='<s:property value="imageFilePath"/>/<s:property value="imageFileName"/>' width="50px" height="50px"/></td>
	   						<td><s:property value="price"/>円</td>
	   						<td><s:property value="releaseCompany"/></td>
	      					<td><s:property value="releaseDate"/></td>
	      					<td><s:property value="productCount"/></td>
	      					<td><s:property value="subTotal"/>円</td>
	  					</tr>

						</s:iterator>
		  			</tbody>

				</table>

				<h2><s:label value="カート合計金額："/><s:property value="totalPrice"/> 円</h2><br>

				<div class="right_button">
					<s:submit value="削除" id="deleteButton" class="submit_btn" disabled="true"/>
				</div>

			</s:form>

			<s:form id="form">

				<div class="left_button">

					<s:if test="#session.loginFlg == 1">
						<s:submit value="決済" class="submit_btn" onclick="setAction('SettlementConfirmAction')"/>
					</s:if>

					<s:else>
						<s:submit value="決済" class="submit_btn" onclick="setAction('GoLoginAction')"/>
						<s:hidden name="cartFlg" value="1"/>
					</s:else>

				</div>

			</s:form>

		</s:if>

		<s:else>
			<div class="info">
				カート情報がありません。
			</div>
		</s:else>

	</div>

</body>
</html>