-- MySQL dump 10.11
--
-- Host: localhost    Database: rscproject
-- ------------------------------------------------------
-- Server version	5.0.51a-24+lenny4-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `appeals_abuse`
--

DROP TABLE IF EXISTS `appeals_abuse`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `appeals_abuse` (
  `id` int(10) NOT NULL auto_increment,
  `time` int(10) NOT NULL,
  `poster` int(10) NOT NULL,
  `account` varchar(255) NOT NULL,
  `text` text NOT NULL,
  `wur` int(1) default '0',
  `closed` int(1) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=218 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `appeals_abuse_replies`
--

DROP TABLE IF EXISTS `appeals_abuse_replies`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `appeals_abuse_replies` (
  `id` int(10) NOT NULL auto_increment,
  `ticket_id` int(10) NOT NULL,
  `poster` int(10) NOT NULL,
  `time` int(10) NOT NULL,
  `text` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=404 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `appeals_ban`
--

DROP TABLE IF EXISTS `appeals_ban`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `appeals_ban` (
  `id` int(10) NOT NULL auto_increment,
  `time` int(10) NOT NULL,
  `poster` int(10) NOT NULL,
  `account` varchar(255) NOT NULL,
  `text` text NOT NULL,
  `wur` int(1) default '0',
  `closed` int(1) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=452 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `appeals_ban_replies`
--

DROP TABLE IF EXISTS `appeals_ban_replies`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `appeals_ban_replies` (
  `id` int(10) NOT NULL auto_increment,
  `ticket_id` int(10) NOT NULL,
  `poster` int(10) NOT NULL,
  `time` int(10) NOT NULL,
  `text` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1739 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `appeals_ticket`
--

DROP TABLE IF EXISTS `appeals_ticket`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `appeals_ticket` (
  `id` int(10) NOT NULL auto_increment,
  `time` int(10) NOT NULL,
  `poster` int(10) NOT NULL,
  `category` int(10) NOT NULL,
  `text` text NOT NULL,
  `wur` int(1) default '0',
  `closed` int(1) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1552 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `appeals_ticket_replies`
--

DROP TABLE IF EXISTS `appeals_ticket_replies`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `appeals_ticket_replies` (
  `id` int(10) NOT NULL auto_increment,
  `ticket_id` int(10) NOT NULL,
  `poster` int(10) NOT NULL,
  `time` int(10) NOT NULL,
  `text` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4047 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `arcade_games`
--

DROP TABLE IF EXISTS `arcade_games`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `arcade_games` (
  `game_id` smallint(5) NOT NULL auto_increment,
  `game_name` varchar(50) NOT NULL default '',
  `game_filename` varchar(30) NOT NULL default '',
  `game_desc` text NOT NULL,
  `game_image` varchar(200) NOT NULL default '',
  `game_width` smallint(3) NOT NULL default '550',
  `game_height` smallint(3) NOT NULL default '400',
  `game_cat` tinyint(3) NOT NULL default '0',
  `game_played` smallint(5) NOT NULL default '0',
  PRIMARY KEY  (`game_id`)
) ENGINE=MyISAM AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `arcade_ranking`
--

DROP TABLE IF EXISTS `arcade_ranking`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `arcade_ranking` (
  `rank_id` smallint(5) NOT NULL auto_increment,
  `rank_game` varchar(50) NOT NULL default '0',
  `rank_player` smallint(5) NOT NULL default '0',
  `rank_score` double NOT NULL default '0',
  `rank_topscore` tinyint(1) NOT NULL default '0',
  `rank_date` int(10) unsigned default NULL,
  PRIMARY KEY  (`rank_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2229 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bans`
--

DROP TABLE IF EXISTS `bans`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bans` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `username` varchar(200) default NULL,
  `ip` varchar(255) default NULL,
  `email` varchar(80) default NULL,
  `message` varchar(255) default NULL,
  `expire` int(10) unsigned default NULL,
  `ban_creator` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `bans_username_idx` (`username`(25))
) ENGINE=MyISAM AUTO_INCREMENT=3817 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bugs`
--

DROP TABLE IF EXISTS `bugs`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bugs` (
  `id` int(10) NOT NULL auto_increment,
  `description` text NOT NULL,
  `poster` int(10) NOT NULL,
  `posted` int(10) NOT NULL,
  `last_updated` int(10) NOT NULL default '0',
  `comments` int(10) NOT NULL default '0',
  `last_edited` int(10) default NULL,
  `rank` tinyint(1) NOT NULL,
  `status` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=644 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bugs_comments`
--

DROP TABLE IF EXISTS `bugs_comments`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bugs_comments` (
  `id` int(10) NOT NULL auto_increment,
  `bug_id` int(10) NOT NULL,
  `poster` int(10) NOT NULL,
  `comment` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=353 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `categories` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `cat_name` varchar(80) NOT NULL default 'New Category',
  `disp_position` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `censoring`
--

DROP TABLE IF EXISTS `censoring`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `censoring` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `search_for` varchar(60) NOT NULL default '',
  `replace_with` varchar(60) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=198 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `client_uploads`
--

DROP TABLE IF EXISTS `client_uploads`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `client_uploads` (
  `id` int(11) NOT NULL auto_increment,
  `link` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=84 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `config` (
  `conf_name` varchar(255) NOT NULL default '',
  `conf_value` text,
  PRIMARY KEY  (`conf_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_bans`
--

DROP TABLE IF EXISTS `dev_bans`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_bans` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `username` varchar(200) default NULL,
  `ip` varchar(255) default NULL,
  `email` varchar(80) default NULL,
  `message` varchar(255) default NULL,
  `expire` int(10) unsigned default NULL,
  `ban_creator` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_categories`
--

DROP TABLE IF EXISTS `dev_categories`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_categories` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `cat_name` varchar(80) NOT NULL default 'New Category',
  `disp_position` int(10) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_censoring`
--

DROP TABLE IF EXISTS `dev_censoring`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_censoring` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `search_for` varchar(60) NOT NULL default '',
  `replace_with` varchar(60) NOT NULL default '',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_config`
--

DROP TABLE IF EXISTS `dev_config`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_config` (
  `conf_name` varchar(255) NOT NULL default '',
  `conf_value` text,
  PRIMARY KEY  (`conf_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_forum_perms`
--

DROP TABLE IF EXISTS `dev_forum_perms`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_forum_perms` (
  `group_id` int(10) NOT NULL default '0',
  `forum_id` int(10) NOT NULL default '0',
  `read_forum` tinyint(1) NOT NULL default '1',
  `post_replies` tinyint(1) NOT NULL default '1',
  `post_topics` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`group_id`,`forum_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_forums`
--

DROP TABLE IF EXISTS `dev_forums`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_forums` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `forum_name` varchar(80) NOT NULL default 'New forum',
  `forum_desc` text,
  `redirect_url` varchar(100) default NULL,
  `moderators` text,
  `num_topics` mediumint(8) unsigned NOT NULL default '0',
  `num_posts` mediumint(8) unsigned NOT NULL default '0',
  `last_post` int(10) unsigned default NULL,
  `last_post_id` int(10) unsigned default NULL,
  `last_poster` varchar(200) default NULL,
  `sort_by` tinyint(1) NOT NULL default '0',
  `disp_position` int(10) NOT NULL default '0',
  `cat_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_groups`
--

DROP TABLE IF EXISTS `dev_groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_groups` (
  `g_id` int(10) unsigned NOT NULL auto_increment,
  `g_title` varchar(50) NOT NULL default '',
  `g_user_title` varchar(50) default NULL,
  `g_moderator` tinyint(1) NOT NULL default '0',
  `g_mod_edit_users` tinyint(1) NOT NULL default '0',
  `g_mod_rename_users` tinyint(1) NOT NULL default '0',
  `g_mod_change_passwords` tinyint(1) NOT NULL default '0',
  `g_mod_ban_users` tinyint(1) NOT NULL default '0',
  `g_read_board` tinyint(1) NOT NULL default '1',
  `g_view_users` tinyint(1) NOT NULL default '1',
  `g_post_replies` tinyint(1) NOT NULL default '1',
  `g_post_topics` tinyint(1) NOT NULL default '1',
  `g_edit_posts` tinyint(1) NOT NULL default '1',
  `g_delete_posts` tinyint(1) NOT NULL default '1',
  `g_delete_topics` tinyint(1) NOT NULL default '1',
  `g_set_title` tinyint(1) NOT NULL default '1',
  `g_search` tinyint(1) NOT NULL default '1',
  `g_search_users` tinyint(1) NOT NULL default '1',
  `g_send_email` tinyint(1) NOT NULL default '1',
  `g_post_flood` smallint(6) NOT NULL default '30',
  `g_search_flood` smallint(6) NOT NULL default '30',
  `g_email_flood` smallint(6) NOT NULL default '60',
  PRIMARY KEY  (`g_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_online`
--

DROP TABLE IF EXISTS `dev_online`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_online` (
  `user_id` int(10) unsigned NOT NULL default '1',
  `ident` varchar(200) NOT NULL default '',
  `logged` int(10) unsigned NOT NULL default '0',
  `idle` tinyint(1) NOT NULL default '0',
  `last_post` int(10) unsigned default NULL,
  `last_search` int(10) unsigned default NULL,
  UNIQUE KEY `dev_online_user_id_ident_idx` (`user_id`,`ident`(25)),
  KEY `dev_online_ident_idx` (`ident`(25)),
  KEY `dev_online_logged_idx` (`logged`)
) ENGINE=MEMORY DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_posts`
--

DROP TABLE IF EXISTS `dev_posts`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_posts` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `poster` varchar(200) NOT NULL default '',
  `poster_id` int(10) unsigned NOT NULL default '1',
  `poster_ip` varchar(39) default NULL,
  `poster_email` varchar(80) default NULL,
  `message` text,
  `hide_smilies` tinyint(1) NOT NULL default '0',
  `posted` int(10) unsigned NOT NULL default '0',
  `edited` int(10) unsigned default NULL,
  `edited_by` varchar(200) default NULL,
  `topic_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `dev_posts_topic_id_idx` (`topic_id`),
  KEY `dev_posts_multi_idx` (`poster_id`,`topic_id`)
) ENGINE=MyISAM AUTO_INCREMENT=602 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_ranks`
--

DROP TABLE IF EXISTS `dev_ranks`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_ranks` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `rank` varchar(50) NOT NULL default '',
  `min_posts` mediumint(8) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_reports`
--

DROP TABLE IF EXISTS `dev_reports`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_reports` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `post_id` int(10) unsigned NOT NULL default '0',
  `topic_id` int(10) unsigned NOT NULL default '0',
  `forum_id` int(10) unsigned NOT NULL default '0',
  `reported_by` int(10) unsigned NOT NULL default '0',
  `created` int(10) unsigned NOT NULL default '0',
  `message` text,
  `zapped` int(10) unsigned default NULL,
  `zapped_by` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`),
  KEY `dev_reports_zapped_idx` (`zapped`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_search_cache`
--

DROP TABLE IF EXISTS `dev_search_cache`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_search_cache` (
  `id` int(10) unsigned NOT NULL default '0',
  `ident` varchar(200) NOT NULL default '',
  `search_data` text,
  PRIMARY KEY  (`id`),
  KEY `dev_search_cache_ident_idx` (`ident`(8))
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_search_matches`
--

DROP TABLE IF EXISTS `dev_search_matches`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_search_matches` (
  `post_id` int(10) unsigned NOT NULL default '0',
  `word_id` int(10) unsigned NOT NULL default '0',
  `subject_match` tinyint(1) NOT NULL default '0',
  KEY `dev_search_matches_word_id_idx` (`word_id`),
  KEY `dev_search_matches_post_id_idx` (`post_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_search_words`
--

DROP TABLE IF EXISTS `dev_search_words`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_search_words` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `word` varchar(20) character set utf8 collate utf8_bin NOT NULL default '',
  PRIMARY KEY  (`word`),
  KEY `dev_search_words_id_idx` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7683 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_subscriptions`
--

DROP TABLE IF EXISTS `dev_subscriptions`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_subscriptions` (
  `user_id` int(10) unsigned NOT NULL default '0',
  `topic_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`user_id`,`topic_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_topics`
--

DROP TABLE IF EXISTS `dev_topics`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_topics` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `poster` varchar(200) NOT NULL default '',
  `subject` varchar(255) NOT NULL default '',
  `posted` int(10) unsigned NOT NULL default '0',
  `first_post_id` int(10) unsigned NOT NULL default '0',
  `last_post` int(10) unsigned NOT NULL default '0',
  `last_post_id` int(10) unsigned NOT NULL default '0',
  `last_poster` varchar(200) default NULL,
  `num_views` mediumint(8) unsigned NOT NULL default '0',
  `num_replies` mediumint(8) unsigned NOT NULL default '0',
  `closed` tinyint(1) NOT NULL default '0',
  `sticky` tinyint(1) NOT NULL default '0',
  `moved_to` int(10) unsigned default NULL,
  `forum_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `dev_topics_forum_id_idx` (`forum_id`),
  KEY `dev_topics_moved_to_idx` (`moved_to`),
  KEY `dev_topics_last_post_idx` (`last_post`),
  KEY `dev_topics_first_post_id_idx` (`first_post_id`)
) ENGINE=MyISAM AUTO_INCREMENT=341 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dev_users`
--

DROP TABLE IF EXISTS `dev_users`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dev_users` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `group_id` int(10) unsigned NOT NULL default '3',
  `username` varchar(200) NOT NULL default '',
  `password` varchar(40) NOT NULL default '',
  `email` varchar(80) NOT NULL default '',
  `title` varchar(50) default NULL,
  `realname` varchar(40) default NULL,
  `url` varchar(100) default NULL,
  `jabber` varchar(80) default NULL,
  `icq` varchar(12) default NULL,
  `msn` varchar(80) default NULL,
  `aim` varchar(30) default NULL,
  `yahoo` varchar(30) default NULL,
  `location` varchar(30) default NULL,
  `signature` text,
  `disp_topics` tinyint(3) unsigned default NULL,
  `disp_posts` tinyint(3) unsigned default NULL,
  `email_setting` tinyint(1) NOT NULL default '1',
  `notify_with_post` tinyint(1) NOT NULL default '0',
  `auto_notify` tinyint(1) NOT NULL default '0',
  `show_smilies` tinyint(1) NOT NULL default '1',
  `show_img` tinyint(1) NOT NULL default '1',
  `show_img_sig` tinyint(1) NOT NULL default '1',
  `show_avatars` tinyint(1) NOT NULL default '1',
  `show_sig` tinyint(1) NOT NULL default '1',
  `timezone` float NOT NULL default '0',
  `dst` tinyint(1) NOT NULL default '0',
  `time_format` int(10) unsigned NOT NULL default '0',
  `date_format` int(10) unsigned NOT NULL default '0',
  `language` varchar(25) NOT NULL default 'English',
  `style` varchar(25) NOT NULL default 'Oxygen',
  `num_posts` int(10) unsigned NOT NULL default '0',
  `last_post` int(10) unsigned default NULL,
  `last_search` int(10) unsigned default NULL,
  `last_email_sent` int(10) unsigned default NULL,
  `registered` int(10) unsigned NOT NULL default '0',
  `registration_ip` varchar(39) NOT NULL default '0.0.0.0',
  `last_visit` int(10) unsigned NOT NULL default '0',
  `admin_note` varchar(30) default NULL,
  `activate_string` varchar(80) default NULL,
  `activate_key` varchar(8) default NULL,
  PRIMARY KEY  (`id`),
  KEY `dev_users_registered_idx` (`registered`),
  KEY `dev_users_username_idx` (`username`(8))
) ENGINE=MyISAM AUTO_INCREMENT=425 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `drops`
--

DROP TABLE IF EXISTS `drops`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `drops` (
  `id` int(11) default NULL,
  `item` varchar(255) default NULL,
  `amount` int(11) default NULL,
  `weight` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `dupe_data`
--

DROP TABLE IF EXISTS `dupe_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `dupe_data` (
  `user` varchar(255) NOT NULL,
  `userhash` varchar(255) NOT NULL,
  `string` text NOT NULL,
  `time` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `error_reports`
--

DROP TABLE IF EXISTS `error_reports`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `error_reports` (
  `data` text,
  `email` varchar(255) default NULL,
  `ip` varchar(255) default NULL,
  `unix` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `forum_perms`
--

DROP TABLE IF EXISTS `forum_perms`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `forum_perms` (
  `group_id` int(10) NOT NULL default '0',
  `forum_id` int(10) NOT NULL default '0',
  `read_forum` tinyint(1) NOT NULL default '1',
  `post_replies` tinyint(1) NOT NULL default '1',
  `post_topics` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`group_id`,`forum_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `forums`
--

DROP TABLE IF EXISTS `forums`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `forums` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `forum_name` varchar(80) NOT NULL default 'New forum',
  `forum_desc` text,
  `redirect_url` varchar(100) default NULL,
  `moderators` text,
  `num_topics` mediumint(8) unsigned NOT NULL default '0',
  `num_posts` mediumint(8) unsigned NOT NULL default '0',
  `last_post` int(10) unsigned default NULL,
  `last_post_id` int(10) unsigned default NULL,
  `last_poster` varchar(200) default NULL,
  `sort_by` tinyint(1) NOT NULL default '0',
  `disp_position` int(10) NOT NULL default '0',
  `cat_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `gp_count`
--

DROP TABLE IF EXISTS `gp_count`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `gp_count` (
  `unixtime` varchar(255) default NULL,
  `amount` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `groups` (
  `g_id` int(10) unsigned NOT NULL auto_increment,
  `g_title` varchar(50) NOT NULL default '',
  `g_user_title` varchar(50) default NULL,
  `g_moderator` tinyint(1) NOT NULL default '0',
  `g_mod_edit_users` tinyint(1) NOT NULL default '0',
  `g_mod_rename_users` tinyint(1) NOT NULL default '0',
  `g_mod_change_passwords` tinyint(1) NOT NULL default '0',
  `g_mod_ban_users` tinyint(1) NOT NULL default '0',
  `g_read_board` tinyint(1) NOT NULL default '1',
  `g_view_users` tinyint(1) NOT NULL default '1',
  `g_post_replies` tinyint(1) NOT NULL default '1',
  `g_post_topics` tinyint(1) NOT NULL default '1',
  `g_edit_posts` tinyint(1) NOT NULL default '1',
  `g_delete_posts` tinyint(1) NOT NULL default '1',
  `g_delete_topics` tinyint(1) NOT NULL default '1',
  `g_set_title` tinyint(1) NOT NULL default '1',
  `g_search` tinyint(1) NOT NULL default '1',
  `g_search_users` tinyint(1) NOT NULL default '1',
  `g_send_email` tinyint(1) NOT NULL default '1',
  `g_post_flood` smallint(6) NOT NULL default '30',
  `g_search_flood` smallint(6) NOT NULL default '30',
  `g_email_flood` smallint(6) NOT NULL default '60',
  `g_rep_minus_min` int(10) unsigned default '0',
  `g_rep_plus_min` int(10) unsigned default '0',
  `g_rep_enable` smallint(6) default '1',
  `g_pm` int(10) unsigned NOT NULL default '1',
  `g_pm_limit` int(10) unsigned NOT NULL default '20',
  PRIMARY KEY  (`g_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `invites`
--

DROP TABLE IF EXISTS `invites`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `invites` (
  `owner` varchar(255) NOT NULL default '',
  `code` varchar(255) NOT NULL default '',
  `time` int(10) unsigned NOT NULL default '0',
  `invites` varchar(255) NOT NULL default '1',
  `email` varchar(255) NOT NULL default ''
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `irc_online`
--

DROP TABLE IF EXISTS `irc_online`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `irc_online` (
  `username` varchar(255) NOT NULL,
  `rank` int(1) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `irc_stats`
--

DROP TABLE IF EXISTS `irc_stats`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `irc_stats` (
  `username` varchar(255) NOT NULL,
  `messages` int(10) default NULL,
  `modes` int(10) default NULL,
  `kicks` int(10) default NULL,
  `kicked` int(10) default NULL,
  `lastTimeSpoken` bigint(11) default NULL,
  `joins` int(10) default NULL,
  `parts` int(10) default NULL,
  `randomstring` text,
  `moderatedchan` int(10) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `items` (
  `id` int(11) default NULL,
  `name` varchar(255) default NULL,
  `description` varchar(255) default NULL,
  `price` int(11) default NULL,
  `stackable` varchar(255) default NULL,
  `wieldable` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `logins`
--

DROP TABLE IF EXISTS `logins`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `logins` (
  `id` int(10) default NULL,
  `login_ip` varchar(15) default NULL,
  `time` int(10) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `messages` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `owner` int(10) NOT NULL default '0',
  `subject` varchar(120) NOT NULL default '',
  `message` text,
  `sender` varchar(120) NOT NULL default '',
  `sender_id` int(10) NOT NULL default '0',
  `posted` int(10) NOT NULL default '0',
  `sender_ip` varchar(120) NOT NULL default '0.0.0.0',
  `smileys` tinyint(1) NOT NULL default '1',
  `status` tinyint(1) NOT NULL default '0',
  `showed` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `messages_owner_idx` (`owner`)
) ENGINE=MyISAM AUTO_INCREMENT=23964 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `mobs`
--

DROP TABLE IF EXISTS `mobs`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `mobs` (
  `id` int(11) default NULL,
  `name` varchar(255) default NULL,
  `atk` int(11) default NULL,
  `str` int(11) default NULL,
  `def` int(11) default NULL,
  `atka` varchar(255) default NULL,
  `aggro` varchar(255) default NULL,
  `respawn` int(11) default NULL,
  `hits` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `online`
--

DROP TABLE IF EXISTS `online`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `online` (
  `user_id` int(10) unsigned NOT NULL default '1',
  `ident` varchar(200) NOT NULL default '',
  `logged` int(10) unsigned NOT NULL default '0',
  `idle` tinyint(1) NOT NULL default '0',
  `last_post` int(10) unsigned default NULL,
  `last_search` int(10) unsigned default NULL,
  UNIQUE KEY `online_user_id_ident_idx` (`user_id`,`ident`),
  KEY `online_ident_idx` (`ident`(25)),
  KEY `online_logged_idx` (`logged`)
) ENGINE=MEMORY DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `online_count`
--

DROP TABLE IF EXISTS `online_count`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `online_count` (
  `unixtime` varchar(255) default NULL,
  `online` int(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `posts` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `poster` varchar(200) NOT NULL default '',
  `poster_id` int(10) unsigned NOT NULL default '1',
  `poster_ip` varchar(39) default NULL,
  `poster_email` varchar(80) default NULL,
  `message` text,
  `hide_smilies` tinyint(1) NOT NULL default '0',
  `posted` int(10) unsigned NOT NULL default '0',
  `edited` int(10) unsigned default NULL,
  `edited_by` varchar(200) default NULL,
  `topic_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `posts_topic_id_idx` (`topic_id`),
  KEY `posts_multi_idx` (`poster_id`,`topic_id`),
  KEY `poster_id` (`poster_id`)
) ENGINE=MyISAM AUTO_INCREMENT=417677 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `ranks`
--

DROP TABLE IF EXISTS `ranks`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ranks` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `rank` varchar(50) NOT NULL default '',
  `min_posts` mediumint(8) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reports` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `post_id` int(10) unsigned NOT NULL default '0',
  `topic_id` int(10) unsigned NOT NULL default '0',
  `forum_id` int(10) unsigned NOT NULL default '0',
  `reported_by` int(10) unsigned NOT NULL default '0',
  `created` int(10) unsigned NOT NULL default '0',
  `message` text,
  `zapped` int(10) unsigned default NULL,
  `zapped_by` int(10) unsigned default NULL,
  PRIMARY KEY  (`id`),
  KEY `reports_zapped_idx` (`zapped`)
) ENGINE=MyISAM AUTO_INCREMENT=1218 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `reputation`
--

DROP TABLE IF EXISTS `reputation`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `reputation` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user_id` int(10) unsigned NOT NULL default '0',
  `from_user_id` int(10) unsigned NOT NULL default '0',
  `time` int(10) unsigned NOT NULL default '0',
  `post_id` int(10) unsigned NOT NULL default '0',
  `reason` text NOT NULL,
  `rep_plus` tinyint(1) unsigned NOT NULL default '0',
  `rep_minus` tinyint(1) unsigned NOT NULL default '0',
  `topics_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `rep_post_id_idx` (`post_id`),
  KEY `rep_multi_user_id_idx` (`topics_id`,`from_user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=15940 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_bank`
--

DROP TABLE IF EXISTS `rsca2_bank`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_bank` (
  `user` varchar(255) default NULL,
  `id` int(10) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL default '1',
  `slot` int(5) unsigned NOT NULL,
  KEY `user` (`user`),
  KEY `id` (`id`),
  KEY `amount` (`amount`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_banlog`
--

DROP TABLE IF EXISTS `rsca2_banlog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_banlog` (
  `user` varchar(255) default NULL,
  `staff` varchar(255) default NULL,
  `time` int(10) default NULL,
  KEY `staff` (`staff`),
  KEY `time` (`time`),
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_curstats`
--

DROP TABLE IF EXISTS `rsca2_curstats`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_curstats` (
  `user` varchar(255) NOT NULL,
  `cur_attack` int(5) unsigned NOT NULL default '1',
  `cur_defense` int(5) unsigned NOT NULL default '1',
  `cur_strength` int(5) unsigned NOT NULL default '1',
  `cur_hits` int(5) unsigned NOT NULL default '10',
  `cur_ranged` int(5) unsigned NOT NULL default '1',
  `cur_prayer` int(5) unsigned NOT NULL default '1',
  `cur_magic` int(5) unsigned NOT NULL default '1',
  `cur_cooking` int(5) unsigned NOT NULL default '1',
  `cur_woodcut` int(5) unsigned NOT NULL default '1',
  `cur_fletching` int(5) unsigned NOT NULL default '1',
  `cur_fishing` int(5) unsigned NOT NULL default '1',
  `cur_firemaking` int(5) unsigned NOT NULL default '1',
  `cur_crafting` int(5) unsigned NOT NULL default '1',
  `cur_smithing` int(5) unsigned NOT NULL default '1',
  `cur_mining` int(5) unsigned NOT NULL default '1',
  `cur_herblaw` int(5) unsigned NOT NULL default '1',
  `cur_agility` int(5) unsigned NOT NULL default '1',
  `cur_thieving` int(5) unsigned NOT NULL default '1',
  `id` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  USING BTREE (`id`),
  KEY `user` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=350907 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_experience`
--

DROP TABLE IF EXISTS `rsca2_experience`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_experience` (
  `user` varchar(255) NOT NULL,
  `exp_attack` int(10) unsigned NOT NULL default '0',
  `exp_defense` int(10) unsigned NOT NULL default '0',
  `exp_strength` int(10) unsigned NOT NULL default '0',
  `exp_hits` int(10) unsigned NOT NULL default '1200',
  `exp_ranged` int(10) unsigned NOT NULL default '0',
  `exp_prayer` int(10) unsigned NOT NULL default '0',
  `exp_magic` int(10) unsigned NOT NULL default '0',
  `exp_cooking` int(10) unsigned NOT NULL default '0',
  `exp_woodcut` int(10) unsigned NOT NULL default '0',
  `exp_fletching` int(10) unsigned NOT NULL default '0',
  `exp_fishing` int(10) unsigned NOT NULL default '0',
  `exp_firemaking` int(10) unsigned NOT NULL default '0',
  `exp_crafting` int(10) unsigned NOT NULL default '0',
  `exp_smithing` int(10) unsigned NOT NULL default '0',
  `exp_mining` int(10) unsigned NOT NULL default '0',
  `exp_herblaw` int(10) unsigned NOT NULL default '0',
  `exp_agility` int(10) unsigned NOT NULL default '0',
  `exp_thieving` int(10) unsigned NOT NULL default '0',
  `id` int(10) unsigned NOT NULL auto_increment,
  `oo_attack` int(11) default NULL,
  `oo_defense` int(11) default NULL,
  `oo_strength` int(11) default NULL,
  `oo_ranged` int(11) default NULL,
  `oo_prayer` int(11) default NULL,
  `oo_magic` int(11) default NULL,
  `oo_cooking` int(11) default NULL,
  `oo_woodcut` int(11) default NULL,
  `oo_fletching` int(11) default NULL,
  `oo_fishing` int(11) default NULL,
  `oo_firemaking` int(11) default NULL,
  `oo_crafting` int(11) default NULL,
  `oo_smithing` int(11) default NULL,
  `oo_mining` int(11) default NULL,
  `oo_herblaw` int(11) default NULL,
  `oo_agility` int(11) default NULL,
  `oo_thieving` int(11) default NULL,
  `oo_hits` int(11) default NULL,
  PRIMARY KEY  USING BTREE (`id`),
  KEY `user` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=350907 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_friends`
--

DROP TABLE IF EXISTS `rsca2_friends`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_friends` (
  `user` varchar(255) NOT NULL,
  `friend` varchar(255) NOT NULL,
  KEY `user` (`user`),
  KEY `friend` (`friend`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_ignores`
--

DROP TABLE IF EXISTS `rsca2_ignores`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_ignores` (
  `user` varchar(255) NOT NULL,
  `ignore` varchar(255) NOT NULL,
  KEY `ignore` (`ignore`),
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_invitems`
--

DROP TABLE IF EXISTS `rsca2_invitems`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_invitems` (
  `user` varchar(255) NOT NULL,
  `id` int(10) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL default '1',
  `wielded` tinyint(1) unsigned NOT NULL default '0',
  `slot` int(5) unsigned NOT NULL,
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_kills`
--

DROP TABLE IF EXISTS `rsca2_kills`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_kills` (
  `user` varchar(255) NOT NULL default '',
  `type` tinyint(1) NOT NULL default '0',
  `killed` varchar(45) NOT NULL,
  `time` int(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_logins`
--

DROP TABLE IF EXISTS `rsca2_logins`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_logins` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user` varchar(45) NOT NULL,
  `time` int(5) unsigned NOT NULL,
  `ip` varchar(15) NOT NULL default '0.0.0.0',
  PRIMARY KEY  (`id`),
  KEY `ip` (`ip`)
) ENGINE=MyISAM AUTO_INCREMENT=4254218 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_market`
--

DROP TABLE IF EXISTS `rsca2_market`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_market` (
  `owner` int(10) NOT NULL,
  `item_id` int(10) NOT NULL,
  `amount` int(10) NOT NULL,
  `selling_price` int(10) NOT NULL,
  PRIMARY KEY  (`owner`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_mutelog`
--

DROP TABLE IF EXISTS `rsca2_mutelog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_mutelog` (
  `user` varchar(255) NOT NULL,
  `staff` varchar(255) NOT NULL,
  `time` int(11) NOT NULL,
  `report_id` int(11) NOT NULL,
  `duration` int(10) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_online`
--

DROP TABLE IF EXISTS `rsca2_online`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_online` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `x` varchar(45) NOT NULL,
  `y` varchar(45) NOT NULL,
  `world` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='InnoDB free: 9216 kB';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_players`
--

DROP TABLE IF EXISTS `rsca2_players`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_players` (
  `user` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL default '',
  `pending_deletion` int(11) default '0',
  `group_id` int(10) default '0',
  `owner` int(5) unsigned NOT NULL,
  `owner_username` varchar(255) default NULL,
  `sub_expires` int(5) unsigned default '0',
  `combat` int(10) default '3',
  `skill_total` int(10) default '3',
  `x` int(5) unsigned default '131',
  `y` int(5) unsigned default '508',
  `fatigue` int(10) default '0',
  `combatstyle` tinyint(1) default '0',
  `block_chat` tinyint(1) unsigned default '0',
  `block_private` tinyint(1) unsigned default '0',
  `block_trade` tinyint(1) unsigned default '0',
  `block_duel` tinyint(1) unsigned default '0',
  `cameraauto` tinyint(1) unsigned default '0',
  `onemouse` tinyint(1) unsigned default '0',
  `soundoff` tinyint(1) unsigned default '0',
  `showroof` tinyint(1) default '0',
  `autoscreenshot` tinyint(1) default '0',
  `combatwindow` tinyint(1) default '0',
  `haircolour` int(5) unsigned default '2',
  `topcolour` int(5) unsigned default '8',
  `trousercolour` int(5) unsigned default '14',
  `skincolour` int(5) unsigned default '0',
  `headsprite` int(5) unsigned default '1',
  `bodysprite` int(5) unsigned default '2',
  `male` tinyint(1) unsigned default '1',
  `skulled` int(10) unsigned default '0',
  `pass` varchar(255) NOT NULL,
  `creation_date` int(10) unsigned NOT NULL default '0',
  `creation_ip` varchar(15) NOT NULL default '0.0.0.0',
  `login_date` int(10) unsigned default '0',
  `login_ip` varchar(15) default '0.0.0.0',
  `playermod` tinyint(1) unsigned default '0',
  `loggedin` tinyint(1) default '0',
  `banned` tinyint(1) default '0',
  `muted` int(10) default '0',
  `deaths` int(10) default '0',
  `id` int(10) unsigned NOT NULL auto_increment,
  `online` tinyint(1) unsigned zerofill default '0',
  `world` int(10) default '1',
  `quest_points` int(5) default NULL,
  `eventcd` int(10) unsigned default '0',
  PRIMARY KEY  (`id`),
  KEY `user` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=350907 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 9216 kB';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_quests`
--

DROP TABLE IF EXISTS `rsca2_quests`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_quests` (
  `id` int(11) default NULL,
  `stage` int(11) default NULL,
  `user` varchar(255) default NULL,
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_reports`
--

DROP TABLE IF EXISTS `rsca2_reports`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_reports` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `from` varchar(255) NOT NULL,
  `about` varchar(255) NOT NULL,
  `time` int(10) unsigned NOT NULL,
  `reason` int(5) unsigned NOT NULL,
  `snapshot_from` longtext NOT NULL,
  `snapshot_about` longtext NOT NULL,
  `chatlogs` longtext NOT NULL,
  `from_x` int(10) NOT NULL,
  `from_y` int(10) NOT NULL,
  `about_x` int(10) NOT NULL,
  `about_y` int(10) NOT NULL,
  `zapped` int(10) unsigned default NULL,
  `zapped_by` varchar(255) default NULL,
  `sendToMod` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4335 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_reports_comments`
--

DROP TABLE IF EXISTS `rsca2_reports_comments`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_reports_comments` (
  `id` int(10) NOT NULL auto_increment,
  `report_id` int(10) NOT NULL,
  `poster` int(10) NOT NULL,
  `time` int(10) NOT NULL,
  `text` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4222 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_stat_reduction`
--

DROP TABLE IF EXISTS `rsca2_stat_reduction`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_stat_reduction` (
  `user` int(10) NOT NULL,
  `account` varchar(255) NOT NULL,
  `account_hash` varchar(255) NOT NULL,
  `skill` int(10) NOT NULL,
  `voucher` varchar(255) default NULL,
  `time` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_ticket`
--

DROP TABLE IF EXISTS `rsca2_ticket`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_ticket` (
  `ticket_id` int(10) NOT NULL auto_increment,
  `owner_id` int(10) NOT NULL,
  `assigned_id` int(10) NOT NULL default '0',
  `status` int(11) NOT NULL default '0',
  `last_viewed` int(11) NOT NULL default '0',
  `viewed` int(10) NOT NULL default '1',
  `type_id` smallint(1) NOT NULL,
  `server_id` smallint(1) NOT NULL,
  `ticket_content` text,
  `refChar` varchar(255) NOT NULL,
  `ticket_time` int(10) NOT NULL,
  PRIMARY KEY  (`ticket_id`)
) ENGINE=MyISAM AUTO_INCREMENT=559 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_ticket_type`
--

DROP TABLE IF EXISTS `rsca2_ticket_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_ticket_type` (
  `type_id` smallint(1) NOT NULL auto_increment,
  `type_title` varchar(50) default NULL,
  PRIMARY KEY  (`type_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_tradelog`
--

DROP TABLE IF EXISTS `rsca2_tradelog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_tradelog` (
  `from` varchar(255) default NULL,
  `to` varchar(255) default NULL,
  `time` int(10) default NULL,
  `id` int(10) default NULL,
  `x` int(10) default NULL,
  `y` int(10) default NULL,
  `amount` int(10) default NULL,
  `type` int(5) default NULL,
  KEY `trade_from` (`from`),
  KEY `trade_to` (`to`),
  KEY `tradelog_time` (`time`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca2_worlds`
--

DROP TABLE IF EXISTS `rsca2_worlds`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca2_worlds` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `location` varchar(45) NOT NULL,
  `ip` varchar(45) NOT NULL,
  `port` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca_drops`
--

DROP TABLE IF EXISTS `rsca_drops`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca_drops` (
  `npcdef_id` int(10) NOT NULL,
  `amount` int(10) default NULL,
  `id` int(10) default NULL,
  `weight` int(10) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca_npcdef`
--

DROP TABLE IF EXISTS `rsca_npcdef`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca_npcdef` (
  `id` int(10) NOT NULL,
  `name` varchar(255) default NULL,
  `description` varchar(255) default NULL,
  `command` varchar(255) default NULL,
  `attack` int(10) default NULL,
  `strength` int(10) default NULL,
  `hits` int(10) default NULL,
  `defense` int(10) default NULL,
  `attackable` tinyint(1) default NULL,
  `aggressive` tinyint(1) default NULL,
  `respawnTime` int(10) default NULL,
  `sprites1` int(10) default NULL,
  `sprites2` int(10) default NULL,
  `sprites3` int(10) default NULL,
  `sprites4` int(10) default NULL,
  `sprites5` int(10) default NULL,
  `sprites6` int(10) default NULL,
  `sprites7` int(10) default NULL,
  `sprites8` int(10) default NULL,
  `sprites9` int(10) default NULL,
  `sprites10` int(10) default NULL,
  `sprites11` int(10) default NULL,
  `sprites12` int(10) default NULL,
  `hairColour` int(10) default NULL,
  `topColour` int(10) default NULL,
  `bottomColour` int(10) default NULL,
  `skinColour` int(10) default NULL,
  `camera1` int(10) default NULL,
  `camera2` int(10) default NULL,
  `walkModel` int(10) default NULL,
  `combatModel` int(10) default NULL,
  `combatSprite` int(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rsca_npclocs`
--

DROP TABLE IF EXISTS `rsca_npclocs`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rsca_npclocs` (
  `npc_id` int(10) default NULL,
  `startX` int(10) default NULL,
  `startY` int(10) default NULL,
  `minX` int(10) default NULL,
  `maxX` int(10) default NULL,
  `minY` int(10) default NULL,
  `maxY` int(10) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_bank`
--

DROP TABLE IF EXISTS `rscd_bank`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_bank` (
  `owner` varchar(255) NOT NULL,
  `id` int(10) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL default '1',
  `slot` int(5) unsigned NOT NULL,
  KEY `owner` (`owner`),
  KEY `id` (`id`),
  KEY `amount` (`amount`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_banlog`
--

DROP TABLE IF EXISTS `rscd_banlog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_banlog` (
  `user` varchar(255) default NULL,
  `staff` varchar(255) default NULL,
  `time` int(10) default NULL,
  KEY `user` (`user`),
  KEY `staff` (`staff`),
  KEY `time` (`time`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_curstats`
--

DROP TABLE IF EXISTS `rscd_curstats`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_curstats` (
  `user` varchar(255) NOT NULL,
  `cur_attack` int(5) unsigned NOT NULL default '1',
  `cur_defense` int(5) unsigned NOT NULL default '1',
  `cur_strength` int(5) unsigned NOT NULL default '1',
  `cur_hits` int(5) unsigned NOT NULL default '10',
  `cur_ranged` int(5) unsigned NOT NULL default '1',
  `cur_prayer` int(5) unsigned NOT NULL default '1',
  `cur_magic` int(5) unsigned NOT NULL default '1',
  `cur_cooking` int(5) unsigned NOT NULL default '1',
  `cur_woodcut` int(5) unsigned NOT NULL default '1',
  `cur_fletching` int(5) unsigned NOT NULL default '1',
  `cur_fishing` int(5) unsigned NOT NULL default '1',
  `cur_firemaking` int(5) unsigned NOT NULL default '1',
  `cur_crafting` int(5) unsigned NOT NULL default '1',
  `cur_smithing` int(5) unsigned NOT NULL default '1',
  `cur_mining` int(5) unsigned NOT NULL default '1',
  `cur_herblaw` int(5) unsigned NOT NULL default '1',
  `cur_agility` int(5) unsigned NOT NULL default '1',
  `cur_thieving` int(5) unsigned NOT NULL default '1',
  `id` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  USING BTREE (`id`),
  KEY `user` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=54007 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_experience`
--

DROP TABLE IF EXISTS `rscd_experience`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_experience` (
  `user` varchar(255) NOT NULL,
  `exp_attack` int(10) unsigned NOT NULL default '0',
  `exp_defense` int(10) unsigned NOT NULL default '0',
  `exp_strength` int(10) unsigned NOT NULL default '0',
  `exp_hits` int(10) unsigned NOT NULL default '1200',
  `exp_ranged` int(10) unsigned NOT NULL default '0',
  `exp_prayer` int(10) unsigned NOT NULL default '0',
  `exp_magic` int(10) unsigned NOT NULL default '0',
  `exp_cooking` int(10) unsigned NOT NULL default '0',
  `exp_woodcut` int(10) unsigned NOT NULL default '0',
  `exp_fletching` int(10) unsigned NOT NULL default '0',
  `exp_fishing` int(10) unsigned NOT NULL default '0',
  `exp_firemaking` int(10) unsigned NOT NULL default '0',
  `exp_crafting` int(10) unsigned NOT NULL default '0',
  `exp_smithing` int(10) unsigned NOT NULL default '0',
  `exp_mining` int(10) unsigned NOT NULL default '0',
  `exp_herblaw` int(10) unsigned NOT NULL default '0',
  `exp_agility` int(10) unsigned NOT NULL default '0',
  `exp_thieving` int(10) unsigned NOT NULL default '0',
  `id` int(10) unsigned NOT NULL auto_increment,
  `oo_attack` int(11) default NULL,
  `oo_defense` int(11) default NULL,
  `oo_strength` int(11) default NULL,
  `oo_ranged` int(11) default NULL,
  `oo_prayer` int(11) default NULL,
  `oo_magic` int(11) default NULL,
  `oo_cooking` int(11) default NULL,
  `oo_woodcut` int(11) default NULL,
  `oo_fletching` int(11) default NULL,
  `oo_fishing` int(11) default NULL,
  `oo_firemaking` int(11) default NULL,
  `oo_crafting` int(11) default NULL,
  `oo_smithing` int(11) default NULL,
  `oo_mining` int(11) default NULL,
  `oo_herblaw` int(11) default NULL,
  `oo_agility` int(11) default NULL,
  `oo_thieving` int(11) default NULL,
  PRIMARY KEY  USING BTREE (`id`),
  KEY `user` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=54006 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_friends`
--

DROP TABLE IF EXISTS `rscd_friends`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_friends` (
  `user` varchar(255) NOT NULL,
  `friend` varchar(255) NOT NULL,
  KEY `user` (`user`),
  KEY `friend` (`friend`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_ignores`
--

DROP TABLE IF EXISTS `rscd_ignores`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_ignores` (
  `user` varchar(255) NOT NULL,
  `ignore` varchar(255) NOT NULL,
  KEY `ignore` (`ignore`),
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_invitems`
--

DROP TABLE IF EXISTS `rscd_invitems`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_invitems` (
  `user` varchar(255) NOT NULL,
  `id` int(10) unsigned NOT NULL,
  `amount` int(10) unsigned NOT NULL default '1',
  `wielded` tinyint(1) unsigned NOT NULL default '0',
  `slot` int(5) unsigned NOT NULL,
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_kills`
--

DROP TABLE IF EXISTS `rscd_kills`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_kills` (
  `user` varchar(255) NOT NULL default '',
  `type` tinyint(1) NOT NULL default '0',
  `killed` varchar(45) NOT NULL,
  PRIMARY KEY  (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_logins`
--

DROP TABLE IF EXISTS `rscd_logins`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_logins` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user` varchar(45) NOT NULL,
  `time` int(5) unsigned NOT NULL,
  `ip` varchar(15) NOT NULL default '0.0.0.0',
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3945055 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_online`
--

DROP TABLE IF EXISTS `rscd_online`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_online` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `user` varchar(45) NOT NULL,
  `username` varchar(45) NOT NULL,
  `x` varchar(45) NOT NULL,
  `y` varchar(45) NOT NULL,
  `world` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='InnoDB free: 9216 kB';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_players`
--

DROP TABLE IF EXISTS `rscd_players`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_players` (
  `user` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL default '',
  `group_id` int(10) default '0',
  `owner` int(5) unsigned NOT NULL,
  `owner_username` varchar(255) default NULL,
  `sub_expires` int(5) unsigned default '0',
  `combat` int(10) default '3',
  `skill_total` int(10) default '3',
  `x` int(5) unsigned default '131',
  `y` int(5) unsigned default '508',
  `fatigue` int(10) default '0',
  `combatstyle` tinyint(1) default '0',
  `block_chat` tinyint(1) unsigned default '0',
  `block_private` tinyint(1) unsigned default '0',
  `block_trade` tinyint(1) unsigned default '0',
  `block_duel` tinyint(1) unsigned default '0',
  `cameraauto` tinyint(1) unsigned default '0',
  `onemouse` tinyint(1) unsigned default '0',
  `soundoff` tinyint(1) unsigned default '0',
  `showroof` tinyint(1) default '0',
  `autoscreenshot` tinyint(1) default '0',
  `combatwindow` tinyint(1) default '0',
  `haircolour` int(5) unsigned default '2',
  `topcolour` int(5) unsigned default '8',
  `trousercolour` int(5) unsigned default '14',
  `skincolour` int(5) unsigned default '0',
  `headsprite` int(5) unsigned default '1',
  `bodysprite` int(5) unsigned default '2',
  `male` tinyint(1) unsigned default '1',
  `skulled` int(10) unsigned default '0',
  `pass` varchar(255) NOT NULL,
  `creation_date` int(10) unsigned NOT NULL default '0',
  `creation_ip` varchar(15) NOT NULL default '0.0.0.0',
  `login_date` int(10) unsigned default '0',
  `login_ip` varchar(15) default '0.0.0.0',
  `playermod` tinyint(1) unsigned default '0',
  `loggedin` tinyint(1) default '0',
  `banned` tinyint(1) default '0',
  `muted` tinyint(1) default '0',
  `deaths` int(10) default '0',
  `id` int(10) unsigned NOT NULL auto_increment,
  `online` tinyint(1) unsigned zerofill default '0',
  `world` int(10) default '1',
  `quest_points` int(5) default NULL,
  PRIMARY KEY  (`id`),
  KEY `user` (`user`)
) ENGINE=MyISAM AUTO_INCREMENT=53995 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='InnoDB free: 9216 kB';
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_quests`
--

DROP TABLE IF EXISTS `rscd_quests`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_quests` (
  `id` int(11) default NULL,
  `stage` int(11) default NULL,
  `user` varchar(255) default NULL,
  KEY `user` (`user`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_reports`
--

DROP TABLE IF EXISTS `rscd_reports`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_reports` (
  `from` varchar(255) NOT NULL,
  `about` varchar(255) NOT NULL,
  `time` int(10) unsigned NOT NULL,
  `reason` int(5) unsigned NOT NULL,
  `x` int(5) unsigned NOT NULL,
  `y` int(5) unsigned NOT NULL,
  `status` varchar(255) NOT NULL,
  `id` int(255) unsigned NOT NULL auto_increment,
  `zapped` int(10) unsigned default NULL,
  PRIMARY KEY  USING BTREE (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8779 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_tradelog`
--

DROP TABLE IF EXISTS `rscd_tradelog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_tradelog` (
  `from` varchar(255) default NULL,
  `to` varchar(255) default NULL,
  `time` int(10) default NULL,
  `id` int(10) default NULL,
  `x` int(10) default NULL,
  `y` int(10) default NULL,
  `amount` int(10) default NULL,
  `type` int(5) default NULL,
  KEY `trade_from` (`from`),
  KEY `trade_to` (`to`),
  KEY `time` (`time`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rscd_worlds`
--

DROP TABLE IF EXISTS `rscd_worlds`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rscd_worlds` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `location` varchar(45) NOT NULL,
  `ip` varchar(45) NOT NULL,
  `port` varchar(45) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `search_cache`
--

DROP TABLE IF EXISTS `search_cache`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `search_cache` (
  `id` int(10) unsigned NOT NULL default '0',
  `ident` varchar(200) NOT NULL default '',
  `search_data` mediumtext,
  PRIMARY KEY  (`id`),
  KEY `search_cache_ident_idx` (`ident`(8))
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `search_matches`
--

DROP TABLE IF EXISTS `search_matches`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `search_matches` (
  `post_id` int(10) unsigned NOT NULL default '0',
  `word_id` mediumint(8) unsigned NOT NULL default '0',
  `subject_match` tinyint(1) NOT NULL default '0',
  KEY `search_matches_word_id_idx` (`word_id`),
  KEY `search_matches_post_id_idx` (`post_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `search_words`
--

DROP TABLE IF EXISTS `search_words`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `search_words` (
  `id` mediumint(8) unsigned NOT NULL auto_increment,
  `word` varchar(20) character set utf8 collate utf8_bin NOT NULL default '',
  PRIMARY KEY  (`word`),
  KEY `search_words_id_idx` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=111605 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `site_votes`
--

DROP TABLE IF EXISTS `site_votes`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `site_votes` (
  `id` int(10) default NULL,
  `votetime` int(10) default NULL,
  `timesvoted` int(10) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `site_voting`
--

DROP TABLE IF EXISTS `site_voting`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `site_voting` (
  `user_id` int(11) unsigned NOT NULL,
  `ip` varchar(255) NOT NULL,
  `time` int(11) unsigned NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `stats`
--

DROP TABLE IF EXISTS `stats`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `stats` (
  `date` int(10) unsigned NOT NULL default '0',
  `posts` varchar(255) NOT NULL default '',
  `users` varchar(255) NOT NULL default '',
  `players` varchar(255) NOT NULL default '',
  `active_users` varchar(255) NOT NULL default '',
  `active_players` varchar(255) NOT NULL default '',
  `topics` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `stats_2`
--

DROP TABLE IF EXISTS `stats_2`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `stats_2` (
  `logins` int(5) default NULL,
  `unique_logins` int(5) default NULL,
  `updated` int(11) default NULL,
  `hits` int(5) default NULL,
  `unique_hits` int(5) default NULL,
  `alexa_rank` int(5) default NULL,
  `kills` int(5) default NULL,
  `total_kills` int(5) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `sub_vouchers`
--

DROP TABLE IF EXISTS `sub_vouchers`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `sub_vouchers` (
  `voucher_code` varchar(255) default NULL,
  `months` int(2) default NULL,
  `redeemed` enum('Y','N') default NULL,
  `redeemed_by` int(10) default NULL,
  `google_no` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `subs`
--

DROP TABLE IF EXISTS `subs`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `subs` (
  `user_id` int(10) default NULL,
  `months` int(10) default NULL,
  `google_no` varchar(255) default NULL,
  `redeem` int(10) default NULL,
  `status` int(5) default NULL,
  `order_time` int(10) default NULL,
  `last_time` int(10) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `subscriptions` (
  `user_id` int(10) unsigned NOT NULL default '0',
  `topic_id` int(10) unsigned NOT NULL default '0',
  `forum_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`user_id`,`topic_id`,`forum_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `topics` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `poster` varchar(200) NOT NULL default '',
  `subject` varchar(255) NOT NULL default '',
  `posted` int(10) unsigned NOT NULL default '0',
  `first_post_id` int(10) unsigned NOT NULL default '0',
  `last_post` int(10) unsigned NOT NULL default '0',
  `last_post_id` int(10) unsigned NOT NULL default '0',
  `last_poster` varchar(200) default NULL,
  `num_views` mediumint(8) unsigned NOT NULL default '0',
  `num_replies` mediumint(8) unsigned NOT NULL default '0',
  `closed` tinyint(1) NOT NULL default '0',
  `sticky` tinyint(1) NOT NULL default '0',
  `moved_to` int(10) unsigned default NULL,
  `forum_id` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `topics_forum_id_idx` (`forum_id`),
  KEY `topics_moved_to_idx` (`moved_to`),
  KEY `topics_last_post_idx` (`last_post`),
  KEY `topics_first_post_id_idx` (`first_post_id`)
) ENGINE=MyISAM AUTO_INCREMENT=55226 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `updatelog`
--

DROP TABLE IF EXISTS `updatelog`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `updatelog` (
  `id` int(10) NOT NULL auto_increment,
  `user` int(10) NOT NULL,
  `time` int(10) NOT NULL,
  `message` text NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=191 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL auto_increment,
  `group_id` int(10) unsigned NOT NULL default '4',
  `username` varchar(200) NOT NULL default '',
  `muted` smallint(10) default '0',
  `muted_time` int(10) default '0',
  `password` varchar(40) NOT NULL default '',
  `email` varchar(80) NOT NULL default '',
  `title` varchar(50) default NULL,
  `realname` varchar(40) default NULL,
  `url` varchar(100) default NULL,
  `jabber` varchar(80) default NULL,
  `icq` varchar(12) default NULL,
  `msn` varchar(80) default NULL,
  `aim` varchar(30) default NULL,
  `yahoo` varchar(30) default NULL,
  `location` varchar(30) default NULL,
  `signature` text,
  `disp_topics` tinyint(3) unsigned default NULL,
  `disp_posts` tinyint(3) unsigned default NULL,
  `email_setting` tinyint(1) NOT NULL default '1',
  `notify_with_post` tinyint(1) NOT NULL default '0',
  `auto_notify` tinyint(1) NOT NULL default '0',
  `show_smilies` tinyint(1) NOT NULL default '1',
  `show_img` tinyint(1) NOT NULL default '1',
  `show_img_sig` tinyint(1) NOT NULL default '1',
  `show_avatars` tinyint(1) NOT NULL default '1',
  `show_sig` tinyint(1) NOT NULL default '1',
  `timezone` float NOT NULL default '0',
  `dst` tinyint(1) NOT NULL default '0',
  `date_format` tinyint(1) NOT NULL default '0',
  `time_format` tinyint(1) NOT NULL default '0',
  `language` varchar(25) NOT NULL default 'English',
  `style` varchar(25) NOT NULL default 'Oxygen',
  `num_posts` int(10) unsigned NOT NULL default '0',
  `last_post` int(10) unsigned default NULL,
  `last_search` int(10) unsigned default NULL,
  `last_email_sent` int(10) unsigned default NULL,
  `registered` int(10) unsigned NOT NULL default '0',
  `registration_ip` varchar(39) NOT NULL default '0.0.0.0',
  `last_visit` int(10) unsigned NOT NULL default '0',
  `admin_note` varchar(30) default NULL,
  `activate_string` varchar(80) default NULL,
  `activate_key` varchar(8) default NULL,
  `sub_expires` int(10) unsigned NOT NULL default '0',
  `invites` varchar(10) NOT NULL default '1',
  `country_code` varchar(50) NOT NULL default '',
  `invited_by` varchar(255) NOT NULL,
  `rep_minus` int(11) unsigned default '0',
  `rep_plus` int(11) unsigned default '0',
  `reputation_enable` smallint(6) default '1',
  `reputation_enable_adm` tinyint(1) unsigned default '1',
  `lastvoted` int(10) NOT NULL default '0',
  `msg_count` int(10) NOT NULL default '0',
  `vote_3` int(10) NOT NULL default '0',
  `points` int(10) NOT NULL default '0',
  `bugger_rank` int(10) NOT NULL default '0',
  `bugger_posted` int(10) NOT NULL default '0',
  `bugger_lastupdated` int(10) NOT NULL default '0',
  `original_email` varchar(50) default NULL,
  `email_opt_out` int(1) default '0',
  `change_email` varchar(255) default NULL,
  `next_change_email_date` int(11) default '0',
  `prev_change_email_date` int(11) default '0',
  `prev_change_password_date` int(11) default '0',
  `time_between_voting_banner` int(11) default '0',
  PRIMARY KEY  (`id`),
  KEY `users_registered_idx` (`registered`),
  KEY `registration_ip` (`registration_ip`),
  KEY `users_username_idx` (`username`(25))
) ENGINE=MyISAM AUTO_INCREMENT=138062 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users_tags`
--

DROP TABLE IF EXISTS `users_tags`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `users_tags` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` int(11) NOT NULL,
  `veteran` tinyint(1) NOT NULL default '0',
  `irc` tinyint(1) NOT NULL default '0',
  `bx` tinyint(1) NOT NULL default '0',
  `pmod` tinyint(1) NOT NULL default '0',
  `ps` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`id`),
  KEY `user_id` (`user_id`)
) ENGINE=MyISAM AUTO_INCREMENT=63 DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `votes`
--

DROP TABLE IF EXISTS `votes`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `votes` (
  `user` int(10) NOT NULL,
  `yes` smallint(1) NOT NULL,
  `no` smallint(1) NOT NULL,
  `ip` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `warnings`
--

DROP TABLE IF EXISTS `warnings`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `warnings` (
  `staff` int(10) NOT NULL,
  `player` int(10) NOT NULL,
  `original_post` text NOT NULL,
  `levels` int(10) NOT NULL,
  `duration` int(10) NOT NULL,
  `when` int(10) NOT NULL,
  `link` varchar(255) NOT NULL,
  PRIMARY KEY  (`player`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2010-08-06 10:46:36
