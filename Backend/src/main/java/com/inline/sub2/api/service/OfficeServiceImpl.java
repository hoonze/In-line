package com.inline.sub2.api.service;

import com.inline.sub2.db.entity.OfficeEntity;
import com.inline.sub2.db.repository.OfficeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OfficeServiceImpl implements OfficeService{
    @Autowired
    OfficeRepository officeRepository;
    @Override

    public OfficeEntity registOffice(String officeName) {
        OfficeEntity officeEntity = new OfficeEntity();
        officeEntity.setOfficeName(officeName);
        return officeRepository.save(officeEntity);
    }

    public Boolean duplicateOfficeName(String officeName){
        OfficeEntity officeEntity = officeRepository.findByOfficeName(officeName);
        boolean isDuplicate = false;
        if(officeEntity != null)
            isDuplicate = true;

        return isDuplicate;
    }
}
