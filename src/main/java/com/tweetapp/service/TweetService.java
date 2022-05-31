package com.tweetapp.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tweetapp.exception.TweetAppException;
import com.tweetapp.model.Tweet;
import com.tweetapp.model.TweetLikes;
import com.tweetapp.model.TweetReplies;
import com.tweetapp.model.User;
import com.tweetapp.repo.TweetLikesRepository;
import com.tweetapp.repo.TweetRepliesRepository;
import com.tweetapp.repo.TweetRepo;
import com.tweetapp.repo.UserRepo;
import com.tweetapp.request.TweetRequest;
import com.tweetapp.util.Envelope;
import com.tweetapp.util.TweetConstant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TweetService {

	@Autowired
	TweetRepo tweetRepository;

	@Autowired
	UserRepo userRepository;

	@Autowired
	TweetLikesRepository tweetLikesRepository;
	
	@Autowired
	TweetRepliesRepository tweetRepliesRepository;

	public ResponseEntity<Envelope<String>> postTweet(String userName, TweetRequest tweetRequest) {
		log.info(TweetConstant.IN_REQUEST_LOG, "postTweet", tweetRequest);
		Optional<User> findByEmailIdName = userRepository.findByEmailId(userName);
		long count = tweetRepository.count();
		log.info("total tweets " + count);
		if (!findByEmailIdName.isPresent())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
					TweetConstant.USER_NAME_NOT_PRESENT);
		tweetRequest.setTweetId((int) count + 1);
		Tweet tweet = new Tweet(tweetRequest.getTweetId(), tweetRequest.getUserName(), tweetRequest.getTweet(),
				new Date(System.currentTimeMillis()));
		tweetRepository.save(tweet);
		log.info(TweetConstant.EXITING_RESPONSE_LOG, "postTweet", tweet);
		return ResponseEntity.ok(new Envelope<String>(HttpStatus.OK.value(), HttpStatus.OK, "Saved"));
	}

	public ResponseEntity<Envelope<List<Tweet>>> getAllTweet() {
		log.info(TweetConstant.IN_REQUEST_LOG, "getAllTweet", "getting All Tweets");
		List<Tweet> findAll = tweetRepository.findAll();
		if (findAll.isEmpty())
			throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
					TweetConstant.NO_TWEETS_FOUND);
		log.info(TweetConstant.EXITING_RESPONSE_LOG, "getAllTweet", findAll);
		return ResponseEntity.ok(new Envelope<List<Tweet>>(HttpStatus.OK.value(), HttpStatus.OK, findAll));
	}

	public ResponseEntity<Envelope<List<Tweet>>> getAllUserTweet(String userName) {
		log.info(TweetConstant.IN_REQUEST_LOG, "getAllUserTweet", userName);
		List<Tweet> findByUserName = tweetRepository.findByUserName(userName);
		if (findByUserName.isEmpty())
			throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
					TweetConstant.NO_TWEETS_FOUND);
		log.info(TweetConstant.EXITING_RESPONSE_LOG, "getAllUserTweet", findByUserName);
		return ResponseEntity.ok(new Envelope<List<Tweet>>(HttpStatus.OK.value(), HttpStatus.OK, findByUserName));
	}
	

	public ResponseEntity<Envelope<String>> updateTweet(String userName, int tweetId, TweetRequest tweetRequest) {
		log.info(TweetConstant.IN_REQUEST_LOG, "updateTweet", tweetRequest);
		tweetAndUserValidation(userName, tweetId);
		
		Tweet tweet = tweetRepository.findByTweetId(tweetRequest.getTweetId());
		tweet.setTweet(tweetRequest.getTweet());
		tweet.setCreated(new Date(System.currentTimeMillis()));
		tweet.setUserName(tweetRequest.getUserName());
		
		tweetRepository.save(tweet);
		if (tweet == null)
			throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Error While Updating Tweet");
		log.info(TweetConstant.EXITING_RESPONSE_LOG, "updateTweet", tweet);
		return ResponseEntity
				.ok(new Envelope<String>(HttpStatus.OK.value(), HttpStatus.OK, TweetConstant.TWEET_UPDATED));
	}

	public ResponseEntity<Envelope<String>> deleteTweet(String userName, int tweetId) {
		log.info(TweetConstant.IN_REQUEST_LOG, "deleteTweet", tweetId);
		tweetAndUserValidation(userName, tweetId);
		tweetRepository.deleteById(tweetId);
		log.info(TweetConstant.EXITING_RESPONSE_LOG, "deleteTweet", TweetConstant.TWEET_DELETED);
		return ResponseEntity
				.ok(new Envelope<String>(HttpStatus.OK.value(), HttpStatus.OK, TweetConstant.TWEET_DELETED));
	}

	public ResponseEntity<Envelope<String>> likeTweet(String userName, int tweetId) {
		log.info(TweetConstant.IN_REQUEST_LOG, "likeTweet", tweetId);
		tweetAndUserValidation(userName, tweetId);
		Tweet tweet = tweetRepository.findByTweetId(tweetId);
		
		 List<TweetLikes>
		 tweetLikes=tweetLikesRepository.findAll();
		 TweetLikes newTweetLikes = new TweetLikes();
		 newTweetLikes.setId(1);
		 newTweetLikes.setUsername(userName);
		 newTweetLikes.setTweet(tweet);
		 newTweetLikes.getTweet().setTweetId(tweetId);
		
		 if(tweetLikes!=null && !tweetLikes.isEmpty()) { 
			 int count=tweetLikes.size();
			 newTweetLikes.setId(count+1);
		 }
		 tweetLikesRepository.save(newTweetLikes);
		 
		if (tweet == null)
			throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Error While Liking");
		return ResponseEntity.ok(new Envelope<String>(HttpStatus.OK.value(), HttpStatus.OK, TweetConstant.LIKED_TWEET));
	}

	public ResponseEntity<Envelope<String>> replyTweet(String userName, int tweetId, String reply) {
		log.info(TweetConstant.IN_REQUEST_LOG, "replyTweet", tweetId + " " + reply);
		tweetAndUserValidation(userName, tweetId);
		Optional<Tweet> findById = tweetRepository.findById(tweetId);
		Tweet tweet = findById.get();
		
		 List<TweetReplies>
		 tweetReplies=tweetRepliesRepository.findAll();
		 TweetReplies newTweetReplies = new TweetReplies();
		 newTweetReplies.setReplyId(1);
		 newTweetReplies.setUserName(userName);
		 newTweetReplies.setTweet(tweet);
		 newTweetReplies.setReply(reply);
		 newTweetReplies.getTweet().setTweetId(tweetId);
		
		 if(tweetReplies!=null && !tweetReplies.isEmpty()) { 
			 int count=tweetReplies.size();
			 newTweetReplies.setReplyId(count+1);
		 }
		 tweetRepliesRepository.save(newTweetReplies);
		
		log.info(TweetConstant.EXITING_RESPONSE_LOG, "replyTweet", tweet);
		if (tweet == null)
			throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
					"Error While replying");
		return ResponseEntity
				.ok(new Envelope<String>(HttpStatus.OK.value(), HttpStatus.OK, TweetConstant.REPLIED_TO_TWEET));
	}

	private void tweetAndUserValidation(String userName, int tweetId) {
		log.info(TweetConstant.IN_REQUEST_LOG, "tweetAndUserValidation :: Validating User", userName);
		Optional<Tweet> findById = tweetRepository.findById(tweetId);
		Optional<User> findByEmailIdName = userRepository.findByEmailId(userName);
		if (!findByEmailIdName.isPresent())
			throw new TweetAppException(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST,
					TweetConstant.USER_NAME_NOT_PRESENT);
		log.info(TweetConstant.IN_REQUEST_LOG, "tweetAndUserValidation :: Validating Tweet", tweetId);
		if (!findById.isPresent())
			throw new TweetAppException(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR,
					TweetConstant.NO_TWEETS_FOUND);
		log.info(TweetConstant.EXITING_RESPONSE_LOG, "tweetAndUserValidation", "User And Tweet Validated");
	}

}
