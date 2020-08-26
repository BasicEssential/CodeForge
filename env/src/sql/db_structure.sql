CREATE TABLE IF NOT EXISTS USER_REPO_MAPPING(
    ID          SERIAL          UNIQUE PRIMARY KEY,
    USER_ID     INT             NOT NULL,
    LOGIN       VARCHAR(100)    NOT NULL,
    REPO_ID     INT             NOT NULL,
    REPO_NAME   VARCHAR(100)    NOT NULL
);

CREATE TABLE IF NOT EXISTS EVENT(
    ID          SERIAL          UNIQUE PRIMARY KEY,
    EVENT_TYPE  VARCHAR(50)     NOT NULL,
    USER_ID     INT             NOT NULL,
    LOGIN       VARCHAR(100)    NOT NULL,
    REPO_ID     INT             NOT NULL,
    REPO_NAME   VARCHAR(100)    NOT NULL,
    CREATED_AT  TIMESTAMP       NOT NULL
);

CREATE TABLE IF NOT EXISTS USERS(
    ID                  INT             UNIQUE PRIMARY KEY,
    LOGIN               VARCHAR(100)    UNIQUE NOT NULL,
    NAME                VARCHAR(150)    NOT NULL,
    COMPANY             VARCHAR(150)    ,
    BIO                 TEXT            ,
    LOCATION            VARCHAR(50)     ,
    GIT_VENDOR          VARCHAR(10)     ,
    AVATAR_URL          TEXT            ,
    URL                 TEXT            ,
    FOLLOWERS_URL       TEXT            ,
    FOLLOWING_URL       TEXT            ,
    SUBSCRIPTIONS_URL   TEXT            ,
    ORGANIZATIONS_URL   TEXT            ,
    REPOS_URL           TEXT            ,
    EVENTS_URL          TEXT            ,
    CREATED_AT          TIMESTAMP
);

CREATE TABLE IF NOT EXISTS USER_STATISTIC(
    ID                  SERIAL          UNIQUE PRIMARY KEY,
    USER_ID             INT             NOT NULL,
    LOGIN               VARCHAR(100)    NOT NULL,
    PUBLIC_REPOS        INTEGER         ,
    PUBLIC_GISTS        INTEGER         ,
    FOLLOWERS           INTEGER         ,
    FOLLOWING           INTEGER         ,
    COMMITS             INTEGER         ,
    MERGES              INTEGER         ,
    MERGE_REQUESTS      INTEGER         ,
    OPEN_ISSUES         INTEGER         ,
    OPEN_ISSUES_CNT     INTEGER         ,
    UPDATED_AT          TIMESTAMP
);

CREATE TABLE IF NOT EXISTS REPOSITORY(
    ID                  INT             UNIQUE PRIMARY KEY,
    NAME                VARCHAR(200)    UNIQUE NOT NULL,
    PRIVATE             BOOLEAN         ,
    OWNER               VARCHAR(100)    NOT NULL,
    GIT_VENDOR          VARCHAR(10)     ,
    PARENT_ID           INT             ,
    PARENT_NAME         VARCHAR(200)    ,
    DESCRIPTION         TEXT            ,
    FORK                BOOLEAN         ,
    HTML_URL            TEXT            ,
    URL                 TEXT            ,
    EVENTS_URL          TEXT            ,
    ISSUES_URL          TEXT            ,
    ORGANIZATIONS_URL   TEXT            ,
    ASSIGNEES_URL       TEXT            ,
    LANGUAGE_URL        TEXT            ,
    CREATED_AT          TIMESTAMP
);

CREATE TABLE IF NOT EXISTS REPOSITRY_STATISTIC(
     ID                  SERIAL          UNIQUE PRIMARY KEY,
     REPO_ID             INT             NOT NULL,
     REPONAME            VARCHAR(200)    NOT NULL,
     FORKS               INTEGER         ,
     FORKS_OPEN          INTEGER         ,
     OPEN_ISSUES         INTEGER         ,
     WATCHERS            INTEGER         ,
     WATCHERS_COUNT      INTEGER         ,
     UPDATED_AT          TIMESTAMP
);
