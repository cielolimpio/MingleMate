package com.minglemate.minglemate.repository;

import com.minglemate.minglemate.models.MemberProfile;
import com.minglemate.minglemate.models.SearchProfilesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MemberRepositoryCustom {
    Page<MemberProfile> searchProfiles(SearchProfilesDto searchProfilesDto, Pageable pageable);
}
