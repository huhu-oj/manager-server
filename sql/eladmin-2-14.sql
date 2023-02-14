create table code_column_config
(
    column_id       bigint auto_increment comment 'ID'
        primary key,
    table_name      varchar(255) null,
    column_name     varchar(255) null,
    column_type     varchar(255) null,
    dict_name       varchar(255) null,
    extra           varchar(255) null,
    form_show       bit          null,
    form_type       varchar(255) null,
    key_type        varchar(255) null,
    list_show       bit          null,
    not_null        bit          null,
    query_type      varchar(255) null,
    remark          varchar(255) null,
    date_annotation varchar(255) null
)
    comment '代码生成字段信息存储' charset = utf8mb3;

create index idx_table_name
    on code_column_config (table_name);

create table code_gen_config
(
    config_id   bigint auto_increment comment 'ID'
        primary key,
    table_name  varchar(255) null comment '表名',
    author      varchar(255) null comment '作者',
    cover       bit          null comment '是否覆盖',
    module_name varchar(255) null comment '模块名称',
    pack        varchar(255) null comment '至于哪个包下',
    path        varchar(255) null comment '前端代码生成的路径',
    api_path    varchar(255) null comment '前端Api文件路径',
    prefix      varchar(255) null comment '表前缀',
    api_alias   varchar(255) null comment '接口名称'
)
    comment '代码生成器配置' charset = utf8mb3;

create index idx_table_name
    on code_gen_config (table_name(100));

create table mnt_app
(
    app_id        bigint auto_increment comment 'ID'
        primary key,
    name          varchar(255)  null comment '应用名称',
    upload_path   varchar(255)  null comment '上传目录',
    deploy_path   varchar(255)  null comment '部署路径',
    backup_path   varchar(255)  null comment '备份路径',
    port          int           null comment '应用端口',
    start_script  varchar(4000) null comment '启动脚本',
    deploy_script varchar(4000) null comment '部署脚本',
    create_by     varchar(255)  null comment '创建者',
    update_by     varchar(255)  null comment '更新者',
    create_time   datetime      null comment '创建日期',
    update_time   datetime      null comment '更新时间'
)
    comment '应用管理' charset = utf8mb3;

create table mnt_database
(
    db_id       varchar(50)  not null comment 'ID'
        primary key,
    name        varchar(255) not null comment '名称',
    jdbc_url    varchar(255) not null comment 'jdbc连接',
    user_name   varchar(255) not null comment '账号',
    pwd         varchar(255) not null comment '密码',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间'
)
    comment '数据库管理' charset = utf8mb3;

create table mnt_deploy
(
    deploy_id   bigint auto_increment comment 'ID'
        primary key,
    app_id      bigint       null comment '应用编号',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null,
    update_time datetime     null comment '更新时间'
)
    comment '部署管理' charset = utf8mb3;

create index FK6sy157pseoxx4fmcqr1vnvvhy
    on mnt_deploy (app_id);

create table mnt_deploy_history
(
    history_id  varchar(50)  not null comment 'ID'
        primary key,
    app_name    varchar(255) not null comment '应用名称',
    deploy_date datetime     not null comment '部署日期',
    deploy_user varchar(50)  not null comment '部署用户',
    ip          varchar(20)  not null comment '服务器IP',
    deploy_id   bigint       null comment '部署编号'
)
    comment '部署历史管理' charset = utf8mb3;

create table mnt_deploy_server
(
    deploy_id bigint not null comment '部署ID',
    server_id bigint not null comment '服务ID',
    primary key (deploy_id, server_id)
)
    comment '应用与服务器关联' charset = utf8mb3;

create index FKeaaha7jew9a02b3bk9ghols53
    on mnt_deploy_server (server_id);

create table mnt_server
(
    server_id   bigint auto_increment comment 'ID'
        primary key,
    account     varchar(50)  null comment '账号',
    ip          varchar(20)  null comment 'IP地址',
    name        varchar(100) null comment '名称',
    password    varchar(100) null comment '密码',
    port        int          null comment '端口',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建时间',
    update_time datetime     null comment '更新时间'
)
    comment '服务器管理' charset = utf8mb3;

create index idx_ip
    on mnt_server (ip);

