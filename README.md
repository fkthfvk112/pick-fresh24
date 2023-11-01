<h1>Pick-fresh24</h1>
<h2>프로젝트 설명</h2>
<p>
	픽업 전문 매장을 컨샙으로 개발한 사이트입니다. 지도, 검색 기반으로 가게를 선택하고, 픽업 주문을 할 수 있습니다.
</p>
<hr/>
<h2>사용한 기술</h2>
<h2>Front-end</h2>
<h4>개발언어</h4>
<ul>
	<li>TypeScript</li>
</ul>
<h4>React</h4>
<ul>
	<li>SPA를 만들기 위한 기반 기술로서 사용</li>
</ul>
<h4>VITE</h4>
<ul>
	<li>프로젝트 관리를 위한 빌드 툴로 사용</li>
</ul>
<h4>Recoil</h4>
<ul>
	<li>React 전역 관리를 위해 사용</li>
</ul>
<h4>React-query</h4>
<ul>
	<li>프로젝트 성능 향상을 위해 사용</li>
</ul>
<hr/>

<h2>Back-end</h2>
<h4>개발언어</h4>
<ul>
	<li>Java</li>
</ul>
<h4>Spring-boot</h4>
<ul>
	<li>Java로 웹서버를 만들고 API를 작성하기 위해 사용</li>
</ul>
<h4>Spring-security</h4>
<ul>
	<li>유저의 인증, 인가 등 보안 관련 작업 및 JWT 관리를 위해 사용</li>
</ul>
<h4>Spring data jpa</h4>
<ul>
	<li>DB에 접근하기 위한 ORM 기술로 사용</li>
</ul>
<hr/>

<h2>Etc</h2>
<h4>Docker</h4>
<ul>
	<li>운영체제에 상관없이 일정한 프로젝트를 컨테이너화해 보여주기 위해 사용</li>
</ul>
<h4>AWS</h4>
<ul>
	<li>Ec2: 서버 배포를 위한 클라우드 컴퓨터로 사용</li>
	<li>Route53: 사이트 도메인 등록을 위해 사용(DNS)</li>
	<li>Certificate Manager: SSL 등록을 위해 사용</li>
	<li>Load balancer: SSL과 결합하여 https 프로토콜 적용을 위해 사용</li>
</ul>
<h4>Git Action</h4>
<ul>
	<li>자동 AWS Ec2로의 자동 배포를 위해 사용</li>
</ul>
<hr/>
<h2>Project architecture</h2>
<img src="https://github.com/emartFresh/emart-fresh-back/assets/76651990/ca69d325-3d19-4670-8764-c97f200d7bdd"/>
<hr/>
<h2>Work flow</h2>
<img src="https://github.com/emartFresh/emart-fresh-front/assets/76651990/250e361d-0a0f-4b2c-95cd-bb30054c7382"/>

<h2>프로젝트 설치 및 실행 방법</h2>
<h2>Front-end</h2>
<ol>
	<li>아래 명령어를 순차적으로 실행</li>
	<li>git clone https://github.com/emartFresh/emart-fresh-front.git</li>
	<li>cd Front-end/emart-fresh-app</li>
 	<li>npm i</li>
  	<li>npm run dev(디벨롭 모드, localhost:8080과 연동)</li>
</ol>
<hr/>
<h2>Back-end</h2>
<ol>
	<li>아래 명령어를 순차적으로 실행(localhost의 mysql id:root, pw:1234와 연동)</li>
	<li>git clone https://github.com/emartFresh/emart-fresh-back</li>
	<li>cd emart-fresh-spring/demo</li>
 	<li>mvn clean package</li>
  	<li>java -jar target/jpa-0.0.1-SNAPSHOT.jar</li>
</ol>
<hr/>
<h2>팀원</h2>
<ul>
	<li>정진성 - 팀장/백엔드/프론트엔드(<a href="https://github.com/fkthfvk112/fkthfvk112">깃허브 프로필로 이동</a>)</li>
	<li>김예원 - 프론트엔드</li>
	<li>강창희 - 프론트엔드</li>
	<li>김현민 - 백엔드</li>
	<li>최무진 - 백엔드</li>
</ul>
