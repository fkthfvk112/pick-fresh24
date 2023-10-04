package mart.fresh.com.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import mart.fresh.com.data.dao.ApplyManagerDao;
import mart.fresh.com.data.dto.ApplyManagerDto;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.data.repository.ApplyManagerRepository;
import mart.fresh.com.service.ApplyManagerService;

@Service
public class ApplyManagerServiceImpl implements ApplyManagerService {
    private final ApplyManagerDao applyManagerDao;
    private final ApplyManagerRepository applyManagerRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public ApplyManagerServiceImpl(ApplyManagerDao applyManagerDao, ApplyManagerRepository applyManagerRepository, Cloudinary cloudinary) {
        this.applyManagerDao = applyManagerDao;
        this.applyManagerRepository = applyManagerRepository;
    	this.cloudinary = cloudinary;
    }

    @Override
    public boolean requestApplyManager(String memberId, MultipartFile file) throws IOException {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

            String imgUrl =  uploadResult.get("secure_url").toString();
            return applyManagerDao.requestApplyManager(memberId, imgUrl, currentTimestamp) > 0;
    }
    
    @Override
    public Page<ApplyManagerDto> showApplyList(int page, int size) {
    	
        Page<ApplyManager> applyManagerPage = applyManagerDao.showApplyList(page, size);
        
        Page<ApplyManagerDto> applyManagerDtoPage = applyManagerPage.map(applyManager -> {
            ApplyManagerDto dto = new ApplyManagerDto();
            dto.setMemberId(applyManager.getMember().getMemberId());
            dto.setApplied(applyManager.isApplied());
            dto.setApplyDate(applyManager.getApplyDate());
            dto.setCertifImgUrl(applyManager.getCertifImgUrl());
            
            int applyManagerCount = applyManagerRepository.applyManagerCount();
            dto.setApplyManagerCount(applyManagerCount);
            return dto;
        });

        return applyManagerDtoPage;
    	
    	
    }

	@Override
	public boolean applyManager(String memberId) {
		return applyManagerDao.applyManager(memberId)>0?true:false;
	}
    
}