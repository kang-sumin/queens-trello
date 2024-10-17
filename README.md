# 🛠️ **Queens-Trello API Documentation**

## 🏁 **프로젝트 목표**

> **"Queens-Trello 프로젝트"**  
> 이 문서는 Queens Trello 프로젝트의 API 명세서, 주요 기능, 및 트러블슈팅 내용을 다룹니다. 프로젝트의 목표는 사용자가 일정을 효과적으로 공유하고
관리할 수 있는 효율적이며 사용자 친화적인 칸반 보드 플랫폼을 제공하는 것입니다.

---

## ⚜ **API 명세서**

### **1. 회원가입, 로그인, 탈퇴, 첨부파일** (노현지)
<img width="1171" alt="회원가입 로그인 비밀번호 변경 탈퇴" src="https://github.com/user-attachments/assets/6911682b-e7f0-49a7-9a6e-754673587710">

### **2. 카드, 댓글** (조은솔)
<img width="1165" alt="가게 생성 수정 조회 폐업" src="https://github.com/user-attachments/assets/14b6de77-f85b-4718-8d10-00a90b593e06">

### **3. 멤버, 역할 관리, 워크스페이스** (강수민)
<img width="1165" alt="메뉴 수정 삭제 카테고리" src="https://github.com/user-attachments/assets/5e97da38-c3cd-467b-be60-30300021fe13">

### **4. 보드, 리스트** (장민경)
<img width="1157" alt="주문 음식주문 배달 상태" src="https://github.com/user-attachments/assets/0746daa3-3071-4f10-a6cf-70389f24e949">

### **4. 검색, 알림** (김진비)
<img width="1157" alt="주문 음식주문 배달 상태" src="https://github.com/user-attachments/assets/0746daa3-3071-4f10-a6cf-70389f24e949">

---

## ⚜ **ERD (Entity-Relationship Diagram)**

