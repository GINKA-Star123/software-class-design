package com.example.storyworkshop.module.story;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.entity.StoryChoice;
import com.example.storyworkshop.module.story.entity.StoryNode;
import com.example.storyworkshop.module.story.mapper.StoryChoiceMapper;
import com.example.storyworkshop.module.story.mapper.StoryMapper;
import com.example.storyworkshop.module.story.mapper.StoryNodeMapper;
import com.example.storyworkshop.module.story.service.StoryValidator;
import com.example.storyworkshop.module.story.vo.StoryValidationVO;

class StoryValidatorTests {

    private StoryMapper storyMapper;
    private StoryNodeMapper nodeMapper;
    private StoryChoiceMapper choiceMapper;
    private StoryValidator validator;

    @BeforeEach
    void setUp() {
        storyMapper = mock(StoryMapper.class);
        nodeMapper = mock(StoryNodeMapper.class);
        choiceMapper = mock(StoryChoiceMapper.class);
        validator = new StoryValidator(storyMapper, nodeMapper, choiceMapper);
    }

    private Story story() {
        Story s = new Story();
        s.setStoryId(1L);
        return s;
    }

    private StoryNode node(long id, int start, int ending) {
        StoryNode n = new StoryNode();
        n.setNodeId(id);
        n.setStoryId(1L);
        n.setIsStart(start);
        n.setIsEnding(ending);
        return n;
    }

    private StoryChoice choice(long from, long to) {
        StoryChoice c = new StoryChoice();
        c.setFromNodeId(from);
        c.setToNodeId(to);
        return c;
    }

    @Test
    void validTreePasses() {
        when(storyMapper.selectById(anyLong())).thenReturn(story());
        when(nodeMapper.selectByStory(anyLong())).thenReturn(List.of(node(1, 1, 0), node(2, 0, 1)));
        when(choiceMapper.selectByStory(anyLong())).thenReturn(List.of(choice(1, 2)));
        StoryValidationVO vo = validator.validate(1L);
        assertTrue(vo.getValid());
    }

    @Test
    void missingEndingFails() {
        when(storyMapper.selectById(anyLong())).thenReturn(story());
        when(nodeMapper.selectByStory(anyLong())).thenReturn(List.of(node(1, 1, 0)));
        when(choiceMapper.selectByStory(anyLong())).thenReturn(List.of());
        StoryValidationVO vo = validator.validate(1L);
        assertFalse(vo.getValid());
        assertTrue(vo.getIssues().stream().anyMatch(i -> i.contains("结局") || i.contains("死分支")));
    }

    @Test
    void unreachableNodeFails() {
        when(storyMapper.selectById(anyLong())).thenReturn(story());
        when(nodeMapper.selectByStory(anyLong())).thenReturn(List.of(node(1, 1, 0), node(2, 0, 1), node(3, 0, 0)));
        when(choiceMapper.selectByStory(anyLong())).thenReturn(List.of(choice(1, 2)));
        StoryValidationVO vo = validator.validate(1L);
        assertFalse(vo.getValid());
        assertTrue(vo.getIssues().stream().anyMatch(i -> i.contains("不可达")));
    }
}
