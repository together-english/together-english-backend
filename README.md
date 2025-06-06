# English Together Backend

**English Together**는 영어 모임을 통해 영어 실력을 향상시키고자 하는 사람들을 위한 플랫폼입니다. 지역과 영어 실력에 맞춰 사용자를 연결하여, 함께 영어를 공부할 수 있는 환경을 제공합니다.

<br><br>

## 🌐 **서비스 링크**

- **웹 서비스:** [English Together](https://www.english-together.shop/)
- **API 스웨거 문서:** [Swagger UI](https://api.english-together.shop/swagger-ui/index.html)

<br><br>

## 🔧 **기술 스택**

- **프레임워크:** Spring Boot 3.3.0
- **ORM:** JPA / QueryDsl
- **언어:** Kotlin 1.9.24
- **데이터베이스:** PostgreSQL
- **CI/CD:** GitAction, Docker
- **클라우드:** AWS (S3, EC2, RDS)

<br><br>

## 🛠️ **주요 기능**

- **영어 모임 생성 및 참여:** 사용자는 자신의 영어 레벨과 위치에 맞는 영어 모임을 찾거나 새로운 모임을 생성할 수 있습니다.
- **리더 정보 및 자기소개:** 모임에 참여하기 전, 리더의 정보를 확인하고 참가자들의 영어 레벨 및 간단한 자기소개를 주고받을 수 있는 기능을 제공합니다.
- **AWS 기반 인프라:** EC2, RDS, S3을 이용해 서비스를 제공합니다.

<br><br>

## 🏗️ **프로젝트 ERD**
![Team of English Together (1)](https://github.com/user-attachments/assets/f73c8f55-2210-4c8e-b009-f3db3222f475)

프로젝트의 [ERD(Entity-Relationship Diagram)](https://www.erdcloud.com/d/jzyEvDZbEjwrWNdth)를 통해 데이터베이스 구조를 확인할 수 있습니다. 주요 엔티티는 다음과 같습니다.

- **회원(Member):** 플랫폼 사용자를 관리합니다.
- **모임(Circle):** 영어 스터디 모임을 구성하며, 레벨 및 지역에 따라 분류됩니다.
- **스케줄(Schedule):** 각 모임의 일정 정보를 저장합니다.
- **댓글(Comment):** 모임에 대한 피드백과 의견을 나눌 수 있는 공간을 제공합니다.

<br><br>

## 🧐 **개발과 함께 스터디**

[Kotlin, JPA 환경에서 Entity 설계에 대한 고민](https://fun-coding-study.tistory.com/404)

[AWS S3를 활용한 프로필 사진 업로드 기능 개발 회고](https://fun-coding-study.tistory.com/405)