> **ERD 구조 다이어그램**  
> Sparta Delivery의 핵심 데이터베이스 구조를 나타내는 ERD입니다. 각 테이블 간의 관계와 주요 엔티티를 시각적으로 표현했습니다.
![ERD](https://github.com/user-attachments/assets/e8a2a5a0-8747-4595-a1c9-84d18e016c34)

---
# Queens-Trello API Documentation

## 📌 1. 회원가입
회원가입 API는 새로운 계정을 생성하고 JWT 토큰을 발급합니다.

- **이메일 중복 검사**: 이미 등록된 이메일이 있으면 예외 발생.
- **비밀번호 암호화**: 비밀번호를 안전하게 암호화하여 저장.
- **유저 역할 설정**: 요청된 역할에 맞게 사용자 역할 부여.
- **닉네임 설정**: 입력된 닉네임을 유저 정보에 저장.
- **슬랙 URL 저장**: 입력된 슬랙 URL을 유저 정보에 저장.
- **JWT 토큰 발급**: 유저 정보로 JWT 토큰을 생성하여 반환.

**예외 처리**:  
`EmailAlreadyExistsException`, `InvalidUserRoleException`

---

## 🔑 2. 로그인
로그인 API는 입력된 정보로 인증을 진행하고 JWT 토큰을 발급합니다.

- **이메일 확인**: 입력된 이메일로 유저를 조회.
- **비밀번호 확인**: 입력된 비밀번호가 저장된 비밀번호와 일치하는지 확인.
- **JWT 토큰 발급**: 인증이 성공하면 JWT 토큰을 생성하여 반환.

**예외 처리**:  
`QueensTrelloException(ErrorCode.USER_NOT_FOUND)`, `QueensTrelloException(ErrorCode.INVALID_PASSWORD)`

---

## 🔄 **3. 비밀번호 변경**

비밀번호 변경 API는 기존 비밀번호 확인 후 새로운 비밀번호로 변경합니다.

- **기존 비밀번호 확인**: 입력된 비밀번호가 일치하는지 확인.
- **새 비밀번호 확인**: 기존 비밀번호와 다른지 확인 후 변경.

**예외 처리**:  
`UserNotFoundException`, `UserInvalidPasswordException`, `SamePasswordException`

---

### 📌 1. 회원탈퇴
사용자 삭제 API는 지정된 사용자의 계정을 삭제합니다.

- **사용자 정보 조회**: 입력된 사용자 ID로 유저를 조회합니다.
- **비밀번호 확인**: 입력된 비밀번호가 저장된 비밀번호와 일치하는지 확인합니다.
- **논리적 삭제 처리**: 사용자의 `isDeleted` 상태를 `true`로 설정하여 계정을 삭제합니다.
- **정보 저장**: 변경된 사용자 정보를 데이터베이스에 저장합니다.

**예외 처리**:  
`QueensTrelloException(ErrorCode.INVALID_USER)`, `QueensTrelloException(ErrorCode.PASSWORD_MISMATCH)`

---

## 📁 파일 관리

### 📌 1. 첨부파일 업로드
첨부파일 업로드 API는 파일을 S3에 업로드하고 URL을 반환합니다.

- **파일 업로드**: 입력된 파일을 S3에 업로드합니다.
- **URL 반환**: 업로드가 완료된 후, S3에서 파일의 URL을 반환합니다.

**예외 처리**:  
`QueensTrelloException(ErrorCode.FILE_UPLOAD_ERROR)`

---

### 📌 2. 파일 삭제
파일 삭제 API는 S3에서 지정된 파일을 삭제합니다.

- **파일 삭제**: 입력된 파일 이름으로 S3에서 파일을 삭제합니다.

**예외 처리**:  
`QueensTrelloException(ErrorCode.FILE_DELETE_ERROR)`

---

## 🗂️ 보드 관리

### 📌 1. 보드 생성
보드 생성 API는 주어진 워크스페이스에 새 보드를 추가합니다.

- **사용자 권한 확인**: 현재 로그인한 사용자의 권한을 확인하고, 보드 생성 권한이 있는지 검사합니다.
- **워크스페이스 확인**: 주어진 워크스페이스가 존재하는지 확인합니다.
- **보드 저장**: 새 보드를 데이터베이스에 저장합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.INVALID_AUTHORITY_CREATE)`

---

### 📌 2. 보드 조회
보드 조회 API는 특정 보드의 정보를 반환합니다.

- **보드 확인**: 주어진 ID로 보드를 조회하고, 존재하지 않으면 예외를 발생합니다.
- **워크스페이스 멤버 확인**: 사용자가 해당 워크스페이스의 멤버인지 확인합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.BOARD_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`

---

### 📌 3. 워크스페이스의 모든 보드 조회
특정 워크스페이스의 모든 보드를 반환하는 API입니다.

- **워크스페이스 확인**: 주어진 ID로 워크스페이스를 조회합니다.
- **멤버 확인**: 사용자가 해당 워크스페이스의 멤버인지 확인합니다.
- **보드 목록 반환**: 워크스페이스에 속한 모든 보드 목록을 반환합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`

---

### 📌 4. 보드 업데이트
보드 업데이트 API는 기존 보드의 정보를 수정합니다.

- **보드 확인**: 주어진 ID로 보드를 조회합니다.
- **멤버 권한 확인**: 사용자의 워크스페이스 멤버 권한을 확인하고, 업데이트 권한이 있는지 검사합니다.
- **정보 수정**: 요청된 정보를 바탕으로 보드를 업데이트합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.BOARD_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.INVALID_AUTHORITY_UPDATE)`

---

### 📌 5. 보드 삭제
보드 삭제 API는 특정 보드를 삭제합니다.

