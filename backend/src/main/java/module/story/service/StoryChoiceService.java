package com.example.storyworkshop.module.story.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.story.dto.StoryChoiceRequest;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.entity.StoryChoice;
import com.example.storyworkshop.module.story.entity.StoryNode;
import com.example.storyworkshop.module.story.mapper.StoryChoiceMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;

@Service
public class StoryChoiceService {
    private final StoryService storyService;
    private final StoryNodeMapper nodeMapper;
    private final StoryChoiceMapper choiceMapper;

    public StoryChoiceService(StoryService storyService, StoryNodeMapper nodeMapper, StoryChoiceMapper choiceMapper) {
        this.storyService = storyService;
        this.nodeMapper = nodeMapper;
        this.choiceMapper = choiceMapper;
    }

    @Transactional
    public StoryChoice add(Long operatorId, Long fromNodeId, StoryChoiceRequest request) {
        StoryNode from = requireNodeEditable(operatorId, fromNodeId);
        StoryNode to = nodeMapper.selectById(request.getToNodeId());
        if (to == null || !to.getStoryId().equals(from.getStoryId())) throw new BusinessException(ResultCode.STORY_NODE_NOT_FOUND);
        if (!ConditionParser.isValid(request.getConditionExpr())) throw new BusinessException(ResultCode.STORY_VALIDATION_FAILED, "条件表达式格式错误");
        StoryChoice choice = new StoryChoice();
        choice.setFromNodeId(fromNodeId);
        choice.setToNodeId(request.getToNodeId());
        choice.setChoiceText(request.getChoiceText());
        choice.setConditionExpr(request.getConditionExpr());
        choice.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        choiceMapper.insert(choice);
        return choice;
    }

    @Transactional
    public void update(Long operatorId, Long choiceId, StoryChoiceRequest request) {
        StoryChoice choice = requireChoice(operatorId, choiceId);
        StoryNode to = nodeMapper.selectById(request.getToNodeId());
        if (to == null || !to.getStoryId().equals(nodeMapper.selectById(choice.getFromNodeId()).getStoryId())) {
            throw new BusinessException(ResultCode.STORY_NODE_NOT_FOUND);
        }
        if (!ConditionParser.isValid(request.getConditionExpr())) throw new BusinessException(ResultCode.STORY_VALIDATION_FAILED, "条件表达式格式错误");
        choice.setToNodeId(request.getToNodeId());
        choice.setChoiceText(request.getChoiceText());
        choice.setConditionExpr(request.getConditionExpr());
        choice.setSortOrder(request.getSortOrder() == null ? 0 : request.getSortOrder());
        choiceMapper.update(choice);
    }

    @Transactional
    public void delete(Long operatorId, Long choiceId) {
        requireChoice(operatorId, choiceId);
        choiceMapper.deleteById(choiceId);
    }

    private StoryNode requireNodeEditable(Long operatorId, Long nodeId) {
        StoryNode node = nodeMapper.selectById(nodeId);
        if (node == null) throw new BusinessException(ResultCode.STORY_NODE_NOT_FOUND);
        storyService.requireOwnedEditable(operatorId, node.getStoryId());
        return node;
    }

    private StoryChoice requireChoice(Long operatorId, Long choiceId) {
        StoryChoice choice = choiceMapper.selectById(choiceId);
        if (choice == null) throw new BusinessException(ResultCode.STORY_CHOICE_NOT_FOUND);
        StoryNode from = nodeMapper.selectById(choice.getFromNodeId());
        if (from == null) throw new BusinessException(ResultCode.STORY_NODE_NOT_FOUND);
        storyService.requireOwnedEditable(operatorId, from.getStoryId());
        return choice;
    }
}
