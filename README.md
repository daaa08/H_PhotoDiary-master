# Diary App
- 나만의 다이어리 앱
---
__기능__
- 로그인 기능
- 리사이클러뷰를 이용한 리스트 페이지
- 쓰기 페이지 (갤러리 사진 불러오기)
>>
- TextView Scroll
```java
위젯이름.setMovementMethod(new ScrollingMovementMethod());
```
- Current Date
```java
private String convertLongToString(long date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    return sdf.format(date);
}
```
- Button
```xml
<item>
    <shape android:shape="rectangle">
        <solid android:color="#fff"/>
        <corners android:radius="10dp" />
        <stroke
            android:width="2dp"
            android:color="#349200"
            />
    </shape>
</item>
```
- CardView
```xml
<android.support.v7.widget.CardView
        android:id="@+id/cardView"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        app:cardCornerRadius="20dp"
        android:elevation="0dp">
```
---
[재생영상](https://youtu.be/D7gmVVW25p8)

![스크린샷 2017-07-07 오전 5.53.53](http://i.imgur.com/IUrCv0E.png)

![스크린샷 2017-07-07 오전 5.55.39](http://i.imgur.com/qJ5vR4K.png)
