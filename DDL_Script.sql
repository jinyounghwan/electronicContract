create table electronic_contract.contract_template
(
    template_seq   int auto_increment comment '템플릿 seq'
        primary key,
    template_title varchar(50)                 not null comment '템플릿 명',
    template_type  varchar(10)                 not null comment '템플릿(계약서) 유형',
    use_yn         char      default 'Y'       not null comment '사용 여부',
    memo           varchar(100)                null comment '메모',
    created_by     varchar(20)                 not null comment '생성자',
    created_at     timestamp default sysdate() not null comment '생성일',
    updated_by     varchar(20)                 null comment '수정자',
    updated_at     timestamp                   null comment '수정일'
)
    comment '계약서 템플릿';

create table electronic_contract.contract_template_details
(
    template_details_seq int auto_increment comment '템플릿 상세 seq'
        primary key,
    template_seq         int                         not null comment '템플릿 seq',
    contract_title_en    varchar(50)                 not null comment '계약서 제목 (영어)',
    contract_title_hu    varchar(50)                 not null comment '계약서 제목 (헝가리어)',
    contents_en          longtext                    not null comment '계약서 내용 (영어)',
    contents_hu          longtext                    not null comment '계약서 내용 (헝가리어)',
    contract_info_en     varchar(500)                null comment '계약 정보 (영어)',
    contract_info_hu     varchar(500)                null comment '계약 정보 (헝가리어)',
    employer_info_en     varchar(500)                null comment '회사 서명 정보 (영어)',
    employer_info_hu     varchar(500)                null comment '회사 서명 정보 (헝가리어)',
    employee_info_en     varchar(500)                null comment '임직원 서명 정보 (영어)',
    employee_info_hu     varchar(500)                null comment '임직원 서명 정보 (헝가리어)',
    created_by           varchar(20)                 not null comment '생성자',
    created_at           timestamp default sysdate() not null comment '생성일',
    updated_by           varchar(20)                 null comment '수정자',
    updated_at           timestamp                   null comment '수정일',
    constraint contract_template_details_contract_template_template_seq_fk
        foreign key (template_seq) references electronic_contract.contract_template (template_seq)
)
    comment '계약서 템플릿 상세';

create table electronic_contract.departments
(
    dept_seq   int auto_increment comment '부서 seq'
        primary key,
    dept_code  varchar(10)                 not null comment '부서 코드',
    name       varchar(50)                 not null comment '부서 명',
    use_yn     char      default 'Y'       not null comment '사용 여부',
    created_by varchar(20)                 not null comment '생성자',
    created_at timestamp default sysdate() not null comment '생성일',
    updated_by varchar(20)                 null comment '수정자',
    updated_at timestamp                   null comment '수정일',
    constraint departments_pk
        unique (dept_code)
)
    comment '부서 정보';

create table electronic_contract.file_integration
(
    file_seq      bigint auto_increment comment '파일 seq'
        primary key,
    original_name varchar(100)                not null comment '원본 파일 명',
    name          varchar(100)                not null comment '파일 명',
    file_no       smallint  default 1         not null comment '파일 번호',
    extension     varchar(5)                  not null comment '확장자',
    storage_path  varchar(100)                not null comment '저장 경로',
    size          int                         not null comment '파일 크기',
    del_yn        char      default 'N'       not null comment '삭제 여부',
    created_by    varchar(20)                 not null comment '생성자',
    created_at    timestamp default sysdate() not null comment '생성일'
)
    comment '통합 파일 관리';

