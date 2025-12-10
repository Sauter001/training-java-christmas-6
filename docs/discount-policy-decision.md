# 할인 정책 설계 의사결정 과정

## 목차
1. [초기 문제 인식](#초기-문제-인식)
2. [OCP 원칙 적용](#ocp-원칙-적용)
3. [파라미터 통일 문제](#파라미터-통일-문제)
4. [Null 객체 문제](#null-객체-문제)
5. [Law of Demeter 준수](#law-of-demeter-준수)
6. [최소 주문 금액 검증 위치](#최소-주문-금액-검증-위치)
7. [Badge 책임 분리](#badge-책임-분리)
8. [최종 아키텍처](#최종-아키텍처)

---

## 초기 문제 인식

### 요구사항
- 크리스마스 디데이 할인
- 평일 할인 (디저트)
- 주말 할인 (메인)
- 특별 할인 (일요일, 25일)
- 증정 이벤트

### 문제점
증정 이벤트 상품(GiftProduct)을 데이터베이스에서 조회하는 구조로 설계하려 했으나, **OCP(Open-Closed Principle)를 어떻게 지킬 것인가?**가 핵심 과제였습니다.

---

## OCP 원칙 적용

### 의사결정: Policy 패턴 도입

**고려한 옵션들:**
1. **Context 객체 패턴 (채택)**
   - EventContext에 필요한 모든 데이터 캡슐화
   - Policy 인터페이스로 다양한 정책 구현
   - 새로운 정책 추가 시 기존 코드 수정 불필요

2. Visitor 패턴
3. Strategy with Builder 패턴

**선택 이유:**
- 이벤트 정책마다 필요한 데이터가 다름
- Context 객체로 통일된 인터페이스 제공 가능
- Repository 패턴과 자연스럽게 결합

**구현:**
```java
public interface DiscountPolicy {
    List<Discount> apply(EventContext context);
}

public interface GiftPolicy {
    List<Gift> apply(EventContext context);
}
```

---

## 파라미터 통일 문제

### 문제 상황
각 할인 정책이 필요로 하는 정보가 달랐습니다:
- **크리스마스 디데이**: VisitDate만 필요
- **평일/주말 할인**: VisitDate + Order (메뉴 타입별 개수)
- **특별 할인**: VisitDate만 필요
- **증정 이벤트**: 총 주문 금액만 필요

### 의사결정: EventContext로 통합

**EventContext가 제공하는 정보:**
```java
public class EventContext {
    private final VisitDate visitDate;
    private final Order order;

    public VisitDate getVisitDate()
    public int getTotalPrice()
    public int countMenuByType(DishType dishType)
}
```

**장점:**
- 모든 Policy가 동일한 인터페이스 사용
- instanceof나 타입 체크 불필요
- 정책별로 필요한 정보만 선택적으로 사용

---

## Null 객체 문제

### 초기 설계의 문제

처음에는 Benefit이라는 통합 개념을 사용했습니다:

```java
// 문제가 있는 초기 설계
public record Benefit(
    String name,
    int amount,
    BenefitType type,  // DISCOUNT or GIFT
    Gift gift          // ← null 위험!
) {}
```

**문제점:**
- 할인 혜택일 때 `gift` 필드가 null
- NPE(NullPointerException) 위험성
- Null 체크 로직 필요

### 의사결정: Discount와 Gift 완전 분리

**데이터베이스 정규화의 원칙을 적용:**

할인과 증정은 본질적으로 다른 도메인이므로 완전히 분리해야 합니다.

```java
// 개선된 설계
public record Discount(String name, int amount) {}
public record Gift(String name, int price, int quantity) {}

public class DiscountEvent {
    public List<Discount> calculateDiscounts(EventContext context)
}

public class GiftEvent {
    public List<Gift> determineGifts(EventContext context)
}
```

**장점:**
- Null 객체 완전 제거
- instanceof 사용 불필요
- 타입 안전성 보장
- 단일 책임 원칙 준수

---

## Law of Demeter 준수

### 문제 상황

초기 구현에서 Law of Demeter 위반이 발생했습니다:

```java
// Law of Demeter 위반
int dessertCount = context.getOrder().countByType(DishType.DESSERT);
```

**문제점:**
- 체이닝(Chaining) 발생
- EventContext가 Order 내부 구조를 노출
- Policy가 Order에 직접 의존

### 의사결정: Delegation 메서드 추가

**해결 방법:**
```java
public class EventContext {
    // getOrder() 제거 ← 직접 접근 차단

    // Delegation 메서드 추가
    public int countMenuByType(DishType dishType) {
        return order.countByType(dishType);  // 내부에서 위임
    }
}
```

**사용:**
```java
// Law of Demeter 준수
int dessertCount = context.countMenuByType(DishType.DESSERT);
```

**장점:**
- Tell, Don't Ask 원칙 준수
- 캡슐화 강화
- Order 구조 변경 시 영향 범위 최소화

---

## 최소 주문 금액 검증 위치

### 요구사항
총 주문 금액 10,000원 이상부터 이벤트 적용

### 고려한 옵션

**옵션 1: 각 Policy에서 개별 검증**
```java
public class ChristmasDDayPolicy implements DiscountPolicy {
    public List<Discount> apply(EventContext context) {
        if (context.getTotalPrice() < MINIMUM_ORDER_AMOUNT) {
            return List.of();
        }
        // 정책 로직...
    }
}
```

**옵션 2: Event 레벨에서 일괄 검증 (채택)**
```java
public class DiscountEvent {
    public List<Discount> calculateDiscounts(EventContext context) {
        if (context.getTotalPrice() < MINIMUM_ORDER_AMOUNT) {
            return List.of();  // 모든 정책 실행 안 함
        }

        return policies.stream()
            .flatMap(policy -> policy.apply(context).stream())
            .toList();
    }
}
```

### 의사결정: Event 레벨 일괄 검증

**선택 이유:**
- 중복 코드 제거
- 정책 변경 시 한 곳만 수정
- 각 Policy는 자신의 고유 로직에만 집중
- 공통 검증은 상위 레벨에서 처리

**적용 범위:**
- `DiscountEvent.calculateDiscounts()`
- `GiftEvent.determineGifts()`

---

## Badge 책임 분리

### 초기 구현

```java
// OutputView에서 직접 판정
private String getBadge(int totalBenefit) {
    if (totalBenefit >= 20000) return "산타";
    if (totalBenefit >= 10000) return "트리";
    if (totalBenefit >= 5000) return "별";
    return "없음";
}
```

**문제점:**
- OutputView가 배지 등급 판정 로직을 가짐
- 배지 기준 변경 시 View 코드 수정 필요

### 의사결정: Badge Enum 분리

```java
public enum Badge {
    SANTA("산타", 20000),
    TREE("트리", 10000),
    STAR("별", 5000),
    NONE("없음", 0);

    private final String name;
    private final int threshold;

    public static Badge from(int totalBenefit) {
        if (totalBenefit >= SANTA.threshold) return SANTA;
        if (totalBenefit >= TREE.threshold) return TREE;
        if (totalBenefit >= STAR.threshold) return STAR;
        return NONE;
    }

    public String getName() {
        return name;
    }
}
```

**장점:**
- 배지 등급 판정 책임이 Badge 자체에 있음
- OutputView는 표시만 담당
- 단일 책임 원칙 준수
- 배지 기준 변경 시 Badge enum만 수정

---

## 최종 아키텍처

### 계층 구조

```
Application
    ↓
DiscountController
    ↓
DiscountStatistic
    ├── DiscountEvent (할인 총괄)
    │   ├── ChristmasDDayPolicy
    │   ├── WeekdayDiscountPolicy
    │   ├── WeekendDiscountPolicy
    │   └── SpecialDiscountPolicy
    │
    └── GiftEvent (증정 총괄)
        └── PriceBasedPolicy
```

### 핵심 컴포넌트

**1. EventContext (데이터 제공자)**
```java
public class EventContext {
    private final VisitDate visitDate;
    private final Order order;

    // Delegation 메서드들
}
```

**2. Policy 인터페이스 (전략)**
```java
public interface DiscountPolicy {
    List<Discount> apply(EventContext context);
}

public interface GiftPolicy {
    List<Gift> apply(EventContext context);
}
```

**3. Event 클래스 (정책 관리자)**
```java
public class DiscountEvent {
    private final List<DiscountPolicy> policies;

    // 최소 주문 금액 검증
    // 모든 정책 실행 및 집계
}

public class GiftEvent {
    private final List<GiftPolicy> policies;

    // 최소 주문 금액 검증
    // 모든 정책 실행 및 집계
}
```

**4. Result 객체 (결과 표현)**
```java
public record Discount(String name, int amount) {}
public record Gift(String name, int price, int quantity) {}
```

**5. Badge (등급 판정)**
```java
public enum Badge {
    // 혜택 금액 기준 배지 판정
}
```

### 의존성 주입 흐름

```
AppConfig
    ├── createDiscountEvent()
    │   └── new DiscountEvent(List.of(4개 정책))
    │
    └── createGiftEvent()
        └── new GiftEvent(List.of(PriceBasedPolicy(giftProducts)))
```

### 설계 원칙 준수 현황

| 원칙 | 적용 방법 |
|------|----------|
| OCP | Policy 패턴으로 새 정책 추가 시 기존 코드 수정 불필요 |
| SRP | 각 Policy는 하나의 할인 로직만 담당, Event는 정책 관리만 담당 |
| DIP | 구체 클래스가 아닌 Policy 인터페이스에 의존 |
| Law of Demeter | EventContext의 Delegation 메서드로 체이닝 방지 |
| Tell, Don't Ask | Context에게 데이터를 요청하지 않고 행위를 요청 |

### 확장 포인트

1. **새로운 할인 정책 추가**
   ```java
   public class NewYearPolicy implements DiscountPolicy {
       @Override
       public List<Discount> apply(EventContext context) {
           // 새로운 정책 로직
       }
   }
   ```
   → `AppConfig.createDiscountEvent()`에만 추가

2. **새로운 증정 정책 추가**
   ```java
   public class TierBasedPolicy implements GiftPolicy {
       @Override
       public List<Gift> apply(EventContext context) {
           // 등급별 증정 로직
       }
   }
   ```
   → `AppConfig.createGiftEvent()`에만 추가

3. **배지 등급 변경**
   → `Badge` enum만 수정

---

## 학습 포인트

### 1. Null 객체는 가능하면 피하라
- Null 체크 로직은 복잡도를 높임
- 타입 시스템으로 해결 가능한 문제는 타입으로 해결
- Optional보다는 도메인 분리가 더 명확할 수 있음

### 2. 공통 검증은 상위 레벨에서
- 각 Policy에 중복 검증 로직 X
- Event 레벨에서 일괄 처리
- Policy는 자신의 고유 로직에만 집중

### 3. Law of Demeter는 캡슐화의 핵심
- 체이닝은 결합도를 높임
- Delegation 메서드로 내부 구조 숨기기
- "물어보지 말고 시켜라"

### 4. 책임을 올바른 객체에 배치
- 배지 등급 판정은 Badge의 책임
- 할인 계산은 Policy의 책임
- 정책 관리는 Event의 책임
- 출력은 View의 책임

### 5. 정규화 원칙은 객체 설계에도 적용
- 서로 다른 도메인은 분리
- Discount ≠ Gift
- 억지로 통합하면 Null이나 instanceof 등장

---

## 결론

이 프로젝트에서 가장 중요했던 의사결정은 **"Discount와 Gift를 완전히 분리하자"**였습니다.

처음에는 Benefit이라는 통합 개념으로 관리하려 했지만, Null 객체 문제와 instanceof 사용 문제가 발생했습니다. 데이터베이스 정규화처럼 서로 다른 도메인은 분리해야 한다는 원칙을 객체 설계에 적용하여 문제를 해결했습니다.

결과적으로:
- Null 체크 완전 제거
- instanceof 사용 불필요
- 타입 안전성 보장
- 확장에 열려있고 수정에 닫힌 구조

**좋은 설계는 억지로 통합하지 않고, 자연스럽게 분리하는 것**임을 배웠습니다.
