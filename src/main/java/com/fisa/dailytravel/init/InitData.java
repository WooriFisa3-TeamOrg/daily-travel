//package com.fisa.dailytravel.init;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.UUID;
//
//@Component
//public class InitData implements CommandLineRunner {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    private static final int BATCH_SIZE = 1_000_000;
//    private static final int TOTAL_POSTS = 1_000_000;
//    private static final int TOTAL_IMAGES = 1_000_000;
//    private static final int TOTAL_USERS = 10_000;
//    private static final int TOTAL_HASHTAGS = 100_000;
//
//    private final Random random = new Random();
//
//    @Override
//    public void run(String... args) throws Exception {
//        createUsers();
//        createPosts();
//        createImages();
//        createHashtags();
//        createPostHashtags();
//        createComments();
//
//        System.out.println("Data initialization completed.");
//    }
//
//    private void createUsers() {
//        List<Object[]> userBatchArgs = new ArrayList<>();
//        long initialUserId = 1L;
//
//        for (int i = 0; i < TOTAL_USERS; i++) {
//            long userId = initialUserId + i;
//            String uuid = UUID.randomUUID().toString();
//            String email = "user" + i + "@example.com";
//            String nickname = "user" + i;
//            userBatchArgs.add(new Object[]{userId, uuid, email, nickname, false});
//
//            if (userBatchArgs.size() == TOTAL_USERS) {
//                jdbcTemplate.batchUpdate(
//                        "INSERT INTO users (users_id, uuid, email, nickname, is_deleted) VALUES (?, ?, ?, ?, ?)",
//                        userBatchArgs
//                );
//                userBatchArgs.clear();
//            }
//        }
//        if (!userBatchArgs.isEmpty()) {
//            jdbcTemplate.batchUpdate(
//                    "INSERT INTO users (users_id, uuid, email, nickname, is_deleted) VALUES (?, ?, ?, ?, ?)",
//                    userBatchArgs
//            );
//        }
//
//        long lastUserId = initialUserId + TOTAL_USERS - 1;
//        jdbcTemplate.execute("ALTER SEQUENCE users_seq RESTART WITH " + (lastUserId + 1));
//    }
//
//    private void createPosts() {
//        List<Object[]> postBatchArgs = new ArrayList<>();
//        long initialPostId = 1L;
//
//        for (int i = 0; i < TOTAL_POSTS; i++) {
//            long postId = initialPostId + i;
//            long userId = random.nextInt(TOTAL_USERS) + 1;
//            String title = "Post Title " + i;
//            String content = "Content of post " + i;
//            postBatchArgs.add(new Object[]{postId, title, content, userId, "thumbnail_path", new java.sql.Timestamp(System.currentTimeMillis()), new java.sql.Timestamp(System.currentTimeMillis()), 0});
//
//            if (postBatchArgs.size() == BATCH_SIZE) {
//                jdbcTemplate.batchUpdate(
//                        "INSERT INTO posts (post_id, post_title, post_content, users_id, thumbnail, updated_at,created_at, likes_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
//                        postBatchArgs
//                );
//                postBatchArgs.clear();
//            }
//        }
//
//        if (!postBatchArgs.isEmpty()) {
//            jdbcTemplate.batchUpdate(
//                    "INSERT INTO posts (post_id, post_title, post_content, users_id, thumbnail, updated_at,created_at, likes_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
//                    postBatchArgs
//            );
//        }
//
//        long lastPostId = initialPostId + TOTAL_POSTS - 1;
//        jdbcTemplate.execute("ALTER SEQUENCE post_seq RESTART WITH " + (lastPostId + 1));
//    }
//
//    private void createImages() {
//        List<Object[]> imageBatchArgs = new ArrayList<>();
//        long initialImageId = 1L;
//
//        for (int i = 0; i < TOTAL_IMAGES; i++) {
//            long imageId = initialImageId + i;
//            String imagePath = "image_path_" + i;
//            long postId = (i % TOTAL_POSTS) + 1;
//            imageBatchArgs.add(new Object[]{imageId, imagePath, postId});
//
//            if (imageBatchArgs.size() == BATCH_SIZE) {
//                jdbcTemplate.batchUpdate(
//                        "INSERT INTO images (image_id, image_path, post_id) VALUES (?, ?, ?)",
//                        imageBatchArgs
//                );
//                imageBatchArgs.clear();
//            }
//        }
//
//        if (!imageBatchArgs.isEmpty()) {
//            jdbcTemplate.batchUpdate(
//                    "INSERT INTO images (image_id, image_path, post_id) VALUES (?, ?, ?)",
//                    imageBatchArgs
//            );
//        }
//
//        long lastImageId = initialImageId + TOTAL_IMAGES - 1;
//        jdbcTemplate.execute("ALTER SEQUENCE image_seq RESTART WITH " + (lastImageId + 1));
//    }
//
//    private void createHashtags() {
//        List<Object[]> hashtagBatchArgs = new ArrayList<>();
//        long initialHashtagId = 1L;
//
//        for (int i = 0; i < TOTAL_HASHTAGS; i++) {
//            long hashtagId = initialHashtagId + i;
//            String hashtagName = "hashtag_" + i;
//            hashtagBatchArgs.add(new Object[]{hashtagId, hashtagName});
//
//            if (hashtagBatchArgs.size() == TOTAL_HASHTAGS) {
//                jdbcTemplate.batchUpdate(
//                        "INSERT INTO hashtags (hashtag_id, hashtag_name) VALUES (?, ?)",
//                        hashtagBatchArgs
//                );
//                hashtagBatchArgs.clear();
//            }
//        }
//
//        if (!hashtagBatchArgs.isEmpty()) {
//            jdbcTemplate.batchUpdate(
//                    "INSERT INTO hashtags (hashtag_id, hashtag_name) VALUES (?, ?)",
//                    hashtagBatchArgs
//            );
//        }
//
//        long lastHashtagId = initialHashtagId + TOTAL_HASHTAGS - 1;
//        jdbcTemplate.execute("ALTER SEQUENCE hashtag_seq RESTART WITH " + (lastHashtagId + 1));
//    }
//
//    private void createPostHashtags() {
//        List<Object[]> postHashtagBatchArgs = new ArrayList<>();
//        long initialPostHashtagId = 1L;
//
//        for (int i = 0; i < TOTAL_POSTS; i++) {
//            long postId = i + 1;
//            for (int j = 0; j < 2; j++) {
//                long postHashtagId = initialPostHashtagId++;
//                long hashtagId = (i * 2 + j) % TOTAL_HASHTAGS + 1;
//                postHashtagBatchArgs.add(new Object[]{postHashtagId, postId, hashtagId});
//
//                if (postHashtagBatchArgs.size() == BATCH_SIZE) {
//                    jdbcTemplate.batchUpdate(
//                            "INSERT INTO post_hashtags (post_hashtag_id, post_id, hashtag_id) VALUES (?, ?, ?)",
//                            postHashtagBatchArgs
//                    );
//                    postHashtagBatchArgs.clear();
//                }
//            }
//        }
//
//        if (!postHashtagBatchArgs.isEmpty()) {
//            jdbcTemplate.batchUpdate(
//                    "INSERT INTO post_hashtags (post_hashtag_id, post_id, hashtag_id) VALUES (?, ?, ?)",
//                    postHashtagBatchArgs
//            );
//        }
//
//        long lastPostHashtagId = initialPostHashtagId - 1;
//        jdbcTemplate.execute("ALTER SEQUENCE post_hashtag_seq RESTART WITH " + (lastPostHashtagId + 1));
//    }
//
//    private void createComments() {
//        List<Object[]> commentBatchArgs = new ArrayList<>();
//        long initialCommentId = 1L;
//
//        for (int i = 0; i < TOTAL_POSTS; i++) {
//            long postId = i + 1;
//            int commentCount = random.nextInt(5) + 1;
//
//            for (int k = 0; k < commentCount; k++) {
//                long commentId = initialCommentId++;  // 수동으로 증가하는 comments_id
//                long commentUserId = random.nextInt(TOTAL_USERS) + 1;
//                String commentContent = "Comment " + k + " on post " + i;
//                commentBatchArgs.add(new Object[]{commentId, commentContent, postId, commentUserId});
//
//                if (commentBatchArgs.size() == BATCH_SIZE) {
//                    jdbcTemplate.batchUpdate(
//                            "INSERT INTO comments (comments_id, comments_content, post_id, users_id) VALUES (?, ?, ?, ?)",
//                            commentBatchArgs
//                    );
//                    commentBatchArgs.clear();
//                }
//            }
//        }
//
//        if (!commentBatchArgs.isEmpty()) {
//            jdbcTemplate.batchUpdate(
//                    "INSERT INTO comments (comments_id, comments_content, post_id, users_id) VALUES (?, ?, ?, ?)",
//                    commentBatchArgs
//            );
//        }
//
//        long lastCommentId = initialCommentId - 1;
//        jdbcTemplate.execute("ALTER SEQUENCE comments_seq RESTART WITH " + (lastCommentId + 1));
//    }
//
//}
