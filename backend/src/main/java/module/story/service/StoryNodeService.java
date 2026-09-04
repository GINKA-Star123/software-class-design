package com.example.storyworkshop.module.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.story.dto.StoryNodeRequest;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.entity.StoryNode;
import com.example.storyworkshop.module.story.mapper.StoryChoiceMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;

@Service
public class StoryNodeService {
    private final StoryService storyService;
    private final StoryNodeMapper nodeMapper;
    private final StoryChoiceMapper choiceMapper;

    public StoryNodeService(StoryService storyService, StoryNodeMapper nodeMapper, StoryChoiceMapper choiceMapper) {
        this.storyService = storyService;
        this.nodeMapper = nodeMapper;
        this.choiceMapper = choiceMapper;
    }

    @Transactional
    public StoryNode add(Long operatorId, Long storyId, StoryNodeRequest request) {
        storyService.requireOwnedEditable(operatorId, storyId);
        StoryNode node = new StoryNode();
        node.setStoryId(storyId);
        node.setNodeText(request.getNodeText());
        node.setIsStart(request.getIsStart() == null ? 0 : request.getIsStart());
        node.setIsEnding(request.getIsEnding() == null ? 0 : request.getIsEnding());
        node.setEndingTitle(Integer.valueOf(1).equals(node.getIsEnding()) ? request.getEndingTitle() : null);
        node.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        nodeMapper.insert(node);
        return node;
    }

    @Transactional
    public void update(Long operatorId, Long nodeId, StoryNodeRequest request) {
        StoryNode node = requireEditable(operatorId, nodeId);
        node.setNodeText(request.getNodeText());
        node.setIsEnding(request.getIsEnding() == null ? 0 : request.getIsEnding());
        node.setEndingTitle(Integer.valueOf(1).equals(node.getIsEnding()) ? request.getEndingTitle() : null);
        node.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        if (request.getIsStart() != null && Integer.valueOf(1).equals(request.getIsStart()) && !Integer.valueOf(1).equals(node.getIsStart())) {
            node.setIsStart(1);
        }
        nodeMapper.update(node);
    }

    @Transactional
    public void delete(Long operatorId, Long nodeId) {
        StoryNode node = requireEditable(operatorId, nodeId);
        if (Integer.valueOf(1).equals(node.getIsStart())) throw new BusinessException(ResultCode.STORY_STATUS_ERROR, "起始节点不能删除");
        choiceMapper.deleteByNode(nodeId);
        nodeMapper.deleteById(nodeId);
    }

    public StoryNode requireEditable(Long operatorId, Long nodeId) {
        StoryNode node = nodeMapper.selectById(nodeId);
        if (node == null) throw new BusinessException(ResultCode.STORY_NODE_NOT_FOUND);
        Story story = storyMapperOf(node.getStoryId());
        if (!story.getAuthorId().equals(operatorId)) throw new BusinessException(ResultCode.FORBIDDEN);
        if (story.getStatus() != null && story.getStatus() != 0 && story.getStatus() != 3) {
            throw new BusinessException(ResultCode.STORY_STATUS_ERROR);
        }
        return node;
    }

    private Story storyMapperOf(Long storyId) {
        return storyService.requireStory(storyId);
    }
}
