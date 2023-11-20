DROP DATABASE edumon;
create DATABASE edumon;

USE edumon;

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


-- 중고거래 테이블
-- drop table product;
CREATE TABLE product(
	no INT PRIMARY KEY AUTO_INCREMENT COMMENT '중고상품번호',
	id VARCHAR(20) NOT NULL COMMENT '작성자',
	cate VARCHAR(100) NOT NULL COMMENT '분류',
	title VARCHAR(100) COMMENT '제목',
	content VARCHAR(2000) COMMENT '내용',
	price INT DEFAULT 10 COMMENT '가격',
	cnt INT DEFAULT 0 COMMENT '조회수',
	resdate DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '게시일',
	act VARCHAR(20) DEFAULT 'JOIN' COMMENT '거래상태', -- JOIN(판매중), DSBLD(판매완료)
	addr VARCHAR(50) NOT NULL COMMENT '서울특별시 구로구',
	buyer VARCHAR(20) DEFAULT '' COMMENT '구매자',
	FOREIGN KEY(id) REFERENCES euser(name) ON DELETE CASCADE
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
	FOREIGN KEY(black) REFERENCES euser(name) ON DELETE CASCADE
);

-- 중고상품에 따른 채팅방 활성화 상태
create view pro_chat AS select 
	pro.no as 'pno', -- 중고상품번호
	pro.title as 'title', -- 중고상품제목
	chat.room_id as 'room_id', -- 채팅방번호
	chat.buyer as 'buyer',
	chat.seller as 'seller',
	chat.name as 'name', -- 채팅방이름
	pro.act as 'pact', -- 중고상품활성화상태 JOIN(활성), DSBLD(비활성)
	chat.act as 'cact' -- 채팅방활성화상태 JOIN(활성), DSBLD(비활성)
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
	eocode VARCHAR(10),
	eoname VARCHAR(100),
	sccode VARCHAR(50),
	scname VARCHAR(100)
);

SELECT * FROM school;

SELECT * FROM school WHERE sc_name LIKE CONCAT('%','신구로', '%') limit 1

DROP TABLE notice;
CREATE table notice(
	no INT AUTO_INCREMENT PRIMARY KEY, -- 번호
	title VARCHAR(200) NOT NULL, -- 제목
	content VARCHAR(2000) NOT NULL, -- 내용
	resdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 작성일
	visit int DEFAULT 0 -- 조회수
);
INSERT INTO notice (title, content)
VALUES (
    '오세훈 변호사가 알려주는 사기 예방 Tip!',
    '1. **직거래 우선**<br> 만약 가능하다면 직접 만나서 거래하는 것이 가장 안전합니다.<br> 공공 장소나 안전한 장소에서 거래하고, 가능하다면 동행을 권장합니다.<br>\<br>
    2. **현장 확인**<br> 중고상품을 사기 전에 상품의 실제 상태를 확인하세요.<br> 가능하다면 기능을 테스트하고, <br>상세한 상품 정보를 사진으로 받아보세요.<br>\<br>
    3. **가격 비교**<br> 시장 가격을 조사하고, 유사한 상품들의 가격을 비교해보세요<br>. 지나치게 저렴한 가격에 의심이 가면 신중해져야 합니다.<br>\<br>
    4. **계좌 이체 주의**<br> 선입금을 요구하는 경우 조심하세요.<br> 현금 거래가 어려운 경우에도 안전한 결제 수단을 사용하는 것이 좋습니다.<br>\<br>
    5. **정보 교환 제한**<br> 개인 정보를 최소화하고,<br> 주소나 연락처 등 민감한 정보는 필요 최소한으로만 제공하세요.<br>\<br>
    6. **신뢰성 확인**<br> 판매자 또는 구매자의 신원을 확인하기 위해 인터넷에서 검색해보세요.<br> 온라인 커뮤니티나 리뷰 사이트를 통해 다른 사람들의 경험을 살펴보세요.<br>\<br>
    7. **이메일 주의**<br> 이메일 상에서 링크를 클릭하거나 개인 정보를 제공하는 것은 피하세요.<br> 사기꾼들은 종종 이메일을 통해 가짜 거래를 시도합니다.<br>\<br>
    8. **신속한 거래 종료**<br> 거래가 너무 급하게 이뤄지는 경우 조심하세요.<br> 급한 거래는 사기의 가능성이 높아질 수 있습니다.<br>\<br>
    9. **접촉 가능성**<br> 판매자 또는 구매자와 계속해서 소통이 가능한지 확인하세요.<br> 연락이 두절된다면 사기 의심이 될 수 있습니다.<br>\<br>
    10. **의심스러운 거래 피하기**<br> 만약 어떤 거래가 너무 좋아 보이거나<br> 의심스러운 느낌이 든다면 즉시 거래를 중지하고 신고해야 합니다.<br><br>'
);

