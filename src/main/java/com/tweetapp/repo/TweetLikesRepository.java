package com.tweetapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweetapp.model.TweetLikes;

public interface TweetLikesRepository extends JpaRepository<TweetLikes, Integer> {
	
	public List<TweetLikes> findByTweetTweetId(int tweetId);

}
