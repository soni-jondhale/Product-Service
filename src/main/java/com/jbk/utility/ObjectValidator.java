package com.jbk.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.jbk.exception.ResourceNotExistException;
import com.jbk.exception.SomethingWentWrong;
import com.jbk.model.ProductModel;
import com.jbk.model.SupplierModel;


@Component
public class ObjectValidator {
	
	@Autowired
	private RestTemplate restTemplate;
	
public  Map<String,String>validateProduct(ProductModel productModel)
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
//		if(supplierId>0)
//		{
//			try {
//				//supplierService.getSupplierById(supplierId);//search-dummy.restapiexample.com
//				
//			} catch (ResourceNotExistException e) {
//				validateMap.put("supplier", e.getMessage());
//			}catch(SomethingWentWrong e) {
//				validateMap.put("supplier", e.getMessage());
//			}
//		}else{
//			validateMap.put("supplier", "Invalid supplier Id");
//		}
		
		if (supplierId > 0) {
	try {
				//supplierService.getSupplierById(supplierId);
				
				try {
					SupplierModel supplierModel = restTemplate.getForObject(
							"http://SUPPLIER-SERVICE/supplier/get-supplier-by-id/" + productModel.getSupplierId(),
							SupplierModel.class);
					if(supplierModel==null || supplierModel.getSupplierId()<=0) {
						validateMap.put("Supplier", "Invalid Supplier Id");
					}


				} catch (ResourceAccessException e) {
					validateMap.put("Supplier", "Supplier Service is down");
				}

			} catch (ResourceNotExistException e) {
				validateMap.put("Supplier", e.getMessage());
			} catch (SomethingWentWrong e) {
				validateMap.put("Supplier", e.getMessage());
			}
		} else {
			validateMap.put("Supplier", "Invalid Supplier Id");
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