- **보드 확인**: 주어진 ID로 보드를 조회합니다.
- **멤버 권한 확인**: 사용자의 워크스페이스 멤버 권한을 확인하고, 삭제 권한이 있는지 검사합니다.
- **보드 삭제**: 데이터베이스에서 보드를 삭제합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.BOARD_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.INVALID_AUTHORITY_CREATE)`

---

## 📁 카드 관리

### 📌 1. 카드 생성
카드 생성 API는 새로운 카드를 생성합니다.

- **카드 생성자 확인**: 생성자가 유효한 사용자이며, 읽기 전용 권한이 아닌지 확인합니다.
- **리스트 확인**: 주어진 ID로 카드가 추가될 리스트를 조회합니다.
- **카드 생성**: 요청된 정보를 바탕으로 카드를 생성하고 리스트에 추가합니다.
- **담당자 추가**: 요청된 ID 리스트에 따라 카드 담당자를 추가합니다.

**예외 처리**:  
- `IllegalArgumentException("유효하지 않는 user id 입니다.")`
- `IllegalStateException("읽기 전용 사용자는 카드를 생성할 수 없습니다.")`
- `IllegalArgumentException("유효하지 않은 listId 입니다.")`

---

### 📌 2. 카드 다건 조회
카드 다건 조회 API는 특정 리스트에 속한 카드를 조회합니다.

- **리스트 확인**: 주어진 ID로 리스트를 조회합니다.
- **카드 목록 반환**: 리스트에 속한 모든 카드 정보를 반환합니다.

**예외 처리**:  
- `IllegalArgumentException("유효하지 않은 listId 입니다.")`

---

### 📌 3. 카드 단건(상세) 조회
카드 단건 조회 API는 특정 카드의 상세 정보를 반환합니다.

- **카드 확인**: 주어진 ID로 카드를 조회합니다.
- **카드 정보 및 담당자 목록 반환**: 카드 정보와 함께 담당자 목록을 반환합니다.

**예외 처리**:  
- `IllegalArgumentException("유효하지 않은 카드 아이디 입니다.")`

---

### 📌 4. 카드 수정
카드 수정 API는 기존 카드의 정보를 수정합니다.

- **사용자 권한 확인**: 수정 요청 사용자의 권한을 확인합니다.
- **카드 확인**: 주어진 ID로 카드를 조회합니다.
- **정보 수정**: 요청된 정보를 바탕으로 카드를 수정합니다.
- **담당자 수정**: 기존 담당자를 업데이트하고, 추가 및 제거 작업을 수행합니다.

**예외 처리**:  
- `IllegalArgumentException("유효하지 않은 사용자 Id 입니다.")`
- `IllegalStateException("읽기 전용 사용자는 카드를 수정할 수 없습니다.")`
- `IllegalArgumentException("유효하지 않은 카드 Id 입니다.")`

---

### 📌 5. 카드 삭제
카드 삭제 API는 특정 카드를 삭제합니다.

- **사용자 확인**: 삭제 요청 사용자의 존재 여부를 확인합니다.
- **카드 확인**: 주어진 ID로 카드를 조회합니다.
- **읽기 전용 사용자 확인**: 읽기 전용 사용자는 카드 삭제를 할 수 없습니다.
- **카드 삭제**: 카드 및 관련 데이터를 삭제합니다.

**예외 처리**:  
- `IllegalArgumentException("유효하지 않은 사용자 Id 입니다.")`
- `IllegalArgumentException("유효하지 않은 카드 Id 입니다.")`
- `IllegalStateException("읽기 전용 사용자는 카드를 삭제할 수 없습니다.")`

---

## 📁 댓글 관리

### 📌 1. 댓글 작성
댓글 작성 API는 새로운 댓글을 작성합니다.

- **댓글 작성자 확인**: 댓글 작성자가 유효한 사용자이며 읽기 전용 권한이 아닌지 확인합니다.
- **카드 존재 여부 확인**: 주어진 ID로 댓글이 작성될 카드를 조회합니다.
- **댓글 생성**: 요청된 정보를 바탕으로 댓글을 생성합니다.

**예외 처리**:  
- `IllegalArgumentException("유효하지 않은 사용자 ID 입니다.")`
- `IllegalStateException("읽기 전용 사용자는 댓글 작성 권한이 없습니다.")`
- `IllegalArgumentException("유효하지 않은 카드 ID입니다.")`

---

### 📌 2. 댓글 수정
댓글 수정 API는 기존 댓글의 내용을 수정합니다.

- **댓글 확인**: 주어진 ID로 댓글을 조회합니다.
- **본인 댓글 확인**: 수정 요청 사용자가 댓글 작성자인지 확인합니다.
- **정보 수정**: 요청된 내용을 바탕으로 댓글을 수정합니다.

**예외 처리**:  
- `IllegalArgumentException("유효하지 않은 댓글 아이디 입니다.")`
- `IllegalStateException("본인 댓글만 수정 가능합니다.")`

---

### 📌 3. 댓글 삭제
댓글 삭제 API는 특정 댓글을 삭제합니다.

- **댓글 확인**: 주어진 ID로 댓글을 조회합니다.
- **본인 댓글 확인**: 삭제 요청 사용자가 댓글 작성자인지 확인합니다.
- **댓글 삭제**: 댓글을 삭제합니다.

**예외 처리**:  
- `IllegalArgumentException("유효하지 않은 댓글ID 입니다.")`
- `IllegalStateException("본인의 댓글만 삭제할 수 있습니다.")`

---

# 📁 보드 리스트 관리

### 📌 1. 보드 리스트 저장
보드 리스트 저장 API는 주어진 보드에 새 리스트를 생성합니다.

- **보드 존재 확인**: 주어진 ID로 보드를 조회합니다.
- **멤버 권한 확인**: 사용자가 해당 보드의 워크스페이스 멤버인지 확인합니다.
- **보드 리스트 생성**: 요청된 정보를 바탕으로 새 보드 리스트를 생성합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.BOARD_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_WRITE)`

