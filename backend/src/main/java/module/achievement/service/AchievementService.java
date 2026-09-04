package com.example.storyworkshop.module.achievement.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.storyworkshop.module.achievement.entity.Achievement;
import com.example.storyworkshop.module.achievement.mapper.AchievementMapper;
import com.example.storyworkshop.module.achievement.mapper.UserAchievementMapper;
import com.example.storyworkshop.module.achievement.vo.AchievementVO;

@Service
public class AchievementService {
    private final AchievementMapper achievementMapper;
    private final UserAchievementMapper userAchievementMapper;

    public AchievementService(AchievementMapper achievementMapper, UserAchievementMapper userAchievementMapper) {
        this.achievementMapper = achievementMapper;
        this.userAchievementMapper = userAchievementMapper;
    }

    public List<AchievementVO> mine(Long userId) {
        Set<String> owned = new HashSet<>(userAchievementMapper.selectCodesByUserId(userId));
        List<AchievementVO> result = new ArrayList<>();
        for (Achievement a : achievementMapper.selectAll()) {
            AchievementVO vo = new AchievementVO();
            vo.setAchId(a.getAchId());
            vo.setAchCode(a.getAchCode());
            vo.setAchName(a.getAchName());
            vo.setDescription(a.getDescription());
            vo.setAchType(a.getAchType());
            vo.setStoryId(a.getStoryId());
            vo.setAchieved(owned.contains(a.getAchCode()));
            result.add(vo);
        }
        return result;
    }
}
