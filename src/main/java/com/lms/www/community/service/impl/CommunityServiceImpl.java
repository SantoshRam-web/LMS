package com.lms.www.community.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.www.community.model.CommunityBookmark;
import com.lms.www.community.model.CommunityChannel;
import com.lms.www.community.model.CommunityMention;
import com.lms.www.community.model.CommunityNotification;
import com.lms.www.community.model.CommunityReaction;
import com.lms.www.community.model.CommunityReply;
import com.lms.www.community.model.CommunityReport;
import com.lms.www.community.model.CommunitySpace;
import com.lms.www.community.model.CommunityThread;
import com.lms.www.community.repository.CommunityBookmarkRepository;
import com.lms.www.community.repository.CommunityChannelRepository;
import com.lms.www.community.repository.CommunityMentionRepository;
import com.lms.www.community.repository.CommunityNotificationRepository;
import com.lms.www.community.repository.CommunityReactionRepository;
import com.lms.www.community.repository.CommunityReplyRepository;
import com.lms.www.community.repository.CommunityReportRepository;
import com.lms.www.community.repository.CommunitySpaceRepository;
import com.lms.www.community.repository.CommunityThreadRepository;
import com.lms.www.community.service.CommunityService;

@Service
public class CommunityServiceImpl implements CommunityService {

private final CommunitySpaceRepository spaceRepo;
private final CommunityChannelRepository channelRepo;
private final CommunityThreadRepository threadRepo;
private final CommunityReplyRepository replyRepo;
private final CommunityReactionRepository reactionRepo;
private final CommunityBookmarkRepository bookmarkRepo;
private final CommunityReportRepository reportRepo;
private final CommunityNotificationRepository notificationRepo;
private final CommunityMentionRepository mentionRepo;

public CommunityServiceImpl(
CommunitySpaceRepository spaceRepo,
CommunityChannelRepository channelRepo,
CommunityThreadRepository threadRepo,
CommunityReplyRepository replyRepo,
CommunityReactionRepository reactionRepo,
CommunityBookmarkRepository bookmarkRepo,
CommunityReportRepository reportRepo,
CommunityNotificationRepository notificationRepo,
CommunityMentionRepository mentionRepo
){
this.spaceRepo = spaceRepo;
this.channelRepo = channelRepo;
this.threadRepo = threadRepo;
this.replyRepo = replyRepo;
this.reactionRepo = reactionRepo;
this.bookmarkRepo = bookmarkRepo;
this.reportRepo = reportRepo;
this.notificationRepo = notificationRepo;
this.mentionRepo = mentionRepo;
}

//////////////////////////////////////////////////////
// SPACES
//////////////////////////////////////////////////////

@Override
public CommunitySpace createSpace(CommunitySpace space){
return spaceRepo.save(space);
}

@Override
public List<CommunitySpace> getSpaces(){
return spaceRepo.findAll();
}

@Override
public CommunitySpace updateCommunityTitle(Long spaceId,String title){

CommunitySpace space = spaceRepo.findById(spaceId).orElseThrow();

space.setSpaceName(title);

return spaceRepo.save(space);
}

@Override
public List<CommunitySpace> searchSpaces(String search){
	return spaceRepo.findBySpaceNameContainingIgnoreCase(search);
}

//////////////////////////////////////////////////////
// CHANNELS
//////////////////////////////////////////////////////

@Override
public List<CommunityChannel> getChannels(Long spaceId){
return channelRepo.findBySpaceId(spaceId);
}

@Override
public CommunityChannel createChannel(Long spaceId,String name,String desc,Boolean adminsOnly){

	CommunityChannel channel = CommunityChannel.builder()
			.spaceId(spaceId)
			.channelName(name)
			.channelType("DISCUSSION")
			.description(desc)
			.adminsOnly(adminsOnly)
			.createdAt(java.time.LocalDateTime.now())
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

//////////////////////////////////////////////////////
// THREADS
//////////////////////////////////////////////////////

@Override
public CommunityThread createThread(CommunityThread thread){

    thread.setStatus("OPEN");
    thread.setIsPinned(false);
    thread.setCreatedAt(java.time.LocalDateTime.now());

    return threadRepo.save(thread);
}
@Override
public List<CommunityThread> getThreads(Long channelId){
return threadRepo.findByChannelId(channelId);
}

@Override
public CommunityThread getThread(Long threadId){
return threadRepo.findById(threadId).orElseThrow();
}

//////////////////////////////////////////////////////
// REPLIES
//////////////////////////////////////////////////////

@Override
public CommunityReply reply(Long threadId,CommunityReply reply){

    reply.setThreadId(threadId);
    reply.setCreatedAt(java.time.LocalDateTime.now());

    if(reply.getIsAnswer()==null)
        reply.setIsAnswer(false);

    if(reply.getIsVerified()==null)
        reply.setIsVerified(false);

    return replyRepo.save(reply);

}
//////////////////////////////////////////////////////
// REACTIONS
//////////////////////////////////////////////////////

@Override
public void react(Long threadId,Long replyId,String reactionType,Long userId){

CommunityReaction reaction = CommunityReaction.builder()
.threadId(threadId)
.replyId(replyId)
.userId(userId)
.reactionType(reactionType)
.build();

reaction.setCreatedAt(java.time.LocalDateTime.now());
reactionRepo.save(reaction);
}

//////////////////////////////////////////////////////
// BOOKMARKS
//////////////////////////////////////////////////////

@Override
public void bookmark(Long threadId,Long userId){

CommunityBookmark bookmark = CommunityBookmark.builder()
.threadId(threadId)
.userId(userId)
.build();

bookmark.setCreatedAt(java.time.LocalDateTime.now());
bookmarkRepo.save(bookmark);
}

@Override
public List<CommunityBookmark> getBookmarks(Long userId){
return bookmarkRepo.findByUserId(userId);
}

//////////////////////////////////////////////////////
// REPORTS
//////////////////////////////////////////////////////

@Override
public CommunityReport report(CommunityReport report){
return reportRepo.save(report);
}

//////////////////////////////////////////////////////
// NOTIFICATIONS
//////////////////////////////////////////////////////

@Override
public List<CommunityNotification> getNotifications(Long userId){
return notificationRepo.findByUserId(userId);
}

@Override
public void mentionUser(Long threadId, Long replyId, Long mentionedUserId){

    CommunityMention mention = CommunityMention.builder()
            .threadId(threadId)
            .replyId(replyId)
            .mentionedUserId(mentionedUserId)
            .createdAt(LocalDateTime.now())
            .build();

    mentionRepo.save(mention);

    CommunityNotification notification = CommunityNotification.builder()
            .userId(mentionedUserId)
            .threadId(threadId)
            .replyId(replyId)
            .type("MENTION")
            .isRead(false)
            .createdAt(LocalDateTime.now())
            .build();

    notificationRepo.save(notification);
}

@Override
public List<CommunityMention> getMentions(Long userId){
	return mentionRepo.findByMentionedUserId(userId);
}
}