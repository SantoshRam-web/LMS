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

@GetMapping("/spaces")
public List<CommunitySpace> spaces(){
return communityService.getSpaces();
}

@GetMapping("/spaces/{spaceId}/channels")
public List<CommunityChannel> channels(@PathVariable Long spaceId){
return communityService.getChannels(spaceId);
}

@PostMapping("/channels")
public CommunityChannel createChannel(
@RequestParam Long spaceId,
@RequestParam String name,
@RequestParam String description,
@RequestParam Boolean adminsOnly
){
return communityService.createChannel(spaceId,name,description,adminsOnly);
}

@PutMapping("/channels/{id}")
public CommunityChannel updateChannel(
@PathVariable Long id,
@RequestParam String name,
@RequestParam String description,
@RequestParam Boolean adminsOnly
){
return communityService.updateChannel(id,name,description,adminsOnly);
}

@PostMapping("/threads")
public CommunityThread createThread(@RequestBody CommunityThread thread){
return communityService.createThread(thread);
}

@GetMapping("/threads/{channelId}")
public List<CommunityThread> threads(@PathVariable Long channelId){
return communityService.getThreads(channelId);
}

@PostMapping("/threads/{threadId}/reply")
public CommunityReply reply(
@PathVariable Long threadId,
@RequestBody CommunityReply reply
){
return communityService.reply(threadId,reply);
}

@PostMapping("/react")
public void react(
@RequestParam Long threadId,
@RequestParam Long replyId,
@RequestParam String reactionType,
@RequestParam Long userId
){
communityService.react(threadId,replyId,reactionType,userId);
}

@PostMapping("/bookmark")
public void bookmark(
@RequestParam Long threadId,
@RequestParam Long userId
){
communityService.bookmark(threadId,userId);
}

}