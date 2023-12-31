package mart.fresh.com.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mart.fresh.com.data.dao.MypageDao;
import mart.fresh.com.data.dao.OrderedProductDao;
import mart.fresh.com.data.dao.StoreDao;
import mart.fresh.com.data.dto.MypageDto;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.StoreSalesAmountDto;
import mart.fresh.com.data.dto.StoreSalesProductTitleDto;
import mart.fresh.com.data.dto.StoreSalesProductTypeDto;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.MypageRepository;
import mart.fresh.com.service.MypageService;

@Service
public class MypageServiceImpl implements MypageService {

	private final MypageDao mypageDao;
	private final MypageRepository mypageRepository;
	private final OrderedProductDao orderedProductDao;
	private final StoreDao storeDao;

	@Autowired
	public MypageServiceImpl(MypageDao mypageDao, MypageRepository mypageRepository,
			OrderedProductDao orderedProductDao, StoreDao storeDao) {
		this.mypageDao = mypageDao;
		this.mypageRepository = mypageRepository;
		this.orderedProductDao = orderedProductDao;
		this.storeDao = storeDao;
	}

	@Override
	public MypageDto getMemberAndIsAppliedByMemberId(String memberId) {
		System.out.println("MypageServiceImpl getMemberAndIsAppliedByMemberId");

		Member entity = mypageDao.getMemberAndIsAppliedByMemberId(memberId);

		ApplyManager applyManagerEntity = mypageRepository.getMemberAndIsAppliedByMemberId(memberId);

		MypageDto resultMemberInfo = new MypageDto();
		resultMemberInfo.setMemberId(entity.getMemberId());
		resultMemberInfo.setMemberName(entity.getMemberName());
		resultMemberInfo.setMemberEmail(entity.getMemberEmail());
		resultMemberInfo.setMemberAuth(entity.getMemberAuth());
		resultMemberInfo.setMemberDel(entity.isMemberDel());
		if (applyManagerEntity != null) {
			resultMemberInfo.setApplied(applyManagerEntity.isApplied());
		} else {
			resultMemberInfo.setApplied(true);
		}

		return resultMemberInfo;
	}

	@Override
	public boolean changePassword(String memberId, String memberPw, String newPw) {
		System.out.println("MypageServiceImpl changePassword");

		return mypageDao.changePassword(memberId, memberPw, newPw) > 0 ? true : false;
	}

	@Override
	public boolean checkEmail(String memberEmail) {
		System.out.println("MypageServiceImpl MypageServiceImpl checkEmail : " + mypageDao.checkEmail(memberEmail));
		return mypageDao.checkEmail(memberEmail) > 0 ? true : false;
	}

	@Override
	public boolean saveVerificationCode(String memberId, String verificationCode, LocalDateTime expiryTime) {
		return mypageDao.saveVerificationCode(memberId, verificationCode, expiryTime) > 0 ? true : false;

	}

	@Override
	public int changeEmail(String memberId, String newEmail, String verificationCode) {
		return mypageDao.changeEmail(memberId, newEmail, verificationCode);
	}

	@Override
	public List<StoreSalesAmountDto> salesChart(String memberId, Timestamp searchDate, String period) {
		System.out.println("MypageServiceImpl MypageServiceImpl salesChart : ");

		Store store = storeDao.findByMemberMemberId(memberId);
		List<OrderedProduct> salesEntityList = orderedProductDao
				.findByStoreStoreIdAndOrderedDateBetween(store.getStoreId(), searchDate, period);

		System.out.println("MypageServiceImpl MypageServiceImpl salesChart : " + salesEntityList.toString());

		switch (period) {
		case "weekly":
			return processWeeklyData(salesEntityList, searchDate);
		case "monthly":
			return processMonthlyData(salesEntityList, searchDate);
		case "yearly":
			return processYearlyData(salesEntityList, searchDate);
		default:
			throw new IllegalArgumentException("Invalid period value");
		}
	}

