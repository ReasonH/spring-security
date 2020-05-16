## Srping Security
---

---

#### Account 요구사항

1. 기본적인 유저정보
    - id, 비밀번호, 이름, 프로필 사진 링크(ProfileHref)
    - 서비스상에서 유저에게 부여하고 싶은 권한
    - 소셜 사용자의 경우 소셜 서비스가 부여한 아이디 코드 (Not login ID)

2. 유저 정보를 인증 과정에서 처리하는 방식
    - 유저모델 그대로 사용
    - 유저 디테일 구현체를 사용 (Spring security 기본)