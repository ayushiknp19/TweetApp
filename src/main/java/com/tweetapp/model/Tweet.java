package com.tweetapp.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Entity
public class Tweet {
	@Transient
	public static final String SEQUENCE_NAME = "tweet_sequence";
	@Id
	private int tweetId;
	private String userName;
	private String tweet;
	private Date created;
	/*
	 * @OneToMany(mappedBy="tweetId") private List<TweetLikes> likes;
	 */
	/*
	 * @OneToMany(mappedBy="tweetId") private List<TweetReplies> replies;
	 */
	//private Map<String, Integer> likes;
	//private Map<String, List<String>> replies;
}