	private List<StoreSalesAmountDto> processWeeklyData(List<OrderedProduct> salesEntityList, Timestamp searchDate) {
		LocalDate endDate = searchDate.toLocalDateTime().toLocalDate();
		LocalDate startDate = endDate.minusDays(6);

		Map<LocalDate, Integer> salesMap = new LinkedHashMap<>();
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			salesMap.put(date, 0);
		}

		for (OrderedProduct salesData : salesEntityList) {
			LocalDate localDate = salesData.getOrderedDate().toLocalDateTime().toLocalDate();
			if (localDate.isAfter(startDate.minusDays(1)) && localDate.isBefore(endDate.plusDays(1))) {
				salesMap.put(localDate, salesMap.get(localDate) + salesData.getTotalAmount());
			}
		}

		List<StoreSalesAmountDto> salesList = new ArrayList<>();
		for (Map.Entry<LocalDate, Integer> entry : salesMap.entrySet()) {
			StoreSalesAmountDto dto = new StoreSalesAmountDto();
			LocalDate localDate = entry.getKey();

			dto.setOrderedDate(localDate);
			dto.setTotalAmount(entry.getValue());
			salesList.add(dto);
		}

		return salesList;
	}

	private List<StoreSalesAmountDto> processMonthlyData(List<OrderedProduct> salesEntityList, Timestamp searchDate) {
	    LocalDate date = searchDate.toLocalDateTime().toLocalDate();
	    Month currentMonth = date.getMonth();
	    int currentYear = date.getYear();

	    LocalDate firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1);
	    LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

	    LocalDate currentWeekStart = firstDayOfMonth;
	    LocalDate currentWeekEnd = currentWeekStart.plusDays(6);

	    Map<Integer, Integer> salesMap = new LinkedHashMap<>();
	    int weekNumber = 1;

	    while (currentWeekStart.isBefore(lastDayOfMonth) || currentWeekStart.isEqual(lastDayOfMonth)) {
	    	
	        if (currentWeekEnd.isAfter(lastDayOfMonth)) {
	            currentWeekEnd = lastDayOfMonth;
	        }

	        salesMap.put(weekNumber, 0); 

	        for (OrderedProduct salesData : salesEntityList) {
	            LocalDate localDate = salesData.getOrderedDate().toLocalDateTime().toLocalDate();

	            if (localDate.isAfter(currentWeekStart.minusDays(1)) && localDate.isBefore(currentWeekEnd.plusDays(1))) {
	                salesMap.put(weekNumber, salesMap.get(weekNumber) + salesData.getTotalAmount());
	            }
	        }

	        currentWeekStart = currentWeekEnd.plusDays(1);
	        currentWeekEnd = currentWeekStart.plusDays(6);
	        weekNumber++;
	    }

	    List<StoreSalesAmountDto> salesList = new ArrayList<>();
	    for (Map.Entry<Integer, Integer> entry : salesMap.entrySet()) {
	        StoreSalesAmountDto dto = new StoreSalesAmountDto();
	        dto.setQuarter(entry.getKey());
	        dto.setTotalAmount(entry.getValue());
	        salesList.add(dto);
	    }

	    return salesList;
	}

	
	private List<StoreSalesAmountDto> processYearlyData(List<OrderedProduct> salesEntityList, Timestamp searchDate) {
		int year = searchDate.toLocalDateTime().getYear();

		Map<Integer, Integer> quarterlySalesMap = new HashMap<>();
		for (int i = 1; i <= 4; i++) {
			quarterlySalesMap.put(i, 0);
		}

		for (OrderedProduct salesData : salesEntityList) {
			LocalDate localDate = salesData.getOrderedDate().toLocalDateTime().toLocalDate();
			if (localDate.getYear() == year) {
				int quarter = (localDate.getMonthValue() - 1) / 3 + 1;
				quarterlySalesMap.put(quarter, quarterlySalesMap.get(quarter) + salesData.getTotalAmount());
			}
		}

		List<StoreSalesAmountDto> salesList = new ArrayList<>();
		for (Map.Entry<Integer, Integer> entry : quarterlySalesMap.entrySet()) {
			StoreSalesAmountDto dto = new StoreSalesAmountDto();
			dto.setQuarter(entry.getKey());
			dto.setTotalAmount(entry.getValue());
			salesList.add(dto);
		}

		return salesList;
	}

	@Override
	public List<StoreSalesProductTypeDto> productTypeChart(String memberId, Timestamp searchDate, String period) {
		System.out.println("MypageServiceImpl productTypeChart : ");

		Store store = storeDao.findByMemberMemberId(memberId);
		List<OrderedProductProduct> salesEntityList = orderedProductDao
				.findByOrderedProductStoreStoreIdAndOrderedProductOrderedDateBetweenOrderByOrderedDateAsc(
						store.getStoreId(), searchDate, period);

		System.out.println("MypageServiceImpl productTypeChart : " + salesEntityList.toString());

		switch (period) {
		case "weekly":
			return processWeeklyProductData(salesEntityList);
		case "monthly":
			return processMonthlyProductData(salesEntityList);
		case "yearly":
			return processYearlyProductData(salesEntityList);
		default:
			throw new IllegalArgumentException("Invalid period value");
		}
	}

	private List<StoreSalesProductTypeDto> processWeeklyProductData(List<OrderedProductProduct> salesEntityList) {
		Map<Integer, StoreSalesProductTypeDto> salesMap = new HashMap<>();
		for (OrderedProductProduct salesData : salesEntityList) {
			int productType = salesData.getProduct().getProductType();

			salesMap.putIfAbsent(productType, new StoreSalesProductTypeDto());
			StoreSalesProductTypeDto existingDto = salesMap.get(productType);

			existingDto.setOrderedQuantity(existingDto.getOrderedQuantity() + salesData.getOrderedQuantity());
			existingDto.setProductType(productType);
		}
		return new ArrayList<>(salesMap.values());
	}

	private List<StoreSalesProductTypeDto> processMonthlyProductData(List<OrderedProductProduct> salesEntityList) {
		Map<Integer, StoreSalesProductTypeDto> salesMap = new HashMap<>();
		for (OrderedProductProduct salesData : salesEntityList) {
			int productType = salesData.getProduct().getProductType();

			salesMap.putIfAbsent(productType, new StoreSalesProductTypeDto());
			StoreSalesProductTypeDto existingDto = salesMap.get(productType);

			existingDto.setOrderedQuantity(existingDto.getOrderedQuantity() + salesData.getOrderedQuantity());
			existingDto.setProductType(productType);
		}
		return new ArrayList<>(salesMap.values());
	}

	private List<StoreSalesProductTypeDto> processYearlyProductData(List<OrderedProductProduct> salesEntityList) {
		Map<Integer, StoreSalesProductTypeDto> salesMap = new HashMap<>();
		for (OrderedProductProduct salesData : salesEntityList) {
			int productType = salesData.getProduct().getProductType();

			salesMap.putIfAbsent(productType, new StoreSalesProductTypeDto());
			StoreSalesProductTypeDto existingDto = salesMap.get(productType);

			existingDto.setOrderedQuantity(existingDto.getOrderedQuantity() + salesData.getOrderedQuantity());
			existingDto.setProductType(productType);
		}
		return new ArrayList<>(salesMap.values());
	}

	@Override
	public List<StoreSalesProductTitleDto> productTitleChart(String memberId, Timestamp searchDate, String period) {
		System.out.println("MypageServiceImpl productTitleChart : ");

		Store store = storeDao.findByMemberMemberId(memberId);
		List<OrderedProductProduct> salesTitleEntityList = orderedProductDao
				.findByOrderedProductStoreStoreIdAndOrderedProductOrderedDateBetweenOrderByOrderedDateAsc(
						store.getStoreId(), searchDate, period);

		System.out.println("MypageServiceImpl productTitleChart : " + salesTitleEntityList.toString());

		switch (period) {
		case "weekly":
			return processWeeklyProductTitleData(salesTitleEntityList);
		case "monthly":
			return processMonthlyProductTitleData(salesTitleEntityList);
		case "yearly":
			return processYearlyProductTitleData(salesTitleEntityList);
		default:
			throw new IllegalArgumentException("Invalid period value");
		}
	}

	private List<StoreSalesProductTitleDto> processWeeklyProductTitleData(
			List<OrderedProductProduct> salesTitleEntityList) {
		Map<String, StoreSalesProductTitleDto> salesMap = new HashMap<>();
		for (OrderedProductProduct salesData : salesTitleEntityList) {
			String productTitle = salesData.getProduct().getProductTitle();

			salesMap.putIfAbsent(productTitle, new StoreSalesProductTitleDto());
			StoreSalesProductTitleDto existingDto = salesMap.get(productTitle);

			existingDto.setOrderedQuantity(existingDto.getOrderedQuantity() + salesData.getOrderedQuantity());
			existingDto.setProductTitle(productTitle);
		}

		List<StoreSalesProductTitleDto> resultList = new ArrayList<>(salesMap.values());
		resultList.sort((dto1, dto2) -> -Integer.compare(dto1.getOrderedQuantity(), dto2.getOrderedQuantity())); // 내림차순
																													// 정렬

		return resultList.subList(0, Math.min(7, resultList.size())); // 상위 7개 반환

	}

	private List<StoreSalesProductTitleDto> processMonthlyProductTitleData(
			List<OrderedProductProduct> salesEntityList) {
		Map<String, StoreSalesProductTitleDto> salesMap = new HashMap<>();
		for (OrderedProductProduct salesData : salesEntityList) {
			String productTitle = salesData.getProduct().getProductTitle();

			salesMap.putIfAbsent(productTitle, new StoreSalesProductTitleDto());
			StoreSalesProductTitleDto existingDto = salesMap.get(productTitle);

			existingDto.setOrderedQuantity(existingDto.getOrderedQuantity() + salesData.getOrderedQuantity());
			existingDto.setProductTitle(productTitle);
		}

		List<StoreSalesProductTitleDto> resultList = new ArrayList<>(salesMap.values());
		resultList.sort((dto1, dto2) -> -Integer.compare(dto1.getOrderedQuantity(), dto2.getOrderedQuantity())); // 내림차순
																													// 정렬

		return resultList.subList(0, Math.min(7, resultList.size())); // 상위 7개 반환

	}

	private List<StoreSalesProductTitleDto> processYearlyProductTitleData(List<OrderedProductProduct> salesEntityList) {
		Map<String, StoreSalesProductTitleDto> salesMap = new HashMap<>();
		for (OrderedProductProduct salesData : salesEntityList) {
			String productTitle = salesData.getProduct().getProductTitle();

			salesMap.putIfAbsent(productTitle, new StoreSalesProductTitleDto());
			StoreSalesProductTitleDto existingDto = salesMap.get(productTitle);

			existingDto.setOrderedQuantity(existingDto.getOrderedQuantity() + salesData.getOrderedQuantity());
			existingDto.setProductTitle(productTitle);
		}

		List<StoreSalesProductTitleDto> resultList = new ArrayList<>(salesMap.values());
		resultList.sort((dto1, dto2) -> -Integer.compare(dto1.getOrderedQuantity(), dto2.getOrderedQuantity())); // 내림차순
																													// 정렬

		return resultList.subList(0, Math.min(7, resultList.size())); // 상위 7개 반환

	}

	@Override
	public boolean productRegistration(ProductDto dto) {
		return mypageDao.productRegistration(dto);
	}
}