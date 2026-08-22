-- ══════════════════════════════
-- EasyFit DB 초기 설정 스크립트 (정리본)
-- 사용법: MySQL Workbench에서 이 파일을 열고 전체 실행(Ctrl+Shift+Enter)
-- 기존 easyfit_db가 있다면 통째로 지우고 새로 만듭니다.
-- ══════════════════════════════

DROP DATABASE IF EXISTS easyfit_db;
CREATE DATABASE easyfit_db DEFAULT CHARACTER SET utf8mb4;
USE easyfit_db;

-- ── 테이블 생성 ──

CREATE TABLE app_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    email       VARCHAR(255),
    nickname    VARCHAR(100),
    provider    VARCHAR(20) NOT NULL,
    provider_id VARCHAR(100) NOT NULL,
    UNIQUE KEY uk_provider_providerid (provider, provider_id)
);

CREATE TABLE body_part_category (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    code       VARCHAR(20) NOT NULL UNIQUE,
    label      VARCHAR(20) NOT NULL,
    sort_order INT NOT NULL
);

CREATE TABLE workout (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    body_part     VARCHAR(20)  NOT NULL,
    title         VARCHAR(100) NOT NULL,
    img_url       VARCHAR(500),
    tags          VARCHAR(100),
    pose          TEXT,
    target_muscle VARCHAR(200),
    caution       TEXT,
    video_url     VARCHAR(500)
);

CREATE TABLE calendar_record (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    record_date DATE NOT NULL,
    stamped     BOOLEAN NOT NULL DEFAULT FALSE,
    memo        TEXT,
    body_parts  VARCHAR(100),
    CONSTRAINT fk_calendar_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    UNIQUE KEY uk_user_date (user_id, record_date)
);

-- ── 부위 탭 데이터 ──
INSERT INTO body_part_category (code, label, sort_order) VALUES
('chest', '🫁 가슴', 1),
('back', '🦾 등', 2),
('leg', '🦵 하체', 3),
('shoulder', '🏋️ 어깨', 4),
('arm', '💪 팔', 5),
('core', '🔥 코어', 6);

-- ── 운동 가이드 데이터 (18개) ──
INSERT INTO workout (body_part, title, img_url, tags, pose, target_muscle, caution, video_url) VALUES
-- 가슴
('chest', '푸시업 (Push-up)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Push-Up.gif',
 '입문,맨몸',
 '어깨너비보다 약간 넓게 손을 짚고, 몸을 일직선으로 유지하세요. 팔꿈치를 45° 각도로 구부리며 천천히 내려갑니다.',
 '대흉근 (중앙/하부), 삼두근, 전면 삼각근',
 '허리가 꺾이지 않도록 코어에 힘을 주세요. 팔꿈치가 너무 벌어지면 어깨에 부담이 됩니다.',
 'https://www.youtube.com/results?search_query=푸시업+정확한+자세'),

('chest', '덤벨 플라이 (Dumbbell Fly)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Fly.gif',
 '중급,덤벨',
 '벤치에 누워 덤벨을 가슴 위로 들고, 팔꿈치를 약간 구부린 채 양쪽으로 넓게 펼칩니다.',
 '대흉근 (외측/전체), 전면 삼각근',
 '덤벨을 너무 무겁게 사용하면 어깨 관절 부상 위험이 있어요. 가슴이 충분히 늘어나는 것을 느끼세요.',
 'https://www.youtube.com/results?search_query=덤벨+플라이+정확한+자세'),

('chest', '케이블 크로스오버',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Cable-Crossover.gif',
 '중급,기구',
 '케이블 기구를 어깨 높이로 설정하고 양손에 잡은 후, 가슴 앞으로 교차하듯 당겨옵니다.',
 '대흉근 (내측 집중), 전면 삼각근',
 '반동을 사용하지 않고 천천히 수축과 이완을 느끼세요. 상체가 앞으로 너무 기울지 않도록 합니다.',
 'https://www.youtube.com/results?search_query=케이블+크로스오버+자세'),