--  자주 묻는 질문(faq)
CREATE TABLE faq (
	fno INT AUTO_INCREMENT PRIMARY KEY,
	question VARCHAR(1000) NOT NULL,
	answer VARCHAR(1000) NOT NULL,
	cnt INT DEFAULT 0 NOT NULL
);

INSERT INTO faq VALUES(
DEFAULT,
'ZOOM 관련 화면 캡쳐 차단 메세지 해결방법!',
'최근에 ZOOM(화상 미팅) 프로그램 사용이 많이 늘고 있습니다.<br>
보안프로그램으로 사용하고있는 kollus(동영상 보안프로그램)에서는<br>
zoom을 캡쳐프로그램으로 인식하여 "에러 1002 코드"를 팝업하며 동영상을 중지 시킬 수 있습니다.<br>
그런 경우에는 zoom 프로그램을 종료 후 접속하면 문제없이 접속 가능합니다.',
DEFAULT
);
INSERT INTO faq VALUES(
DEFAULT,
'수강확인증은 어떻게 받을 수 있나요?',
'많은 분들이 청년취업지원, 제대군인 지원, 학교 내 어학지원 등<br>
각종 지원 사업을 통해 수강을 하고 계십니다.<br>
지원 사업이다보니 수강확인증을 필요로 하시는데요!<br>
신청방법은 카카오톡 플친으로 성함과 인강사이트 아이디를 알려주시고<br>
필요한 확인증을 요청하시면 간단한 수강이력 확인 후 발급해드립니다.',
DEFAULT
);
INSERT INTO faq VALUES(
DEFAULT,
'환불 규정에 대해 알려주세요.',
'정규과정 상품 환불정책<br>
① 수강시작 후 7일 미만 & 5강 미만 수강 시에는 100% 환불 가능<br>
② 수강시작 후 7일 이상 또는 5강 이상 수강 시에는 잔여일에 대한 일할 계산 환불 가능<br>
③ 수강시작 후 수강기간 또는 수강한 강의 수가 전체의 50% 이상 경과한 경우 환불 불가<br>
※ 환불금액 = 결제금액 – {정상가*(실제 수강일/전체 수강일)}<br>
    단, 할인적용 등록 시 정상가를 기준으로 환불됨을 숙지해주세요.<br>
※ 전체 수강일은 복습기간을 제외한 수강일입니다.',
DEFAULT
);
INSERT INTO faq VALUES(
DEFAULT,
'수강결제를 했는데 수강시작은 언제부터 가능한가요?',
'수강결제와 함께 수강일 카운트가 바로 시작됩니다!<br>
필히 확인 후 결제 해주시기 바랍니다.<br>
수업의 특성상 교재를 가지고 강의를 들어야 하기 때문에<br>
교재가 없는 경우 교재를 배송 받을 기간동안 수업을 들을 수 있도록<br>
수업 전 공지사항에 샘플교재를 다운 받으실 수 있습니다.',
DEFAULT
);
INSERT INTO faq VALUES (
  DEFAULT,
  '교재를 미리 구매하고 싶으면 어떻게 해야하나요?',
  '교재는 수강 신청 시에 함께 구매할 수 있으며,<br>
  과목마다 필요교재가 다르기 때문에<br>
  수강하려는 과목에 필수교재 확인 후 구매 부탁드립니다.<br><br>
  1.카르페디엠 : 카르페디엠SET(본교재,토익,워크북 3종)<br>
  *카르페디엠SET 구매하면 카르페디엠 1,2,3,4 수강 시 사용가능합니다.<br>
  2.카이로스 : 카이로스SET(본교재,서플리먼트 2종)<br>
  3.독해하이라이트 : 독해하이라이트SET(본교재,리딩북 2종)<br>
  *리딩북의 경우 카르페디엠3,4에서도 사용되는 교재임으로 구매하신 경우<br>
  SET가 아닌 독해본교재 단품 구매해주세요.',
  DEFAULT
);
INSERT INTO faq VALUES(
DEFAULT,
'구독하기를 결제하면 어떤 혜택이 있나요?',
'연간 전체구독권 및 전체구독권 구매하시면<br>
6종 쿠폰이 자동결제 시 마다 지급이 됩니다.<br>
연간결제는 사용 기간은 12개월 안으로 6종 쿠폰 사용 ,<br>
월간결제는 사용 기간은 1개월 안으로 6종 쿠폰 사용이 가능합니다.<br>
<br>
<지급쿠폰의 종류><br>
 - 오디오 북 : 2천 원 쿠폰<br>
 - 중학 프리미엄: 단과과정 1만 원 쿠폰, 프리패스 과정 3만 원 쿠폰<br>
 - 직업: 단과과정 1만 원 쿠폰, 패키지 과정 3만 원 쿠폰<br>
 - 직업(컴퓨터활용능력): 특정 패키지 과정 1만 원 쿠폰',
DEFAULT
);
INSERT INTO faq VALUES(
DEFAULT,
'회원 탈퇴는 어떻게 하나요?',
'	
회원 탈퇴 방법은 아래와 같습니다.<br>
<br>
1. 로그인<br>
2. [마이페이지 > 회원정보수정] 메뉴 선택<br>
3. [회원 탈퇴하기] 선택 후 진행',
DEFAULT
);
INSERT INTO faq VALUES(
DEFAULT,
'모바일웹에서 문제콘텐츠는 어떻게 이용하나요?',
'1. 안드로이드 계열 모바일 이용 환경<br>
<br>
안드로이드 버전 6.0이상에서 최적화되어 있습니다.<br>
구형 안드로이드 핸드폰(갤럭시S4 이상)의 기본 인터넷 브라우저의 경우<br>
문제은행 서비스 이용이 원활하지 않을 수 있습니다.<br>
이 경우 아래와 같은 방법으로 문제를 해결하실 수 있습니다.<br><br>
① 크롬 브라우저 이용<br>
② 삼성 브라우저 앱 업데이트<br><br>
2. iOS 계열 모바일 이용 환경<br><br>
iOS 버전 9.3이상에서 안정적으로 서비스를 이용하실 수 있습니다.<br>
설정>소프트웨어 업데이트 메뉴로 들어가<br>
"신규 소프트웨어 업데이트"를 통해 버전 업데이트를 하시기 바랍니다.<br>
<br>
*수식입력을 제외한 모든 기능은 낮은 버전에서도 동일하게 이용 가능합니다.',
DEFAULT
);

SELECT * FROM faq;
-- 자유게시판 (free)
CREATE TABLE free(
	no int PRIMARY KEY AUTO_INCREMENT, -- 번호
	id VARCHAR(20) NOT NULL, -- 작성자
	title VARCHAR(100) NOT NULL,
	content VARCHAR(1000),
	visit INT DEFAULT 0,
	resdate DATETIME DEFAULT CURRENT_TIMESTAMP
);
-- 자유게시판 댓글
CREATE TABLE freeComment(
	no INT PRIMARY KEY AUTO_INCREMENT, -- 댓글 번호
	id VARCHAR(20) NOT NULL, -- 작성자
	content VARCHAR(300) NOT NULL, -- 내용
	resdate DATETIME DEFAULT CURRENT_TIMESTAMP,
	par INT -- 게시판 글 번호
);

-- 출석체크
CREATE TABLE attendance (
   ano INT NOT NULL PRIMARY KEY AUTO_INCREMENT,		-- 출석체크 번호
   id VARCHAR(20),											-- 회원 아이디
   attend DATE DEFAULT CURRENT_DATE,					-- 출석날짜
	givaway VARCHAR(50));									-- 경품