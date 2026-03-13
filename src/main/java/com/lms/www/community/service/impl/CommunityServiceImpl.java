package com.lms.www.community.service.impl;

import com.lms.www.community.model.*;
import com.lms.www.community.repository.*;
import com.lms.www.community.service.CommunityService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl implements CommunityService {

private final CommunitySpaceRepository spaceRepo;
private final CommunityChannelRepository channelRepo;
private final CommunityThreadRepository threadRepo;
private final CommunityReplyRepository replyRepo;
private final CommunityReactionRepository reactionRepo;
private final CommunityBookmarkRepository bookmarkRepo;

public CommunityServiceImpl(
CommunitySpaceRepository spaceRepo,
CommunityChannelRepository channelRepo,
CommunityThreadRepository threadRepo,
CommunityReplyRepository replyRepo,
CommunityReactionRepository reactionRepo,
CommunityBookmarkRepository bookmarkRepo
){
this.spaceRepo = spaceRepo;
this.channelRepo = channelRepo;
this.threadRepo = threadRepo;
this.replyRepo = replyRepo;
this.reactionRepo = reactionRepo;
this.bookmarkRepo = bookmarkRepo;
}

@Override
public List<CommunitySpace> getSpaces(){
return spaceRepo.findAll();
}

@Override
public CommunitySpace updateCommunityTitle(String title){

CommunitySpace space = spaceRepo.findAll().get(0);
space.setSpaceName(title);
return spaceRepo.save(space);

}

@Override
public List<CommunityChannel> getChannels(Long spaceId){
return channelRepo.findBySpaceId(spaceId);
}

@Override
public CommunityChannel createChannel(Long spaceId,String name,String desc,Boolean adminsOnly){

CommunityChannel channel = CommunityChannel.builder()
.spaceId(spaceId)
.channelName(name)	
.description(desc)
.adminsOnly(adminsOnly)
.build();

return channelRepo.save(channel);

}

@Override
public CommunityChannel updateChannel(Long channelId,String name,String desc,Boolean adminsOnly){

CommunityChannel channel = channelRepo.findById(channelId).orElseThrow();

channel.setChannelName(name);
channel.setDescription(desc);
channel.setAdminsOnly(adminsOnly);

return channelRepo.save(channel);

}

@Override
public CommunityThread createThread(CommunityThread thread){
return threadRepo.save(thread);
}

@Override
public List<CommunityThread> getThreads(Long channelId){
return threadRepo.findByChannelId(channelId);
}

@Override
public CommunityReply reply(Long threadId,CommunityReply reply){

reply.setThreadId(threadId);
return replyRepo.save(reply);

}

@Override
public void react(Long threadId,Long replyId,String reactionType,Long userId){

CommunityReaction reaction = CommunityReaction.builder()
.threadId(threadId)
.replyId(replyId)
.userId(userId)
.reactionType(reactionType)
.build();

reactionRepo.save(reaction);

}

@Override
public void bookmark(Long threadId,Long userId){

CommunityBookmark bookmark = CommunityBookmark.builder()
.threadId(threadId)
.userId(userId)
.build();

bookmarkRepo.save(bookmark);

}

}