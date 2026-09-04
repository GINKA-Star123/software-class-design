package com.example.storyworkshop.module.story.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.entity.StoryChoice;
import com.example.storyworkshop.module.story.entity.StoryNode;
import com.example.storyworkshop.module.story.mapper.StoryChoiceMapper;
import com.example.storyworkshop.module.story.mapper.StoryMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;
import com.example.storyworkshop.module.story.vo.StoryValidationVO;
import org.springframework.stereotype.Service;

@Service
public class StoryValidator {
    private final StoryMapper storyMapper;
    private final StoryNodeMapper nodeMapper;
    private final StoryChoiceMapper choiceMapper;

    public StoryValidator(StoryMapper storyMapper, StoryNodeMapper nodeMapper, StoryChoiceMapper choiceMapper) {
        this.storyMapper = storyMapper;
        this.nodeMapper = nodeMapper;
        this.choiceMapper = choiceMapper;
    }

    public StoryValidationVO validate(Long storyId) {
        Story story = storyMapper.selectById(storyId);
        if (story == null) throw new BusinessException(ResultCode.STORY_NOT_FOUND);
        List<StoryNode> nodes = nodeMapper.selectByStory(storyId);
        List<StoryChoice> choices = choiceMapper.selectByStory(storyId);
        Map<Long, StoryNode> nodeMap = new HashMap<>();
        for (StoryNode n : nodes) nodeMap.put(n.getNodeId(), n);
        Map<Long, List<StoryChoice>> out = new HashMap<>();
        for (StoryChoice c : choices) out.computeIfAbsent(c.getFromNodeId(), k -> new ArrayList<>()).add(c);

        StoryValidationVO vo = new StoryValidationVO();
        vo.setNodeCount(nodes.size());
        vo.setIssues(new ArrayList<>());

        StoryNode start = nodes.stream().filter(n -> Integer.valueOf(1).equals(n.getIsStart())).findFirst().orElse(null);
        if (start == null) {
            vo.setValid(false);
            vo.getIssues().add("缺少起始节点：请标记一个起始节点");
            return vo;
        }
        Set<Long> visited = new HashSet<>();
        Deque<Long> queue = new ArrayDeque<>();
        queue.add(start.getNodeId());
        visited.add(start.getNodeId());
        while (!queue.isEmpty()) {
            Long cur = queue.poll();
            for (StoryChoice c : out.getOrDefault(cur, List.of())) {
                if (c.getToNodeId() != null && nodeMap.containsKey(c.getToNodeId()) && !visited.contains(c.getToNodeId())) {
                    visited.add(c.getToNodeId());
                    queue.add(c.getToNodeId());
                }
            }
        }
        List<Long> unreachable = new ArrayList<>();
        List<Long> deadEnds = new ArrayList<>();
        for (StoryNode n : nodes) {
            if (!visited.contains(n.getNodeId())) unreachable.add(n.getNodeId());
            if (visited.contains(n.getNodeId()) && !Integer.valueOf(1).equals(n.getIsEnding())) {
                List<StoryChoice> cs = out.getOrDefault(n.getNodeId(), List.of());
                boolean hasTarget = cs.stream().anyMatch(c -> c.getToNodeId() != null && nodeMap.containsKey(c.getToNodeId()));
                if (cs.isEmpty() || !hasTarget) deadEnds.add(n.getNodeId());
            }
        }
        if (!unreachable.isEmpty()) vo.getIssues().add("存在不可达节点（编号：" + unreachable + "）");
        if (!deadEnds.isEmpty()) vo.getIssues().add("存在死分支节点（非结局且无有效出口，编号：" + deadEnds + "）");

        int ending = 0;
        for (Long id : visited) {
            StoryNode n = nodeMap.get(id);
            if (Integer.valueOf(1).equals(n.getIsEnding())) ending++;
        }
        vo.setEndingCount(ending);
        if (ending == 0) vo.getIssues().add("故事缺少可达的结局节点");
        vo.setValid(vo.getIssues().isEmpty());
        return vo;
    }
}
