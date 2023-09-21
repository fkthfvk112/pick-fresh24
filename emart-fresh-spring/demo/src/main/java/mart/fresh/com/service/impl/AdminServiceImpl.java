package mart.fresh.com.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.AdminDao;
import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dto.ManagerOrderObjDto;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;
import mart.fresh.com.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{
	private final AdminDao adminDao;

	@Autowired
	public AdminServiceImpl(AdminDao adminDao) {
		this.adminDao = adminDao;
	}
	
	@Override
	public List<ManagerOrderObjDto> getManagerOrderObjsByDate(String date) {
		
		 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 
	     Date parsedDate = null;
			try {
				parsedDate = dateFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		List<ManagerOrderWithObj> orderResultList = adminDao.getManagerOrderObjsByDate(parsedDate);
		List<ManagerOrderObjDto> orderDtos = new ArrayList();
		
		for(ManagerOrderWithObj orderObj:orderResultList) {
			ManagerOrderObjDto dto = new ManagerOrderObjDto();
			dto.setManagerOrderDate(orderObj.getManagerOrderDate());
			dto.setManagerOrderNum(orderObj.getManagerOrderNum());
			dto.setManagerOrderQuantity(orderObj.getManagerOrderQuantity());
			dto.setManagerOrderStatus(orderObj.isManagerOrderStatus());
			
			ProductDto productDto = new ProductDto();
			productDto.setProductId(orderObj.getProduct().getProductId());
			productDto.setPriceNumber(orderObj.getProduct().getPriceNumber());
			productDto.setPriceString(orderObj.getProduct().getPriceString());
			productDto.setProductTitle(orderObj.getProduct().getProductTitle());
			productDto.setProductExpirationDate(orderObj.getProduct().getProductExpirationDate());
			productDto.setProductType(orderObj.getProduct().getProductType());
			productDto.setProductImgUrl(orderObj.getProduct().getProductImgUrl());
			productDto.setProductEvent(orderObj.getProduct().getProductEvent());
			productDto.setCreatedAt(orderObj.getProduct().getCreatedAt());
			productDto.setProductTimeSale(orderObj.getProduct().getProductTimeSale());
			
			
			StoreDto storeDto = new StoreDto();
			storeDto.setStoreAddress(orderObj.getStore().getStoreAddress());
			storeDto.setStoreId(orderObj.getStore().getStoreId());
			
			dto.setProduct(productDto);
			dto.setStore(storeDto);
			
			orderDtos.add(dto);
		}
		
		return orderDtos;
	}

	@Override
	public int insertOrderIntoStoreProduct(List<Integer> ordernums) {
		// TODO Auto-generated method stub
		return adminDao.insertOrderIntoStoreProduct(ordernums);
	}

}