---

### 📌 2. 보드 리스트 수정
보드 리스트 수정 API는 기존 리스트의 정보를 수정합니다.

- **보드 존재 확인**: 주어진 ID로 보드를 조회합니다.
- **멤버 권한 확인**: 사용자의 워크스페이스 멤버 권한을 확인하고, 수정 권한이 있는지 검사합니다.
- **정보 수정**: 요청된 정보를 바탕으로 보드 리스트를 업데이트합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.BOARD_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.BOARDLIST_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_WRITE)`

---

### 📌 3. 보드 리스트 삭제
보드 리스트 삭제 API는 특정 리스트를 삭제합니다.

- **보드 존재 확인**: 주어진 ID로 보드를 조회합니다.
- **멤버 권한 확인**: 사용자의 워크스페이스 멤버 권한을 확인하고, 삭제 권한이 있는지 검사합니다.
- **보드 리스트 삭제**: 데이터베이스에서 보드 리스트를 삭제합니다.
- **순서 조정**: 삭제 후 다른 리스트들의 순서를 조정합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.BOARD_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.BOARDLIST_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_WRITE)`

---

### 📌 4. 보드 리스트 순서 변경
보드 리스트 순서 변경 API는 리스트의 순서를 조정합니다.

- **보드 리스트 확인**: 주어진 ID로 보드 리스트를 조회합니다.
- **기존 순서 저장**: 리스트의 현재 순서를 저장합니다.
- **순서 조정**: 새로운 순서에 따라 리스트의 순서를 변경합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.BOARDLIST_NOT_FOUND`

---

# 📁 슬랙 서비스

### 📌 1. 슬랙 메시지 전송
슬랙 메시지 전송 API는 다양한 유형의 메시지를 슬랙으로 전송합니다.

- **알림 유형 확인**: `Classification`에 따라 알림 메시지의 형식을 결정합니다.
- **메시지 전송**: 슬랙 URL로 메시지를 전송합니다.

**예외 처리**:  
- `IOException`: 메시지 전송 중 오류 발생 시 스택 트레이스를 출력합니다.

---

### 📌 2. 마스터로 승급
마스터로 승급하는 API는 사용자를 마스터로 설정합니다.

- **사용자 확인**: 주어진 ID로 사용자를 조회합니다.
- **슬랙 URL 확인**: 승급된 사용자의 슬랙 URL을 가져옵니다.
- **승급 메시지 전송**: 마스터 승급 알림 메시지를 슬랙으로 전송합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.INVALID_USER)`