create table oj_answer_record
(
    id                bigint auto_increment
        primary key,
    problem_id        bigint                             not null comment '所属题目',
    user_id           bigint                             not null comment '所属用户',
    code              text                               not null comment '代码',
    execute_time      bigint   default -1                null comment '执行时间',
    language_id       bigint                             not null comment '所属语言',
    log               text                               null comment '日志',
    error             text                               null comment '错误日志',
    pass_num          int      default 0                 not null comment '通过数',
    not_pass_num      int      default 0                 not null comment '未通过数',
    execute_result_id bigint                             not null comment '执行结果',
    note              text                               not null comment '备注',
    create_time       datetime default CURRENT_TIMESTAMP not null,
    update_time       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '做题记录';

create table oj_answer_record_execute_result
(
    execute_result_id int    not null comment '执行结果id',
    answer_answer_id  bigint not null comment '做题记录id',
    primary key (execute_result_id, answer_answer_id)
)
    comment '做题记录执行结果映射';

create table oj_clazz
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(50)                        not null comment '名称',
    grade_id    bigint                             not null comment '年级',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '班级';

create table oj_examination_paper
(
    id               bigint auto_increment
        primary key,
    name             varchar(50)                        not null comment '名称',
    description      text                               null comment '描述',
    create_time      datetime default CURRENT_TIMESTAMP not null,
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    description_html text                               null
)
    comment '试卷';

create table oj_examination_paper_problem
(
    examination_paper_id bigint not null comment '试卷id',
    problem_id           bigint not null comment '题目id',
    problem_sort         int    not null comment '排序',
    primary key (examination_paper_id, problem_id)
)
    comment '试卷题目映射';

create table oj_execute_result
(
    id    int auto_increment
        primary key,
    name  varchar(50) not null comment '名称',
    color varchar(50) null comment '颜色'
)
    comment '执行结果';

create table oj_grade
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(50)                        not null comment '名称',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '年级';

create table oj_hint
(
    id               bigint auto_increment
        primary key,
    description      text                               not null comment '描述',
    description_html text                               null comment '渲染文本',
    problem_id       bigint                             not null comment '所属题目',
    create_time      datetime default CURRENT_TIMESTAMP not null,
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '提示';

create table oj_judge_machine
(
    id               bigint auto_increment
        primary key,
    name             varchar(50)                          not null comment '名称',
    username         varchar(50)                          not null comment '主机账号',
    password         varchar(50)                          not null comment '密码',
    url              varchar(50)                          not null comment '主机地址',
    enabled          tinyint(1) default 1                 not null comment '是否启动',
    create_time      datetime   default CURRENT_TIMESTAMP not null,
    update_time      datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    support_language varchar(255)                         null
)
    comment '判题机';

create table oj_knowledge
(
    id               bigint auto_increment
        primary key,
    name             varchar(50)                        not null comment '知识点名称',
    description      text                               not null comment '描述',
    description_html text                               null comment '渲染文本',
    create_time      datetime default CURRENT_TIMESTAMP not null,
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '知识点';

create table oj_label
(
    id          bigint auto_increment
        primary key,
    name        varchar(50)                        not null comment '标签名',
    url         varchar(50)                        null comment '链接',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '标签';

create table oj_language
(
    id                bigint auto_increment
        primary key,
    name              varchar(50)                        not null comment '语言名称',
    compile_statement text                               not null comment '编译语句',
    create_time       datetime default CURRENT_TIMESTAMP not null,
    update_time       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '编程语言';

create table oj_problem
(
    id               bigint auto_increment
        primary key,
    title            varchar(50)                        not null comment '标题',
    description      text                               not null comment '描述',
    description_html text                               null comment '渲染文本',
    create_time      datetime default CURRENT_TIMESTAMP not null,
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '题目';

create table oj_problem_knowledge
(
    problem_id   bigint        not null comment '题目id',
    knowledge_id bigint        not null comment '知识点id',
    weight       int default 1 not null comment '权重',
    primary key (problem_id, knowledge_id)
)
    comment '问题知识点映射';

create table oj_problem_label
(
    problem_id bigint not null comment '题目id',
    label_id   bigint not null comment '标签id',
    primary key (problem_id, label_id)
)
    comment '问题标签映射';

create table oj_right
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(50)                        not null comment '名称',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '权限';

create table oj_role
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(50)                        not null comment '名称',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '角色';

create table oj_role_right
(
    right_id bigint not null comment '权限id',
    role_id  bigint not null comment '角色id',
    primary key (right_id, role_id)
)
    comment '角色权限映射';

create table oj_solution
(
    id               bigint auto_increment
        primary key,
    title            varchar(50)                        not null comment '标题',
    description      text                               not null comment '描述',
    description_html text                               null comment '渲染文本',
    problem_id       bigint                             not null comment '所属题目',
    create_time      datetime default CURRENT_TIMESTAMP not null,
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '知识点';

create table oj_solution_label
(
    solution_id bigint not null comment '题解id',
    label_id    bigint not null comment '标签id',
    primary key (solution_id, label_id)
)
    comment '题解标签映射';

create table oj_standard_io
(
    id          bigint auto_increment
        primary key,
    input       text                               not null comment '输入',
    output      text                               not null comment '输出',
    problem_id  bigint                             not null comment '所属题目',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '标准输入输出';

create table oj_test
(
    id                   bigint auto_increment
        primary key,
    title                varchar(50)                          not null comment '标题',
    description          text                                 not null comment '备注',
    examination_paper_id bigint                               not null comment '试卷id',
    start_time           datetime                             null comment '开始时间',
    end_time             datetime                             null comment '结束时间',
    enabled              tinyint(1) default 0                 null comment '是否启用',
    create_time          datetime   default CURRENT_TIMESTAMP not null,
    update_time          datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '测验';

create table oj_user
(
    id          bigint auto_increment comment 'id'
        primary key,
    name        varchar(50)                        not null comment '名称',
    password    varchar(255)                       not null comment '密码',
    class_id    int                                null comment '班级',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
)
    comment '用户';

create table oj_user_role
(
    user_id bigint not null comment '用户id',
    role_id bigint not null comment '角色id',
    primary key (user_id, role_id)
)
    comment '用户角色映射';

create table sys_dept
(
    dept_id     bigint auto_increment comment 'ID'
        primary key,
    pid         bigint          null comment '上级部门',
    sub_count   int default 0   null comment '子部门数目',
    name        varchar(255)    not null comment '名称',
    dept_sort   int default 999 null comment '排序',
    enabled     bit             not null comment '状态',
    create_by   varchar(255)    null comment '创建者',
    update_by   varchar(255)    null comment '更新者',
    create_time datetime        null comment '创建日期',
    update_time datetime        null comment '更新时间'
)
    comment '部门' charset = utf8mb3;

create index inx_enabled
    on sys_dept (enabled);

create index inx_pid
    on sys_dept (pid);

create table sys_dict
(
    dict_id     bigint auto_increment comment 'ID'
        primary key,
    name        varchar(255) not null comment '字典名称',
    description varchar(255) null comment '描述',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间'
)
    comment '数据字典' charset = utf8mb3;

create table sys_dict_detail
(
    detail_id   bigint auto_increment comment 'ID'
        primary key,
    dict_id     bigint       null comment '字典id',
    label       varchar(255) not null comment '字典标签',
    value       varchar(255) not null comment '字典值',
    dict_sort   int          null comment '排序',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间'
)
    comment '数据字典详情' charset = utf8mb3;

create index FK5tpkputc6d9nboxojdbgnpmyb
    on sys_dict_detail (dict_id);

create table sys_job
(
    job_id      bigint auto_increment comment 'ID'
        primary key,
    name        varchar(255) not null comment '岗位名称',
    enabled     bit          not null comment '岗位状态',
    job_sort    int          null comment '排序',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间',
    constraint uniq_name
        unique (name)
)
    comment '岗位' charset = utf8mb3;

create index inx_enabled
    on sys_job (enabled);

create table sys_log
(
    log_id           bigint auto_increment comment 'ID'
        primary key,
    description      varchar(255) null,
    log_type         varchar(255) null,
    method           varchar(255) null,
    params           text         null,
    request_ip       varchar(255) null,
    time             bigint       null,
    username         varchar(255) null,
    address          varchar(255) null,
    browser          varchar(255) null,
    exception_detail text         null,
    create_time      datetime     null
)
    comment '系统日志' charset = utf8mb3;

create index inx_log_type
    on sys_log (log_type);

create index log_create_time_index
    on sys_log (create_time);

create table sys_menu
(
    menu_id     bigint auto_increment comment 'ID'
        primary key,
    pid         bigint           null comment '上级菜单ID',
    sub_count   int default 0    null comment '子菜单数目',
    type        int              null comment '菜单类型',
    title       varchar(255)     null comment '菜单标题',
    name        varchar(255)     null comment '组件名称',
    component   varchar(255)     null comment '组件',
    menu_sort   int              null comment '排序',
    icon        varchar(255)     null comment '图标',
    path        varchar(255)     null comment '链接地址',
    i_frame     bit              null comment '是否外链',
    cache       bit default b'0' null comment '缓存',
    hidden      bit default b'0' null comment '隐藏',
    permission  varchar(255)     null comment '权限',
    create_by   varchar(255)     null comment '创建者',
    update_by   varchar(255)     null comment '更新者',
    create_time datetime         null comment '创建日期',
    update_time datetime         null comment '更新时间',
    constraint uniq_name
        unique (name),
    constraint uniq_title
        unique (title)
)
    comment '系统菜单' charset = utf8mb3;

create index inx_pid
    on sys_menu (pid);

create table sys_quartz_job
(
    job_id              bigint auto_increment comment 'ID'
        primary key,
    bean_name           varchar(255) null comment 'Spring Bean名称',
    cron_expression     varchar(255) null comment 'cron 表达式',
    is_pause            bit          null comment '状态：1暂停、0启用',
    job_name            varchar(255) null comment '任务名称',
    method_name         varchar(255) null comment '方法名称',
    params              varchar(255) null comment '参数',
    description         varchar(255) null comment '备注',
    person_in_charge    varchar(100) null comment '负责人',
    email               varchar(100) null comment '报警邮箱',
    sub_task            varchar(100) null comment '子任务ID',
    pause_after_failure bit          null comment '任务失败后是否暂停',
    create_by           varchar(255) null comment '创建者',
    update_by           varchar(255) null comment '更新者',
    create_time         datetime     null comment '创建日期',
    update_time         datetime     null comment '更新时间'
)
    comment '定时任务' charset = utf8mb3;

create index inx_is_pause
    on sys_quartz_job (is_pause);

create table sys_quartz_log
(
    log_id           bigint auto_increment comment 'ID'
        primary key,
    bean_name        varchar(255) null,
    create_time      datetime     null,
    cron_expression  varchar(255) null,
    exception_detail text         null,
    is_success       bit          null,
    job_name         varchar(255) null,
    method_name      varchar(255) null,
    params           varchar(255) null,
    time             bigint       null
)
    comment '定时任务日志' charset = utf8mb3;

create table sys_role
(
    role_id     bigint auto_increment comment 'ID'
        primary key,
    name        varchar(255) not null comment '名称',
    level       int          null comment '角色级别',
    description varchar(255) null comment '描述',
    data_scope  varchar(255) null comment '数据权限',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间',
    constraint uniq_name
        unique (name)
)
    comment '角色表' charset = utf8mb3;

create index role_name_index
    on sys_role (name);

create table sys_roles_depts
(
    role_id bigint not null,
    dept_id bigint not null,
    primary key (role_id, dept_id)
)
    comment '角色部门关联' charset = utf8mb3;

create index FK7qg6itn5ajdoa9h9o78v9ksur
    on sys_roles_depts (dept_id);

create table sys_roles_menus
(
    menu_id bigint not null comment '菜单ID',
    role_id bigint not null comment '角色ID',
    primary key (menu_id, role_id)
)
    comment '角色菜单关联' charset = utf8mb3;

create index FKcngg2qadojhi3a651a5adkvbq
    on sys_roles_menus (role_id);

create table sys_user
(
    user_id        bigint auto_increment comment 'ID'
        primary key,
    dept_id        bigint           null comment '部门名称',
    username       varchar(255)     null comment '用户名',
    nick_name      varchar(255)     null comment '昵称',
    gender         varchar(2)       null comment '性别',
    phone          varchar(255)     null comment '手机号码',
    email          varchar(255)     null comment '邮箱',
    avatar_name    varchar(255)     null comment '头像地址',
    avatar_path    varchar(255)     null comment '头像真实路径',
    password       varchar(255)     null comment '密码',
    is_admin       bit default b'0' null comment '是否为admin账号',
    enabled        bigint           null comment '状态：1启用、0禁用',
    create_by      varchar(255)     null comment '创建者',
    update_by      varchar(255)     null comment '更新者',
    pwd_reset_time datetime         null comment '修改密码的时间',
    create_time    datetime         null comment '创建日期',
    update_time    datetime         null comment '更新时间',
    constraint UK_kpubos9gc2cvtkb0thktkbkes
        unique (email),
    constraint uniq_email
        unique (email),
    constraint uniq_username
        unique (username),
    constraint username
        unique (username)
)
    comment '系统用户' charset = utf8mb3;

create index FK5rwmryny6jthaaxkogownknqp
    on sys_user (dept_id);

create index FKpq2dhypk2qgt68nauh2by22jb
    on sys_user (avatar_name);

create index inx_enabled
    on sys_user (enabled);

create table sys_users_jobs
(
    user_id bigint not null comment '用户ID',
    job_id  bigint not null comment '岗位ID',
    primary key (user_id, job_id)
)
    charset = utf8mb3;

create table sys_users_roles
(
    user_id bigint not null comment '用户ID',
    role_id bigint not null comment '角色ID',
    primary key (user_id, role_id)
)
    comment '用户角色关联' charset = utf8mb3;

create index FKq4eq273l04bpu4efj0jd0jb98
    on sys_users_roles (role_id);

create table tool_alipay_config
(
    config_id               bigint       not null comment 'ID'
        primary key,
    app_id                  varchar(255) null comment '应用ID',
    charset                 varchar(255) null comment '编码',
    format                  varchar(255) null comment '类型 固定格式json',
    gateway_url             varchar(255) null comment '网关地址',
    notify_url              varchar(255) null comment '异步回调',
    private_key             text         null comment '私钥',
    public_key              text         null comment '公钥',
    return_url              varchar(255) null comment '回调地址',
    sign_type               varchar(255) null comment '签名方式',
    sys_service_provider_id varchar(255) null comment '商户号'
)
    comment '支付宝配置类' charset = utf8mb3;

create table tool_email_config
(
    config_id bigint       not null comment 'ID'
        primary key,
    from_user varchar(255) null comment '收件人',
    host      varchar(255) null comment '邮件服务器SMTP地址',
    pass      varchar(255) null comment '密码',
    port      varchar(255) null comment '端口',
    user      varchar(255) null comment '发件者用户名'
)
    comment '邮箱配置' charset = utf8mb3;

create table tool_local_storage
(
    storage_id  bigint auto_increment comment 'ID'
        primary key,
    real_name   varchar(255) null comment '文件真实的名称',
    name        varchar(255) null comment '文件名',
    suffix      varchar(255) null comment '后缀',
    path        varchar(255) null comment '路径',
    type        varchar(255) null comment '类型',
    size        varchar(100) null comment '大小',
    create_by   varchar(255) null comment '创建者',
    update_by   varchar(255) null comment '更新者',
    create_time datetime     null comment '创建日期',
    update_time datetime     null comment '更新时间'
)
    comment '本地存储' charset = utf8mb3;

create table tool_qiniu_config
(
    config_id  bigint       not null comment 'ID'
        primary key,
    access_key text         null comment 'accessKey',
    bucket     varchar(255) null comment 'Bucket 识别符',
    host       varchar(255) not null comment '外链域名',
    secret_key text         null comment 'secretKey',
    type       varchar(255) null comment '空间类型',
    zone       varchar(255) null comment '机房'
)
    comment '七牛云配置' charset = utf8mb3;

create table tool_qiniu_content
(
    content_id  bigint auto_increment comment 'ID'
        primary key,
    bucket      varchar(255) null comment 'Bucket 识别符',
    name        varchar(255) null comment '文件名称',
    size        varchar(255) null comment '文件大小',
    type        varchar(255) null comment '文件类型：私有或公开',
    url         varchar(255) null comment '文件url',
    suffix      varchar(255) null comment '文件后缀',
    update_time datetime     null comment '上传或同步的时间',
    constraint uniq_name
        unique (name)
)
    comment '七牛云文件存储' charset = utf8mb3;


