create DATABASE team15;

USE team15;

CREATE TABLE euser(
	id int PRIMARY KEY AUTO_INCREMENT COMMENT '회원번호',
	name VARCHAR(20) NOT NULL COMMENT '회원아이디',
	password VARCHAR(300) NOT NULL COMMENT '비밀번호',
	username VARCHAR(50) NOT NULL COMMENT '회원이름',
	email VARCHAR(100) NOT NULL COMMENT '이메일',
	addr1 VARCHAR(300) COMMENT '기본주소',
	addr2 VARCHAR(300) COMMENT '주소상세',
	postcode varchar(10) COMMENT '우편번호',
	tel VARCHAR(20) COMMENT '전화번호',
	birth DATE DEFAULT CURRENT_TIME COMMENT '생일', 
	regdate DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '가입일',
	lev VARCHAR(20) DEFAULT 'USER' COMMENT '회원등급',
	act VARCHAR(20) DEFAULT 'JOIN' COMMENT '활성화상태', -- JOIN(활성), DSBLD(비활성)
	CONSTRAINT key_name UNIQUE(NAME) -- 아이디 중복 방지
);

-- 더미 데이터
INSERT INTO euser VALUES (DEFAULT, 'kim1', '1234', '김', 'kim@gmail.com', '서울시 금천구', '가산동','11100', '010-1004-1004', '1985-01-11', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO euser VALUES (DEFAULT, 'lee1', '1234', '오', 'oh@gmail.com', '서울시 관악구', '신림동' ,'22222' , '010-7979-2848', '1999-06-28', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO euser VALUES (DEFAULT, 'admin', '1234', '박세훈', 'park@gmail.com', '서울시 관악구', '신림동' ,'22222' , '010-2424-7942', '1990-11-22', DEFAULT, DEFAULT, DEFAULT);

UPDATE euser SET lev='ADMIN' WHERE NAME='admin';
UPDATE euser SET lev='EMP' WHERE NAME='lee';

SELECT * FROM euser;

UPDATE euser SET PASSWORD='$2a$10$N4HrCSDECM/wNWqBGhzDMOrLN1Aw9WRHtmEqxuBK9sWJ3K97Jqau6' WHERE PASSWORD='1234';

COMMIT;



DROP TABLE euser;

DESC euser;


-- 게시판
DROP TABLE board;

CREATE TABLE board(
	no int PRIMARY KEY AUTO_INCREMENT COMMENT '게시글번호',
	id VARCHAR(20) NOT NULL COMMENT '작성자',
	title VARCHAR(100) NOT NULL COMMENT '제목',
	content VARCHAR(1000) COMMENT '글내용',
	cnt INT DEFAULT 0 COMMENT '조회수',
	resdate DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '게시일',
	FOREIGN KEY(id) REFERENCES euser(name) ON DELETE 		
		CASCADE
);


INSERT INTO board values(DEFAULT, "admin", "admin1", "내용1", default, DEFAULT);
INSERT INTO board values(DEFAULT, "admin", "admin2", "내용2", default, DEFAULT);
INSERT INTO board values(DEFAULT, "admin", "admin3", "내용3", default, DEFAULT);
INSERT INTO board values(DEFAULT, "admin", "admin4", "내용4", default, DEFAULT);
INSERT INTO board values(DEFAULT, "admin", "admin5", "내용5", default, DEFAULT);
INSERT INTO board values(DEFAULT, "lee1", "lee1", "내용1", default, DEFAULT);
INSERT INTO board values(DEFAULT, "lee1", "lee2", "내용2", default, DEFAULT);
INSERT INTO board values(DEFAULT, "lee1", "lee3", "내용3", default, DEFAULT);
INSERT INTO board values(DEFAULT, "lee1", "lee4", "내용4", default, DEFAULT);
INSERT INTO board values(DEFAULT, "lee1", "lee5", "내용5", default, DEFAULT);
INSERT INTO board values(DEFAULT, "kim1", "kim1", "내용1", default, DEFAULT);
INSERT INTO board values(DEFAULT, "kim1", "kim2", "내용2", default, DEFAULT);
INSERT INTO board values(DEFAULT, "kim1", "kim3", "내용3", default, DEFAULT);
INSERT INTO board values(DEFAULT, "kim1", "kim4", "내용4", default, DEFAULT);
INSERT INTO board values(DEFAULT, "kim1", "kim5", "내용5", default, DEFAULT);

-- 게시판 댓글
CREATE TABLE board_com(
	no BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '댓글 번호',
	id VARCHAR(20) NOT NULL COMMENT '댓글작성자',
	CONTENT VARCHAR(300) NOT NULL COMMENT '댓글내용',
	resdate DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '게시일',
	par INT COMMENT '게시판 번호',
	FOREIGN KEY(id) REFERENCES euser(name) ON DELETE 		
		CASCADE,
	FOREIGN KEY(par) REFERENCES board(no) ON DELETE 		
		CASCADE
);

-- 중고 상품 테이블
DROP TABLE product;

CREATE TABLE product(
	no INT PRIMARY KEY AUTO_INCREMENT COMMENT '중고상품번호',
	id VARCHAR(20) NOT NULL COMMENT '작성자',
	title VARCHAR(100) COMMENT '제목',
	content VARCHAR(2000) COMMENT '내용',
	cnt INT DEFAULT 0 COMMENT '조회수',
	resdate DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '게시일',
	act VARCHAR(20) DEFAULT 'DEAL' COMMENT '거래상태', -- JOIN(판매중), DSBLD(판매완료)
	addr VARCHAR(50) NOT NULL COMMENT '서울특별시 구로구',
	FOREIGN KEY(id) REFERENCES euser(name) ON DELETE 		
		CASCADE
);

ALTER TABLE product ADD COLUMN addr VARCHAR(50) NOT NULL;
	
-- 자료실 db
CREATE TABLE fileobj (
	no int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	pno INT NOT NULL, -- 상품 테이블 번호
	savefolder VARCHAR(400),
	originfile VARCHAR(400),
	savefile VARCHAR(800),
	filesize LONG,
	uploaddate VARCHAR(100)
);

-- 채팅방
CREATE TABLE chatroom(
	room_id VARCHAR(200) PRIMARY key COMMENT '채팅방번호',
	NAME VARCHAR(200) COMMENT '채팅방이름',
	buyer VARCHAR(20) COMMENT '구매자',
	seller VARCHAR(20) COMMENT '판매자',
	pno int COMMENT '중고상품번호',
	act VARCHAR(20) DEFAULT 'JOIN' COMMENT '활성화상태', -- JOIN(활성), DSBLD(비활성)
	FOREIGN KEY(buyer) REFERENCES euser(name) ON DELETE CASCADE,
	FOREIGN KEY(seller) REFERENCES euser(name) ON DELETE CASCADE,
	FOREIGN KEY(pno) REFERENCES product(no) ON DELETE CASCADE
);

-- 채팅 차단
create table black_list (
	no BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '차단번호',
	sbscr varchar(20) NOT NULL COMMENT '등록자',
	black varchar(20) NOT NULL COMMENT '차단아이디',
	FOREIGN KEY(sbscr) REFERENCES euser(name) ON DELETE CASCADE,
	FOREIGN KEY(black) REFERENCES euser(name) ON DELETE CASCADE,
);

-- 중고상품에 따른 채팅방 활성화 상태
edumoncreate view pro_chat AS select 
	pro.no AS 'pno', -- 중고상품번호
	pro.title 'title', -- 중고상품제목
	chat.room_id 'room_id', -- 채팅방번호
	chat.buyer 'buyer',
	chat.seller 'seller',
	chat.name 'name', -- 채팅방이름
	pro.act 'pact', -- 중고상품활성화상태 JOIN(활성), DSBLD(비활성)
	chat.act 'cact' -- 채팅방활성화상태 JOIN(활성), DSBLD(비활성)
		FROM product pro RIGHT OUTER JOIN chatroom chat
			ON pro.no = chat.pno;

-- 채팅 메세지
CREATE TABLE chatmsg(
	no BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '채팅메세지번호',
	room_id VARCHAR(200) NOT NULL COMMENT '채팅방번호',
	mtype VARCHAR(10) NOT NULL COMMENT '메세지타입',
	sender VARCHAR(20) NOT NULL COMMENT '보내는이',
	message VARCHAR(1000) NOT NULL COMMENT '내용',
	resdate DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작성일',
	FOREIGN KEY(sender) REFERENCES euser(name) ON DELETE CASCADE,
	FOREIGN KEY(room_id) REFERENCES chatroom(room_id) ON DELETE CASCADE
);

-- 학교 정보
CREATE TABLE school(
	eo_code VARCHAR(10),
	eo_name VARCHAR(100),
	sc_code VARCHAR(50),
	sc_name VARCHAR(100)
);

SELECT * FROM school;

SELECT * FROM school WHERE sc_name LIKE CONCAT('%','신구로', '%') limit 1