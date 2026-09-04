package com.example.storyworkshop.module.play.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storyworkshop.common.constant.AppConstants;
import com.example.storyworkshop.common.constant.StoryStatusConstants;
import com.example.storyworkshop.common.exception.BusinessException;
import com.example.storyworkshop.common.result.ResultCode;
import com.example.storyworkshop.module.achievement.entity.Achievement;
import com.example.storyworkshop.module.achievement.mapper.AchievementMapper;
import com.example.storyworkshop.module.achievement.mapper.UserAchievementMapper;
import com.example.storyworkshop.module.play.entity.GameProgress;
import com.example.storyworkshop.module.play.mapper.ProgressMapper;
import com.example.storyworkshop.module.play.vo.PlayChoiceVO;
import com.example.storyworkshop.module.play.vo.PlayNodeVO;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.entity.StoryChoice;
import com.example.storyworkshop.module.story.entity.StoryNode;
import com.example.storyworkshop.module.story.mapper.StoryChoiceMapper;
import com.example.storyworkshop.module.story.mapper.StoryMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StoryEngineService {
    private final StoryMapper storyMapper;
    private final StoryNodeMapper nodeMapper;
    private final StoryChoiceMapper choiceMapper;
    private final ProgressMapper progressMapper;
    private final AchievementMapper achievementMapper;
    private final UserAchievementMapper userAchievementMapper;
    private final ConditionEvaluator evaluator;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StoryEngineService(StoryMapper storyMapper, StoryNodeMapper nodeMapper,
                              StoryChoiceMapper choiceMapper, ProgressMapper progressMapper,
                              AchievementMapper achievementMapper, UserAchievementMapper userAchievementMapper,
                              ConditionEvaluator evaluator) {
        this.storyMapper = storyMapper;
        this.nodeMapper = nodeMapper;
        this.choiceMapper = choiceMapper;
        this.progressMapper = progressMapper;
        this.achievementMapper = achievementMapper;
        this.userAchievementMapper = userAchievementMapper;
        this.evaluator = evaluator;
    }

    @Transactional
    public PlayNodeVO start(Long userId, Long storyId, Integer requestedSlot) {
        Story story = storyMapper.selectById(storyId);
        if (story == null || !StoryStatusConstants.canPlay(story.getStatus())) throw new BusinessException(ResultCode.STORY_NOT_PUBLISHED);
        StoryNode start = nodeMapper.selectStartByStory(storyId);
        if (start == null) throw new BusinessException(ResultCode.STORY_NODE_NOT_FOUND, "故事缺少起始节点");

        GameProgress ongoing = progressMapper.selectOngoing(userId, storyId);
        if (ongoing != null) return buildNode(ongoing.getProgressId(), story, nodeById(ongoing.getCurrentNodeId(), start), codesOf(userId), endingsOf(ongoing));

        List<GameProgress> rows = progressMapper.selectByUserStory(userId, storyId);
        GameProgress progress;
        if (rows.size() < AppConstants.MAX_PROGRESS_SLOT_COUNT) {
            progress = new GameProgress();
            progress.setUserId(userId);
            progress.setStoryId(storyId);
            progress.setSlotNo(freeSlot(rows));
        } else {
            // 三个槽位都已占用：复用最久未更新的进度
            GameProgress reuse = rows.get(rows.size() - 1);
            if (requestedSlot != null) {
                for (GameProgress r : rows) {
                    if (requestedSlot.equals(r.getSlotNo())) { reuse = r; break; }
                }
            }
            progress = reuse;
        }
        progress.setCurrentNodeId(start.getNodeId());
        progress.setStatus(0);
        progress.setHistory(toJsonPath(List.of(), List.of()));
        progress.setEndingCount(0);
        if (progress.getProgressId() == null) progressMapper.insert(progress); else progressMapper.update(progress);
        grantFirstPlayIfNeeded(userId);
        grantBeginIfPresent(userId, storyId);
        return buildNode(progress.getProgressId(), story, start, codesOf(userId), new HashSet<>());
    }

    private int freeSlot(List<GameProgress> rows) {
        Set<Integer> used = new HashSet<>();
        for (GameProgress p : rows) used.add(p.getSlotNo());
        for (int i = 1; i <= AppConstants.MAX_PROGRESS_SLOT_COUNT; i++) {
            if (!used.contains(i)) return i;
        }
        return 1;
    }

    @Transactional
    public PlayNodeVO choose(Long userId, Long progressId, Long choiceId) {
        GameProgress progress = progressMapper.selectById(progressId);
        if (progress == null || !progress.getUserId().equals(userId)) throw new BusinessException(ResultCode.PROGRESS_NOT_FOUND);
        if (progress.getStatus() != null && progress.getStatus() == 1) throw new BusinessException(ResultCode.PROGRESS_NOT_FOUND, "该进度已完成");
        StoryChoice choice = choiceMapper.selectById(choiceId);
        if (choice == null) throw new BusinessException(ResultCode.STORY_CHOICE_NOT_FOUND);
        StoryNode current = nodeMapper.selectById(progress.getCurrentNodeId());
        if (current == null || !current.getNodeId().equals(choice.getFromNodeId())) throw new BusinessException(ResultCode.STORY_CHOICE_NOT_FOUND, "选项与当前节点不匹配");
        Story story = storyMapper.selectById(progress.getStoryId());
        if (story == null || !StoryStatusConstants.canPlay(story.getStatus())) throw new BusinessException(ResultCode.STORY_NOT_PUBLISHED);

        List<Long> path = new ArrayList<>(pathOf(progress.getHistory()));
        Set<String> endings = new LinkedHashSet<>(endingsOf(progress));
        Set<String> codes = codesOf(userId);
        if (!evaluator.evaluate(choice.getConditionExpr(), codes, endings)) {
            throw new BusinessException(ResultCode.CHOICE_CONDITION_NOT_MATCH, "条件不满足：需要达成 " + choice.getConditionExpr());
        }
        StoryNode next = nodeMapper.selectById(choice.getToNodeId());
        if (next == null || !next.getStoryId().equals(story.getStoryId())) throw new BusinessException(ResultCode.STORY_NODE_NOT_FOUND);
        path.add(next.getNodeId());
        boolean ending = Integer.valueOf(1).equals(next.getIsEnding());
        boolean newEnding = false;
        if (ending && next.getEndingTitle() != null) {
            newEnding = endings.add(next.getEndingTitle());
            if (newEnding) grantEndingIfPresent(userId, story.getStoryId(), next.getEndingTitle());
        }
        progress.setCurrentNodeId(next.getNodeId());
        progress.setStatus(ending ? 1 : 0);
        progress.setEndingCount(endings.size());
        progress.setHistory(toJsonPath(path, new ArrayList<>(endings)));
        progressMapper.update(progress);
        return buildNode(progress.getProgressId(), story, next, codesOf(userId), endings);
    }

    public GameProgress requireProgress(Long userId, Long progressId) {
        GameProgress p = progressMapper.selectById(progressId);
        if (p == null || !p.getUserId().equals(userId)) throw new BusinessException(ResultCode.PROGRESS_NOT_FOUND);
        return p;
    }

    private StoryNode nodeById(Long nodeId, StoryNode fallback) {
        if (nodeId == null) return fallback;
        StoryNode node = nodeMapper.selectById(nodeId);
        return node == null ? fallback : node;
    }

    private PlayNodeVO buildNode(Long progressId, Story story, StoryNode node, Set<String> codes, Set<String> endings) {
        PlayNodeVO vo = new PlayNodeVO();
        vo.setProgressId(progressId);
        vo.setStoryId(story.getStoryId());
        vo.setStoryTitle(story.getTitle());
        vo.setNodeId(node.getNodeId());
        vo.setNodeText(node.getNodeText());
        vo.setIsStart(node.getIsStart());
        vo.setIsEnding(node.getIsEnding());
        vo.setEndingTitle(node.getEndingTitle());
        boolean ending = Integer.valueOf(1).equals(node.getIsEnding());
        vo.setJustEnded(ending);
        vo.setEndingCount(ending ? endings.size() : null);
        List<PlayChoiceVO> choices = new ArrayList<>();
        for (StoryChoice c : choiceMapper.selectByFromNode(node.getNodeId())) {
            PlayChoiceVO cv = new PlayChoiceVO();
            cv.setChoiceId(c.getChoiceId());
            cv.setToNodeId(c.getToNodeId());
            cv.setChoiceText(c.getChoiceText());
            cv.setConditionExpr(c.getConditionExpr());
            boolean ok = evaluator.evaluate(c.getConditionExpr(), codes, endings);
            cv.setAvailable(ok);
            if (!ok) cv.setLockReason("需达成：" + c.getConditionExpr());
            choices.add(cv);
        }
        vo.setChoices(choices);
        return vo;
    }

    private Set<String> codesOf(Long userId) {
        try {
            return new HashSet<>(userAchievementMapper.selectCodesByUserId(userId));
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    private Set<String> endingsOf(GameProgress p) {
        try {
            return new LinkedHashSet<>(objectMapper.readValue(p.getHistory(), new TypeReference<PathRecord>() {
            }).endings);
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    private List<Long> pathOf(String history) {
        try {
            return new ArrayList<>(objectMapper.readValue(history, new TypeReference<PathRecord>() {
            }).path);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private String toJsonPath(List<Long> path, List<String> endings) {
        try {
            PathRecord rec = new PathRecord();
            rec.path = path;
            rec.endings = endings;
            return objectMapper.writeValueAsString(rec);
        } catch (Exception e) {
            return "{\"path\":[],\"endings\":[]}";
        }
    }

    private void grantFirstPlayIfNeeded(Long userId) {
        Achievement first = findByCodeIgnoreCase("FIRST_PLAY", "first_play");
        if (first != null) insertAchievement(userId, first.getAchId());
    }

    private void grantBeginIfPresent(Long userId, Long storyId) {
        Achievement begin = findByCodeIgnoreCase("BEGIN", "begin");
        if (begin != null && storyId.equals(begin.getStoryId())) insertAchievement(userId, begin.getAchId());
    }

    private Achievement findByCodeIgnoreCase(String... codes) {
        Achievement hit = null;
        for (String code : codes) {
            hit = achievementMapper.selectByCode(code);
            if (hit != null) return hit;
        }
        for (Achievement a : achievementMapper.selectAll()) {
            for (String code : codes) {
                if (code.equalsIgnoreCase(a.getAchCode())) return a;
            }
        }
        return null;
    }

    private void grantEndingIfPresent(Long userId, Long storyId, String endingTitle) {
        if (endingTitle == null) return;
        for (Achievement a : achievementMapper.selectAll()) {
            if (Integer.valueOf(1).equals(a.getAchType()) && storyId.equals(a.getStoryId()) && endingTitle.equals(a.getAchName())) {
                insertAchievement(userId, a.getAchId());
            }
        }
    }

    private void insertAchievement(Long userId, Long achId) {
        try {
            if (userAchievementMapper.exists(userId, achId) == 0) {
                userAchievementMapper.insert(userId, achId);
            }
        } catch (Exception ignored) {
        }
    }

    static class PathRecord {
        public List<Long> path = new ArrayList<>();
        public List<String> endings = new ArrayList<>();
    }
}