create table electronic_contract.ghr_account_info
(
    emp_no             int           not null comment '사번'
        primary key,
    name               varchar(50)   not null comment '이름',
    team               varchar(100)  null comment '팀',
    `group`            varchar(100)  null comment '그룹',
    part               varchar(100)  null comment '파트',
    sub_part           varchar(100)  null comment '하위 파트',
    org_level_code     varchar(100)  null comment '조직 레벨 코드',
    department_code    varchar(100)  null comment '부서 코드',
    department         varchar(100)  null comment '부서 명',
    level_code         char(2)       null comment '레벨 코드',
    level              varchar(50)   null comment '레벨 명',
    pay_band           char(2)       null comment '페이밴드',
    j_band             char(2)       null,
    job                varchar(100)  null comment '업무',
    empl_type          varchar(50)   null comment '임직원 유형',
    s_empl_type        varchar(50)   null comment '임직원 고용 상태',
    duty               varchar(50)   null comment '직무',
    cost_center        char(9)       null comment '코스트 센터 코드',
    cost_center_name   varchar(100)  null comment '코스트 센터 명',
    hire_date          timestamp     null comment '채용일',
    company_service    smallint      null comment '근무 기간 (월)',
    probationer        char          null comment '수습 직원 여부',
    probation_end_date timestamp     null comment '수습 종료일',
    resignation_date   timestamp     null comment '퇴사일',
    shift              varchar(50)   null comment '교대 근무 코드',
    nationality        varchar(50)   null comment '국적',
    shift_name         varchar(50)   null comment '교대 근무 명',
    birth_place        varchar(50)   null comment '출생지',
    wage_type          char          null comment '급여 형태 (M - month or H - hour)',
    salary_no          int default 0 null comment '급여액(숫자)'
)
    comment 'GHR_계정 정보';

create table electronic_contract.individual_contract_template_details
(
    individual_template_seq bigint auto_increment comment '계약서 별 템플릿 seq'
        primary key,
    template_details_seq    int                         not null comment '템플릿 상세 seq',
    contract_no             bigint                      not null comment '계약서 번호',
    emp_no                  varchar(20)                 not null comment '사번',
    contract_title_en       varchar(50)                 not null comment '영문 계약서 제목',
    contract_title_hu       varchar(50)                 not null comment '헝가리어 계약서 제목',
    contents_en             longtext                    not null comment '계약서 내용 (영어)',
    contents_hu             longtext                    not null comment '계약서 내용 (헝가리어)',
    contract_info_en        varchar(500)                null comment '계약정보 (영어)',
    contract_info_hu        varchar(500)                null comment '계약정보 (헝가리어)',
    employer_info_en        varchar(500)                null comment '회사 서명 정보 (영어)',
    employer_info_hu        varchar(500)                null comment '회사 서명 정보 (헝가리어)',
    employee_info_en        varchar(500)                null comment '임직원 서명 정보 (영어)',
    employee_info_hu        varchar(500)                null comment '임직원 서명 정보 (헝가리어)',
    created_by              varchar(20)                 not null comment '생성자',
    created_at              timestamp default sysdate() not null comment '생성일',
    updated_by              varchar(20)                 null comment '수정자',
    updated_at              timestamp                   null comment '수정일',
    constraint individual_contract_template_details_seq_fk
        foreign key (template_details_seq) references electronic_contract.contract_template_details (template_details_seq)
)
    comment '계약서 별 계약서 템플릿 상세 정보';

create table electronic_contract.menu
(
    menu_seq   int auto_increment comment '메뉴 seq'
        primary key,
    menu_code  char(10)                    not null comment '메뉴 코드',
    name       varchar(50)                 not null comment '메뉴 명',
    ord        smallint                    null comment '메뉴 순서',
    path       varchar(100)                null comment '메뉴 접속경로',
    display_yn char      default 'Y'       not null comment '노출 여부',
    use_yn     char      default 'Y'       not null comment '사용 여부',
    del_yn     char      default 'N'       not null comment '삭제 여부',
    created_by varchar(20)                 not null comment '생성자',
    created_at timestamp default sysdate() not null comment '생성일',
    updated_by varchar(20)                 null comment '수정자',
    updated_at timestamp                   null comment '수정일'
)
    comment '메뉴 정보';

create table electronic_contract.menu_authorization
(
    auth_seq   bigint auto_increment comment '권한 seq'
        primary key,
    menu_seq   int                         not null comment '메뉴 seq',
    auth_c     char      default 'N'       not null comment '생성 권한',
    auth_r     char      default 'N'       not null comment '조회 권한',
    auth_u     char      default 'N'       not null comment '수정 권한',
    auth_d     char      default 'N'       not null comment '삭제 권한',
    auth_f     char      default 'N'       not null comment '파일 권한',
    grant_to   varchar(20)                 not null comment '권한 대상자',
    display_yn char      default 'Y'       not null comment '노출 여부',
    created_by varchar(20)                 not null comment '생성자',
    created_at timestamp default sysdate() not null comment '생성일',
    updated_by varchar(20)                 null comment '수정자',
    updated_at timestamp                   null comment '수정일',
    constraint Menu_Authorization_menu_menu_seq_fk
        foreign key (menu_seq) references electronic_contract.menu (menu_seq)
)
    comment '메뉴 권한 정보';

