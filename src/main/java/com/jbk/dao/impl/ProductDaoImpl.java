package com.jbk.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.RollbackException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.criterion.AggregateProjection;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.jbk.dao.ProductDao;
import com.jbk.entity.ProductEntity;
import com.jbk.entity.ProductEntity;
import com.jbk.entity.ProductEntity;
import com.jbk.entity.ProductEntity;
import com.jbk.exception.ResourceAlreadyExistException;

import com.jbk.exception.ResourceNotExistException;

import com.jbk.exception.SomethingWentWrong;

import com.jbk.model.ProductModel;

//@Component
@Repository
public class ProductDaoImpl implements ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean addProduct(ProductEntity productEntity){
		boolean isAdded = false;
		Session session = null;

		try 
			{session = sessionFactory.openSession();
			Transaction transaction;
			// check its exists or not
			ProductEntity dbEntity = getProductByName(productEntity.getProductName());

			if (dbEntity == null) {
				session.save(productEntity); // insert into
				transaction = session.beginTransaction();
				transaction.commit();
				isAdded = true;

				// session=null;
			} else {
				throw new ResourceAlreadyExistException(
						"Product Already Exists with Id : " + productEntity.getProductId());
			}

		}

		catch (RollbackException e) {
			e.printStackTrace();
			throw new SomethingWentWrong("Something went wrong in during add product, check unique fields");
		} finally {
			if (session != null) {
				session.close();
			}

		}

		return isAdded;
	}

	@Override
	public ProductEntity getProductById(long productId) {
		ProductEntity productEntity = null;
		try {
			Session session = sessionFactory.openSession();

			productEntity = session.get(ProductEntity.class, productId); // select * from product where
																			// productId=10;
		} catch (HibernateException e) {
			throw new SomethingWentWrong("Connection Failure");
		}
		return productEntity;
	}

	@Override
	public boolean deleteProductById(ProductEntity productEntity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateProduct(ProductEntity productEntity) {
		boolean isUpdated = false;
		try {
			Session session = sessionFactory.openSession();

			ProductEntity dbProduct = getProductById(productEntity.getProductId());

			if (dbProduct != null) {
				session.update(productEntity);
				session.beginTransaction().commit();
				isUpdated = true;

			} else {
				isUpdated = false;
				// throw new ResourceNotExistsException("Product Not Exists with ID : " +
				// productEntity.getProductId());
			}

		}
//		catch (ResourceNotExistsException e) {
//			throw new ResourceNotExistsException("Product Not Exists with ID : " + productEntity.getProductId());
//		}
		catch (Exception e) {
			throw new SomethingWentWrong("Something Went Wrong During Update Product");
		}
		return isUpdated;
	}

	@Override
	public List<ProductEntity> getAllProducts() {
		List<ProductEntity> list = null;
		try {
			Session session = sessionFactory.openSession();
			// criteria
			Criteria criteria = session.createCriteria(ProductEntity.class);

			list = criteria.list(); // select * from product

		} catch (Exception e) {
			e.printStackTrace();
			throw new SomethingWentWrong("Something Went Wrong During retrive all Product");
		}
		return list;
	}

	@Override
	public List<ProductEntity> sortProduct(String orderType, String property) {
		List<ProductEntity> list = null;
		try {
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);

			if (orderType.equalsIgnoreCase("desc")) {
				criteria.addOrder(Order.desc(property));
			} else {
				criteria.addOrder(Order.asc(property));
			}

			list = criteria.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public double getMaxProductPrice() {
		double maxPrice = 0;
		try {
			Session session = sessionFactory.openSession();

			Criteria criteria = session.createCriteria(ProductEntity.class);

			Projection productPriceProjection = Projections.max("productPrice");

			criteria.setProjection(productPriceProjection); /// select max(productPrice) from ProductEntity

			List list = criteria.list();
			if (!list.isEmpty()) {
				maxPrice = (double) list.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxPrice;
	}

	@Override
	public List<ProductEntity> getMaxPriceProduct() {
		double maxProductPrice = getMaxProductPrice();
		List<ProductEntity> list = null;
		if (maxProductPrice > 0) {

			// find max price product
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(ProductEntity.class);

			// select * from product where product_price=?
			criteria.add(Restrictions.eq("productPrice", maxProductPrice)); // filter query

			list = criteria.list();

		} else {
			throw new ResourceNotExistException("Product Not Exists");
		}

		return list;

	}

	@Override
	public ProductEntity getProductByName(String productName) {

		// select * from product where product_name=? // Restrictions

		// HQL: from ProductEntity where productName= :parametername (parametername :
		// pname)
		List<ProductEntity> list = null;
		ProductEntity productEntity = null;
		try {
			Session session = sessionFactory.openSession();

			Query<ProductEntity> query = session.createQuery("FROM ProductEntity WHERE productName= :name");

			query.setParameter("name", productName);

			list = query.list();

			if (!list.isEmpty()) {
				productEntity = list.get(0);
			} else {
				return null;
			}

		}
//			catch (ResourceNotExistException e) {
//			throw new ResourceNotExistException("Product Not Exists");
//		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return productEntity;
	}

	@Override
	public List<ProductEntity> getAllProducts(double low, double high) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductEntity> getProductStartWith(String expression) {
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
	 public List<Object[]> getProductIdNamePrice(String productName) {
	        try (Session session = sessionFactory.openSession()) {
	            Query<Object[]> query = session.createQuery("SELECT p.productId, p.productName, p.productPrice FROM ProductEntity p WHERE p.productName = :name");
	            query.setParameter("name", productName);
	            return query.list();
	        } catch (Exception e) {
	            throw new RuntimeException("Error occurred while fetching data from database", e);
	        }
	    }
	 
	 
	 

	   

		@Override
		public List<Object[]> getProductIdNamePricess(String productName) { 
			try {
			    Session session = sessionFactory.openSession();
			    Query<Object[]> query = session.createQuery("SELECT p.productId, p.productName, p.productPrice FROM ProductEntity p WHERE p.productName = :name");
			    query.setParameter("name", productName);
			    List<Object[]> resultList = query.list();
			    if (resultList.isEmpty()) {
			        // Throw ProductNotFoundException if the result list is empty
			    	throw new ResourceNotExistException("Product Not Exists");
			    }
			    return resultList;
			} catch (Exception e) {
			    e.printStackTrace();
			    throw new ResourceNotExistException("Product Not Exists");
			}
		}}

		
	

