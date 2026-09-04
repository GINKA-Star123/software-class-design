package com.example.storyworkshop.module.stat.vo;

import java.util.ArrayList;
import java.util.List;

public class HomeRecommendVO {
    private java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> recommend;
    private java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> hot;
    private java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> fresh;

    public java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> getRecommend() {
        return recommend;
    }

    public void setRecommend(java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> recommend) {
        this.recommend = recommend;
    }

    public java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> getHot() {
        return hot;
    }

    public void setHot(java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> hot) {
        this.hot = hot;
    }

    public java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> getFresh() {
        return fresh;
    }

    public void setFresh(java.util.List<com.example.storyworkshop.module.story.vo.StoryCardVO> fresh) {
        this.fresh = fresh;
    }

    public HomeRecommendVO() {
        this.recommend = new ArrayList<>();
        this.hot = new ArrayList<>();
        this.fresh = new ArrayList<>();
    }
}
