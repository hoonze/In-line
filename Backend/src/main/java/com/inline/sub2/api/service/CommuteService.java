package com.inline.sub2.api.service;

import com.inline.sub2.api.dto.CommuteDto;
import com.inline.sub2.db.entity.CommuteEntity;
import org.springframework.transaction.annotation.Transactional;

public interface CommuteService {
    @Transactional(rollbackFor = Exception.class)
    CommuteEntity commuteIn(CommuteDto commuteDto);
    CommuteEntity commuteOut(Long commuteId);
}