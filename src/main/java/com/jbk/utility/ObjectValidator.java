package com.jbk.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbk.exception.ResourceNotExistException;
import com.jbk.exception.SomethingWentWrong;
import com.jbk.model.ProductModel;


@Component
public class ObjectValidator {
	
public static Map<String,String>validateProduct(ProductModel productModel)
{
		Map<String, String>validateMap=new HashMap<String,String>();
		
		String productName = productModel.getProductName();
		double productPrice = productModel.getProductPrice();
		int productQty = productModel.getProductQty();
		int deliveryCharges = productModel.getDeliveryCharges();
		long supplierId = productModel.getSupplierId();
		long categoryId = productModel.getCategoryId();
		
		if(productName==null || productName.trim().equals("")) {
			validateMap.put("product name", "Invalid product name");
		}
		if(productPrice<=0)
		{
			validateMap.put("product price", "Product price must be greater than zero");
		}
		if(productQty<=0)
		{
			validateMap.put("product quantity", "product quantity mus be greater than zero");
		}
		if(deliveryCharges<0)
		{
			validateMap.put("product charges", "Delivery charges should not be negative");
		}
		if(supplierId>0)
		{
			try {
				//supplierService.getSupplierById(supplierId);//search-dummy.restapiexample.com
				
			} catch (ResourceNotExistException e) {
				validateMap.put("supplier", e.getMessage());
			}catch(SomethingWentWrong e) {
				validateMap.put("supplier", e.getMessage());
			}
		}else{
			validateMap.put("supplier", "Invalid supplier Id");
		}
		
		
		if(categoryId>0)
		{
			try {
				//categoryService.getCategoryById(categoryId);
				
			} catch (ResourceNotExistException e) {
				validateMap.put("category", e.getMessage());
			}catch(SomethingWentWrong e) {
				validateMap.put("category", e.getMessage());
			}
		}else{
			validateMap.put("category", "Invalid category Id");
		}

		return validateMap;

		}	
}


