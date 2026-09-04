package com.example.storyworkshop.module.stat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.storyworkshop.module.stat.mapper.StatMapper;
import com.example.storyworkshop.module.stat.vo.DashboardVO;
import com.example.storyworkshop.module.stat.vo.HomeRecommendVO;
import com.example.storyworkshop.module.stat.vo.RankStoryVO;
import com.example.storyworkshop.module.story.entity.Story;
import com.example.storyworkshop.module.story.service.StoryService;
import com.example.storyworkshop.module.story.vo.StoryCardVO;

@Service
public class StatService {
    private final StatMapper statMapper;
    private final StoryService storyService;

    public StatService(StatMapper statMapper, StoryService storyService) {
        this.statMapper = statMapper;
        this.storyService = storyService;
    }

    public HomeRecommendVO home() {
        HomeRecommendVO vo = new HomeRecommendVO();
        List<Story> hot = statMapper.selectHot(10);
        List<Story> fresh = statMapper.selectNewest(8);
        vo.setRecommend(new ArrayList<>());
        for (int i = 0; i < Math.min(3, hot.size()); i++) vo.getRecommend().add(storyService.toCard(hot.get(i)));
        vo.setHot(new ArrayList<>());
        for (Story s : hot) vo.getHot().add(storyService.toCard(s));
        vo.setFresh(new ArrayList<>());
        for (Story s : fresh) vo.getFresh().add(storyService.toCard(s));
        return vo;
    }

    public List<RankStoryVO> rank(int limit) {
        int n = Math.max(1, Math.min(limit, 50));
        List<RankStoryVO> result = new ArrayList<>();
        for (Story s : statMapper.selectHot(n)) {
            RankStoryVO vo = new RankStoryVO();
            vo.setStoryId(s.getStoryId());
            vo.setTitle(s.getTitle());
            vo.setAuthorName(s.getAuthorName());
            vo.setPlayCount(s.getPlayCount());
            vo.setLikeCount(s.getLikeCount());
            vo.setFavoriteCount(s.getFavoriteCount());
            vo.setCommentCount(s.getCommentCount());
            vo.setHeat(1L * s.getPlayCount() + 2L * s.getLikeCount() + 3L * s.getFavoriteCount() + 4L * s.getCommentCount());
            result.add(vo);
        }
        return result;
    }

    public DashboardVO dashboard() {
        Map<String, Object> row = statMapper.selectDashboard();
        DashboardVO vo = new DashboardVO();
        vo.setTotalUsers(num(row.get("totalUsers")));
        vo.setTotalStories(num(row.get("totalStories")));
        vo.setPublishedStories(num(row.get("publishedStories")));
        vo.setPendingStories(num(row.get("pendingStories")));
        vo.setTotalPlays(num(row.get("totalPlays")));
        vo.setPendingReports(num(row.get("pendingReports")));
        return vo;
    }

    private long num(Object o) {
        return o == null ? 0L : ((Number) o).longValue();
    }
}
