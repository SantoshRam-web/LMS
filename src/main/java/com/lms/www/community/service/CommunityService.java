package com.lms.www.community.service;

import com.lms.www.community.model.*;

import java.util.List;

public interface CommunityService {

List<CommunitySpace> getSpaces();

CommunitySpace updateCommunityTitle(String title);

List<CommunityChannel> getChannels(Long spaceId);

CommunityChannel createChannel(Long spaceId,String name,String description,Boolean adminsOnly);

CommunityChannel updateChannel(Long channelId,String name,String description,Boolean adminsOnly);

CommunityThread createThread(CommunityThread thread);

List<CommunityThread> getThreads(Long channelId);

CommunityReply reply(Long threadId,CommunityReply reply);

void react(Long threadId,Long replyId,String reactionType,Long userId);

void bookmark(Long threadId,Long userId);

}