/*
 Navicat Premium Dump SQL

 Source Server         : docker-pgsql
 Source Server Type    : PostgreSQL
 Source Server Version : 170004 (170004)
 Source Host           : localhost:5432
 Source Catalog        : cloud-platform
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 170004 (170004)
 File Encoding         : 65001

 Date: 21/08/2025 22:08:34
*/


-- ----------------------------
-- Table structure for table_name
-- ----------------------------
DROP TABLE IF EXISTS "public"."table_name";
CREATE TABLE "public"."table_name" (
                                       "id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
                                       "del_flag" int4,
                                       "create_by" varchar(32) COLLATE "pg_catalog"."default",
                                       "create_time" timestamp(6),
                                       "update_by" varchar(32) COLLATE "pg_catalog"."default",
                                       "update_time" timestamp(6)
)
;

-- ----------------------------
-- Primary Key structure for table table_name
-- ----------------------------
ALTER TABLE "public"."table_name" ADD CONSTRAINT "table_name_pkey" PRIMARY KEY ("id");