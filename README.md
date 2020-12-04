# HwpTool1
HWP text replace tool.

CSV를 읽어 지정한 텍스트를 대치해 다른 이름으로 저장합니다.

## 실행 방법
`java -jar -Dfile.encoding=UTF-8 hwpTool1-1.0-SNAPSHOT.jar`

## 명령줄 인수
- `-t` `-template`: 템플릿이 될 hwp 파일 이름. 기본값:`./template.hwp`
- `-d` `-data`: CSV 파일 이름. 기본값:`./data.csv`
- `-m` `-macro`: 파일 합치는 매크로 명령어를 저장할 텍스트 파일 이름. 기본값:`./macro.txt`
- `-o` `-output`: 다른 이름으로 저장될 hwp의 경로. 기본값:`./output/`

## CSV파일 형식
첫 번째 줄에는 찾을 키워드를 입력합니다. (순번 행은 필수입니다.)

`순번,<안녕>,<하세요>`

두 번째 줄 이후부터는 바꿀 텍스트를 입력합니다.

`가,반갑,습니다`

프로그램을 실행하면 `./output/가.hwp` 가 생성되며`<안녕>`이 `반갑`으로,`<하세요>`가`습니다`로 대치됩니다.