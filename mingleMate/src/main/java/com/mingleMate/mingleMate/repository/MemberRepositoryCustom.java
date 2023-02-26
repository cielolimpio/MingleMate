package com.mingleMate.mingleMate.repository;

import com.mingleMate.mingleMate.models.MemberProfile;
import com.mingleMate.mingleMate.models.payload.request.SearchProfilesRequest;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberProfile> searchProfiles(SearchProfilesRequest request);
}
