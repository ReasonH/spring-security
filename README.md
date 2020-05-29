## Srping Security

Spring Security에 대해 학습합니다.

### 목표
1. Spring Security의 기본 개념 학습
2. 레퍼런스 문서 읽기
3. 내장된 기본 구현체 내부 구조 파악
4. Auth0 Java-JWT( https://github.com/auth0/java-jwt)를 사용한 JWT를 발급, 인증
5. 소셜공급자 연동

참조: [봄이네집 스프링](https://github.com/wheejuni/spring-jwt)

---

### Chap01 - Data modeling
- Security 구현 개념 학습
- 데이터 모델링

#### 구현 개념도
1. Request
2. Filter Chain (Username/PW auth 필터, CORS 필터, Custom 인증 필터 등)
    - 요청의 가장 앞단에서 Request를 걸러내서 필요한 처리를 한다
    - 필터를 거친 요청은, 인증 요청 객체로 변환
    - 인증 요청 객체 → DTO → DTO를 다시 authentication을 상속받은 객체로 변환해서 Provider manager에 전달
3. Manager에 있는 Provider에서는 인증된 객체를 반환
    - 이 객체는 isauthenticated 메소드에 true를 반환함
4. 인증콘텍스트를 인증콘텍스트 관리자에 제공
5. 모든 절차가 끝나면 Response로 반환

#### AuthenticationManager

**AuthenticationProvider**를 가진 주머니 역할
- Builder 패턴으로 구현
- Provider들에 접근하는 유일한 객체
- 단순 인터페이스, 구현해서 사용하지 않음

#### AuthenticationProvider

실제 인증이 진행되는 부분

- 인증 전 객체를 받아 인증 가능 여부 체크 후, 인증 후 객체를 만들어 돌려주거나 예외를 throw
- 구현이 필요한 Interface
- 필요에 맞게 정교하게 구현하고 인증 관리자에 등록필요

#### 인증객체

- Authentication interface의 모든 subclass
- Principle - 인증의 주체가 되는 Object, 인증의 시작을 명시하는 객체
    - ex) Username을 통해서 User 객체를 불러오고 인증처리를 한다. Principle은 이 때 인증 프로세스의 시작을 알리는 역할을 하며 Principle은 Username에 걸린다.

#### 구현해야 할 부분

- 요청을 받아낼 필터 **AbstractAuthenticationFilter**
    - 인증의 의미단위별로 구현해야함
    - ex) 폼로그인, 소셜로그인 구현시 ⇒ 2개의 필터를 만들어야함

- Manager에 등록시킬 **AuthenticationProvider**
    - 인증을 처리하는 과정이 다른 인증과 완전히 달라서 다른 클래스의 기능을 가져야할 때는 Provider 구현을 추가적으로 진행

- AJAX 방식이라면, 인증 정보를 담을 DTO 구현
- 각 인증에 따른 추가 구현체, 기본적 성공/실패 핸들러
- 소셜 인증의 경우 각 소셜 공급자의 규격에 맞는 DTO와 HTTP req 객체
- 인증 시도/성공 시에 각각 사용할 Authentication 객체 (선택)

#### Implicit Grant Flow

최근에는 각 소셜공급자들이 제공하는 JS sdk등을 이용해 프론트에서 인증을 받고 그 결과를 서버로 보내 토큰을 발급받는 구현 방식이 대세이다.

#### DB 설계 (유저 객체 설계)

- 유저 인증을 위해 필요한 정보, 서비스 제공 위한 정보를 필요한 만큼 저장
- 비밀번호를 비롯 민감 정보는 암호화가 원칙
- 소셜 회원도 담을 수 있게 확장성 있게 구현
- @ElementCollection, Enum 등 활용
    - User 권한을 담을 때 사용하면 좋음 (변경이 잦지 않음)
    - 권한을 Entity로 묶어서 사용하지 말아야함

