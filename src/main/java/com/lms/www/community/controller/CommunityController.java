package com.lms.www.community.controller;

import com.lms.www.community.model.*;
import com.lms.www.community.service.CommunityService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

private final CommunityService communityService;

public CommunityController(CommunityService communityService){
this.communityService = communityService;
}

//////////////////////////////////////////////////////
// COMMUNITY SPACES
//////////////////////////////////////////////////////

@PostMapping("/spaces")
public CommunitySpace createSpace(@RequestBody CommunitySpace space){
return communityService.createSpace(space);
}

@GetMapping("/spaces")
public List<CommunitySpace> getSpaces(){
return communityService.getSpaces();
}

@PutMapping("/spaces/{spaceId}")
public CommunitySpace updateCommunityTitle(
@PathVariable Long spaceId,
@RequestBody CommunitySpace space
){
return communityService.updateCommunityTitle(spaceId,space.getSpaceName());
}

@GetMapping("/spaces/search")
public List<CommunitySpace> searchSpaces(@RequestParam String search){
    return communityService.searchSpaces(search);
}

//////////////////////////////////////////////////////
// CHANNELS
//////////////////////////////////////////////////////

@GetMapping("/spaces/{spaceId}/channels")
public List<CommunityChannel> getChannels(@PathVariable Long spaceId){
return communityService.getChannels(spaceId);
}

@PostMapping("/channels")
public CommunityChannel createChannel(@RequestBody CommunityChannel channel){
return communityService.createChannel(
channel.getSpaceId(),
channel.getChannelName(),
channel.getDescription(),
channel.getAdminsOnly()
);
}

@PutMapping("/channels/{channelId}")
public CommunityChannel updateChannel(
@PathVariable Long channelId,
@RequestBody CommunityChannel channel
){
return communityService.updateChannel(
channelId,
channel.getChannelName(),
channel.getDescription(),
channel.getAdminsOnly()
);
}

//////////////////////////////////////////////////////
// THREADS
//////////////////////////////////////////////////////

@PostMapping("/threads")
public CommunityThread createThread(@RequestBody CommunityThread thread){
return communityService.createThread(thread);
}

@GetMapping("/threads/{channelId}")
public List<CommunityThread> getThreads(@PathVariable Long channelId){
return communityService.getThreads(channelId);
}

@GetMapping("/thread/{threadId}")
public CommunityThread getThread(@PathVariable Long threadId){
return communityService.getThread(threadId);
}

//////////////////////////////////////////////////////
// REPLIES
//////////////////////////////////////////////////////

@PostMapping("/threads/{threadId}/reply")
public CommunityReply reply(
@PathVariable Long threadId,
@RequestBody CommunityReply reply
){
return communityService.reply(threadId,reply);
}

//////////////////////////////////////////////////////
// REACTIONS
//////////////////////////////////////////////////////

@PostMapping("/react")
public void react(@RequestBody CommunityReaction reaction){
communityService.react(
reaction.getThreadId(),
reaction.getReplyId(),
reaction.getReactionType(),
reaction.getUserId()
);
}

//////////////////////////////////////////////////////
// BOOKMARKS
//////////////////////////////////////////////////////

@PostMapping("/bookmark")
public void bookmark(@RequestBody CommunityBookmark bookmark){
communityService.bookmark(
bookmark.getThreadId(),
bookmark.getUserId()
);
}

@GetMapping("/bookmarks/{userId}")
public List<CommunityBookmark> getBookmarks(@PathVariable Long userId){
return communityService.getBookmarks(userId);
}

//////////////////////////////////////////////////////
// REPORTS
//////////////////////////////////////////////////////

@PostMapping("/report")
public CommunityReport report(@RequestBody CommunityReport report){
return communityService.report(report);
}

//////////////////////////////////////////////////////
// NOTIFICATIONS
//////////////////////////////////////////////////////

@GetMapping("/notifications/{userId}")
public List<CommunityNotification> notifications(@PathVariable Long userId){
return communityService.getNotifications(userId);
}

@PostMapping("/mention")
public void mentionUser(
        @RequestParam(required = false) Long threadId,
        @RequestParam(required = false) Long replyId,
        @RequestParam Long mentionedUserId
){
    communityService.mentionUser(threadId, replyId, mentionedUserId);
}

@GetMapping("/mentions/{userId}")
public List<CommunityMention> getMentions(@PathVariable Long userId){
    return communityService.getMentions(userId);
}

}