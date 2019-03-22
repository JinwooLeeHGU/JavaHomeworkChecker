# JavaHomeworkChecker
<h1>input</h1>

<h3>example argument list</h3>

## 실행파일 생성 및 압축해제
$ gradle distZip </br>
$ unzip build/distributions/JavaHomeworkChecker.zip -d build/distributions/ </br>

## 실행하기 (build/distributions/bin/ path설정하면 어디서든 실행가능)
$ build/distributions/bin/JavaHomeworkChecker . 01_input.txt 02_output.txt 03_javafilelist.txt 04_classfileformain.txt 05_studentpathlistMT67.txt 06_proejctrootname.txt

</br>
0번째 파라미터는 unpassed.csv파일이 저장되는 경로임 .은 현재 경로에 저장된다는 말. 나머지 파일들은 이름으로 뭐하는 파일인지 알 수 있을 것임.


<h1>output</h1>

통과 못한 학생 목록.

https://stackoverflow.com/questions/13991007/execute-external-program-in-java
