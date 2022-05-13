# Review Notes
## 프로젝트 소개
스마트폰으로 언제 어디서든 열어볼 수 있는 나만의 오답 노트

## 개발 환경
+ Android Studio
+ Galaxy Note 9

## 실행 화면 및 프로그램 기능
+ 메인 화면
<img src="https://user-images.githubusercontent.com/50476562/168234502-221fde31-9fe8-4d4e-835b-a4e59506f1c0.jpg"/>

+ 오답 추가
<img src="https://user-images.githubusercontent.com/50476562/168234144-4ff864fb-e9e9-4997-928c-da70af5218a0.jpg"/>

+ 오답/정답 촬영
<img src="https://user-images.githubusercontent.com/50476562/168234720-b2fe8d47-40c6-44c2-b440-104c5082fbb2.jpg"/>

+ 오답 목록
<img src="https://user-images.githubusercontent.com/50476562/168236066-03353625-271c-41df-aa52-89c12cb022bc.jpg"/>

+ 정답 확인 전/후
<div class="grid-image">
    <img src="https://user-images.githubusercontent.com/50476562/168236181-9d0929c0-df3b-4bda-ac25-e88aa5e5ae18.jpg"/>
    <img src="https://user-images.githubusercontent.com/50476562/168236197-8a2a8496-add7-49f1-979c-a91a2ac37a91.jpg"/>
</div>

  
+ 메모 작성
<img src="https://user-images.githubusercontent.com/50476562/168236391-af04a7b8-a0e1-458e-aa42-51fe5a7adf49.jpg"/>
  
+ 메모 확인
<img src="https://user-images.githubusercontent.com/50476562/168236397-1aae4abb-5da6-4dbb-8335-023c857d989a.jpg"/>
  
+ 오답 노트 삭제
<img src="https://user-images.githubusercontent.com/50476562/168236528-f1e478f2-3284-4208-850b-371a7b743476.jpg"/>
  
+ 예외 처리
<img src="https://user-images.githubusercontent.com/50476562/168236659-d603c6a5-4809-40aa-803d-d7a7ac29533c.jpg"/>
<img src="https://user-images.githubusercontent.com/50476562/168236662-102f5f13-3b98-4ee5-a07e-83a52070635a.jpg"/>
<img src="https://user-images.githubusercontent.com/50476562/168236664-1f40456d-bc71-40c4-907c-a9e1d99d66da.jpg"/>
  
+ 권한
<img src="https://user-images.githubusercontent.com/50476562/168236831-0afbc9a9-49ba-4b16-af28-73e2bb65e663.jpg"/>
<img src="https://user-images.githubusercontent.com/50476562/168236833-317a3a3d-54f1-4d10-8ca1-34fc040456eb.jpg"/>
---
<style>
/* CSS */
.grid-image {
    display:flex;
    flex-wrap:wrap;
    align-items:flex-start;
    margin:30px 0;
}
.grid-image img {
    width:calc(33.333% - 10px);
    margin:0 15px 15px 0;
}
.grid-image img:nth-of-type(3n),
.grid-image img:last-child {
    margin-right:0;
}
@media screen and (max-width:640px){
  .grid-image img {
    width:calc(50% - 15px);
  }
}
@media screen and (max-width:480px){
  .grid-image img:nth-of-type(2n) {
    margin-right:0;
  }
  .grid-image img:nth-of-type(3n) {
    margin-right:15px;
  }
}
</style>
