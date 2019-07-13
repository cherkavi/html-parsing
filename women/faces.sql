SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `whores`
--

-- --------------------------------------------------------
--
-- Table structure for table `human`
--

CREATE TABLE IF NOT EXISTS `human` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'unique id',
  `url` varchar(255) NOT NULL COMMENT 'url to proposition',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'name ( usually not real )',
  `session_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31 ;

-- --------------------------------------------------------
--
-- Table structure for table `human_description`
--

CREATE TABLE IF NOT EXISTS `human_description` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `human_id` int(10) unsigned NOT NULL,
  `parameter` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'body parameter',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'description of body parameter',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=181 ;


-- --------------------------------------------------------
--
-- Table structure for table `human_images`
--

CREATE TABLE IF NOT EXISTS `human_images` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `human_id` int(11) NOT NULL COMMENT 'FK human',
  `image_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------
--
-- Table structure for table `human_phones`
--

CREATE TABLE IF NOT EXISTS `human_phones` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `human_id` int(10) unsigned NOT NULL COMMENT 'FK to human',
  `phone` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=31 ;


-- --------------------------------------------------------
--
-- Table structure for table `human_prices`
--

CREATE TABLE IF NOT EXISTS `human_prices` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `human_id` int(10) unsigned NOT NULL,
  `service_time` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '1/2/3 hour, all night',
  `service_price` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'amount of grn',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=61 ;

-- --------------------------------------------------------

--
-- Table structure for table `human_services`
--

CREATE TABLE IF NOT EXISTS `human_services` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `human_id` int(11) NOT NULL COMMENT 'FK to human',
  `service` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'service which can be provided',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=616 ;

-- --------------------------------------------------------

--
-- Table structure for table `parser_session`
--

CREATE TABLE IF NOT EXISTS `parser_session` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `session_id` int(10) unsigned DEFAULT NULL,
  `status` varchar(20) NOT NULL,
  `statustime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