---

### 📌 3. 멤버 초대
멤버 초대 API는 사용자를 워크스페이스에 초대합니다.

- **사용자 조회**: 초대한 사용자와 초대받은 사용자를 조회합니다.
- **워크스페이스 확인**: 초대한 사용자의 워크스페이스를 조회합니다.
- **초대 메시지 전송**: 초대 알림 메시지를 슬랙으로 전송합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.No_WORKSPACE_MASTER)`
- `QueensTrelloException(ErrorCode.INVALID_USER)`

---

### 📌 4. 멤버 추가
멤버 추가 API는 워크스페이스에 멤버를 추가합니다.

- **사용자 조회**: 주어진 ID로 사용자를 조회합니다.
- **워크스페이스 확인**: 주어진 ID로 워크스페이스를 조회합니다.
- **멤버 추가 메시지 전송**: 새로운 멤버 추가 알림 메시지를 슬랙으로 전송합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.INVALID_USER)`

---

### 📌 5. 카드 변경
카드 변경 API는 카드의 내용을 확인하고 알림을 전송합니다.

- **사용자 조회**: 주어진 ID로 사용자를 조회합니다.
- **카드 확인**: 주어진 ID로 카드를 조회합니다.
- **매니저 권한 확인**: 사용자가 카드의 매니저인지 확인합니다.
- **카드 변경 알림 전송**: 카드의 제목과 내용을 포함한 알림 메시지를 슬랙으로 전송합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.INVALID_CARD)`
- `QueensTrelloException(ErrorCode.NOT_CARD_MANAGER)`

---

### 📌 6. 댓글 추가
댓글 추가 API는 카드에 댓글을 추가하고 알림을 전송합니다.

- **사용자 조회**: 주어진 ID로 사용자를 조회합니다.
- **댓글 확인**: 주어진 ID로 댓글을 조회합니다.
- **카드 확인**: 댓글이 속한 카드를 조회합니다.
- **매니저 권한 확인**: 사용자가 카드의 매니저인지 확인합니다.
- **댓글 알림 전송**: 댓글 작성자의 닉네임과 내용을 포함한 알림 메시지를 슬랙으로 전송합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.INVALID_COMMENT)`
- `QueensTrelloException(ErrorCode.NOT_CARD_MANAGER)`

---

# 📁 검색 서비스

### 📌 1. 카드 통합 검색
카드 통합 검색 API는 주어진 조건에 맞는 카드를 검색합니다.

- **닉네임 확인**: 제공된 닉네임이 있는 경우, 해당 닉네임을 가진 사용자를 조회합니다.
- **페이지 설정**: 페이지와 사이즈를 기반으로 `Pageable` 객체를 생성합니다.
- **카드 검색**: 카드 리포지토리를 통해 주어진 제목, 내용, 마감일, 매니저를 기반으로 카드를 검색합니다.
- **응답 변환**: 검색된 카드를 `SearchResponse` DTO 형태로 변환하여 반환합니다.

**예외 처리**:  
- `NoNicnameUserException(ErrorCode.NOT_FOUND_NICKNAME)`

--- 

# 📁 마스터 요청 서비스

### 📌 1. MASTER 권한 변경 신청 저장
MASTER 권한 변경 신청 API는 일반 사용자가 MASTER 권한을 요청하는 내용을 저장합니다.

