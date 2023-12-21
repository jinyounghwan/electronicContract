<%@ page language="java" contentType="text/html;charset=UTF-8" %>

<!-- Begin Left Side-Bar -->
<aside class="side-bar">
    <h1 class="sid-bar_title">menu</h1>
    <section class="side-bar__icon-box">
        <section class="side-bar__icon-1">
            <div></div>
            <div></div>
            <div></div>
        </section>
    </section>
    <ul>
        <li>
            <a href="#">menu1</a>
            <ul>
                <li><a href="#">sample1</a></li>
                <li><a href="#">sample2</a></li>
                <li><a href="#">sample3</a></li>
                <li><a href="#">sample4</a></li>
            </ul>
        </li>
        <li>
            <a href="#">menu2</a>
            <ul>
                <li><a href="#">sample1</a></li>
                <li><a href="#">sample2</a></li>
                <li><a href="#">sample3</a></li>
                <li><a href="#">sample4</a></li>
            </ul>
        </li>
        <li>
            <a href="#">menu3</a>
            <ul>
                <li><a href="#">sample1</a></li>
                <li><a href="#">sample2</a></li>
                <li><a href="#">sample3</a></li>
                <li><a href="#">sample4</a></li>
            </ul>
        </li>
    </ul>
</aside>
<!-- End Left Side-Bar -->
<style>
 body, ul, li {
     margin: 0;
     padding: 0;
     list-style: none;
 }

 a {
     color: inherit;
     text-decoration: none;
 }

/* depth 2이상 메뉴를 숨기기 */
 .side-bar > ul ul {
     display : none;
 }

 /* 사이드바의 너비와 높이를 변수를 통해 통제 */
 :root {
     --side-bar-width: 270px;
     --side-bar-height: 100vh;
 }

 .side-bar {
     position: fixed; /* 스크롤을 따라오도록 지정 */
     background-color: black;
     width: var(--side-bar-width);
     min-height: var(--side-bar-height); /* 사이드바의 높이를 전체 화면 높이의 90%로 지정 */
     margin-top: calc((100vh - var(--side-bar-height)) / 2); /* 사이드바 위와 아래의 마진을 동일하게 지정 */
 }

 /* 모든 메뉴의 a에 속성값 부여 */
 .side-bar ul > li > a {
     display: block;
     color:white;
     font-size: 1.4rem;
     font-weight: bold;
     padding-top: 20px;
     padding-bottom : 20px;
     padding-left : 50px;
 }

 .side-bar > ul > li {
     position: relative;
 }

 /* 모든 메뉴가 마우스 인식 시 반응 */
 .side-bar ul > li:hover > a {
    background-color: #555;
     border-bottom : 1px solid #999;
 }

 /* 1차 메뉴의 항목이 마우스 인식 시에 2차 메뉴 등장 */
 .side-bar > ul > li:hover > ul {
     display: block;
     position: absolute;
     background-color: #888;
     top:0;         /* 2차 메뉴의 상단을 1차 메뉴의 상단에 고정 */
     left:100%;     /* 2차 메뉴를 1차 메뉴의 너비만큼 이동 */
     width:100%;    /* 1차 메뉴의 너비를 상속 */
 }

 .sid-bar_title{
     color:white;
     padding-top: 20px;
     padding-bottom : 20px;
     padding-left : 50px;
     font-size: 2.6rem;
     margin:0;
 }
</style>
