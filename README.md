# Quantara

## 프로젝트 개요

**Quantara**는 'Quantum(양자)'에서 유래한 'Quant'와 라틴어로 '꿈'을 의미하는 'Tara'를 결합한 이름의 프로젝트입니다. 이 프로젝트는 사용자가 입력한 텍스트 프롬프트(예: 꿈이나 상상 속
이야기)에 담긴 감정을 분석하고, 양자역학적 연산을 통해 해당 감정의 "실현 가능성"을 계산하여 시각화하는 독창적인 애플리케이션입니다. 감정 분석과 양자 컴퓨팅 기술을 융합하여, 판타지적이거나 감정적으로 강렬한
텍스트를 데이터 기반으로 해석하고 창의적인 결과를 도출하는 것을 목표로 합니다.

Quantara는 재미와 창의성을 위한 실험적 프로젝트로, 지속적인 기능 추가, 성능 최적화, UI/UX 개선을 통해 발전할 예정입니다. 이 프로젝트는 `Google Cloud Natural Language API`를
활용한 감정 분석과 `Strange` 및 `StrangeFX` 라이브러리를 사용한 양자역학적 계산 및 시각화를 핵심 기술로 사용합니다.

## 기술 스택

Quantara는 다음과 같은 주요 기술 스택을 기반으로 구성됩니다.

- **감정 분석**:
    - **Google Cloud Natural Language API**: 텍스트의 감정 점수 및 세부 감정(긍정, 부정, 중립 등)을 분석