##### Account 요구사항
1. 기본적인 유저정보
    - id, 비밀번호, 이름, 프로필 사진 링크(ProfileHref)
    - 서비스상에서 유저에게 부여하고 싶은 권한
    - 소셜 사용자의 경우 소셜 서비스가 부여한 아이디 코드 (Not login ID)

2. 유저 정보를 인증 과정에서 처리하는 방식
    - 유저모델 그대로 사용
    - 유저 디테일 구현체를 사용 (Spring security 기본)

---

### Chap02 - Security Component
- 기본적인 Security 구조 구현과 파악
- Filter, handler, jwtfactory 등

#### JWT란?

콘텐츠를 JSON 형태로만들고 이를 다시한번 암호화시켜 토큰값으로 만든 것

HTTP Auth 헤더나, URI 쿼리 파라미터 등 공백 사용 불가능한 곳에서 가볍게 사용할 수 있는 토큰으로 설계됨 (Form으로 POST 안써도 되는게 강점이다)

- 마침표를 구분자로 세 부분(헤더, 클레임셋, 시그니쳐)로 나뉨

1. 헤더: 암호화 알고리즘, 토큰 타입을 명시적으로 보임

2. 페이로드(클레임셋): 실제 토큰 정보(json 형태), reserved field 가 있지만 이외에는 자유롭게 사용 가능

3. 시그니쳐: 토큰 무결성 검증, 인코딩된 헤더 + 페이로드를 임의 키로 암호화
이 때, 암호화 키는 절대로 하드코딩하면 안된다.

#### 특징

- 토큰 자체가 데이터를 가지고 있음
- 기존 토큰 방식의 DB 엑세스 오버헤드가 없음(기존의 토큰은 키만 갖기 때문)

#### JWT 주의사항

- 클레임셋은 암호화 하지 않음
- 즉, 보안이 중요한 데이터를 넣으면 안됨
- 유저의 단순 식별자, 권한정보 등만 넣는 것이 원칙 (유출되도 의미 X)
- 데이터를 주고받는다 X → 인증 컨텍스트를 만든다 O
- 인코딩 특성상 클레임셋의 내용과 토큰 길이는 비례 → 너무 많은 정보를 담으면 안됨

---

### Chap03 - JWT request auth

- JWT Filter / Provider / Custom RequestMatcher 구현
- 로그인 요청에 대해서는 토큰 검증을 하지 않기 때문에 Custom RequestMatcher 구현이 필요함

#### JwtAuthenticationProvider 로직
1. jwt가 우리가 가진 signing key로 서명이 되었는지 확인
2. 필요한 claimset을 뽑아서 Account context를 생성
3. 이 때 DB조회가 없어야함

#### Process
1. client 요청시 API 요청 header에 토큰을 넣어서 전달
2. 서버는 헤더에 있는 토큰값을 검증
3. 적절한 인증 객체 생성
4. Security Context에 세팅
5. Security Context holder에 **4**를 세팅
이 과정 이후에는 AOP 기반 인증이 사용 가능하다.

##### Controller
~~~ java
    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_USER')") // SPEL exp lang, user role을 소유해야만 메소드 진입 가능
    public String getUsername(Authentication authentication) {
        PostAuthorizationToken token = (PostAuthorizationToken)authentication;
        return token.getAccountContext().getUsername();
    }
~~~

---

### Chap04 - Auth0 JWT Library

카카오 로그인을 구현한다.
- 유저의 카카오 토큰으로 유저 정보를 얻어오는 서비스 작성
- 추후 다른 소셜 로그인 서비스 공급자와도 연동 가능하도록 확장성 있게 구현
- 최대한 객체지향적으로 설계
    - 새로운 공급자가 들어와도 로직이 수정되는 일이 없도록 해야함, DIP
- DTO 작성

인증 후 JWT 발급 
- DB에서 검색되지 않는 사용자는 회원가입 처리 후 발급
- DB에 있는 사용자는 바로 발급

#### 소셜 공급자로 빼올 데이터
    - email
    - 고유번호 ('id')
    - 프사 href
    - 닉네임
    
#### 인터페이스 구현
- SocialUserProperty
