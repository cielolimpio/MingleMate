package com.gongyeon.gongyeon.repository;

import com.gongyeon.gongyeon.models.MemberProfile;
import com.gongyeon.gongyeon.models.payload.request.SearchProfilesRequest;

import java.util.List;

public interface MemberRepositoryCustom {
    List<MemberProfile> searchProfiles(SearchProfilesRequest request);
}