create table electronic_contract.process_log
(
    log_seq      bigint auto_increment comment '로그 seq'
        primary key,
    log_type     varchar(50)                 not null comment '로그 유형',
    contract_no  bigint                      null comment '계약 번호',
    process_step char(8)                     null comment '처리 단계',
    ip_address   varchar(50)                 not null comment '접근자 IP 주소',
    mac_address  varchar(50)                 null comment '접근자 MAC 주소',
    created_by   varchar(20)                 not null comment '생성자',
    created_at   timestamp default sysdate() not null comment '생성일'
)
    comment '처리 내역 로그';

create table electronic_contract.users
(
    emp_no       varchar(20)                 not null comment '사번'
        primary key,
    admin_id     varchar(20)                 null comment '관리자용 ID',
    dept_code    varchar(10)                 not null comment '부서 코드',
    user_pw      varchar(200)                not null comment '유저 password',
    name         varchar(100)                not null comment '이름',
    account_type char(7)                     not null comment '계정 유형',
    position     char(6)                     null comment '직위',
    email        varchar(50)                 null comment '이메일 주소',
    phone        varchar(10)                 not null comment '연락처',
    use_yn       char      default 'Y'       not null comment '사용 여부',
    employed_at  timestamp                   not null comment '채용일',
    resigned_at  timestamp                   null comment '퇴사일',
    created_by   varchar(20)                 not null comment '생성자',
    created_at   timestamp default sysdate() not null comment '생성일',
    updated_by   varchar(20)                 null comment '수정자',
    updated_at   timestamp                   null comment '수정일',
    constraint users_departments_dept_code_fk
        foreign key (dept_code) references electronic_contract.departments (dept_code)
)
    comment '유저 정보';

create table electronic_contract.contracts
(
    contract_no       bigint auto_increment comment '계약 번호'
        primary key,
    emp_no            varchar(20)                 not null comment '사번',
    template_seq      int                         not null comment '템플릿 seq',
    dept_code         char(10)                    not null comment '부서 코드',
    doc_status        char(8)                     not null comment '계약서 생성/배부 상태',
    process_status    char(8)                     not null comment '계약 진행 상태',
    name              varchar(50)                 not null comment '이름',
    hire_date_hu      varchar(20)                 not null comment '입사일 (헝가리어 - yyyy.mm.dd)',
    hire_date_en      varchar(20)                 not null comment '입사일 (영어 - dd/mm/yyyy)',
    contract_date_hu  varchar(20)                 not null comment '계약일 (헝가리어 - yyyy.mm.dd)',
    contract_date_en  varchar(20)                 not null comment '계약일 (영어 - dd/mm/yyyy)',
    job_title_hu      varchar(50)                 null comment '직무 (헝가리어)',
    job_title_en      varchar(50)                 null comment '직무 (영어)',
    salary_no         int                         null comment '급여 (숫자)',
    salary_hu         varchar(100)                null comment '급여 (문자 - 헝가리어)',
    salary_en         varchar(100)                null comment '급여 (문자 - 영어)',
    wage_type_hu      varchar(10)                 null comment '급여형태 (헝가리어)',
    wage_type_en      varchar(10)                 null comment '급여형태 (영어)',
    signature_data_no varchar(10)                 null comment '서명 파일 번호',
    sign_date         timestamp                   null comment '서명일',
    validation        char                        not null comment '계약 유효 여부',
    agree_yn          char      default 'N'       not null comment '전자계약서 동의 여부',
    del_yn            char      default 'N'       not null comment '삭제 여부',
    created_by        varchar(20)                 not null comment '생성자',
    created_at        timestamp default sysdate() not null comment '생성일',
    updated_by        varchar(20)                 null comment '수정자',
    updated_at        timestamp                   null comment '수정일',
    assigned_by       varchar(20)                 null comment '배부자',
    assigned_at       timestamp                   null comment '배부일',
    reject_reason     varchar(50)                 null comment '거절 사유',
    constraint contracts_contract_template_template_seq_fk
        foreign key (template_seq) references electronic_contract.contract_template (template_seq),
    constraint contracts_users_emp_no_fk
        foreign key (emp_no) references electronic_contract.users (emp_no)
)
    comment '계약서';

