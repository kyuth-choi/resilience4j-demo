# resilience4j-demo

AOP 기반으로 동작하기 때문에 resilience4j dependency 이외에도 aop관련 dependency도 추가하여야 동작함

CircuitBreaker는 유한상태기계(finite state machine) 통해 구현됨

states : CLOSED, OPEN, HALF_OPEN

special states : DISABLED(항상 접근 허용), FORCED_OPEN(항상 접근 거부)

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/34821f45-d112-4fa9-869d-6766c5fb5294/Untitled.png)

CircuitBreaker는 요청의 저장과 총계를 내는데 sliding window를 사용한다 

count-based sliding window와 time-based sliding window를 선택적으로 사용가능함

count-based sliding window는 최신 N개의 요청에 대해서 집계하며 time-based sliding window는 최근 N 초에 들어온 요청에 대해서 집계함 

## **Count-based sliding window**

sliding window는 N개 측정값의 원형 배열로 구현됨

sliding window는 점진적으로 총 집계를 업데이트 하며 총 집계는 새로운 요청이 레코드에 들어왔을때 업데이트된다.

 가장 오래된 측정값이 나갈때, 해당 측정값는 총 집계에서 빠지며 bucket은 초기화됨 (Subtract-on-Evict)

- sliding window 즉 원형 배열의 각 영역을 bucket이라고 하는듯

## **Time-based sliding window**

time-based sliding window는 N개 이하의  원형 배열로 구현됨

*time-based sliding window 사이즈가 10이라면 원열 배열은 10개 이하의 부분 집계이다

 모든 버킷은 특정 시간동안 들어온 요청에 대해 집계를 한다 

가장 최신 순으로 sliding window 의 bucket에 특정시간동안 들어온 요청에 대한 집계가 저장된다.

총 집계는 신규 요청이 레코드로 들어왔을때 업데이트되며 가장 오래된 버킷은 퇴출된다

또한 총집계에서도 빠지게 되며 해당 버킷은 초기화 된다.


Example with 1 Thread:

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/d95eb81e-bfa2-49c7-aba8-46546364f34c/Untitled.png)

Example with 3 Threads:

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/2c6c3d32-4d27-45c0-a829-111b2d912fad/Untitled.png)