- **양자역학 연산**:
    - **[Strange](https://github.com/redfx-quantum/strange)**: 양자 컴퓨팅 시뮬레이션을 위한 자바 기반 라이브러리
    - **[StrangeFX](https://github.com/redfx-quantum/strangefx)**: Strange 라이브러리의 시각화 확장으로, 계산 결과를 그래픽으로 표현
- **개발 환경**:
    - **Java**: 버전 21.0.8 이상
    - **JavaFX**: 사용자 인터페이스 및 시각화 구현
- **구성 관리**:
    - JSON 기반 설정 파일(`config.json`)을 통해 애플리케이션 동작을 커스터마이징

## 감정 분석

Quantara는 사용자가 입력한 텍스트 프롬프트에서 단어 및 문장의 감정적 특성을 분석합니다. 이를 위해 **Google Cloud Natural Language API**를 사용하며, 다음과 같은 프로세스를
거칩니다.

1. 사용자가 입력한 텍스트를 API로 전송하여 감정 점수(sentiment score)와 감정 크기(magnitude)를 추출
2. 추출된 감정 데이터를 양자역학적 연산에 입력으로 사용하여 실현 가능성을 계산
3. 계산 결과를 JavaFX 기반 UI에서 시각적으로 표현

**중요**: `Google Cloud Natural Language API`는 현재 한국어 텍스트를 직접 처리하지 못하므로, 입력 프롬프트는 **영어**로 작성되어야 합니다. 한국어 프롬프트를 처리하려면 별도의
번역 모듈(Google Translate API, DeepL 등) 통합이 필요할 수 있습니다.

### Google Cloud 설정

`Google Cloud Natural Language API`를 사용하려면 다음 단계를 따라야 합니다.

1. Google Cloud Console에서 서비스 계정을 생성하고, `.json` 형식의 키 파일을 발급
2. 발급받은 키 파일을 프로젝트의 `priv-resources` 디렉토리에 저장
3. `config.json` 파일에 키 파일 경로를 설정(아래 [구성 설정](#구성-설정) 참조)

## 양자역학 연산

Quantara는 양자역학적 연산을 위해 **Strange** 라이브러리를 사용합니다. `Strange`는 자바 기반의 양자 컴퓨팅 시뮬레이터로, 양자 회로를 구성하고 계산을 수행할 수 있습니다. Quantara는
감정 분석 결과를 기반으로 양자 상태를 정의하고, 이를 통해 특정 감정의 "실현 가능성"을 확률적으로 계산합니다.

**StrangeFX**는 `Strange`의 계산 결과를 시각화하는 데 사용됩니다. 이를 통해 사용자는 복잡한 양자역학적 결과를 직관적인 그래픽(확률 분포 그래프, 별자리 형태의 시각화 등)으로 확인할 수
있습니다. 예를 들어, 감정 데이터에 따라 생성된 양자 상태는 JavaFX 윈도우에서 별자리 형태로 렌더링되어 사용자에게 시각적 피드백을 제공합니다.

## 구성 설정

Quantara의 설정은 `config.json` 파일을 통해 관리됩니다. 아래는 기본 설정입니다.

```json
{
  "googleCloudKeyfilePath": "keyfile.json",
  "uiWidth": 1366,
  "uiHeight": 768,
  "minPromptLength": 20,
  "maxPromptLength": 2000,
  "language": "ko",
  "createConstellationTimeoutMilli": 60000
}
```

### 설정 항목 설명

- **`googleCloudKeyfilePath`**: Google Cloud 서비스 계정 키 파일의 경로. `priv-resources` 디렉토리 내에 위치해야 합니다.
- **`uiWidth`**: JavaFX 애플리케이션 윈도우의 최대 너비(픽셀 단위).
- **`uiHeight`**: JavaFX 애플리케이션 윈도우의 최대 높이(픽셀 단위).
- **`minPromptLength`**: 입력 프롬프트의 최소 길이(문자 수). 너무 짧은 입력은 분석의 정확도를 떨어뜨릴 수 있습니다.
- **`maxPromptLength`**: 입력 프롬프트의 최대 길이(문자 수). 과도한 입력을 제한하여 성능을 최적화합니다.
- **`language`**: 애플리케이션 로그 및 UI 언어. 현재는 한국어(`ko`)만 지원합니다. 향후 다국어 지원이 추가될 예정입니다.
- **`createConstellationTimeoutMilli`**: 양자역학적 계산 및 별자리 시각화 생성의 최대 대기 시간(밀리초). 기본값은 60초입니다.

## 사용법

### 전제 조건

- **Java**: JDK 21.0.8 이상.
- **의존성**:
    - `Google Cloud Natural Language API` 클라이언트 라이브러리
    - `Strange` 및 `StrangeFX` 라이브러리
    - JavaFX SDK
- **Google Cloud 계정**: `Google Cloud Natural Language API`를 활성화하고 서비스 계정 키 파일을 발급받아야 합니다.

### 실행 방법

1. Google Cloud Console에서 발급받은 `.json` 키 파일을 `priv-resources` 디렉토리에 저장합니다.
2. `config.json` 파일을 위의 형식에 맞춰 작성하고, `googleCloudKeyfilePath`를 키 파일의 정확한 경로로 설정합니다.
3. IDE에서 프로젝트를 불러오고, 메인 클래스 `Quantara.java`를 실행합니다.
4. 애플리케이션이 시작되면, 영어로 작성된 텍스트 프롬프트를 입력하여 감정 분석 및 양자역학적 계산 결과를 확인할 수 있습니다.

### 제한사항

- `Google Cloud Natural Language API`는 한국어 텍스트를 지원하지 않으므로, 입력 프롬프트는 영어로 작성해야 합니다.
- 양자역학적 계산은 시뮬레이션 기반이므로, 실제 양자 컴퓨터가 아닌 CPU/GPU에서 실행됩니다. 따라서 복잡한 계산의 경우 성능 병목이 발생할 수 있습니다.

## 향후 계획

- 다국어 지원(특히 한국어 프롬프트 처리)을 위한 번역 모듈 통합(이를 위해 개인 모델 학습 가능성 있음)
- Strange 라이브러리의 최신 버전 업데이트 및 성능 최적화
- JavaFX UI 개선 및 추가 시각화 옵션(3D 별자리 렌더링 등)
- 클라우드 기반 양자 컴퓨팅 서비스(IBM Quantum 등)와의 연동 가능성 탐구

## 기여

Quantara는 오픈소스 프로젝트로, 기여를 환영합니다. 버그 리포트, 기능 제안, 코드 기여는 [GitHub 저장소](https://github.com/Quant-Off/)를 통해 가능합니다. 자세한 기여
가이드라인은 `CONTRIBUTING.md`를 참조하세요.

프로젝트가 초기화될 때 디자인 틀과 `Google Cloud Natural Language API` 등록에 기여해준 `Clem`에게 감사합니다!