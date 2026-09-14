-- Bloomtoget 프로젝트 PostgreSQL 초기화 스크립트
-- 이 스크립트는 컨테이너 최초 실행 시 자동으로 실행됩니다.

-- 1. 확장 기능 설치
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";          -- UUID 생성 함수
CREATE EXTENSION IF NOT EXISTS "pg_trgm";            -- 유사도 검색 (Full-text search)
CREATE EXTENSION IF NOT EXISTS "btree_gin";          -- GIN 인덱스 최적화
CREATE EXTENSION IF NOT EXISTS "btree_gist";         -- GiST 인덱스 최적화

-- 2. 데이터베이스 기본 설정
ALTER DATABASE bloomtoget SET timezone TO 'Asia/Seoul';

-- 3. 개발용 스키마 생성 (선택적)
-- CREATE SCHEMA IF NOT EXISTS app;

-- 4. 로깅 설정 (개발 환경)
ALTER SYSTEM SET log_statement = 'all';              -- 모든 SQL 로깅
ALTER SYSTEM SET log_duration = 'on';                -- 쿼리 실행 시간 로깅
ALTER SYSTEM SET log_min_duration_statement = 100;   -- 100ms 이상 쿼리 로깅

-- 5. 성능 최적화 (개발 환경)
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET work_mem = '16MB';
ALTER SYSTEM SET maintenance_work_mem = '128MB';

-- 설정 적용을 위한 리로드
SELECT pg_reload_conf();

-- 6. 초기화 완료 메시지
DO $$
BEGIN
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'Bloomtoget Database Initialization Complete!';
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'Database: bloomtoget';
    RAISE NOTICE 'Extensions: uuid-ossp, pg_trgm, btree_gin, btree_gist';
    RAISE NOTICE 'Timezone: Asia/Seoul';
    RAISE NOTICE '==============================================';
END $$;

-- 7. 개발용 샘플 데이터 (선택적, 필요시 주석 해제)
/*
-- 샘플 사용자 추가
INSERT INTO users (email, password, name, created_at, updated_at) VALUES
    ('dev@bloomtoget.com', '$2a$10$DUMMY_HASH', 'Developer', NOW(), NOW()),
    ('test@bloomtoget.com', '$2a$10$DUMMY_HASH', 'Tester', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;
*/
