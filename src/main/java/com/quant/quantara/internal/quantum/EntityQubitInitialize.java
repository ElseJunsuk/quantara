package com.quant.quantara.internal.quantum;

import com.quant.quantara.internal.GCEntityWrapper;
import org.redfx.strange.*;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.RotationY;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;
import org.redfx.strangefx.render.RenderEngine;

import java.util.Arrays;
import java.util.List;

/**
 * 각 엔티티의 감정 점수를 사용하여 큐비트에 매핑하기 위한 클래스입니다.
 * 우선적으로 개별 엔티티의 감정 점수를 통해 큐비트의 중첩 상태를 만들고,
 * 큐비트를 초기화합니다.
 *
 * @author Q. T. Felix
 * @since 1.0.0
 */
public class EntityQubitInitialize {

    private List<GCEntityWrapper> entities;
    private double averageScore;

    private double theta;
    private Result simulationResult;

    private Program program;

    public EntityQubitInitialize(double averageScore) {
        this.averageScore = averageScore;
    }

    /**
     * {@link EntityQubitInitialize} 클래스를 인스턴스화 하기 위해 사용자가 입력한
     * 프롬프트에서 추출된 개별 엔티티를 자료구조에 담아야 합니다. 엔티티는 래핑된 클래스
     * {@link GCEntityWrapper} 클래스가 사용됩니다.
     *
     * @param entities 개별 엔티티 자료구조
     */
    public EntityQubitInitialize(List<GCEntityWrapper> entities) {
        this.entities = entities;
    }

    /**
     * 감정 점수의 평균을 구한 후 큐비트를 초기화하기 위해 정규화합니다.
     * 만약 {@link #entities} 자료구조가 비어있는 경우 {@link #theta} 값을 {@code -1.0}로 정의하여
     * {@link #simulate()}메소드를 사용할 수 없도록 합니다. 세타 값은 긍정적일수록 작습니다.
     *
     * @return 세티(정규화 값)가 할당된 {@link EntityQubitInitialize}
     */
    public EntityQubitInitialize normalize() {
        if (entities == null) { // 인스턴스를 생성할 때 엔티티 리스트가 아닌 평균 값을 전달한 경우
            this.theta = (1 - averageScore) / Math.PI;
        } else { // 엔티티 리스트를 전달하여 인스턴스를 생성한 경우
            if (entities.isEmpty()) {
                this.theta = -1.0;
                return this;
            }
            // 감정 점수 평균 계산
            double avgScore = entities.stream()
                    .mapToDouble(GCEntityWrapper::getSentimentScore)
                    .average()
                    .orElse(0D);
            // 정규화: 점수 -1.0 ~ 1.0을 0 ~ 파이값으로 매핑
            this.theta = (1 - avgScore) / Math.PI;
        }
        // 디버그 출력
        System.out.println("[Debug] 큐비트 정규화(theta): " + theta);
        return this;
    }

    /**
     * 세타 값을 사용하여 큐비트 상태를 생성합니다. 이는 시각화를 위해 사용되기도 합니다.
     * <p>아다마르 게이트를 부여하여 중첩 상태를 만들고, 파울리-Y 게이트를 적용시켜
     * 감정 기반으로 회전한 큐비트를 생성합니다.
     *
     * @return 시뮬레이션 값이 적용된 {@link EntityQubitInitialize}
     */
    public EntityQubitInitialize simulate() {
        if (this.theta < 0) return this;
        // 단일 큐비트 프로그랩 생성
        this.program = new Program(1);
        // 먼저 중첩 상태를 만들기 위해 아다마르 게이트 적용
        Step step1 = new Step();
        step1.addGate(new Hadamard(0));
        program.addStep(step1);

        // 해당 상태에 회전을 위한 게이트 적용(감정 기반 회전)
        Step step2 = new Step();
        step2.addGate(new RotationY(theta, 0));
        program.addStep(step2);

        // 결과를 위한 시뮬레이션 수행
        QuantumExecutionEnvironment environment = new SimpleQuantumExecutionEnvironment();
        this.simulationResult = environment.runProgram(program);
        return this;
    }

    /**
     * 시뮬레이션이 완료된 후 해당 결과 객체 {@link Result}를 사용하여
     * 상태가 0에 있을 확률을 반환합니다. 해당 확률에 100을 곱해 백분율로 표현하세요.
     *
     * @return 상태가 0에 있을 확률
     */
    public double successProbability() {
        if (this.simulationResult == null) return -1.0;
        Complex[] probability = this.simulationResult.getProbability();
        // 디버그 출력
        System.out.println("[Debug] 큐비트의 복소 확률값(1큐비트에서 상태가 0 또는 1에 있을 확률): " + Arrays.toString(probability));
        // 상태가 0에 있을 확률 반환(계수의 절댓값 제곱)
        return probability[0].abssqr();
    }

    public RenderEngine fxRenderEngine() {
        if (this.program == null)
            throw new RuntimeException("program이 null입니다!");
        return RenderEngine.createNode(program);
    }

    public List<GCEntityWrapper> getEntities() {
        return entities;
    }

    /**
     * 엔티티 자료구조를 정의합니다.
     *
     * @param entities 엔티티 자료구조
     */
    public void setEntities(List<GCEntityWrapper> entities) {
        this.entities = entities;
    }

    public double getTheta() {
        return theta;
    }

    /**
     * 테스트를 위해 세타 값을 임의로 정의할 수 있습니다.
     * 세타 값이 작을수록 긍정적임을 의미합니다.
     *
     * @param theta 0 ~ 파이 값 사이의 세타 값
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    public Result getSimulationResult() {
        return simulationResult;
    }

    /**
     * 테스트를 위해 시뮬레이션 결과를 임의로 정의할 수 있습니다.
     *
     * @param simulationResult 시뮬레이션 결과 객체 {@link Result}
     */
    public void setSimulationResult(Result simulationResult) {
        this.simulationResult = simulationResult;
    }
}