- **사용자 정보 확인**: 현재 로그인된 사용자의 정보를 가져옵니다.
- **권한 확인**: 요청한 사용자의 권한이 USER인지 확인합니다. 
- **신청 내역 확인**: 해당 사용자가 이미 MASTER 권한 변경 요청을 했는지 확인합니다.
- **신청 객체 생성**: 새로운 MASTER 요청 객체를 생성하여 저장합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION_MASTER_REQUEST)`
- `QueensTrelloException(ErrorCode.MASTER_REQUEST_ALREADY_EXIST)`

---

# 📁 워크스페이스 관리자 서비스

### 📌 1. 사용자 역할 업데이트
사용자 권한 변경 API는 특정 사용자의 역할을 MASTER로 변경합니다.

- **사용자 확인**: 변경할 사용자의 ID로 사용자를 조회합니다.
- **MASTER 요청 확인**: 해당 사용자의 MASTER 요청이 존재하는지 확인합니다.
- **권한 업데이트**: 사용자의 권한을 MASTER로 변경하고, MASTER 요청 상태를 수락으로 업데이트합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.USER_NOT_FOUND)`: 사용자가 존재하지 않는 경우 발생합니다.
- `QueensTrelloException(ErrorCode.MASTER_REQUEST_NOT_FOUND)`: MASTER 요청이 존재하지 않는 경우 발생합니다.

---

### 📌 2. 승인되지 않은 MASTER 권한 변경 요청 내역 조회
승인되지 않은 MASTER 권한 요청 내역을 페이지 단위로 조회하는 API입니다.

- **페이지 설정**: 요청한 페이지 번호와 크기로 Pageable 객체를 생성합니다.
- **요청 내역 조회**: 승인되지 않은 MASTER 요청을 페이지 단위로 조회합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.MASTER_REQUEST_NOT_EXIST)`: 승인되지 않은 MASTER 요청이 없는 경우 발생합니다.

---

### 📌 3. 멤버 역할 업데이트
워크스페이스의 멤버 권한을 변경하는 API입니다.

- **사용자 확인**: 변경할 사용자의 ID로 사용자를 조회합니다.
- **역할 확인**: 사용자의 현재 역할이 USER인지 확인합니다.
- **워크스페이스 확인**: 해당 워크스페이스가 존재하는지 확인합니다.
- **멤버 확인**: 사용자가 해당 워크스페이스의 멤버인지 확인합니다.
- **역할 변경**: 멤버의 권한을 변경합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.USER_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.USER_HAS_NOT_PERMISSION)`
- `QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.WORKSPACE_MEMBER_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.SAME_EXIST_MEMBER_ROLE)`

---

### 📌 1. 워크스페이스 생성
워크스페이스를 생성하는 API입니다.

- **사용자 확인**: 요청한 사용자의 권한이 ADMIN 또는 MASTER인지 확인합니다.
- **워크스페이스 생성**: 워크스페이스 정보를 기반으로 새 워크스페이스를 생성합니다.
- **멤버 추가**: 사용자를 워크스페이스 멤버로 추가합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION)`

---

### 📌 2. 워크스페이스에 멤버 초대
워크스페이스에 새로운 멤버를 초대하는 API입니다.

- **사용자 확인**: 로그인한 사용자의 권한이 적절한지 확인합니다.
- **워크스페이스 조회**: 초대할 워크스페이스를 조회합니다.
- **사용자 검색**: 초대할 사용자의 이메일로 사용자를 검색합니다.
- **멤버 추가**: 사용자를 워크스페이스 멤버로 추가합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION)`
- `QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND)`
- `QueensTrelloException(ErrorCode.USER_NOT_FOUND)`

---

### 📌 3. 워크스페이스 수정
기존 워크스페이스 정보를 수정하는 API입니다.

- **사용자 확인**: 수정 권한이 있는 사용자인지 확인합니다.
- **워크스페이스 조회**: 수정할 워크스페이스를 조회합니다.
- **정보 수정**: 요청받은 정보로 워크스페이스를 업데이트합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION)`
- `QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND)`

---

### 📌 4. 워크스페이스 조회
사용자가 가입된 워크스페이스 목록을 조회하는 API입니다.

- **페이지 설정**: 페이지 번호와 크기를 기반으로 조회합니다.
- **워크스페이스 ID 검색**: 사용자의 ID로 등록된 워크스페이스 ID를 검색합니다.
- **워크스페이스 정보 반환**: 조회된 워크스페이스 정보를 반환합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND)`