-- 등
('back', '랫 풀다운 (Lat Pulldown)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Lat-Pulldown.gif',
 '입문,기구',
 '바를 어깨너비보다 넓게 잡고, 상체를 약간 뒤로 기울이며 바를 쇄골 방향으로 당깁니다.',
 '광배근, 이두근, 후면 삼각근',
 '바를 뒷목으로 당기지 마세요. 등이 아닌 팔로 당기는 느낌이 들면 그립을 조정하세요.',
 'https://www.youtube.com/results?search_query=랫+풀다운+자세'),

('back', '덤벨 로우 (Dumbbell Row)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Row.gif',
 '중급,덤벨',
 '벤치나 고정된 곳에 한 손과 무릎을 짚고, 반대쪽 손으로 덤벨을 쥔 채 등 근육을 이용해 골반 방향으로 당깁니다.',
 '광배근, 승모근, 척추기립근',
 '허리가 굽지 않게 곧게 펴고, 팔이 아닌 등으로 당긴다는 느낌에 집중하세요.',
 'https://www.youtube.com/results?search_query=덤벨+로우+자세'),

('back', '시티드 케이블 로우',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Seated-Cable-Row.gif',
 '입문,기구',
 '케이블 기구 앞에 앉아 V바를 잡고 상체를 곧게 세운 채 배쪽으로 당깁니다.',
 '광배근 (하부), 중간 등 근육',
 '당길 때 상체가 과도하게 뒤로 젖혀지지 않도록 하세요. 등의 수축을 느끼는 것이 중요합니다.',
 'https://www.youtube.com/results?search_query=시티드+케이블+로우+자세'),

-- 하체
('leg', '핵 스쿼트 (Hack Squat)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Sled-Hack-Squat.gif',
 '중급,기구',
 '머신에 등을 대고 어깨 패드에 밀착한 후, 발판에 발을 어깨너비로 놓습니다. 무릎이 90도가 될 때까지 내린 뒤 밀어 올립니다.',
 '대퇴사두근(허벅지 앞), 둔근',
 '발을 발판 너무 아래에 두면 무릎에 무리가 갈 수 있습니다. 무릎이 안쪽으로 모이지 않게 주의하세요.',
 'https://www.youtube.com/results?search_query=핵+스쿼트+자세'),

('leg', '런지 (Lunge)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Lunge.gif',
 '입문,덤벨',
 '한 발을 크게 앞으로 내딛고, 앞 무릎이 90°가 되도록 내려갑니다. 뒷 무릎은 바닥에 가볍게 닿도록.',
 '대퇴사두근, 둔근, 햄스트링',
 '앞 무릎이 발끝을 너무 많이 넘어가지 않도록 하세요. 상체는 곧게 유지합니다.',
 'https://www.youtube.com/results?search_query=런지+자세'),

('leg', '레그 프레스 (Leg Press)',
 'https://fitnessprogramer.com/wp-content/uploads/2015/11/Leg-Press.gif',
 '중급,기구',
 '발판에 발을 어깨너비로 놓고, 무릎이 90° 될 때까지 내린 후 밀어올립니다.',
 '대퇴사두근, 둔근 (발 위치에 따라 다름)',
 '무릎을 완전히 펴서 관절을 잠그지 마세요. 허리가 시트에서 떨어지지 않도록 합니다.',
 'https://www.youtube.com/results?search_query=레그+프레스+자세'),

-- 어깨
('shoulder', '숄더 프레스 (Shoulder Press)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Shoulder-Press.gif',
 '중급,덤벨',
 '덤벨을 귀 높이에서 잡고, 팔꿈치가 약간 앞쪽을 향하게 한 채 머리 위로 밀어올립니다.',
 '전면/측면 삼각근, 삼두근',
 '허리가 과도하게 아치를 그리지 않도록 코어에 힘을 주세요. 무게를 너무 무겁게 사용하지 마세요.',
 'https://www.youtube.com/results?search_query=덤벨+숄더+프레스+자세'),

('shoulder', '레터럴 레이즈 (Lateral Raise)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Lateral-Raise.gif',
 '입문,덤벨',
 '팔꿈치를 약간 구부린 채 덤벨을 양옆으로 어깨 높이까지 올립니다.',
 '측면 삼각근',
 '반동을 사용하지 마세요. 올릴 때 새끼손가락이 약간 위를 향하면 측면 삼각근이 더 자극됩니다.',
 'https://www.youtube.com/results?search_query=사이드+레터럴+레이즈+자세'),

