package com.tweetapp.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tweetapp.model.TweetReplies;

public interface TweetRepliesRepository extends JpaRepository<TweetReplies, Integer> {
	
	public List<TweetReplies> findByTweetTweetId(int tweetId);

}
