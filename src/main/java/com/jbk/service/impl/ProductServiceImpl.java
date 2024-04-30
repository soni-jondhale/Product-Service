package com.jbk.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.jbk.dao.ProductDao;
import com.jbk.dao.impl.ProductDaoImpl;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExistException;
import com.jbk.exception.ResourceNotExistException;
import com.jbk.exception.SomethingWentWrong;
import com.jbk.model.CategoryModel;
import com.jbk.model.ProductModel;
import com.jbk.model.Product_Supplier_Category;
import com.jbk.model.SupplierModel;
import com.jbk.service.ProductService;
import com.jbk.utility.ObjectValidator;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	ProductDao dao;

	@Autowired
	private ModelMapper mapper;
	
	@Autowired
    private ObjectValidator validator;
	
	Map<Integer, Map<String, String>> rowError = new HashMap<Integer, Map<String, String>>();
	//Map<String, Map<Integer, Map<String, String>>> badRecordMap = new HashMap<String, Map<Integer, Map<String, String>>>();
	Map<String, Object> finalMap = new LinkedHashMap<String, Object>();
	Map<String, String> validationMap = new HashMap<String, String>();
	int totalRecords = 0;
	List<Integer> rowNumList = new ArrayList<Integer>();

	@Override
	public boolean addProduct(ProductModel productModel) {

		ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);

		return dao.addProduct(productEntity);
	}

	@Override
	public ProductModel getProductById(long productId) {

		ProductEntity productEntity = dao.getProductById(productId);

		if (productEntity != null) {
			ProductModel productModel = mapper.map(productEntity, ProductModel.class);
			return productModel;
		} else {
			throw new ResourceNotExistException("Product Not Exists ID = " + productId);
		}

	}

	@Override
	public boolean deleteProductById(long productId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(ProductModel product) {

		return dao.updateProduct(mapper.map(product, ProductEntity.class));
	}

	@Override
	public List<ProductModel> getAllProducts() {
		List<ProductEntity> entityList = dao.getAllProducts();
		List<ProductModel> modelList = new ArrayList<ProductModel>();
		if (!entityList.isEmpty()) {

			for (ProductEntity productEntity : entityList) {

				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);

			}

			return modelList;
		} else {
			throw new ResourceNotExistException("Product Not Exists");
		}
	}

	@Override
	public List<ProductModel> sortProduct(String orderType, String property) {
		List<ProductEntity> entityList = dao.sortProduct(orderType, property);
		List<ProductModel> modelList = new ArrayList<ProductModel>();
		if (!entityList.isEmpty()) {

			for (ProductEntity productEntity : entityList) {

				ProductModel productModel = mapper.map(productEntity, ProductModel.class);
				modelList.add(productModel);

			}

			return modelList;
		} else {
			throw new ResourceNotExistException("Product Not Exists");
		}
	}

	@Override
	public double getMaxProductPrice() {
		double maxProductPrice = dao.getMaxProductPrice();
		if (maxProductPrice > 0) {
			return maxProductPrice;
		} else {
			throw new ResourceNotExistException("Product Not Exists");
		}

	}

	@Override
	public List<ProductModel> getMaxPriceProduct() {
		List<ProductEntity> list = dao.getMaxPriceProduct();

		List<ProductModel> modelList = new ArrayList<ProductModel>();
//		
//		for (ProductEntity productEntity : list) {
//			ProductModel productModel = mapper.map(productEntity, ProductModel.class);
//			modelList.add(productModel);
//		}

		modelList = list.stream().map(productEntity -> mapper.map(productEntity, ProductModel.class))
				.collect(Collectors.toList());

		return modelList;
	}

	@Override
	public ProductModel getProductByName(String productName) {
		ProductEntity dbProduct = dao.getProductByName(productName);
		if (dbProduct != null)
		{
			return mapper.map(dao.getProductByName(productName), ProductModel.class);
		}else
		{
		return null;
		}
	}

	@Override
	public List<ProductModel> getAllProducts(double lowPrice, double highPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductModel> getProductStartWith(String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double productPriceAverage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double countOfTotalProducts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ProductModel> getAllProducts(long category, long supplier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductModel> getAllProducts(String supplier) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public List<ProductModel> getProductByIdNamePrice(String productName) {
//	
//		return (List<ProductModel>) mapper.map(dao.getProductIdNamePrice(productName), ProductModel.class);
//	}

//	@Override

	public List<ProductModel> getProductByIdNamePrice(String productName) {
		List<Object[]> resultList = dao.getProductIdNamePrice(productName);
		List<ProductModel> productModels = new ArrayList<>();

		for (Object[] result : resultList) {
			ProductModel productModel = new ProductModel();
			productModel.setProductId((long) result[0]);
			productModel.setProductName((String) result[1]);
			productModel.setProductPrice((double) result[2]);
			productModels.add(productModel);
		}

		if (productModels.isEmpty()) {
			throw new ResourceNotExistException("Product Not Exists");
		}

		return productModels;
	}

	@Override
	public List<Object[]> getProductIdNamePricess(String productName) {
		return dao.getProductIdNamePricess(productName);

	}

	private List<ProductModel> readExcel(String filePath) {

		List<ProductModel> list = new ArrayList<>();
		// point out file
		try {
			FileInputStream fis = new FileInputStream(filePath);

			// workbook
			Workbook workbook = WorkbookFactory.create(fis);
			// sheet
			Sheet sheet = workbook.getSheetAt(0);

			totalRecords = sheet.getLastRowNum();
			// rows
			Iterator<Row> rows = sheet.rowIterator();
			// iterate rows and pointout every row

			while (rows.hasNext()) {
				Row row = rows.next();
				int rowNum = row.getRowNum();
				if (rowNum == 0) {
					continue;
				}
//				if (row.getRowNum() == 0) {
//					continue;
//				}
				// iterate row(single) and pointout every cell

				ProductModel productModel = new ProductModel();
				String strDate = new SimpleDateFormat("yyyyMMddHHMMSS").format(Calendar.getInstance().getTime());
				productModel.setProductId(Long.parseLong(strDate) + rowNum);

				Iterator<Cell> cells = row.cellIterator();
				while (cells.hasNext()) {
					Cell cell = cells.next();
					// getData
					// System.out.println(cell.getStringCellValue());
//				CellType cellType = cell.getCellType();
//				if(cellType==CellType.STRING)
//				{
//					System.out.println(cell.getStringCellValue());
//				}else
//				{System.out.println(cell.getNumericCellValue());
//				}
					int columnIndex = cell.getColumnIndex();
					switch (columnIndex) {
					case 0: {
						String productName = cell.getStringCellValue();
						productModel.setProductName(productName);
						break;
					}
					case 1: {
						double supplierId = cell.getNumericCellValue();
						productModel.setSupplierId((long)cell.getNumericCellValue());// here object of supplier so make supplier object
						break;
					}
					case 2: {
						double categoryId = cell.getNumericCellValue();
						productModel.setCategoryId((long)cell.getNumericCellValue());
						break;
					}
					case 3: {
						double productQty = cell.getNumericCellValue();
						productModel.setProductQty((int) productQty);
						break;
					}
					case 4: {
						double productPrice = cell.getNumericCellValue();
						productModel.setProductPrice(productPrice);
						break;
					}
					case 5: {
						double charges = cell.getNumericCellValue();
						productModel.setDeliveryCharges((int) charges);
						break;
					}

					}
				}
				// check data is proper or not
				//validationMap = ObjectValidator.validateProduct(productModel);
				 validationMap = validator.validateProduct(productModel);
				if (!validationMap.isEmpty()) {
					// add validation map with row number
					rowError.put(rowNum + 1, validationMap);

				} else {
					// check its exists or not

					ProductModel dbProduct = getProductByName(productModel.getProductName());
					if (dbProduct != null) {
						rowNumList.add(rowNum + 1);
					} else {
						// add product into list
						list.add(productModel);
					}
				}
			//	list.add(productModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e.getMessage());
		}
		return list;

	}

	@Override
	public Map<String, Object> uploadSheet(MultipartFile file) {
	//public String uploadSheet(MultipartFile file) {
		String msg = "";
		int isAddedCounter = 0;
		int isAlreadyExistsCounter = 0;
		int issueCounter = 0;
		try {
			String path = "uploaded";
			String fileName = file.getOriginalFilename();
			FileOutputStream fout = new FileOutputStream(path + File.separator + fileName);
			byte[] data = file.getBytes();
			fout.write(data);
			// read excel code
			List<ProductModel> list = readExcel(path + File.separator + fileName);

			for (ProductModel productModel : list) {
				// System.out.println(productModel);
				ProductEntity productEntity = mapper.map(productModel, ProductEntity.class);
				try {

					boolean isAddedd = dao.addProduct(productEntity);

					if (isAddedd) {
						isAddedCounter = isAddedCounter + 1;
					}
				}
//			catch (ResourceAlreadyExistException e) 
//			{
//				isAlreadyExistsCounter=isAlreadyExistsCounter+1;
//			}
				catch (SomethingWentWrong e) {
					issueCounter = issueCounter + 1;
				}
			}
			finalMap.put("Total records in sheet", totalRecords);
			finalMap.put("uploaded record in Db", isAddedCounter);
			finalMap.put("Total exists record in Db", rowNumList.size());
			finalMap.put("Row Num Exists records in Db ", rowNumList);
			finalMap.put("Total excluded record", rowError.size());
			finalMap.put("Bad record row number", rowError);

		} catch (Exception e) {
			e.printStackTrace();
		}
		 //return "Addedd = "+isAddedCounter+ " Already Exists = "+isAlreadyExistsCounter+" Issuies = "+issueCounter;
		return finalMap;
	}

	@Override
	public Product_Supplier_Category getProductWithSCByPId(long productId) {
		Product_Supplier_Category psc = null;
	ProductModel productModel = getProductById(productId);
	if(productModel!=null) {
		psc=new Product_Supplier_Category();
		psc.setProductModel(productModel);
	
	try {
		SupplierModel supplierModel=restTemplate.getForObject("http://localhost:8086/supplier/get-supplier-by-id/"+productModel.getSupplierId(), SupplierModel.class);
	
		psc.setSupplierModel(supplierModel);
		} catch (ResourceAccessException e) {
		psc.setSupplierModel(null);
	}

	
	try {
		CategoryModel categoryModel=restTemplate.getForObject("http://localhost:8087/category/get-category-by-id/"+productModel.getCategoryId(), CategoryModel.class);
		if(categoryModel.getCategoryId()<=0)
		{
			psc.setCategoryModel(null);
		}else {psc.setCategoryModel(categoryModel);}
		
	} catch (ResourceAccessException e) {
		psc.setCategoryModel(null);
	}

	
	
	}else {
		}
	//}
	//SupplierModel supplierModel = null;
	//CategoryModel categoryModel = null;
	//Product_Supplier_Category psc=new Product_Supplier_Category(productModel,supplierModel,categoryModel);
	return psc;
	}

}