('shoulder', '페이스풀 (Face Pull)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Face-Pull.gif',
 '입문,기구',
 '케이블을 얼굴 높이로 설정하고, 양손으로 로프를 잡아 얼굴 방향으로 당깁니다.',
 '후면 삼각근, 회전근개, 승모근',
 '어깨 건강에 매우 좋은 운동입니다. 너무 무거운 무게보다 정확한 자세로 수행하세요.',
 'https://www.youtube.com/results?search_query=페이스풀+자세'),

-- 팔
('arm', '바이셉 컬 (Bicep Curl)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Dumbbell-Curl.gif',
 '입문,덤벨',
 '덤벨을 아래팔로만 들어올리고, 팔꿈치는 옆구리에 고정합니다.',
 '이두근 (단두/장두)',
 '팔꿈치가 앞으로 나오거나 상체 반동을 쓰지 마세요. 천천히 내리는 것도 중요합니다.',
 'https://www.youtube.com/results?search_query=덤벨+컬+자세'),

('arm', '트라이셉 딥스 (Tricep Dips)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Triceps-Dips.gif',
 '입문,맨몸',
 '의자나 벤치를 등지고 손을 짚은 뒤, 팔꿈치를 구부리며 몸을 내립니다.',
 '삼두근 (전체)',
 '어깨가 위로 솟지 않도록 하세요. 내려갈 때 어깨 통증이 있으면 범위를 줄이세요.',
 'https://www.youtube.com/results?search_query=삼두+딥스+자세'),

('arm', '해머 컬 (Hammer Curl)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/02/Hammer-Curl.gif',
 '입문,덤벨',
 '덤벨을 세워서(엄지가 위) 들고, 바이셉 컬과 동일하게 수행합니다.',
 '이두근, 상완근, 전완근',
 '바이셉 컬에 비해 전완 부위 자극이 강합니다. 천천히 수행하세요.',
 'https://www.youtube.com/results?search_query=해머+컬+자세'),

-- 코어
('core', '백 익스텐션 (Back Extension)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/06/Weighted-Back-Extension.gif',
 '중급,기구',
 '기구에 골반을 대고 엎드린 후, 상체를 천천히 숙였다가 척추기립근과 둔근의 힘으로 일직선이 될 때까지 들어 올립니다.',
 '척추기립근(코어 후면), 둔근, 햄스트링',
 '상체를 들어 올릴 때 허리를 과도하게 뒤로 꺾으면 부상 위험이 있습니다. 몸이 일자가 되는 지점까지만 올리세요.',
 'https://www.youtube.com/results?search_query=백+익스텐션+자세'),

('core', '크런치 (Crunch)',
 'https://fitnessprogramer.com/wp-content/uploads/2015/11/Crunch.gif',
 '입문,맨몸',
 '무릎을 세우고 누운 채, 상체를 복근으로 말아올립니다. 목이 아닌 복부로 올라가는 것을 느끼세요.',
 '복직근 (상부)',
 '목에 힘을 주면 경추에 무리가 가요. 손을 귀 옆에 가볍게 대고 목을 당기지 마세요.',
 'https://www.youtube.com/results?search_query=크런치+자세'),

('core', '푸시업 플러스 (코어/전거근)',
 'https://fitnessprogramer.com/wp-content/uploads/2021/06/Push-Up-Plus.gif',
 '중급,맨몸',
 '푸시업 자세에서 팔을 곧게 편 상태로 날개뼈만 모았다가, 바닥을 강하게 밀어내며 등을 둥글게 맙니다.',
 '전거근, 코어 전체, 어깨 안정화',
 '팔꿈치가 구부러지지 않게 주의하고, 코어의 긴장을 계속 유지한 채 견갑골(날개뼈)만 움직이세요.',
 'https://www.youtube.com/results?search_query=푸시업+플러스+자세');

-- ── 확인용 ──
SHOW TABLES;
DESC app_user;
DESC calendar_record;
DESC workout;
DESC body_part_category;
SELECT body_part, COUNT(*) FROM workout GROUP BY body_part;