---

### 📌 5. 워크스페이스 삭제
워크스페이스를 삭제하는 API입니다.

- **사용자 확인**: 삭제 권한이 있는 사용자인지 확인합니다.
- **워크스페이스 조회**: 삭제할 워크스페이스를 조회합니다.
- **워크스페이스 삭제**: 해당 워크스페이스를 삭제합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION)`
- `QueensTrelloException(ErrorCode.WORKSPACE_NOT_FOUND)`

---

### 📌 6. 워크스페이스 서비스 권한 확인
서비스 권한을 확인하는 내부 메서드입니다.

- **사용자 확인**: 로그인한 사용자를 확인합니다.
- **권한 확인**: 사용자의 현재 권한을 확인합니다.

**예외 처리**:  
- `QueensTrelloException(ErrorCode.HAS_NOT_ACCESS_PERMISSION)`


## 🚀 **주요 기능 및 구현**

### 1. **JWT 토큰 사용자 인증 (AuthUser)**

- **기능**:  
  사용자 ID, 이메일, 닉네임, 역할 정보를 바탕으로 JWT 토큰을 생성하여 인증합니다.
  
---

### 2. **파일 첨부 서비스 (AttachmentsService)**

- **기능**:  
  S3에 파일을 업로드하고 그 URL을 반환합니다.
  
---

### 3. **워크스페이스 서비스 (WorkspaceService)**

- **기능**:  
  워크스페이스를 관리하는 기능을 제공합니다.
  
---

### 4. **보드 서비스 (BoardService)**

- **기능**:  
  특정 워크스페이스 내의 보드를 관리합니다.

---

### 5. **카드 서비스 (CardService)**

- **기능**:  
  보드 내의 카드를 관리합니다.

---

### 6. **슬랙 알림 서비스 (SlackNotificationService)**

- **기능**:  
  작업 변경사항을 슬랙 채널로 알림합니다.

---

### 7. **전역 예외 처리 (GlobalException)**

- **기능**:  
  커스텀 예외를 통합 관리합니다.

---

## 🛠️ **트러블슈팅**

### 1. **@Value 경로 지정 오류**

- **문제**:  
  `@Value` 애노테이션에서 잘못된 경로로 인해 설정 값이 제대로 로드되지 않고 컴파일 오류가 발생했습니다.

- **해결**:  

---

### 2. **회원가입 시 User Role 공백 오류**

- **문제**:  
  회원가입 요청 시, 역할(Role) 정보가 누락되어 API 요청이 실패했습니다.

- **해결**:  
  
---

### 3. **Non-null 매개변수에 null 전달 오류**

- **문제**:  
  메서드 호출 시, null이 전달되었고 해당 메서드에서 null을 허용하지 않아 오류가 발생했습니다.

- **해결**:  
 

---

### 4. **멤버가 중복으로 추가되는 오류**

- **문제**:  
  같은 멤버가 중복으로 시스템에 추가되는 문제가 발생했습니다.

- **해결**:  


---

### 5. **특정 위치에 새로운 리스트 삽입 시 원하는 위치로 추가되지 않음**

- **문제**:  
  리스트를 특정 위치에 삽입하려 했으나 예상한 위치로 추가되지 않았습니다.

- **해결**:  
  
---

## 🔗 **참조**

- **JWT**: [JWT.io](https://jwt.io/)
- **AWS S3**: [AWS S3 Documentation](https://docs.aws.amazon.com/s3/index.html)
- **Spring Security**: [Spring Security Documentation](https://spring.io/projects/spring-security)
- **Slack API**: [Slack API Documentation](https://api.slack.com/)

