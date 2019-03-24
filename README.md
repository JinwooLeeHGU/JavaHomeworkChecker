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

## 01_input.txt 02_output.txt 파일 형식
### 01_input.txt
한줄이 java 실행할 때 들어가야 할 input 하나임</br>
java ....ClassName 2 3</br>
java ....ClassName 10 10<br>
이런식으로 두 번 실행 한다고 한다면 첫줄에 2 3 둘째줄에  10 10 넣으면 됨
```
2 3
10 10
```


### 02_output.txt
출력은 여러 라인에 걸쳐서 이루어지므로 %%%%% 다섯개로 시작하는 라인이 새로운 아웃풋을 알리는 마지막줄에 무조건 %%%%%로 넣어야 함. 
```
%%%%% output for 2 3
SUM: 5
SUBTRACT: -1
MULTIPLY: 6
DIVIDE: 0
The number of arithmetic operators processed!: 4
%%%%% output for 10 10
SUM: 20
SUBTRACT: 0
MULTIPLY: 100
DIVIDE: 1
The number of arithmetic operators processed!: 4
%%%%% End
```

<h1>output</h1>

통과 못한 학생 목록.

https://stackoverflow.com/questions/13991007/execute-external-program-in-java
