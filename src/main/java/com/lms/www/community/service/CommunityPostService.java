package com.lms.www.community.service;

import com.lms.www.community.model.CommunityPost;
import java.util.List;

public interface CommunityPostService {

    CommunityPost createPost(Long channelId, CommunityPost post);

    List<CommunityPost> getPostsByChannel(Long channelId);

}