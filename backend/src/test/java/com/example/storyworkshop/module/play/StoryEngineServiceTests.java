package com.example.storyworkshop.module.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.example.storyworkshop.module.achievement.mapper.AchievementMapper;
import com.example.storyworkshop.module.achievement.mapper.UserAchievementMapper;
import com.example.storyworkshop.module.play.entity.GameProgress;
import com.example.storyworkshop.module.play.mapper.ProgressMapper;
import com.example.storyworkshop.module.play.service.ConditionEvaluator;
import com.example.storyworkshop.module.play.service.StoryEngineService;
import com.example.storyworkshop.module.play.vo.PlayNodeVO;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.entity.StoryChoice;
import com.example.storyworkshop.module.story.entity.StoryNode;
import com.example.storyworkshop.module.story.mapper.StoryChoiceMapper;
import com.example.storyworkshop.module.story.mapper.StoryMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;

class StoryEngineServiceTests {

    private StoryMapper storyMapper;
    private StoryNodeMapper nodeMapper;
    private StoryChoiceMapper choiceMapper;
    private ProgressMapper progressMapper;
    private AchievementMapper achievementMapper;
    private UserAchievementMapper userAchievementMapper;
    private StoryEngineService engine;

    @BeforeEach
    void setUp() {
        storyMapper = mock(StoryMapper.class);
        nodeMapper = mock(StoryNodeMapper.class);
        choiceMapper = mock(StoryChoiceMapper.class);
        progressMapper = mock(ProgressMapper.class);
        achievementMapper = mock(AchievementMapper.class);
        userAchievementMapper = mock(UserAchievementMapper.class);
        engine = new StoryEngineService(storyMapper, nodeMapper, choiceMapper, progressMapper,
                achievementMapper, userAchievementMapper, new ConditionEvaluator());
    }

    private Story publishedStory() {
        Story s = new Story();
        s.setStoryId(1L);
        s.setTitle("测试故事");
        s.setStatus(2);
        return s;
    }

    private StoryNode startNode() {
        StoryNode n = new StoryNode();
        n.setNodeId(10L);
        n.setStoryId(1L);
        n.setNodeText("起点");
        n.setIsStart(1);
        n.setIsEnding(0);
        return n;
    }

    private StoryNode endingNode() {
        StoryNode n = new StoryNode();
        n.setNodeId(20L);
        n.setStoryId(1L);
        n.setNodeText("终点");
        n.setIsStart(0);
        n.setIsEnding(1);
        n.setEndingTitle("测试结局");
        return n;
    }

    @Test
    void startCreatesProgressWhenNoRows() {
        when(storyMapper.selectById(1L)).thenReturn(publishedStory());
        when(nodeMapper.selectStartByStory(1L)).thenReturn(startNode());
        when(progressMapper.selectOngoing(1L, 1L)).thenReturn(null);
        when(progressMapper.selectByUserStory(1L, 1L)).thenReturn(List.of());
        when(achievementMapper.selectByCode("FIRST_PLAY")).thenReturn(null);
        when(achievementMapper.selectByCode("first_play")).thenReturn(null);
        when(achievementMapper.selectAll()).thenReturn(List.of());
        when(userAchievementMapper.selectCodesByUserId(1L)).thenReturn(List.of());

        PlayNodeVO vo = engine.start(1L, 1L, null);
        assertEquals(10L, vo.getNodeId());
        ArgumentCaptor<GameProgress> captor = ArgumentCaptor.forClass(GameProgress.class);
        verify(progressMapper).insert(captor.capture());
        assertEquals(1, captor.getValue().getSlotNo());
    }

    @Test
    void chooseAdvancesToEnding() {
        GameProgress progress = new GameProgress();
        progress.setProgressId(1L);
        progress.setUserId(1L);
        progress.setStoryId(1L);
        progress.setCurrentNodeId(10L);
        progress.setStatus(0);
        progress.setHistory("{\"path\":[],\"endings\":[]}");
        progress.setEndingCount(0);

        StoryChoice choice = new StoryChoice();
        choice.setChoiceId(5L);
        choice.setFromNodeId(10L);
        choice.setToNodeId(20L);
        choice.setChoiceText("前进");

        when(progressMapper.selectById(1L)).thenReturn(progress);
        when(choiceMapper.selectById(5L)).thenReturn(choice);
        when(nodeMapper.selectById(10L)).thenReturn(startNode());
        when(storyMapper.selectById(1L)).thenReturn(publishedStory());
        when(nodeMapper.selectById(20L)).thenReturn(endingNode());
        when(userAchievementMapper.selectCodesByUserId(1L)).thenReturn(List.of());
        when(userAchievementMapper.exists(anyLong(), anyLong())).thenReturn(0);
        when(achievementMapper.selectAll()).thenReturn(List.of());
        when(choiceMapper.selectByFromNode(10L)).thenReturn(List.of());
        when(choiceMapper.selectByFromNode(20L)).thenReturn(List.of());

        PlayNodeVO vo = engine.choose(1L, 1L, 5L);
        assertTrue(vo.getJustEnded());
        assertEquals("测试结局", vo.getEndingTitle());
        ArgumentCaptor<GameProgress> captor = ArgumentCaptor.forClass(GameProgress.class);
        verify(progressMapper).update(captor.capture());
        assertEquals(1, captor.getValue().getStatus());
    }
}
