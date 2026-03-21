package com.lms.www.community.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.lms.www.community.model.CommunityBookmark;
import com.lms.www.community.model.CommunityChannel;
import com.lms.www.community.model.CommunityChannelMember;
import com.lms.www.community.model.CommunityMention;
import com.lms.www.community.model.CommunityNotification;
import com.lms.www.community.model.CommunityReaction;
import com.lms.www.community.model.CommunityReply;
import com.lms.www.community.model.CommunityReport;
import com.lms.www.community.model.CommunitySpace;
import com.lms.www.community.model.CommunityThread;
import com.lms.www.community.repository.CommunityBookmarkRepository;
import com.lms.www.community.repository.CommunityChannelMemberRepository;
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
private final CommunityChannelMemberRepository memberRepo;

public CommunityServiceImpl(
CommunitySpaceRepository spaceRepo,
CommunityChannelRepository channelRepo,
CommunityThreadRepository threadRepo,
CommunityReplyRepository replyRepo,
CommunityReactionRepository reactionRepo,
CommunityBookmarkRepository bookmarkRepo,
CommunityReportRepository reportRepo,
CommunityNotificationRepository notificationRepo,
CommunityMentionRepository mentionRepo,
CommunityChannelMemberRepository memberRepo
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
this.memberRepo = memberRepo;
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

@Override
public void autoJoinGlobalCommunity(Long userId){

    // 1️⃣ STRICT SPACE FETCH
    CommunitySpace globalSpace = spaceRepo
            .findBySpaceName("Global Community")
            .orElseGet(() -> {
                CommunitySpace newSpace = CommunitySpace.builder()
                        .spaceName("Global Community")
                        .createdAt(LocalDateTime.now())
                        .build();
                return spaceRepo.save(newSpace);
            });

    // 2️⃣ STRICT CHANNEL FETCH
    CommunityChannel globalChannel = channelRepo
            .findBySpaceIdAndChannelName(
                    globalSpace.getSpaceId(),
                    "general"
            )
            .orElseGet(() -> {
                CommunityChannel newChannel = CommunityChannel.builder()
                        .spaceId(globalSpace.getSpaceId())
                        .channelName("general")
                        .channelType("DISCUSSION")
                        .description("Global community discussions")
                        .adminsOnly(false)
                        .createdAt(LocalDateTime.now())
                        .build();
                return channelRepo.save(newChannel);
            });

    // 3️⃣ STRICT MEMBERSHIP CHECK
    boolean exists = memberRepo.existsByChannelIdAndUserId(
            globalChannel.getChannelId(),
            userId
    );

    if(!exists){
        CommunityChannelMember member =
                CommunityChannelMember.builder()
                        .channelId(globalChannel.getChannelId())
                        .userId(userId)
                        .joinedAt(LocalDateTime.now())
                        .build();

        memberRepo.save(member);
    }
}

@Override
public void autoJoinMarketingChannel(Long userId){

    // 1️⃣ STRICT SPACE FETCH
    CommunitySpace marketingSpace = spaceRepo
            .findBySpaceName("Marketing Community")
            .orElseGet(() -> {
                CommunitySpace newSpace = CommunitySpace.builder()
                        .spaceName("Marketing Community")
                        .createdAt(LocalDateTime.now())
                        .build();
                return spaceRepo.save(newSpace);
            });

    // 2️⃣ STRICT CHANNEL FETCH
    CommunityChannel marketingChannel = channelRepo
            .findBySpaceIdAndChannelName(
                    marketingSpace.getSpaceId(),
                    "marketing-updates"
            )
            .orElseGet(() -> {
                CommunityChannel newChannel = CommunityChannel.builder()
                        .spaceId(marketingSpace.getSpaceId())
                        .channelName("marketing-updates")
                        .channelType("DISCUSSION")
                        .description("Course discounts and announcements")
                        .adminsOnly(false)
                        .createdAt(LocalDateTime.now())
                        .build();
                return channelRepo.save(newChannel);
            });

    // 3️⃣ MEMBERSHIP CHECK
    boolean exists = memberRepo.existsByChannelIdAndUserId(
            marketingChannel.getChannelId(),
            userId
    );

    if(!exists){
        CommunityChannelMember member =
                CommunityChannelMember.builder()
                        .channelId(marketingChannel.getChannelId())
                        .userId(userId)
                        .joinedAt(LocalDateTime.now())
                        .build();

        memberRepo.save(member);
    }
}

@Override
public void addStudentToCourseCommunity(Long courseId, Long userId) {
    addMemberToCourseCommunity(courseId, userId);
}

@Override
public void addInstructorToCourseCommunity(Long courseId, Long userId) {
    addMemberToCourseCommunity(courseId, userId);
}

private void addMemberToCourseCommunity(Long courseId, Long userId) {

    CommunitySpace space = spaceRepo.findByCourseId(courseId)
            .orElseThrow(() -> new RuntimeException("Course community not found"));

    CommunityChannel channel = channelRepo
            .findBySpaceIdAndChannelName(space.getSpaceId(), "general")
            .orElseThrow(() -> new RuntimeException("Channel not found"));

    boolean exists = memberRepo.existsByChannelIdAndUserId(
            channel.getChannelId(),
            userId
    );

    if (!exists) {
        memberRepo.save(
                CommunityChannelMember.builder()
                        .channelId(channel.getChannelId())
                        .userId(userId)
                        .joinedAt(LocalDateTime.now())
                        .build()
        );
    }
}

@Override
public void createCourseCommunity(Long courseId, String courseName){

    // 1️⃣ Check if already exists (NO DUPLICATE)
    CommunitySpace space = spaceRepo.findByCourseId(courseId)
            .orElseGet(() -> {

                CommunitySpace newSpace = CommunitySpace.builder()
                        .spaceName(courseName + " Community")
                        .courseId(courseId)
                        .createdAt(LocalDateTime.now())
                        .build();

                return spaceRepo.save(newSpace);
            });

    // 2️⃣ Create default channel
    channelRepo.findBySpaceIdAndChannelName(space.getSpaceId(), "general")
            .orElseGet(() -> {

                CommunityChannel channel = CommunityChannel.builder()
                        .spaceId(space.getSpaceId())
                        .channelName("general")
                        .channelType("DISCUSSION")
                        .description("Course discussions")
                        .adminsOnly(false)
                        .createdAt(LocalDateTime.now())
                        .build();

                return channelRepo.save(channel);
            });
}



@Override
public void autoJoinRoleCommunity(Long userId, String roleName){

    // ❌ Skip for LEAD (already handled separately)
    if ("ROLE_LEAD".equals(roleName)) {
        return;
    }

    // Convert role → clean name
    String role = roleName.replace("ROLE_", ""); // STUDENT

    String spaceName = role + " Community";       // STUDENT Community
    String channelName = "general";

    // 1️⃣ Find/Create Space
    CommunitySpace space = spaceRepo
            .findBySpaceName(spaceName)
            .orElseGet(() -> spaceRepo.save(
                    CommunitySpace.builder()
                            .spaceName(spaceName)
                            .createdAt(LocalDateTime.now())
                            .build()
            ));

    // 2️⃣ Find/Create Channel
    CommunityChannel channel = channelRepo
            .findBySpaceIdAndChannelName(space.getSpaceId(), channelName)
            .orElseGet(() -> channelRepo.save(
                    CommunityChannel.builder()
                            .spaceId(space.getSpaceId())
                            .channelName(channelName)
                            .channelType("DISCUSSION")
                            .description(role + " discussions")
                            .adminsOnly(false)
                            .createdAt(LocalDateTime.now())
                            .build()
            ));

    // 3️⃣ Add Member (safe)
    boolean exists = memberRepo.existsByChannelIdAndUserId(
            channel.getChannelId(),
            userId
    );

    if (!exists) {
        memberRepo.save(
                CommunityChannelMember.builder()
                        .channelId(channel.getChannelId())
                        .userId(userId)
                        .joinedAt(LocalDateTime.now())
                        .build()
        );
    }
}

@Override
public Map<String, Object> getMyCommunities(Long userId) {

    List<CommunityChannelMember> memberships = memberRepo.findByUserId(userId);

    Map<String, Object> response = new HashMap<>();
    response.put("userId", userId);

    if (memberships.isEmpty()) {
        response.put("spaces", new ArrayList<>());
        return response;
    }

    List<Long> channelIds = memberships.stream()
            .map(CommunityChannelMember::getChannelId)
            .toList();

    List<CommunityChannel> channels = channelRepo.findByChannelIdIn(channelIds);

    if (channels.isEmpty()) {
        response.put("spaces", new ArrayList<>());
        return response;
    }

    List<Long> spaceIds = channels.stream()
            .map(CommunityChannel::getSpaceId)
            .distinct()
            .toList();

    List<CommunitySpace> spaces = spaceRepo.findBySpaceIdIn(spaceIds);

    Map<Long, CommunitySpace> spaceMap = new HashMap<>();
    for (CommunitySpace space : spaces) {
        spaceMap.put(space.getSpaceId(), space);
    }

    Map<Long, Map<String, Object>> groupedSpaces = new LinkedHashMap<>();

    for (CommunityChannel channel : channels) {
        CommunitySpace space = spaceMap.get(channel.getSpaceId());
        if (space == null) {
            continue;
        }

        Map<String, Object> spaceObj = groupedSpaces.get(space.getSpaceId());
        if (spaceObj == null) {
            spaceObj = new LinkedHashMap<>();
            spaceObj.put("spaceId", space.getSpaceId());
            spaceObj.put("spaceName", space.getSpaceName());
            spaceObj.put("channels", new ArrayList<Map<String, Object>>());
            groupedSpaces.put(space.getSpaceId(), spaceObj);
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> channelList =
                (List<Map<String, Object>>) spaceObj.get("channels");

        Map<String, Object> channelObj = new LinkedHashMap<>();
        channelObj.put("channelId", channel.getChannelId());
        channelObj.put("channelName", channel.getChannelName());
        channelObj.put("channelType", channel.getChannelType());
        channelObj.put("description", channel.getDescription());

        channelList.add(channelObj);
    }

    response.put("spaces", new ArrayList<>(groupedSpaces.values()));
    return response;
}

@Override
public Map<String, Object> getMarketingCommunity() {

    CommunitySpace marketingSpace = spaceRepo
            .findBySpaceName("Marketing Community")
            .orElseGet(() -> {
                CommunitySpace newSpace = CommunitySpace.builder()
                        .spaceName("Marketing Community")
                        .createdAt(LocalDateTime.now())
                        .build();
                return spaceRepo.save(newSpace);
            });

    List<CommunityChannel> channels = channelRepo.findBySpaceId(marketingSpace.getSpaceId());

    // Safety: create default marketing channel if missing
    if (channels == null || channels.isEmpty()) {
        CommunityChannel marketingChannel = CommunityChannel.builder()
                .spaceId(marketingSpace.getSpaceId())
                .channelName("marketing-updates")
                .channelType("DISCUSSION")
                .description("Course discounts and announcements")
                .adminsOnly(false)
                .createdAt(LocalDateTime.now())
                .build();

        channelRepo.save(marketingChannel);
        channels = channelRepo.findBySpaceId(marketingSpace.getSpaceId());
    }

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("spaceId", marketingSpace.getSpaceId());
    response.put("spaceName", marketingSpace.getSpaceName());

    List<Map<String, Object>> channelList = new ArrayList<>();

    for (CommunityChannel channel : channels) {
        // Only public-safe channels
        if (Boolean.TRUE.equals(channel.getAdminsOnly())) {
            continue;
        }

        Map<String, Object> channelObj = new LinkedHashMap<>();
        channelObj.put("channelId", channel.getChannelId());
        channelObj.put("channelName", channel.getChannelName());
        channelObj.put("channelType", channel.getChannelType());
        channelObj.put("description", channel.getDescription());

        channelList.add(channelObj);
    }

    response.put("channels", channelList);
    return response;
}
}