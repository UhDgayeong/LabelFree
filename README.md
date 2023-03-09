# LabelFree
<p align="center"><img src="https://user-images.githubusercontent.com/96832560/223196668-6a9f50d8-8b01-4e75-9285-636dd0200ffa.jpg" width = "600"></p>


### 라벨프리는 무라벨 음료의 기존 라벨 정보를 알려주는 어플입니다.
> 무라벨 음료는 환경 보호에 큰 이바지를 하고 있지만 라벨이 없어 영양성분 및 여러 정보들을 제공할 수 없는 단점이 있습니다. </br> 이러한 단점을 보완하고자, 사용자가 어플에서 음료를 검색해 라벨 표기 사항들을 확인할 수 있도록 하고자 어플을 개발하게 되었습니다.
</br>

---
</br>

## 기능 소개

| 메인 화면 | 검색 | 서랍 메뉴 |
|:--------:|:----:|:--------:|
|![KakaoTalk_20230307_030318189](https://user-images.githubusercontent.com/96832560/223439574-aef0e2b9-b29a-41f0-bca0-bb61b962cf31.jpg)| <img src="https://user-images.githubusercontent.com/96832560/224104760-d151c94c-2a40-4780-b005-bc7bb0089795.gif" width = "300">  | ![KakaoTalk_20230307_030318189_05](https://user-images.githubusercontent.com/96832560/223442483-a71b213c-9432-4fed-bf6c-423c24608d47.jpg) |

* 검색창에 랜덤으로 음료명을 띄워주도록 하였습니다.
* AutoCompleteTextView를 이용하여 자동완성 검색 기능을 구현하였습니다.
* DrawLayout을 이용하여 메뉴를 구현하였습니다.
  - 서랍에서 로그인한 계정의 이메일 정보를 볼 수 있게 하였습니다.
  - 하단의 로그아웃 버튼을 통해 로그아웃을 할 수 있습니다.
</br>

| 라벨 정보 조회 | 제품 리스트 조회 | 제품 리스트에서 음료 클릭 시 |
|:-------------:|:---------------:|:--------------------------:|
|![KakaoTalk_20230307_030318189_02](https://user-images.githubusercontent.com/96832560/223450982-17ef41e2-1ca5-4a9d-8f88-9c3f8ca027f8.jpg)|![KakaoTalk_20230307_030318189_03](https://user-images.githubusercontent.com/96832560/223451147-a47cf0ae-9ccf-47a5-9aaf-ead809ac4dcb.jpg)|![KakaoTalk_20230307_030318189_04](https://user-images.githubusercontent.com/96832560/223451203-9bd40070-8cea-4af5-8286-877c0e7bb31e.jpg)|

* 라벨 표기 사항들을 화면에서 확인할 수 있습니다.
  - Glide 라이브러리를 이용하여 이미지를 빠르게 로딩할 수 있도록 하였습니다.
  - MPAndroidChart 라이브러리를 이용하여 막대 그래프를 구현하였습니다.
    - https://github.com/PhilJay/MPAndroidChart
  - 표의 값은 1일 영양성분 기준치 값을 기준으로 해당 음료에 영양성분이 들어있는 값을 퍼센테이지로 나타낸 것입니다.
* 서랍 메뉴 - 제품 리스트 를 누르면 제품 리스트를 조회할 수 있습니다.
  - 리스트에서 조회를 원하는 음료를 클릭하면 해당 음료의 라벨 표기 사항들을 조회하는 화면으로 바로 넘어갈 수 있습니다.
</br>

| 인트로 화면 | 회원가입 화면 | 로그인 화면 |
|:-----------:|:------------:|:-----------:|
|![KakaoTalk_20230307_030318189_06](https://user-images.githubusercontent.com/96832560/223454429-ac369507-4085-4f8c-a24d-27c3b843bcbf.jpg)|![KakaoTalk_20230307_030318189_07](https://user-images.githubusercontent.com/96832560/223454492-bcb4b559-2a1c-4732-b4dc-002746a07e92.jpg)|![KakaoTalk_20230307_030318189_08](https://user-images.githubusercontent.com/96832560/223454521-c356fc38-ec9e-40ef-8569-f917c6a544a2.jpg)|

* 스플래쉬 화면 이후 로그인 또는 회원가입을 선택할 수 있는 화면을 띄워줍니다.
* 회원가입 시 동일한 이메일 계정이 Firebase의 Authentication에 존재하는지 확인한 후, 새로운 계정임이 확인되면 회원가입 처리 후 메인 화면으로 이동됩니다.
  - 이메일 주소 칸은 영문, 숫자만 입력이 가능하고, 비밀번호 칸은 영문, 숫자, 특수기호만 입력 가능하도록 입력 제한을 설정하였습니다.
* 로그인 화면에서 이메일로 회원가입한 계정으로 직접 로그인할 수도 있고, 구글 또는 페이스북 로그인을 통해 빠른 로그인을 할 수도 있습니다.
</br>

---
</br>

## 개선할 점 또는 추가할 기능
- 음료 즐겨찾기 기능
- 회원 탈퇴 기능

## 기타 사항
- 제작 기간
  - 2022/1/17 ~
